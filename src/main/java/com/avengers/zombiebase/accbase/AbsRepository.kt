package com.avengers.zombiebase.accbase

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

/**
 * @author Jervis
 * @data 2018-07-17
 * Repository作用的抽象类
 * haveCache 默认具备缓存能力
 * assemblyVMResult 是一切的开始，它负责组装驱动所有事件发生的关键对数据对象
 * 最低颗粒度的情况下包含：一个请求回来的结果，一个请求状态，一个重试或者叫刷新的函数对象
 *
 */
abstract class AbsRepository<T : IBeanResponse>(private var haveCache: Boolean = true) {

    var netWorkState = MutableLiveData<NetworkState>()

    private lateinit var dataSource: LiveData<T>

    /**
     * 组装一个界面主要数据的模型BaseVMResult
     */
    fun assemblyVMResult(vararg args: Any): BaseCoreResult<T> {

        dataSource = getLiveData(haveCache, args)

        superRefresh(args)

        return BaseCoreResult(dataSource, netWorkState) {
            superRefresh(args)
        }
    }

    private fun superRefresh(vararg args: Any) {
        netWorkState.postValue(NetworkState.LOADING)
        refresh(args)
    }

    /**
     * 请求刷新数据
     */
    abstract fun refresh(vararg args: Any)

    /**
     * 保存数据。有缓存是需要子类自己实现，不要缓存时直接设置到LiveData中
     */
    fun saveData(t: T) {
        when {
            haveCache -> addToDb(t)
            else -> (dataSource as MutableLiveData).postValue(t)
        }
    }


    /**
     * 获取LiveData，有缓存时取数据库，不要缓存时实例化一个空的LiveData
     */
    private fun getLiveData(haveCache: Boolean, vararg args: Any): LiveData<T> {
        return when {
            haveCache -> queryFromDb(args)!!
            else -> MutableLiveData<T>()
        }
    }

    /**
     * 有缓存时需要自己实现插入数据库的函数
     */
    open fun addToDb(t: T) {

    }

    /**
     * 有缓存时需要自己实现查询数据库的函数
     */
    open fun queryFromDb(vararg args: Any): LiveData<T>? {
        return null
    }


}