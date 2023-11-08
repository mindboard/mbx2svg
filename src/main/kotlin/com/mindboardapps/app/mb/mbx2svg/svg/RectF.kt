package com.mindboardapps.app.mb.mbx2svg.svg

class RectF(val left: Float, val top: Float, val right: Float, val bottom: Float){
    val width: Float = right - left
    val height: Float = bottom - top

    override fun toString(): String {
        return "$left, $top, $right, $bottom"
    }
}
