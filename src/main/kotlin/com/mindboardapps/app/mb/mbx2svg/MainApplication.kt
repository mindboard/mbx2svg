package com.mindboardapps.app.mb.mbx2svg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

import java.io.File
import kotlin.math.min
import kotlin.math.max
import com.mindboardapps.app.mb.mbx2svg.svg.PathCmd
import com.mindboardapps.app.mb.mbx2svg.svg.RectF
import com.mindboardapps.app.mb.mbx2svg.svg.PointF
import com.mindboardapps.app.mb.mbx2svg.utils.BranchUtils
import com.mindboardapps.app.mb.mbx2svg.model.MyColorUtils
import com.mindboardapps.app.mb.mbx2svg.model.Page
import com.mindboardapps.app.mb.mbx2svg.model.Node
import com.mindboardapps.app.mb.mbx2svg.model.Stroke
import com.mindboardapps.app.mb.mbx2svg.model.Group
import com.mindboardapps.app.mb.mbx2svg.model.Matrix
import com.mindboardapps.app.mb.mbx2svg.model.MyColor
import com.mindboardapps.app.mb.mbx2svg.model.PaperSize

typealias PageListener = (Page)->Unit
typealias NodeListener = (Node)->Unit
typealias StrokeListener = (Stroke)->Unit
typealias GroupListener = (Group)->Unit

typealias BackgroundColor = Int
typealias PaperRectangle = RectF
typealias NodeMap = Map<String, Node>

typealias NodePathCmdList = List<PathCmd>
typealias StrokePathCmdList = List<PathCmd>

data class SVGParams(
    val backgroundColor: BackgroundColor,
    val paperRectangle: PaperRectangle,
    val matrix: Matrix,
    val nodeMap: NodeMap)

object SVGParamsBuilder {
    private val toMinValue: (Float?, Float)->Float = { value0, value1->
        if( value0==null ){ value1 } else { min(value0, value1) }
    }

    private val toMaxValue: (Float?, Float)->Float = { value0, value1->
        if( value0==null ){ value1 } else { max(value0, value1) }
    }

    val parse: (File)->SVGParams = { mbxFile->
        var mapRectangleLeft: Float? = null
        var mapRectangleTop: Float? = null
        var mapRectangleRight: Float? = null
        var mapRectangleBottom: Float? = null

        var backgroundColor  = MyColor.WHITE
        val nodeMap = mutableMapOf<String, Node>()

        val pageListener: PageListener = { page->
            backgroundColor = page.backgroundColor
        }

        val nodeListener: NodeListener = { node->
            nodeMap.put(node.uuid, node)

            val left = node.x
            val top  = node.y
            val right = left +node.width
            val bottom = top + node.height
    
            mapRectangleLeft = toMinValue(mapRectangleLeft, left)
            mapRectangleTop = toMinValue(mapRectangleTop, top)
            mapRectangleRight = toMaxValue(mapRectangleRight, right)
            mapRectangleBottom = toMaxValue(mapRectangleBottom, bottom)
        }

        val strokeListener: StrokeListener = { stroke->
            val left = stroke.left
            val top = stroke.top
            val right = stroke.right
            val bottom = stroke.bottom
    
            mapRectangleLeft = toMinValue(mapRectangleLeft, left)
            mapRectangleTop = toMinValue(mapRectangleTop, top)
            mapRectangleRight = toMaxValue(mapRectangleRight, right)
            mapRectangleBottom = toMaxValue(mapRectangleBottom, bottom)
        }
    
        val groupListener: GroupListener = { group->
            val left = group.x
            val top  = group.y
            val right = left +group.width
            val bottom = top + group.height
    
            mapRectangleLeft = toMinValue(mapRectangleLeft, left)
            mapRectangleTop = toMinValue(mapRectangleTop, top)
            mapRectangleRight = toMaxValue(mapRectangleRight, right)
            mapRectangleBottom = toMaxValue(mapRectangleBottom, bottom)
        }

        MbxParser(mbxFile).proc(
            pageListener,
            nodeListener,
            strokeListener,
            groupListener)
    

        val paperSize = PaperSize.defaultSVGPaperSize

        val mapRectangle = RectF(
            mapRectangleLeft?:0f,
            mapRectangleTop?:0f,
            mapRectangleRight?:0f,
            mapRectangleBottom?:0f)
    
        val scale = min(
            paperSize.width.toFloat() / mapRectangle.width,
            paperSize.height.toFloat() / mapRectangle.height) * 0.9f
    
        val dx = (mapRectangleLeft?:0f) - (paperSize.width.toFloat()  - mapRectangle.width*scale)*0.5f * (1f/scale)
        val dy = (mapRectangleTop?:0f)  - (paperSize.height.toFloat() - mapRectangle.height*scale)*0.5f * (1f/scale)
        val matrix = Matrix(dx, dy, scale)
        val paperRectangle = RectF(0f, 0f, paperSize.width.toFloat(), paperSize.height.toFloat())
    
        SVGParams(backgroundColor, paperRectangle, matrix, nodeMap)
    }
}

