package com.avengers.zombiebase.adapter


import android.arch.paging.PagedListAdapter
import android.databinding.BaseObservable
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.avengers.zombiebase.adapter.DataBindingLiteAdapter

/**
 * 支持本地数据简单分页的pagelist的基础BindingAdapter
 * @param <T> PageList的item对象，需要绑定DataBinding，继承BaseObservable
 * @param <E> 关于ItemView的 ViewDataBinding
 * @author Jervis 2018-07-2
 * 一个简单的通过databinding实现的recycleView 的Adapter
 * @see //com.taiwu.bigdata.ui.humman.IndexHRDbdActivity.onGetDimissionDbdList
</E></T> */
open class DataBindingPagedListLiteAdapter<T, E : ViewDataBinding>(
        private var layoutRes: Int,
        private var variableId: Int,
        diffCallback: DiffUtil.ItemCallback<T>,
        private var onBindView: ((view: ViewDataBinding, sd: Int) -> Unit?)? = null)
    : PagedListAdapter<T, DataBindingLiteHolder<E>>(diffCallback) {

    var onItemClickFun: ((view: View, position: Int) -> Unit?)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingLiteHolder<E> {
        val vdb = DataBindingUtil.inflate<E>(LayoutInflater.from(parent.context), layoutRes, parent, false)
        return DataBindingLiteHolder(vdb, onItemClickFun)
    }

    override fun onBindViewHolder(holder: DataBindingLiteHolder<E>, position: Int) {
        val vdb = holder.binding
        vdb.setVariable(variableId, getItem(position))
        onBindView.let { it?.invoke(vdb, position) }
        vdb.executePendingBindings()
    }

}


