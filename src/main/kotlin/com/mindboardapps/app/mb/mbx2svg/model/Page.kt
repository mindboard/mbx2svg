package com.mindboardapps.app.mb.mbx2svg.model

import java.util.UUID

class Page(val uuid: String) {
    companion object {
        fun getInstance(uuid: String, themeConfig: MyThemeConfig): Page {
            val page = Page( uuid )
            
            page.contents = ""
            page.useLabelColor = false
            page.labelColor = MyColor.BLACK
            page.folderId = 0 // as primary folder
            
            page.canvasDx = 0f
            page.canvasDy = 0f
            page.canvasScale = 1f
    
            page.themeConfig = themeConfig
            applyThemeConfig( themeConfig, page )
    
            return page
        }

        private fun applyThemeConfig(themeConfig: MyThemeConfig, page: Page){
            page.pen0Color = themeConfig.pen0Color
            page.pen1Color = themeConfig.pen1Color
            page.pen2Color = themeConfig.pen2Color
    
            page.pen0ColorList = themeConfig.pen0ColorList
            page.pen1ColorList = themeConfig.pen1ColorList
            page.pen2ColorList = themeConfig.pen2ColorList
    
            page.pen0StrokeWidth = themeConfig.pen0StrokeWidth
            page.pen1StrokeWidth = themeConfig.pen1StrokeWidth
            page.pen2StrokeWidth = themeConfig.pen2StrokeWidth
    
            page.branchColorList = themeConfig.branchColorList
            page.borderColorList = themeConfig.borderColorList
            page.backgroundColor = themeConfig.backgroundColor
        }
    }

    var removed: Boolean = false

    var createTime: Long = System.currentTimeMillis()
    var updateTime: Long = System.currentTimeMillis()

    var dataType = IData.DATA_TYPE_PAGE

    var contents: String = ""

    var useLabelColor: Boolean = false
    var labelColor: Int = 0
    var folderId: Int = 0

    var canvasDx: Float = 0f
    var canvasDy: Float = 0f
    var canvasScale: Float = 1f


    var pen0Color: Int? = null
    var pen1Color: Int? = null
    var pen2Color: Int? = null

    var penColorList: List<Int>? = null

    var pen0StrokeWidth: Float = 1f
    var pen1StrokeWidth: Float = 1f
    var pen2StrokeWidth: Float = 1f

    var backgroundColor: Int = 0

    var themeConfig: MyThemeConfig? = null

    var pen0ColorList: List<Int> = listOf<Int>()
    var pen1ColorList: List<Int> = listOf<Int>()
    var pen2ColorList: List<Int> = listOf<Int>()

    var branchColorList: List<Int> = listOf<Int>()
    var borderColorList: List<Int> = listOf<Int>()
}
