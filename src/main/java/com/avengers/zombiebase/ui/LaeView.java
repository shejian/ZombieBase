package com.avengers.zombiebase.ui;

/**
 * Created by duo.chen on 2018/7/25
 * loading and error views interface
 */
public interface LaeView {

    void showLoadView();

    void reloadData();

    void showLoadTransView();

    boolean initErrorLayout();

    void showContentView();

    void showErrorView(String error);

}
