package com.avengers.zombiebase

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.avengers.zombielibrary.R

/**
 * @author Jervis 2018-05-30
 */
object RecycleHelper {

    fun initBaseRecycleView(context: Context, recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    fun initSwipeRefresh(mSwipeRefreshLayout: SwipeRefreshLayout?, refreshFun: () -> Unit?) {
        mSwipeRefreshLayout.let {
            mSwipeRefreshLayout?.setColorSchemeResources(R.color.mainColor)
            mSwipeRefreshLayout?.setOnRefreshListener { refreshFun.invoke() }
            //刷新可用,分页的时候，关闭刷新
            mSwipeRefreshLayout?.isEnabled = true
            //设置刷新状态是未完成
            mSwipeRefreshLayout?.isRefreshing = false
        }
    }


    fun initBaseRecycleViewNoScroll(context: Context, recyclerView: RecyclerView) {
        val layoutManager = ScrollEnabledLinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
    }


}
