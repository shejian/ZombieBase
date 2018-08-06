package com.avengers.zombiebase.aacbase

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * 辅助activity或者fragment构建aac的基础框架代码
 * 构建常用界面的ViewModel和DataBinding
 * @author Jervis
 * @date 20180726
 */
class AACBaseHelpV3<B : ViewDataBinding, V : ViewModel, P > {

    lateinit var activity: FragmentActivity
    lateinit var fragment: Fragment
    private var iAAC: IAACHelp<P>

    constructor(activity: FragmentActivity,
                iAAC: IAACHelp<P>) {
        this.activity = activity
        this.iAAC = iAAC
    }

    constructor(fragment: Fragment, iAAC: IAACHelp<P>) {
        this.fragment = fragment
        this.iAAC = iAAC
    }


    interface IAACHelp<P> {

        val layout: Int

        fun createRepository(): P

        fun createModelFactory(repository: P): ViewModelProvider.Factory?

    }

    var mViewModel: V? = null

    var mDataBinding: B? = null


    fun getDataBinding(activity: FragmentActivity): B {
        return DataBindingUtil.setContentView(activity, iAAC.layout)
    }

    fun createViewModel(activity: FragmentActivity, modelClass: Class<V>): V {
        return ViewModelProviders
                .of(activity, iAAC.createModelFactory(iAAC.createRepository()))
                .get(modelClass)
    }

    /**
     * 独立使用Help时调用，在basefragment或者baseActivity中是不会调用的
     */
    fun init(activity: FragmentActivity, modelClass: Class<V>) {
        mViewModel = createViewModel(activity, modelClass)
        mDataBinding = getDataBinding(activity)
    }

    //Fragment 的相关函数-----------------------------------------------------------------

    fun getDataBinding(inflater: LayoutInflater, container: ViewGroup?): B {
        return DataBindingUtil.inflate(inflater, iAAC.layout, container, false)
    }

    /**
     * 注意，ViewModel的构建可以为Activity实现，也可以为Fragment实现，那么对应的范围是不一样的
     * 默认情况下，Fragment是使用基于Fragment实现的，假如采用Activity范围的，使用另外一个创建函数
     */
    fun createViewModel(modelClass: Class<V>): V {
        return ViewModelProviders
                .of(fragment, iAAC.createModelFactory(iAAC.createRepository()))
                .get(modelClass)
    }

    /**
     * 独立使用Help时调用，在basefragment或者baseActivity中是不会调用的
     */
    fun init(inflater: LayoutInflater, container: ViewGroup?, modelClass: Class<V>) {
        mViewModel = createViewModel(modelClass)
        mDataBinding = getDataBinding(inflater, container).apply {
            setLifecycleOwner(fragment)
        }
    }


}