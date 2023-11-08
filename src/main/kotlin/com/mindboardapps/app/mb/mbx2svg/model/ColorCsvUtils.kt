package com.mindboardapps.app.mb.mbx2svg.model

object ColorCsvUtils {
    fun toList(csv: String?): List<Int> {
        if( csv==null ){
            return listOf<Int>()
        }

        val colorList: List<String> = csv.split(",")
        if( colorList.size==0 ){
            return listOf<Int>()
        }

        return colorList.map { it.toInt() }
    }    
    
    fun toString(list: List<Int>): String{
        return list.joinToString(",")
    }
}
