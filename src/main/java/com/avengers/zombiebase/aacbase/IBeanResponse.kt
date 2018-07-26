package com.avengers.zombiebase.aacbase

import android.arch.persistence.room.Ignore
import com.bty.retrofit.net.bean.JsonBeanRequest
import com.bty.retrofit.net.bean.JsonBeanResponse

/**
 * 用来对主要的操作数据做标记
 */
open class IBeanResponse : JsonBeanResponse() {
    @Ignore
    var message: String = ""
    @Ignore
    var status: String = ""
}

/**
 * 请求参数必须实现这个接口，用于标记是请求参数
 */
open class IReqParam : JsonBeanRequest() {

}
