package com.mindboardapps.app.mb.mbx2svg

import java.io.File
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.FileInputStream
import java.util.zip.GZIPInputStream
import com.google.gson.stream.JsonReader
import com.mindboardapps.app.mb.mbx2svg.model.DataHeader
import com.mindboardapps.app.mb.mbx2svg.model.IData
import com.mindboardapps.app.mb.mbx2svg.model.Page
import com.mindboardapps.app.mb.mbx2svg.model.Node
import com.mindboardapps.app.mb.mbx2svg.model.Stroke
import com.mindboardapps.app.mb.mbx2svg.model.Group
import com.mindboardapps.app.mb.mbx2svg.model.MyHelper
import com.mindboardapps.app.mb.mbx2svg.model.DataSaveUtils
import com.mindboardapps.app.mb.mbx2svg.model.NodeJson
import com.mindboardapps.app.mb.mbx2svg.model.StrokeJson
import com.mindboardapps.app.mb.mbx2svg.model.GroupJson
import com.mindboardapps.app.mb.mbx2svg.model.ColorCsvUtils

class MbxParser(val mbxFile: File){
    fun proc(
        pageListener:   (Page)->Unit,
        nodeListener:   (Node)->Unit,
        strokeListener: (Stroke)->Unit,
        groupListener:  (Group)->Unit) {

        JsonReader(BufferedReader(
            InputStreamReader(
                GZIPInputStream(
                    FileInputStream(mbxFile)), Charsets.UTF_8))).use { reader->
        
            val dataHeader = DataHeader()
        
            var headerFound = false
        
            reader.beginObject()
            while( reader.hasNext() ){
                val name1 = reader.nextName()
                if( name1 == "header" ){
                    headerFound = true
                    reader.beginObject()
                    while( reader.hasNext() ){
                        val name2 = reader.nextName()
                        when(name2){
                            "version" ->     dataHeader.version = reader.nextString()
                            "updateTime" ->  dataHeader.updateTime = reader.nextLong() 
                            "deviceUuid" ->  dataHeader.deviceUuid = reader.nextString() 
                            "pageUuid" ->    dataHeader.pageUuid = reader.nextString() 
                            "themeName" ->   dataHeader.themeName = reader.nextString() 
                            "canvasScale" -> dataHeader.canvasScale = reader.nextDouble().toFloat()
                            "contents" ->    dataHeader.contents = reader.nextString() 
                            "useLabelColor"->dataHeader.useLabelColor = reader.nextBoolean() 
                            "labelColor" ->  dataHeader.labelColor = reader.nextInt() 
        
                            "penColor0" ->   dataHeader.pen0Color = reader.nextInt() 
                            "penColor1" ->   dataHeader.pen1Color = reader.nextInt() 
                            "penColor2" ->   dataHeader.pen2Color = reader.nextInt() 
        
                            "penColorList"    -> dataHeader.penColorList = ColorCsvUtils.toList( reader.nextString() )
                            "backgroundColor" -> dataHeader.backgroundColor =  reader.nextInt()
        
                            "pen0ColorList" -> dataHeader.pen0ColorList = ColorCsvUtils.toList( reader.nextString() ) 
                            "pen1ColorList" -> dataHeader.pen1ColorList = ColorCsvUtils.toList( reader.nextString() ) 
                            "pen2ColorList" -> dataHeader.pen2ColorList = ColorCsvUtils.toList( reader.nextString() ) 
        
                            "branchColor" -> dataHeader.branchColor = reader.nextInt() 
                            "borderColor" -> dataHeader.borderColor = reader.nextInt() 
        
                            "folderId"  -> dataHeader.folderId = reader.nextInt() 
                            "removed"   -> dataHeader.removed =  reader.nextBoolean() 
        
                            else        -> reader.skipValue()
                        }
                    }
                    reader.endObject()
        
                    val penColorList = dataHeader.penColorList
                    if( penColorList!=null ){
                        if( dataHeader.pen0ColorList==null ) {
                            dataHeader.pen0ColorList = penColorList.map { it }
                        }
                        if( dataHeader.pen1ColorList==null ) {
                            dataHeader.pen1ColorList = penColorList.map { it }
                        }
                        if( dataHeader.pen2ColorList==null ) {
                            dataHeader.pen2ColorList = penColorList.map { it }
                        }
                    }
        
                    dataHeader.empty = false
                }
                else if( name1=="list" && headerFound ){
                    val newPage = MyHelper.toPage( dataHeader )
                    pageListener(newPage)
        
                    reader.beginArray()
                    while (reader.hasNext()) {
                        var dataTypeAsString: String? = null
        
                        reader.beginObject()
                        while( reader.hasNext() ){
                            val name2 = reader.nextName();
                            when( name2 ){
                                "dataType" -> {
                                    dataTypeAsString = reader.nextString()
                                }
                                "json" -> {
                                    var proced = false
            
                                    // Pay attention for depending on parameters order in JSON.
                                    // This code would not work if _dataType_ parameter is first.
                                    if( dataTypeAsString!=null ){
                                        try {
                                            val dataTypeAsInt =  DataSaveUtils.toDataTypeAsInt( dataTypeAsString )
            
                                            if( dataTypeAsInt==IData.DATA_TYPE_NODE  ){
                                                val node = NodeJson.loadFromJsonReader(reader)
                                                nodeListener(node)
                                                proced = true
                                            }
                                            else if( dataTypeAsInt==IData.DATA_TYPE_STROKE ){
                                                val stroke = StrokeJson.loadFromJsonReader(reader);
                                                strokeListener(stroke)
                                                proced = true
                                            }
                                            else if( dataTypeAsInt==IData.DATA_TYPE_GROUP ){
                                                val group = GroupJson.loadFromJsonReader(reader)
                                                groupListener(group)
                                                proced = true
                                            }
                                        } catch(ex: Exception){
                                            println( "Error: ${ex}" )
                                        }
                                    }
            
                                    if( !proced ){
                                        reader.skipValue()
                                    }
                                }
                                else -> {
                                    reader.skipValue()
                                }
                            }
                            // close when with name2
                        }
                        reader.endObject()
                    }
                    reader.endArray()
                }
                else {
                    reader.skipValue()
                }
            }
            reader.endObject()
        
            if( !headerFound ){
                println("Error: Header Not Found!")
            }
        }
    }    
}