object SVGCmdPathBuilder {
    private val toNodeListener1: (Matrix, MutableList<PathCmd>, Map<String, Node>)->NodeListener = { matrix, pathCmdList, nodeMap->
        { node->
            if( !node.removed ){ 
                val parentNode = nodeMap.get(node.parentNodeUuid)
                if( parentNode!=null ){
                    // Resolve branch stroke width.
                    val branchStrokeWidth = BranchUtils.toLineStrokeWidth(
                        BranchUtils.toChildCount(nodeMap, node)) * matrix.scale
                    val branchColor = node.branchColor
    
                    val leftPt = BranchUtils.toLeftPoint(node)
                    val rightPt = BranchUtils.toRightPoint(node)
    
                    if( !node.isTypeCenter() ){
                        // Draw an across line on the node.
                        val startX = matrix.fixX(leftPt.x)
                        val startY = matrix.fixY(leftPt.y)
                        val stopX  = matrix.fixX(rightPt.x)
                        val stopY  = matrix.fixY(rightPt.y)
    
                        val cmd = listOf<String>( "M${startX},${startY}", "L${stopX},${stopY}").joinToString(" ")
                        pathCmdList.add(PathCmd(cmd, branchColor, branchStrokeWidth))
                    }
    
                    if( !node.isTypeCenter() && node.isTypeLeft(parentNode) ){
                        // Draw a branch(connection) from parentNode leftPoint to this node rightPoint.
                        val parentNodeLeftPt = BranchUtils.toLeftPoint(parentNode)
                        val parentNodeCenterPt = BranchUtils.toCenterPoint(parentNode)
    
                        val startX0 = if( parentNode.isTypeCenter() ){ parentNodeCenterPt.x } else { parentNodeLeftPt.x }
                        val startY0 = if( parentNode.isTypeCenter() ){ parentNodeCenterPt.y } else { parentNodeLeftPt.y }
    
                        val startX = matrix.fixX(startX0)
                        val startY = matrix.fixY(startY0)
                        val stopX  = matrix.fixX(rightPt.x)
                        val stopY  = matrix.fixY(rightPt.y)
    
                        val (controlPtA, controlPtB) =
                            BranchUtils.toControlPoints(PointF(startX, startY), PointF(stopX, stopY))
    
                        val cmd = listOf<String>(
                            "M${startX},${startY}",
                            "C${controlPtA.x},${controlPtA.y} ${controlPtB.x},${controlPtB.y} ${stopX},${stopY}").joinToString(" ")
                        pathCmdList.add(PathCmd(cmd, branchColor, branchStrokeWidth))
                    }
    
                    if( !node.isTypeCenter() && node.isTypeRight(parentNode) ){
                        // Draw a branch(connection) from parentNode rightPoint to this node leftPoint.
                        val parentNodeRightPt = BranchUtils.toRightPoint(parentNode)
                        val parentNodeCenterPt = BranchUtils.toCenterPoint(parentNode)
    
                        val startX0 = if( parentNode.isTypeCenter() ){ parentNodeCenterPt.x } else { parentNodeRightPt.x }
                        val startY0 = if( parentNode.isTypeCenter() ){ parentNodeCenterPt.y } else { parentNodeRightPt.y }
    
                        val startX = matrix.fixX(startX0)
                        val startY = matrix.fixY(startY0)
                        val stopX  = matrix.fixX(leftPt.x)
                        val stopY  = matrix.fixY(leftPt.y)
    
                        val (controlPtA, controlPtB) =
                            BranchUtils.toControlPoints(PointF(startX, startY), PointF(stopX, stopY))
    
                        val cmd = listOf<String>(
                            "M${startX},${startY}",
                            "C${controlPtA.x},${controlPtA.y} ${controlPtB.x},${controlPtB.y} ${stopX},${stopY}").joinToString(" ")
                        pathCmdList.add(PathCmd(cmd, branchColor, branchStrokeWidth))
                    }
                }
            }
        }
    }

