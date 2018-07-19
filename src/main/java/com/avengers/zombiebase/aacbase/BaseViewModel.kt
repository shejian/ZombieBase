package com.avengers.zombiebase.aacbase

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.map
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel

/**
 * @author Jervis
 * @data 2018-07-17
 * ViewModel的一个基础类，给他已经强制定义了一些网络请求发生后必须要使用的一些基础函数和对象，复杂业务需要自行继承并扩展
 * 说明如下
 * queryParam ：请求条件参数
 * result ：触发请求事件的关键
 * liveData ：请求结果，提供给View显示的关键
 * netWorkState ：网络状态
 * refresh() ：对外提供的刷新重试函数
 */
open class BaseViewModel<K : IReqParam,T : IBeanResponse>(private val repository: Repository<K,T>) : ViewModel() {

    private val queryParam = MutableLiveData<K>()

    private val result = map(queryParam) { repository.assembleResult(it) }

    val liveData = switchMap(result) { it.data }!!

    val netWorkState = switchMap(result) { it.netWorkState }!!

    fun refresh() {
        result.value?.refresh?.invoke()
    }

    fun request(value: K) {
        queryParam.postValue(value)
    }


}