package com.mindboardapps.app.mb.mbx2svg.utils

import com.mindboardapps.app.mb.mbx2svg.model.Node
import com.mindboardapps.app.mb.mbx2svg.svg.PointF

object BranchUtils {
    val CONNECTION_LINE_WIDTH = 20f

    val toLineStrokeWidth: (Int)->Float = { childCount->
        val r = childCount/16f + 1.0f
        CONNECTION_LINE_WIDTH * r
    }

    val toChildCount: (Map<String, Node>, Node)->Int = { nodeMap, node->
        val targetUuid = node.uuid
        nodeMap.values.filter { it.parentNodeUuid == targetUuid }.size
    }

    val toCenterPoint: (Node)->PointF = { node->
        val x = node.x + node.width*0.5f
        val y = node.y + node.height*0.5f
        PointF(x, y)
    }

    val toLeftPoint: (Node)->PointF = { node->
        val y = node.y + node.height*0.5f
        PointF(node.x, y)
    }

    val toRightPoint: (Node)->PointF = { node->
        val y = node.y + node.height*0.5f
        PointF( (node.x + node.width), y)
    }

    val toControlPoints: (PointF, PointF)->Pair<PointF, PointF> = { startPt, stopPt->
        val startX = startPt.x
        val startY = startPt.y
        val stopX = stopPt.x
        val stopY = stopPt.y

        val controlXa = (startX + stopX)*0.5f
        val controlYa = startY
        val controlXb = controlXa
        val controlYb = stopY

        Pair(
            PointF(controlXa, controlYa), 
            PointF(controlXb, controlYb))
    }

    val isCollapsed: (Node)->Boolean = { node->
        // do mask 128 (=10000000) 
        // and then if value is 128, first bit value 1.
        ( ( node.borderType and 128 ) == 128 )
    }
}
