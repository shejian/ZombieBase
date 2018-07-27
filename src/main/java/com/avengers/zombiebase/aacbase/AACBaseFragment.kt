package com.avengers.zombiebase.aacbase

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avengers.zombiebase.SnackbarUtil
import java.lang.reflect.ParameterizedType

/**
 * 辅助Fragment构建针对基础界面的aac的基础框架代码
 * @author Jervis
 * @date 20180726
 */
abstract class AACBaseFragment<B : ViewDataBinding, V : BaseViewModel<*, *>, P : Repository<*, *>>
    : Fragment(), AACBaseHelp.IAACHelp<P> {

    abstract override val layout: Int

    abstract override fun createRepository(): P

    //  abstract override fun createModelPageListRepositoryFactory(repository: P): ViewModelProvider.Factory

    private lateinit var aacHelp: AACBaseHelp<B, V, P>

    lateinit var mViewModel: V

    lateinit var mDataBinding: B

    lateinit var statusViewHelper: StatusViewHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        aacHelp = AACBaseHelp(this, this)
        genericViewModelClassToInit(inflater, container)
        addStatusView(container)
        return mDataBinding.root
    }

    /**
     * 创建StatusView，并加入到activity中
     * 假如mDataBinding.root 不是RelativeLayout或者FrameLayout 有可能会显示不出来
     */
    private fun addStatusView(container: ViewGroup?) {
        statusViewHelper = StatusViewHelper(LayoutInflater.from(context), container)
        val view: ViewGroup = mDataBinding.root as ViewGroup
        view.addView(statusViewHelper.baseStatusLayout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        statusViewHelper.setRefreshClick {
            mViewModel.refresh()
        }
    }

    /*
       通过设置NetworkState，动态改变界面的状态
       fun settingStatusView(ns: NetworkState, haveLocalData: Boolean) {
           if (haveLocalData && Status.FAILED == ns.status) {
               statusViewHelper.setNs(NetworkState.LOADED)
               SnackbarUtil.showActionLong(mDataBinding.root, "数据获取失败", "点击重试", {
                   mViewModel.refresh()
               }, Snackbar.LENGTH_LONG)
           } else {
               statusViewHelper.setNs(ns)
           }
       }*/


    /**
     * 通过设置NetworkState
     */
    fun settingStatusView(ns: NetworkState) {
        if (Status.CACHED_FAILED == ns.status) {
            statusViewHelper.setNs(NetworkState.LOADED)
            SnackbarUtil.showActionLong(mDataBinding.root, "数据获取失败", "点击重试", {
                mViewModel.refresh()
            }, Snackbar.LENGTH_LONG)
        } else {
            statusViewHelper.setNs(ns)
        }
    }


    private fun genericViewModelClassToInit(inflater: LayoutInflater, container: ViewGroup?) {
        val modelClass: Class<V>
        val genType = javaClass.genericSuperclass
        if (genType is ParameterizedType) {
            val params = genType.actualTypeArguments
            @Suppress("UNCHECKED_CAST")
            modelClass = params[1] as Class<V>

            // mViewModel = aacHelp.createViewModel(this.activity!!, modelClass) //这样能创建Activity范围可共享的ViewModel
            mViewModel = aacHelp.createViewModel(modelClass)
            mDataBinding = aacHelp.getDataBinding(inflater, container).apply {
                setLifecycleOwner(this@AACBaseFragment)
            }

        }
    }


    override fun createModelFactory(repository: P): ViewModelProvider.Factory? {
        val viewModelClass: Class<V>
        val genType = javaClass.genericSuperclass
        if (genType is ParameterizedType) {
            val params = genType.actualTypeArguments
            @Suppress("UNCHECKED_CAST")
            viewModelClass = params[1] as Class<V>

            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(viewModelClass)) {
                        @Suppress("UNCHECKED_CAST")
                        return viewModelClass.getConstructor(repository.javaClass).newInstance(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        } else {
            return null
        }
    }


}
