package com.avengers.zombiebase.aacbase

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import java.util.concurrent.Executor

/**
 * @author Jervis
 * @date 2018-07-17
 * Repository作用的抽象类
 * haveCache 默认具备缓存能力
 * assembleResult 是一切的开始，它负责组装驱动所有事件发生的关键对数据对象
 * 最低颗粒度的情况下包含：一个请求回来的结果，一个请求状态，一个重试或者叫刷新的函数对象
 *
 */
abstract class Repository<V : IReqParam, T : IBeanResponse>(private val executor: Executor, private var haveCache: Boolean = false) {

    var netWorkState = MutableLiveData<NetworkState>()

    private lateinit var dataSource: LiveData<T>

    /**
     * 组装一个界面主要数据的模型BaseVMResult
     */
    fun assembleResult(args: V): BaseCoreResult<T> {

        dataSource = getLiveData(haveCache, args)

        superRefresh(args)

        return BaseCoreResult(data = dataSource, netWorkState = netWorkState) {
            superRefresh(args)
        }
    }

    private fun superRefresh(args: V) {
        netWorkState.postValue(NetworkState.LOADING)
        refresh(args)
    }

    /**
     * 请求刷新数据
     */
    abstract fun refresh(args: V)

    /**
     * 保存数据。有缓存是需要子类自己实现，不要缓存时直接设置到LiveData中
     */
    fun saveData(t: T) {
        when {
            haveCache -> executor.execute {
                addToDb(t)
                netWorkState.postValue(NetworkState.LOADED)
            }
            else -> {
                (dataSource as MutableLiveData).postValue(t)
                netWorkState.postValue(NetworkState.LOADED)
            }
        }
    }

    /**
     * 获取LiveData，有缓存时取数据库，不要缓存时实例化一个空的LiveData
     */
    private fun getLiveData(haveCache: Boolean, args: V): LiveData<T> {
        return when {
            haveCache -> {
                queryFromDb(args)!!
            }

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
    open fun queryFromDb(args: V): LiveData<T>? {
        return null
    }

    fun haveData(): Boolean {
        return dataSource.value != null
    }

}