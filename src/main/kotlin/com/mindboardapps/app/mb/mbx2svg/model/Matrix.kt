package com.mindboardapps.app.mb.mbx2svg.model

class Matrix(val dx: Float, val dy: Float, val scale: Float) {
    fun fixX(x: Float): Float { return (x - dx) * scale }
    fun fixY(y: Float): Float { return (y - dy) * scale }

    override fun toString(): String {
        return "($dx, $dy), $scale"
    }
}