    // Fill center cells background.
    private val toNodeListener2: (Matrix, MutableList<PathCmd>, Int)->NodeListener = { matrix, pathCmdList, backgroundColor->
        { node->
            if( !node.removed && node.isTypeCenter() ){
                // Fill this cell background to hide branche lines on the center type cell.
                val left   = matrix.fixX(node.x)
                val top    = matrix.fixY(node.y)
                val right  = matrix.fixX(node.x+node.width)
                val bottom = matrix.fixY(node.y+node.height)

                val round = (bottom-top) * 0.5f
                val roundHalf = round * 0.5f

                val cmd = listOf<String>(
                        "M${left},${(top+round)}",
                        "C${left},${(top+roundHalf)} ${(left+roundHalf)},${top} ${(left+round)},${top}",
                        "L${(right-round)},${top}",
                        "C${right-roundHalf},${top} ${right},${(top+roundHalf)} ${right},${(top+round)}",
                        "L${right},${(bottom-round)}",
                        "C${right},${(bottom-roundHalf)} ${(right-roundHalf)},${bottom} ${(right-round)},${bottom}",
                        "L${(left+round)},${bottom}",
                        "C${(left+roundHalf)},${bottom} ${left},${(bottom-roundHalf)} ${left},${(bottom-round)}",
                        "L${left},${top+round}",
                        "z").joinToString(" ")
                pathCmdList.add(PathCmd(cmd, backgroundColor, 0f, true))
            }
        }
    }

    val toStrokeListener: (Matrix, MutableList<PathCmd>, Map<String, Node>)->StrokeListener = { matrix, pathCmdList, nodeMap->
        { stroke->
            val nodeUuid = stroke.xxxUuid
            val node = nodeMap.get(nodeUuid)

            if( node!=null && !node.removed ){
                val nodeCenterX0 = node.x + node.width/2f
                val nodeCenterY0 = node.y + node.height/2f

                val pts = stroke.pts
                val len = pts.size
                val xList = 0.until(len).step(2).map { pts[it] }
                val yList = 1.until(len).step(2).map { pts[it] }

                val cmd = xList.zip(yList).mapIndexed { index, it->
                    val x0 = it.first  + nodeCenterX0
                    val y0 = it.second + nodeCenterY0

                    val x = matrix.fixX(x0)
                    val y = matrix.fixY(y0)

                    if( index==0 ){ "M${x},${y}" } else { "L${x},${y}" }
                }.joinToString(" ")

                pathCmdList.add(PathCmd(cmd, stroke.color, stroke.width*matrix.scale))
            }
        }
    }

    private val dummyPageListener: (Page)->Unit = { }
    // private val dummyNodeListener: NodeListener = {}
    private val dummyStrokeListener: StrokeListener = {}
    private val dummyGroupListener: (Group)->Unit = { }

    val build:(File, Matrix, NodeMap, BackgroundColor)->Pair<StrokePathCmdList, NodePathCmdList> = { mbxFile, matrix, nodeMap, backgroundColor->
        // 2)
        val nodePathCmdList = mutableListOf<PathCmd>()
        val strokePathCmdList = mutableListOf<PathCmd>()
    
        // 2-1)
        MbxParser(mbxFile).proc(
            dummyPageListener,
            toNodeListener1(matrix, nodePathCmdList, nodeMap),
            dummyStrokeListener,
            dummyGroupListener)
    
        // 2-2)
        MbxParser(mbxFile).proc(
            dummyPageListener,
            toNodeListener2(matrix, nodePathCmdList, backgroundColor),
            toStrokeListener(matrix, strokePathCmdList, nodeMap),
            dummyGroupListener)

        Pair(nodePathCmdList, strokePathCmdList)
    }
}

