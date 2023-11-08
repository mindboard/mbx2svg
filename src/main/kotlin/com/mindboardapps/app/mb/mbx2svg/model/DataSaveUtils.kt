package com.mindboardapps.app.mb.mbx2svg.model

object DataSaveUtils {
    val toDataTypeAsInt: (String)-> Int = { dataType->
          when(dataType){
              "PAGE"   -> IData.DATA_TYPE_PAGE
              "NODE"   -> IData.DATA_TYPE_NODE
              "STROKE" -> IData.DATA_TYPE_STROKE
              "GROUP"  -> IData.DATA_TYPE_GROUP
              else     -> IData.DATA_TYPE_UNKNOWN
        }
    }    
}
