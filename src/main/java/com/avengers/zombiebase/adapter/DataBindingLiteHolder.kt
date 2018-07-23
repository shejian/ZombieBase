package com.avengers.zombiebase.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.ListView


/**
 * @author Jervis 2018-07-2
 * 使用ViewDataBinding绑定item 的view 的通用RecyclerViewViewHolder
 */
class DataBindingLiteHolder<E : ViewDataBinding>(
        var binding: E,
        var onItemClickFun: ((view: View, position: Int) -> Unit?)? = null
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.root.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        onItemClickFun?.invoke(v, this.adapterPosition)
    }
}
