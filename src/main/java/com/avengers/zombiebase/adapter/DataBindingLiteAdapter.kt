package com.avengers.zombiebase.adapter


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/** 单列表的基础DataBindingAdapter
 * @param <T> List的item对象，需要绑定DataBinding，继承BaseObservable
 * @param <E> 关于ItemView的 ViewDataBinding
 * @author Jervis 2018-05-30
 * 一个简单的通过databinding实现的recycleView 的Adapter
</E></T> */
open class DataBindingLiteAdapter<T, E : ViewDataBinding>(
        private val list: MutableList<T>?,
        private val layoutId: Int,
        private val bdId: Int,
        private var onBindView: ((view: ViewDataBinding, sd: Int) -> Unit?)? = null)
    : RecyclerView.Adapter<DataBindingLiteHolder<E>>() {

    var onItemClickFun: ((view: View, position: Int) -> Unit?)? = null

    fun setNewList(list: List<T>?) {
        if (list == null || list.isEmpty()) {
            return
        }
        this.list?.clear()
        this.list?.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingLiteHolder<E> {
        //1.拿到itemView的viewDataBinding对象
        val viewDataBinding = DataBindingUtil.inflate<E>(LayoutInflater.from(parent.context), layoutId, parent, false)
        return DataBindingLiteHolder(viewDataBinding, onItemClickFun)
    }

    override fun onBindViewHolder(holder: DataBindingLiteHolder<E>, position: Int) {
        val binding = holder.binding
        //2.为viewDataBinding对象设置XML中的数据属性
        binding.setVariable(bdId, list!![position])
        onBindView.let { it?.invoke(binding, position) }
        //3.据说时为了解决使用dataBinding导致RecycleView 的闪烁问题
        binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}
