package com.mindboardapps.app.mb.mbx2svg.model

class DataHeader {
    var themeName: String = XDefaultThemeConfig.NAME

    var empty: Boolean = false
    var version: String? = null
    var deviceUuid: String? = null
    var pageUuid: String? = null
    var updateTime: Long? = null

    var removed: Boolean = false
    var folderId: Int = 0
    var canvasScale: Float = 1f

    var contents: String = ""
    var useLabelColor: Boolean = false
    var labelColor: Int = MyColor.BLACK //-16777216 This is Color.BLACK value.

    var pen0Color: Int? = null
    var pen1Color: Int? = null
    var pen2Color: Int? = null

    var penColorList: List<Int>? = null

    var pen0ColorList: List<Int>? = null
    var pen1ColorList: List<Int>? = null
    var pen2ColorList: List<Int>? = null

    var backgroundColor: Int = MyColor.WHITE

    var branchColorList: List<Int>? = null
    var borderColorList: List<Int>? = null

    var branchColor: Int = 0 
    var borderColor: Int = 0
}
