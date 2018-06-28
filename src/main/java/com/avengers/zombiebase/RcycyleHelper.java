package com.avengers.zombiebase;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avengers.zombielibrary.R;

/**
 * @author Jervis 2018-05-30
 */
public class RcycyleHelper {

    public static void initBaseRcycyleView(Context context, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public static void initSwipeRefresh(SwipeRefreshLayout mSwipeRefreshLayout) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.mainColor);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                }
            });
            //刷新可用,分页的生活，关闭刷新
            mSwipeRefreshLayout.setEnabled(true);
            //设置刷新状态是未完成
            mSwipeRefreshLayout.setRefreshing(true);

        }
    }


    public static void initBaseRcycyleViewNoScroll(Context context, RecyclerView recyclerView) {
        ScrollEnabledLinearLayoutManager layoutManager = new ScrollEnabledLinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


}
