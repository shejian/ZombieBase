package com.avengers.zombiebase.adapter

import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avengers.zombiebase.aacbase.NetworkState
import com.avengers.zombielibrary.BR
import com.avengers.zombielibrary.R
import com.avengers.zombielibrary.databinding.NetworkStateItemBinding

/**
 * 一个典型的支持分页和刷新的Adapter
 * @author Jervis
 * @date 20170723
 * 1.利用多type的方式，实现一个网络状态的ItemView，以供加载更多的逻辑切换
 * 2.允许在onBindView中自定义实现itemView的值设置，提供闭包和子类重载两种方式
 * 3.开放"重试"的点击事件
 * 4.提供了item点击事件的闭包函数
 *
 */
open class DataBindingPagedListAdapter<T>(
        private val view_item_layout: Int,
        private val brId: Int,
        diffCallback: DiffUtil.ItemCallback<T>,
        private var onBindView: ((view: ViewDataBinding, sd: Int) -> Unit?)? = null)
    : PagedListAdapter<T, DataBindingLiteHolder<ViewDataBinding>>(diffCallback) {

    var onItemClickFun: ((view: View, position: Int) -> Unit?)? = null

    var onRetryFun: ((view: View, position: Int) -> Unit?)? = null

    private var networkState: NetworkState = NetworkState.error("")

    private fun hasExtraRow() = networkState != NetworkState.LOADED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingLiteHolder<ViewDataBinding> {

        //1.拿到itemView的viewDataBinding对象
        return when (viewType) {
            NETWORK_ITEM_LAYOUT -> {
                val networkStateDataBinding = DataBindingUtil.inflate<NetworkStateItemBinding>(LayoutInflater.from(parent.context), NETWORK_ITEM_LAYOUT, parent, false)
                DataBindingLiteHolder(
                        networkStateDataBinding,
                        onRetryFun
                )
            }
            else -> {//view_item_layout ->
                val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), view_item_layout, parent, false)
                DataBindingLiteHolder(
                        viewDataBinding,
                        onItemClickFun)
            }
        }
    }

    override fun onBindViewHolder(holder: DataBindingLiteHolder<ViewDataBinding>, position: Int) {
        val binding = holder.binding
        //2.为viewDataBinding对象设置XML中的数据属性
        val layoutId = getItemViewType(position)
        binding.setVariable(switchVariableId(layoutId), switchValue(layoutId, position))

        if (layoutId == view_item_layout) {
            //闭包实现 onBindView
            onBindView.let { it?.invoke(binding, position) }
            onBindView(holder, position)
        }
        //3.为了解决使用dataBinding导致RecycleView的闪烁问题
        binding.executePendingBindings()
    }


    open fun onBindView(holder: DataBindingLiteHolder<ViewDataBinding>, position: Int) {
        //子类实现 onBindView
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            hasExtraRow() && position == itemCount - 1 -> NETWORK_ITEM_LAYOUT
            else -> view_item_layout
        }
    }

    private fun switchVariableId(layoutId: Int): Int {
        return when (layoutId) {
            view_item_layout -> brId
            NETWORK_ITEM_LAYOUT -> BR.ns
            else -> brId
        }
    }

    private fun switchValue(layoutId: Int, position: Int): Any? {
        return when (layoutId) {
            view_item_layout -> getItem(position)
            NETWORK_ITEM_LAYOUT -> networkState
            else -> getItem(position)
        }
    }


    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState.msg = newNetworkState?.msg
        this.networkState.status = newNetworkState?.status!!
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                //增加删除动画
                //  notifyItemRemoved(super.getItemCount())
            } else {
                //增加插入动画
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            //使用livedata的情况下不用手动刷新
            //notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val NETWORK_ITEM_LAYOUT = R.layout.network_state_item
    }
}

