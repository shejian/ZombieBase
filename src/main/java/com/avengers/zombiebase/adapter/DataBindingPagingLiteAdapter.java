package com.avengers.zombiebase.adapter;


import android.arch.paging.PagedListAdapter;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avengers.zombiebase.adapter.DataBindingLiteAdapter;

/**
 * @param <T> PageList的item对象，需要绑定DataBinding，继承BaseObservable
 * @param <E> 关于ItemView的 ViewDataBinding
 * @author Jervis 2018-07-2
 * 一个简单的通过databinding实现的recycleView 的Adapter
 * @see //com.taiwu.bigdata.ui.humman.IndexHRDbdActivity#onGetDimissionDbdList(LeaveTrendDbdResp)  调用案例
 */
public class DataBindingPagingLiteAdapter<T, E extends ViewDataBinding>
        extends PagedListAdapter<T, DataBindingLiteHolder<E>> {

    int layoutRes, variableId;

    protected DataBindingPagingLiteAdapter(int layoutRes, int variableId, @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
        this.layoutRes = layoutRes;
        this.variableId = variableId;
    }

    @NonNull
    @Override
    public DataBindingLiteHolder<E> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        E vdb = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutRes, parent, false);
        return new DataBindingLiteHolder<>(vdb, new DataBindingLiteAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull DataBindingLiteHolder<E> holder, int position) {
        final E vdb = holder.getBinding();
        vdb.setVariable(variableId, getItem(position));
        vdb.executePendingBindings();
    }

}


