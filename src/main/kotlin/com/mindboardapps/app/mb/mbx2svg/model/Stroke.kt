package com.mindboardapps.app.mb.mbx2svg.model

import com.google.gson.stream.JsonReader

class Stroke(val pts: FloatArray, val uuid: String) {
    companion object {
        fun getInstance(
            pts: FloatArray,
            uuid: String,
            myXxxUuid: String,
            inGroup: Boolean,
            parentGroupUuid: String?, 
            width: Float?,
            color: Int?,
            
            left: Float,
            top: Float,
            right: Float,
            bottom: Float,

            updateTime: Long,
            removed: Boolean,
            doubleRemoved: Boolean): Stroke {
    
            val st = Stroke(pts, uuid)
    
            st.xxxUuid = myXxxUuid 
    
            st.updateTime = updateTime
            st.removed = removed 
            st.doubleRemoved = doubleRemoved
    
            st.inGroup = inGroup

            parentGroupUuid?.let {
                st.parentGroupUuid = it
            }
    
            width?.let { st.width = it }
            color?.let { st.color = it }
    
            st.left = left
            st.top = top
            st.right = right
            st.bottom = bottom
    
            return st
        }
    }

    var xxxUuid: String = XJson.NULL_UUID

    var inGroup: Boolean = false
    var parentGroupUuid: String = XJson.NULL_UUID

    var left: Float = 0f
    var top: Float = 0f
    var right: Float = 0f
    var bottom: Float = 0f

    var width: Float = 0f
    var color: Int = MyColor.BLACK

    var updateTime: Long = 0L
    var removed: Boolean = false
    var doubleRemoved: Boolean = false
}


object StrokeJson {
    val loadFromJsonReaderGetPts: (JsonReader)-> FloatArray = { reader->
        val ptList = mutableListOf<Float>()

        reader.beginArray()
        while( reader.hasNext() ){
            var x: Float? = null
            var y: Float? = null

            reader.beginObject();
            while( reader.hasNext() ){
                val name2 = reader.nextName()
                when(name2){
                    "x" -> { x = reader.nextDouble().toFloat() }
                    "y" -> { y = reader.nextDouble().toFloat() }
                    else -> {
                        reader.skipValue()
                    }
                }
            }
            reader.endObject();

            x?.let { xDash->
                y?.let { yDash->
                    ptList.add(xDash)
                    ptList.add(yDash)
                }
            }
        }
        reader.endArray()

        ptList.toFloatArray()
    }

    val loadFromJsonReader: (JsonReader)->Stroke = { reader->
        var pts: FloatArray? = null

        var uuid: String?  = null
        var xxxUuid: String? = null
        var nodeUuid: String? = null

        var updateTime: Long? = null
        var removed: Int? = null
        var inGroup:Boolean = false
        var parentGroupUuid: String? = null
        var strokeWidth: Float? = null
        var color: Int? = null

        var left: Float = 0f
        var top: Float = 0f
        var right: Float = 0f
        var bottom: Float = 0f


        reader.beginObject()

        while( reader.hasNext() ){
            val name1 = reader.nextName()
            when(name1) {
                "ptList" -> { pts = loadFromJsonReaderGetPts( reader ) }
                "uuid" -> { uuid = reader.nextString() }
                "xxxUuid"  -> { xxxUuid = reader.nextString() }
                "nodeUuid" -> { nodeUuid = reader.nextString() }
                "updateTime" -> {  updateTime = reader.nextLong() }

                "removed" -> { removed = reader.nextInt() }
                "inGroup" -> { inGroup = reader.nextBoolean() }

                "parentGroupUuid" -> { parentGroupUuid = reader.nextString() }
                "strokeWidth"     -> { strokeWidth = reader.nextDouble().toFloat() }
                "color" ->   {  color = reader.nextInt() }

                "left" ->   { left  = reader.nextDouble().toFloat() }
                "top" ->    { top   = reader.nextDouble().toFloat() }
                "right" ->  { right = reader.nextDouble().toFloat() }
                "bottom" -> { bottom= reader.nextDouble().toFloat() }
                else -> {
                    reader.skipValue()
                }
            }
        }
        reader.endObject()

        if( pts==null ){ 
            pts = floatArrayOf()
        }

        var myRemoved = true
        if( removed!=null && removed==0 ){ myRemoved = false }

        var myDoubleRemoved = false
        if( removed!=null && removed==2 ) { myDoubleRemoved = true }

        var myXxxUuid: String? = if( xxxUuid!=null ){ xxxUuid } else { nodeUuid }

        Stroke.getInstance(
            pts,
            XJson.fixUuid(uuid),
            XJson.fixUuid(myXxxUuid),
            inGroup,
            parentGroupUuid, 
            strokeWidth, color,
            left,top,right,bottom,
            XJson.fixUpdateTime(updateTime),
            myRemoved,
            myDoubleRemoved )
    }
}
