package com.avengers.zombiebase.aacbase.paging

import android.arch.paging.PagedList
import com.avengers.appwakanda.webapi.createStatusLiveData
import com.avengers.zombiebase.ApplicationInitBase

//分页的边界回调类
abstract class PagedListBoundaryCallback<T> : PagedList.BoundaryCallback<T>() {
    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    //实例化PagingRequestHelper
    val helper = PagingRequestHelper(ApplicationInitBase.getInstanceExecutors().diskIO())

    //在该函数中，实现了对三种请求状态的数据监听，其结果在内部做了监控
    val networkState = helper.createStatusLiveData()

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            loadInit()
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: T) {
        // ignored, since we only ever append to what's in the DB
    }

    override fun onItemAtEndLoaded(itemAtEnd: T) {
        super.onItemAtEndLoaded(itemAtEnd)
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            loadMore(itemAtEnd, it)
        }
    }


    abstract fun loadInit()
    abstract fun loadMore(itemAtEnd: T, callBack: PagingRequestHelper.Request.Callback?)

}