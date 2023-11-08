package com.mindboardapps.app.mb.mbx2svg.model

class PaperSize(val width: Int, val height: Int) {
    companion object {
        val A4_LANDSCAPE_WIDTH  = 842
        val A4_LANDSCAPE_HEIGHT = 595
        
        val A5_LANDSCAPE_WIDTH  = A4_LANDSCAPE_WIDTH/2
        val A5_LANDSCAPE_HEIGHT = A4_LANDSCAPE_HEIGHT/2
        
        val A3_LANDSCAPE_WIDTH  = A4_LANDSCAPE_WIDTH*2
        val A3_LANDSCAPE_HEIGHT = A4_LANDSCAPE_HEIGHT*2
    
        val A2_LANDSCAPE_WIDTH  = A3_LANDSCAPE_WIDTH*2
        val A2_LANDSCAPE_HEIGHT = A3_LANDSCAPE_HEIGHT*2
    
        val A1_LANDSCAPE_WIDTH  = A2_LANDSCAPE_WIDTH*2
        val A1_LANDSCAPE_HEIGHT = A2_LANDSCAPE_HEIGHT*2

        val defaultSVGPaperSize = PaperSize(A3_LANDSCAPE_WIDTH, A3_LANDSCAPE_HEIGHT)
    }
}
