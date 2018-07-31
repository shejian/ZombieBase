package com.avengers.zombiebase.aacbase.paging

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.PagedList
import com.avengers.zombiebase.aacbase.IBeanResponse
import com.avengers.zombiebase.aacbase.IReqParam
import com.avengers.zombiebase.aacbase.ItemCoreResult
import com.avengers.zombiebase.aacbase.NetworkState

/**
 * @author Jervis
 * @date 2018-07-17
 * Repository作用的抽象类
 * haveCache 默认具备缓存能力
 * assembleResult 是一切的开始，它负责组装驱动所有事件发生的关键对数据对象
 * 最低颗粒度的情况下包含：一个请求回来的结果，一个请求状态，一个重试或者叫刷新的函数对象
 *
 */
abstract class PagedListRepository<V : IReqParam, T : IBeanResponse>(
        var haveCache: Boolean = true) {

    companion object {
        const val DB_PAGE_SIZE = 20
        const val VISIBLE_THRESHOLD = 5
    }

    //  var refreshState = MutableLiveData<NetworkState>()

    private lateinit var dataSource: LiveData<T>

    /**
     * 组装一个界面主要数据的模型BaseVMResult
     */
    fun assembleResult(args: V): ItemCoreResult<T> {

        var callback = when {
            haveCache -> getCallBack(args)
            else -> null
        }

        val dataSourceFactory = getDataSourceFactory(args)

        val liveDataPagedList = buildLiveDataPagedList(dataSourceFactory, callback)

        return ItemCoreResult(liveDataPagedList,
                getNetworkState(dataSourceFactory, callback),
                getRefreshState(dataSourceFactory),
                { superRefresh(args, dataSourceFactory) },
                { getRetryFun(dataSourceFactory, callback) })
    }

    abstract fun getRefreshState(factory: DataSource.Factory<Int, T>): LiveData<NetworkState>

    abstract fun getRetryFun(factory: DataSource.Factory<Int, T>,
                             callback: PagedListBoundaryCallback<T>?): () -> Unit

    abstract fun getNetworkState(factory: DataSource.Factory<Int, T>,
                                 callback: PagedListBoundaryCallback<T>?): LiveData<NetworkState>

    abstract fun buildLiveDataPagedList(factory: DataSource.Factory<Int, T>,
                                        callback: PagedListBoundaryCallback<T>?): LiveData<PagedList<T>>

    abstract fun getDataSourceFactory(args: V): DataSource.Factory<Int, T>

    abstract fun getCallBack(args: V): PagedListBoundaryCallback<T>

    private fun superRefresh(args: V, factory: DataSource.Factory<Int, T>) {
        refresh(args, factory)
    }

    /**
     * 请求刷新数据
     */
    abstract fun refresh(args: V, factory: DataSource.Factory<Int, T>)

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
    /*   private fun getLiveData(haveCache: Boolean, args: V): LiveData<T> {
           return when {
               haveCache -> queryFromDb(args)!!
               else -> MutableLiveData<T>()
           }
       }
   */
    /**
     * 有缓存时需要自己实现插入数据库的函数
     */
    open fun addToDb(t: T) {

    }

    /**
     * 有缓存时需要自己实现查询数据库的函数
     */
    open fun queryFromDb(args: V): DataSource.Factory<Int, T>? {
        return null
    }


}