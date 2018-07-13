package com.avengers.zombiebase.adapter;


import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.List;

/**
 * @param <T> List的item对象，需要绑定DataBinding，继承BaseObservable
 * @param <E> 关于ItemView的 ViewDataBinding
 * @author Jervis 2018-05-30
 * 一个简单的通过databinding实现的recycleView 的Adapter
 * @see //com.taiwu.bigdata.ui.humman.IndexHRDbdActivity#onGetDimissionDbdList(LeaveTrendDbdResp)  调用案例
 */
public class DataBindingLiteAdapter<T extends BaseObservable, E extends ViewDataBinding, World> extends RecyclerView.Adapter<DataBindingLiteHolder<E>> {

    public interface DbdAdapterEvent<T extends BaseObservable, E extends ViewDataBinding> {
        /**
         * 需要在itemView设置中做特殊逻辑处理时使用的
         *
         * @param holder    ListItem  holder
         * @param position  ListItem 的 position
         * @param viewModel viewModel
         */
        void customSetItemView(DataBindingLiteHolder<E> holder, int position, T viewModel);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    private int layoutId;
    private int bdId;
    private List<T> list;
    private DbdAdapterEvent<T, E> dbdAdapterEvent;

    private DataBindingLiteAdapter(final List<T> list, final int layoutId, int bdId) {
        this.list = list;
        this.layoutId = layoutId;
        this.bdId = bdId;
    }

    public DataBindingLiteAdapter(final List<T> list, final int layoutId, int bdId, DbdAdapterEvent<T, E> dbdAdapterEvent) {
        this(list, layoutId, bdId);
        this.dbdAdapterEvent = dbdAdapterEvent;
    }

    public void setNewList(List<T> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public DataBindingLiteHolder<E> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //1.拿到itemView的viewDataBinding对象
        E viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
        return new DataBindingLiteHolder<>(viewDataBinding, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("shejian", "onItemClick:"+position);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull DataBindingLiteHolder<E> holder, int position) {
        E binding = holder.getBinding();
        //2.为viewDataBinding对象设置XML中的数据属性
        binding.setVariable(bdId, list.get(position));
        if (dbdAdapterEvent != null) {
            T t = list.get(position);
            dbdAdapterEvent.customSetItemView(holder, position, t);
        }
        //3.据说时为了解决使用dataBinding导致RecycleView 的闪烁问题
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

/*    public static class DbdLiteHolder<E extends ViewDataBinding> extends RecyclerView.ViewHolder {

        E viewDataBinding;

        public   DbdLiteHolder(E binding) {
            super(binding.getRoot());
            viewDataBinding = binding;
        }

        public E getBinding() {
            return viewDataBinding;
        }
    }*/
}
