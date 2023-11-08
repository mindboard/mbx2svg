package com.mindboardapps.app.mb.mbx2svg.model

data class MyColorData(val r:Int, val g:Int, val b:Int)

object AndroidColorUtils {
    fun alpha(color: UInt): Int {
        return color.toInt() ushr 24
    }

    fun red(color: UInt): Int {
        return color.toInt() shr 16 and 0xFF
    }

    fun green(color: UInt): Int {
        return color.toInt() shr 8 and 0xFF
    }

    fun blue(color: UInt): Int {
        return color.toInt() and 0xFF
    }

    val toMyColorData: (UInt)->MyColorData = { androidColor->
        MyColorData(
            red(androidColor), 
            green(androidColor), 
            blue(androidColor))
    }

    val toAndroidColor: (MyColorData)->Int = { myColor->
        (0xFF shl 24) or (myColor.r shl 16 ) or (myColor.g shl 8) or myColor.b
    }
}

class MyColor {
    companion object {
        val BLACK: Int = AndroidColorUtils.toAndroidColor( AndroidColorUtils.toMyColorData(0xFF000000u) )
        val WHITE: Int = AndroidColorUtils.toAndroidColor( AndroidColorUtils.toMyColorData(0xFFFFFFFFu) )

        val rgb: (Int, Int, Int)->Int = { r,g,b->
            (0xFF shl 24) or (r shl 16 ) or (g shl 8) or b
        }

        val argb: (Int, Int, Int, Int)->Int = { a,r,g,b->
            (a shl 24) or (r shl 16 ) or (g shl 8) or b
        }
    }
}
