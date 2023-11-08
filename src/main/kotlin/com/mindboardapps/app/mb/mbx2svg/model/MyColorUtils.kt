package com.mindboardapps.app.mb.mbx2svg.model

object MyColorUtils {
    fun alpha(color: Int): Int {
        return color.toInt() ushr 24
    }

    fun red(color: Int): Int {
        return color.toInt() shr 16 and 0xFF
    }

    fun green(color: Int): Int {
        return color.toInt() shr 8 and 0xFF
    }

    fun blue(color: Int): Int {
        return color.toInt() and 0xFF
    }

    val fixColor: (Int, Int)->Int = { parentColor, selfColor->
        val selfAlpha = alpha(selfColor)

        if( selfAlpha>=255 ){
            selfColor
        } else {
            val srcR = red(parentColor)/255f
            val srcG = green(parentColor)/255f
            val srcB = blue(parentColor)/255f
            val dstR = red(selfColor)/255f
            val dstG = green(selfColor)/255f
            val dstB = blue(selfColor)/255f
    
            val alpha = selfAlpha / 255f
            
            val newR = srcR * alpha + dstR * (1f - alpha)
            val newG = srcG * alpha + dstG * (1f - alpha)
            val newB = srcB * alpha + dstB * (1f - alpha)
    
            MyColor.argb(
                255,
                (newR*255f).toInt(),
                (newG*255f).toInt(),
                (newB*255f).toInt() )
        }    
    }

    fun fix(c: Int): Int {
        val a = 186
        return fix(c,a)
    }

    fun fix(c: Int, alpha: Int): Int {
        val r = red(c)
        val g = green(c)
        val b = blue(c)

        return MyColor.argb(alpha, r, g, b)
    }
}
