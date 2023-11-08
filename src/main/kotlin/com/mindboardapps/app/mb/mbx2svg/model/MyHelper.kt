package com.mindboardapps.app.mb.mbx2svg.model

object MyHelper {
    val toPage: (DataHeader)-> Page = { dataHeader->
        val themeConfig = MyThemeConfig()

        val newPage = Page.getInstance(
            XJson.fixUuid(dataHeader.pageUuid),
            themeConfig)
        
        newPage.contents = dataHeader.contents
        newPage.useLabelColor = dataHeader.useLabelColor
        newPage.labelColor = dataHeader.labelColor
        newPage.canvasScale = dataHeader.canvasScale

        newPage.removed = dataHeader.removed
        newPage.folderId = dataHeader.folderId
        
        if( dataHeader.pen0Color!=null ){ newPage.pen0Color = dataHeader.pen0Color }
        if( dataHeader.pen1Color!=null ){ newPage.pen1Color = dataHeader.pen1Color }
        if( dataHeader.pen2Color!=null ){ newPage.pen2Color = dataHeader.pen2Color }

        dataHeader.pen0ColorList?.let { newPage.pen0ColorList = it }
        dataHeader.pen1ColorList?.let { newPage.pen1ColorList = it }
        dataHeader.pen2ColorList?.let { newPage.pen2ColorList = it }

        // override theme default background color with specific one.
        newPage.backgroundColor = dataHeader.backgroundColor
        
        newPage
    }
}
