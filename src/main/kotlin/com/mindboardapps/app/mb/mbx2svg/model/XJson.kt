package com.mindboardapps.app.mb.mbx2svg.model

import java.util.UUID

object XJson {
    val NULL_UUID= "0"

    val fixUuid: (String?)->String = { uuidValue->
        if( uuidValue!=null ){ uuidValue } else { UUID.randomUUID().toString() }
    }

    val fixUpdateTime: (Long?)->Long = { updateTime->
        if( updateTime!=null ){ updateTime } else { System.currentTimeMillis() }
    }
}

