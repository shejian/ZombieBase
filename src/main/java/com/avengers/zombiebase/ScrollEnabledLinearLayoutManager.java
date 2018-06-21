package com.avengers.zombiebase;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Jervis on 2017/5/19.
 * 使用RecycleView 时，如果数据量很少只有几个，需求不需要它上下左右滑动，在xml配置中加上 Android:scrollbars=”none”，这只是去掉了滑动bar。
 */
public class ScrollEnabledLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = false;

    public ScrollEnabledLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}