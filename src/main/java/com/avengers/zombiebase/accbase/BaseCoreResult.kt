package com.avengers.zombiebase.accbase

import android.arch.lifecycle.LiveData

/**
 * 最低颗粒度的数据驱动的关键类
 */
open class BaseCoreResult<T>(
        //api数据
        open var data: LiveData<T>,
        //网络状态
        open var netWorkState: LiveData<NetworkState>,
        //刷新数据
        open var refresh: () -> Unit
)



