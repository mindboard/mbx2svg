package com.mindboardapps.app.mb.mbx2svg.model

import com.google.gson.stream.JsonReader
import com.mindboardapps.app.mb.mbx2svg.utils.BranchUtils

class Node(val uuid: String, val pageUuid: String) {
    companion object {
        fun getInstance(
            uuid: String,
            pageUuid: String,
            parentNodeUuid: String?,

            borderColor: Int, borderType: Int, branchColor: Int,
            typeMainCenter: Boolean, typeDefaultCenter: Boolean,

            x: Float,
            y: Float, 
            width: Float,
            height: Float,

            canvasDx: Float,
            canvasDy: Float,
            canvasScale: Float,
            updateTime: Long,
            myRemoved: Boolean ): Node {

            val node = Node(uuid, pageUuid)

            parentNodeUuid?.let { node.parentNodeUuid = it }

            node.borderColor = borderColor
            node.borderType =  borderType
            node.branchColor = branchColor

            node.typeMainCenter = typeMainCenter
            node.typeDefaultCenter = typeDefaultCenter
            
            node.x = x
            node.y = y 
            node.width = width
            node.height = height

            node.canvasDx = canvasDx
            node.canvasDy = canvasDy
            node.canvasScale = canvasScale

            node.updateTime = updateTime
            node.removed = myRemoved

            return node
        }
    }

    var parentNodeUuid: String? = null

    var canvasDx: Float = 0f
    var canvasDy: Float = 0f
    var canvasScale: Float = 1.45f


    var typeDefaultCenter: Boolean = false
    var typeMainCenter: Boolean = false
    val isTypeCenter: ()->Boolean = { ( typeDefaultCenter || typeMainCenter ) }

    val isTypeLeft: (Node)->Boolean = { parentNode->
        !isTypeRight(parentNode)
    }
    val isTypeRight: (Node)->Boolean = { parentNode->
        BranchUtils.toCenterPoint(parentNode).x < BranchUtils.toCenterPoint(this).x 
    }

    var x: Float = 0f
    var y: Float = 0f
    var width: Float = 0f
    var height: Float = 0f

    var borderColor: Int = 0
    var borderType: Int = 0
    var branchColor: Int = 0

    var updateTime: Long = 0L
    var removed: Boolean = false
}

object NodeJson {
    val loadFromJsonReader: (JsonReader)->Node = { reader->
        var uuid: String? = null
        var pageUuid: String? = null
        var parentNodeUuid: String? = null

        var borderColor: Int = 0
        var borderType: Int = 0
        var branchColor: Int = 0

        var typeMainCenter = false
        var typeDefaultCenter = false

        var x: Float = 0f
        var y: Float = 0f
        var width: Float = 0f
        var height: Float = 0f

        var canvasDx: Float = 0f
        var canvasDy: Float = 0f
        var canvasScale: Float = 1f

        var updateTime: Long? = null
        var removed: Int? = null

        reader.beginObject()
        while( reader.hasNext() ){
            val name1 = reader.nextName()
            when(name1){
                "uuid" -> { uuid = reader.nextString() }
                "pageUuid" -> { pageUuid = reader.nextString() }
                "parentNodeUuid" -> { parentNodeUuid = reader.nextString() }
                "borderColor" -> { borderColor = reader.nextInt() }
                "borderType" -> { borderType = reader.nextInt() }
                "branchColor" -> { branchColor = reader.nextInt() }
                "typeMainCenter" -> { typeMainCenter = reader.nextBoolean() }
                "typeDefaultCenter" -> { typeDefaultCenter = reader.nextBoolean() }
                "x" -> { x = reader.nextDouble().toFloat() }
                "y" -> { y = reader.nextDouble().toFloat() }
                "width" ->  { width  = reader.nextDouble().toFloat() }
                "height" -> { height = reader.nextDouble().toFloat() }
                "canvasDx" -> { canvasDx = reader.nextDouble().toFloat() }
                "canvasDy" -> { canvasDy = reader.nextDouble().toFloat() }
                "canvasScale" -> { canvasScale = reader.nextDouble().toFloat() }
                "updateTime" -> { updateTime = reader.nextLong() }
                "removed" -> { removed = reader.nextInt() }
                else ->  { reader.skipValue() }
            }
        }
        reader.endObject()

        var myRemoved = true
        if( removed!=null && removed==0 ){ myRemoved = false }

        Node.getInstance(
            XJson.fixUuid(uuid),
            XJson.fixUuid(pageUuid),
            parentNodeUuid,
            borderColor, borderType, branchColor,
            typeMainCenter, typeDefaultCenter,
            x,y,width,height,
            canvasDx,canvasDy,canvasScale,
            XJson.fixUpdateTime(updateTime),
            myRemoved )
    }
}
