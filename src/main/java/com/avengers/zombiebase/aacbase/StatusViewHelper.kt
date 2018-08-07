package com.avengers.zombiebase.aacbase

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avengers.zombielibrary.R
import com.avengers.zombielibrary.databinding.BaseStatusLayoutBinding

/**
 * 控制显示网络错误，重试的辅助类，配合AACBaseActivity或
 * 可参见 WeatherActivity 中的实现，
 * @author Jervis
 * @
 */
class StatusViewHelper(val factory: LayoutInflater, var container: ViewGroup?) {

    companion object {
        var BASE_STATUS_LAYOUT = R.layout.base_status_layout
    }


    var baseStatusLayout: View

    private var baseStatusLayoutBinding: BaseStatusLayoutBinding? = null


    init {
        baseStatusLayoutBinding = DataBindingUtil.inflate(factory, BASE_STATUS_LAYOUT, container, false)
        baseStatusLayout = baseStatusLayoutBinding?.root!!
        baseStatusLayoutBinding?.handlerClick = HandlerClick()
        baseStatusLayoutBinding?.state = NetworkState.LOADED
    }


    fun setNetworkState(state: NetworkState) {
        baseStatusLayoutBinding?.state = state
    }

    inner class HandlerClick {
        fun refreshClick() {
            retry?.invoke()
        }
    }


    var retry: (() -> Unit)? = null

    fun setRefreshClick(retry: () -> Unit) {
        this.retry = retry
    }


}