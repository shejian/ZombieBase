package com.avengers.zombiebase.aacbase

import android.databinding.DataBindingUtil
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avengers.zombiebase.SnackbarUtil
import com.avengers.zombiebase.ToastOneUtil
import com.avengers.zombielibrary.R
import com.avengers.zombielibrary.databinding.BaseStatusLayoutBinding

class StatusViewHelper(val factory: LayoutInflater, var container: ViewGroup?) {

    companion object {
        var BASE_STATUS_LAYOUT = R.layout.base_status_layout
    }


    var baseStatusLayout: View

    var baseStatusLayoutBinding: BaseStatusLayoutBinding? = null


    init {
        baseStatusLayoutBinding = DataBindingUtil.inflate(factory, BASE_STATUS_LAYOUT, container, false)
        baseStatusLayout = baseStatusLayoutBinding?.root!!
        baseStatusLayoutBinding?.handlerClick = HandlerClick()
    }


    fun setNs(state: NetworkState) {
        baseStatusLayoutBinding?.ns = state
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