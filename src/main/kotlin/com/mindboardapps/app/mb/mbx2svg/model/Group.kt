package com.mindboardapps.app.mb.mbx2svg.model

import com.google.gson.stream.JsonReader

class Group(val uuid: String){
    companion object {
        fun getInstance(
            uuid: String,
            xxxUuid: String,
            inGroup: Boolean,
            priority: Int,
            parentGroupUuid: String?,
            
            x: Float,
            y: Float,
            width: Float,
            height: Float,

            updateTime: Long,
            removed: Boolean,
            doubleRemoved: Boolean ): Group{

            val g = Group(uuid)
    
            g.xxxUuid = xxxUuid
            g.inGroup = inGroup
            g.priority = priority
    
            parentGroupUuid?.let { g.parentGroupUuid = parentGroupUuid }
    
            g.x = x
            g.y = y
            g.width = width
            g.height = height 
    
            g.updateTime = updateTime
            g.removed = removed
            g.doubleRemoved = doubleRemoved
    
            return g
        }
    }

    var xxxUuid: String = XJson.NULL_UUID

    var inGroup: Boolean = false
    var priority: Int = 0

    var parentGroupUuid: String? = null

    var x: Float = 0f
    var y: Float = 0f
    var width: Float = 0f
    var height: Float = 0f

    var updateTime: Long = 0L
    var removed: Boolean = false
    var doubleRemoved: Boolean = false
}

object GroupJson {
    val loadFromJsonReader: (JsonReader)->Group = { reader->
        var uuid: String? = null
        var xxxUuid: String? = null
        var nodeUuid: String? = null
        var inGroup: Boolean = false
        var priority: Int = 0
        var parentGroupUuid: String? = null

        var x: Float = 0f
        var y: Float = 0f
        var width: Float = 0f
        var height: Float = 0f

        var updateTime: Long? = null
        var removed: Int? = null

        reader.beginObject()
        while( reader.hasNext() ){
            val name1 = reader.nextName()
            when(name1){
                "uuid" -> { uuid = reader.nextString() }
                "xxxUuid" -> {      xxxUuid = reader.nextString() }
                "nodeUuid" -> {     nodeUuid = reader.nextString() }
                "inGroup" -> {      inGroup = reader.nextBoolean() }
                "priority" -> {     priority = reader.nextInt() }
                "parentGroupUuid" -> {       parentGroupUuid = reader.nextString() }

                "x" ->       {  x      = reader.nextDouble().toFloat() }
                "y" ->       {  y      = reader.nextDouble().toFloat() }
                "width" ->   {  width  = reader.nextDouble().toFloat() }
                "height" ->  {  height = reader.nextDouble().toFloat() }

                "updateTime" -> {   updateTime = reader.nextLong() }
                "removed" -> {      removed = reader.nextInt() }
                else -> { reader.skipValue() }
            }
        }
        reader.endObject()

        var myRemoved = true
        if( removed!=null && removed==0 ){ myRemoved = false }

        var myDoubleRemoved = false
        if( removed!=null && removed==2 ) { myDoubleRemoved = true }

        var myXxxUuid: String? = if( xxxUuid!=null ){ xxxUuid } else { nodeUuid }

        Group.getInstance(
            XJson.fixUuid(uuid),
            XJson.fixUuid(myXxxUuid),
            inGroup,
            priority,
            parentGroupUuid,
            x,y,width,height,
            XJson.fixUpdateTime(updateTime),
            myRemoved,
            myDoubleRemoved )
    }
}
