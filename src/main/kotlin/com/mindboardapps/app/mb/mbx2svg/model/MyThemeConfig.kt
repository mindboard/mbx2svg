package com.mindboardapps.app.mb.mbx2svg.model

object XDefaultThemeConfig {
    val NAME = "WHITEBOARD"
}

class MyThemeConfig {
    private companion object {
        val BACKGROUND_COLOR = MyColor.WHITE

        val DRAWING_C0 = MyColor.argb(255,  31,31,31) // default black
        val DRAWING_C1 = MyColor.argb(255,  31,31,91) // blue
        val DRAWING_C2 = MyColor.argb(255, 150,57,3)  // red
        val DRAWING_C3 = MyColor.argb(255,  99,140,11)// green
        val DRAWING_C4 = MyColor.argb(255, 255, 173, 41) // orange
        val DRAWING_C5 = MyColor.argb(255, 204, 204, 204) // disabled color (gray)

        val PEN_0 = DRAWING_C0
        val PEN_1 = DRAWING_C1
        val PEN_2 = DRAWING_C2
        val PEN_3 = DRAWING_C3
        val PEN_4 = DRAWING_C4
        val PEN_5 = DRAWING_C5
    
        val BRANCH_0 = MyColorUtils.fixColor(BACKGROUND_COLOR, MyColorUtils.fix(PEN_0,232))
        val BRANCH_1 = MyColorUtils.fixColor(BACKGROUND_COLOR, MyColorUtils.fix(PEN_1))
        val BRANCH_2 = MyColorUtils.fixColor(BACKGROUND_COLOR, MyColorUtils.fix(PEN_2)) 
        val BRANCH_3 = MyColorUtils.fixColor(BACKGROUND_COLOR, MyColorUtils.fix(PEN_3))
        val BRANCH_4 = MyColorUtils.fixColor(BACKGROUND_COLOR, MyColorUtils.fix(PEN_4))
        val BRANCH_5 = MyColorUtils.fixColor(BACKGROUND_COLOR, MyColorUtils.fix(PEN_5))
    
        val BORDER_0 = BRANCH_0
        val BORDER_1 = BRANCH_1
        val BORDER_2 = BRANCH_2
        val BORDER_3 = BRANCH_3
        val BORDER_4 = BRANCH_4
        val BORDER_5 = BRANCH_5

        val DEFAULT_PEN_STROKE_WIDTH = 2.8f // MB2019's value
    }

    val name: String = XDefaultThemeConfig.NAME

    //
    // default color or current color
    //
    val pen0Color: Int = PEN_0
    val pen1Color: Int = PEN_0
    val pen2Color: Int = PEN_0

    val pen0StrokeWidth: Float = DEFAULT_PEN_STROKE_WIDTH
    val pen1StrokeWidth: Float = DEFAULT_PEN_STROKE_WIDTH
    val pen2StrokeWidth: Float = DEFAULT_PEN_STROKE_WIDTH

    val branchColor: Int = BRANCH_0
    val borderColor: Int = BORDER_0

    val backgroundColor:Int = BACKGROUND_COLOR

    val pen0ColorList = listOf<Int>(PEN_0, PEN_1, PEN_2, PEN_3, PEN_4, PEN_5)
    val pen1ColorList = listOf<Int>(PEN_0, PEN_1, PEN_2, PEN_3, PEN_4, PEN_5)
    val pen2ColorList = listOf<Int>(PEN_0, PEN_1, PEN_2, PEN_3, PEN_4, PEN_5)

    val branchColorList = listOf<Int>(BRANCH_0, BRANCH_1, BRANCH_2, BRANCH_3, BRANCH_4, BRANCH_5)
    val borderColorList = listOf<Int>(BORDER_0, BORDER_1, BORDER_2, BORDER_3, BORDER_4, BORDER_5)
}