object SVGBuilder {
    val toSVG: (PaperRectangle, BackgroundColor, List<PathCmd>, List<PathCmd>)->String = { paperRectangle, backgroundColor, nodePathCmdList, strokePathCmdList->
        val w = paperRectangle.width
        val h = paperRectangle.height

        val bgR = MyColorUtils.red( backgroundColor )
        val bgG = MyColorUtils.green( backgroundColor )
        val bgB = MyColorUtils.blue( backgroundColor )

        val bgRectPathCmd = listOf<String>(
            "M${paperRectangle.left},${paperRectangle.top}",
            "L${paperRectangle.right},${paperRectangle.top}",
            "L${paperRectangle.right},${paperRectangle.bottom}",
            "L${paperRectangle.left},${paperRectangle.bottom}",
            "z" 
        ).joinToString(" ")

        val f: (PathCmd)->List<String> = {
            val color = it.color
            val r = MyColorUtils.red( color )
            val g = MyColorUtils.green( color )
            val b = MyColorUtils.blue( color )

            if( !it.fill ){
                listOf<String>(
                    "<g stroke=\"rgb(${r},${g},${b})\" stroke-width=\"${it.strokeWidth}\" fill=\"none\">",
                        "<path d=\"${it.cmd}\"/>",
                    "</g>")
            } else {
                listOf<String>(
                    "<g fill=\"rgb(${r},${g},${b})\">",
                        "<path d=\"${it.cmd}\"/>",
                    "</g>")
            }
        }

        val nodePathElements = nodePathCmdList.map(f).flatten().joinToString("")
        val strokePathElements = strokePathCmdList.map(f).flatten().joinToString("")

        listOf<String>(
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>",
            "\n",
            "<!DOCTYPE svg PUBLIC ",
                "\"-//W3C//DTD SVG 1.1//EN\" ",
                "\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">",
            "\n",
            "<svg ",
                "xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" ",
                "x=\"0px\" y=\"0px\" width=\"${w}px\" height=\"${h}px\" ",
                "viewBox=\"0.0 0.0 ${w} ${h}\">",
            "<g fill=\"rgb(${bgR},${bgG},${bgB})\" >",
                "<path d=\"${bgRectPathCmd}\"/>",
            "</g>",
            nodePathElements,
            strokePathElements,
            "</svg>").joinToString("")
    }
}


@SpringBootApplication
class MainApplication
fun main(args: Array<String>) {
    runApplication<MainApplication>(*args) 

    val setText: (File, String)->Unit = {file, content->
        val charset = Charsets.UTF_8
        file.printWriter(charset).use { pw-> pw.println(content) }
    }

    val toInputFileAndOutputFile: (List<String>)->Pair<File?, File?> = { argsAsList->
        if( CmdLineParser.isValid(argsAsList) ){
            val input  = CmdLineParser.toInputOptionValue(argsAsList)
            val output = CmdLineParser.toOutputOptionValue(argsAsList)
            if( input!=null && File(input).exists() && output!=null ){
                Pair(File(input), File(output))
            } else {
                Pair(null, null)
            }
        } else {
            Pair(null, null)
        }
    }

    val (inputFile, outputFile) = toInputFileAndOutputFile( args.map { it } )
    if( inputFile!=null && outputFile!=null ){
        val svgParams = SVGParamsBuilder.parse(inputFile)

        val backgroundColor = svgParams.backgroundColor
        val paperRectangle  = svgParams.paperRectangle
        val matrix  = svgParams.matrix
        val nodeMap = svgParams.nodeMap

        val (nodePathCmdList, strokePathCmdList) = SVGCmdPathBuilder.build(inputFile, matrix, nodeMap, backgroundColor)
        val svg = SVGBuilder.toSVG(paperRectangle, backgroundColor, nodePathCmdList, strokePathCmdList)
        setText(outputFile, svg)
    }
}
