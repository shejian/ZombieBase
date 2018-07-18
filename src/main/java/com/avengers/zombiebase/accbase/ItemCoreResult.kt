package com.avengers.zombiebase.accbase

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList


/**
 * 驱动recycleview的关键数据对象
 */
data class ItemCoreResult<T>(
        override var data: LiveData<PagedList<T>>,
        override var netWorkState: LiveData<NetworkState>,
        val refreshState: LiveData<NetworkState>,
        override var refresh: () -> Unit,
        val retry: () -> Unit
) : BaseCoreResult<PagedList<T>>(data, refreshState, refresh)



