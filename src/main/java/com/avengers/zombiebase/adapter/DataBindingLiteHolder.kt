package com.avengers.zombiebase.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView


/**
 * @author Jervis 2018-07-2
 * 使用ViewDataBinding绑定item 的view 的通用RecyclerViewViewHolder
 */
class DataBindingLiteHolder<E : ViewDataBinding>(val binding: E) : RecyclerView.ViewHolder(binding.root)