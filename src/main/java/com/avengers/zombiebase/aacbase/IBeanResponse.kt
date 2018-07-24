package com.avengers.zombiebase.aacbase

import com.bty.retrofit.net.bean.JsonBeanRequest

/**
 * 用来对主要的操作数据做标记
 */
open class IBeanResponse : JsonBeanRequest() {

    var message: String = ""
    var status: Int = 0
}

/**
 * 请求参数必须实现这个接口，用于标记是请求参数
 */
open class IReqParam : JsonBeanRequest()