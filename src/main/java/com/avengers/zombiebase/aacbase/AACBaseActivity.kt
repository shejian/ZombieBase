package com.avengers.zombiebase.aacbase

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.avengers.zombiebase.SnackbarUtil
import java.lang.reflect.ParameterizedType

/**
 * 辅助Activity构建针对基础界面的aac的基础框架代码
 * @author Jervis
 * @date 20180726
 */
abstract class AACBaseActivity<B : ViewDataBinding, V : BaseViewModel<*, *>, P : Repository<*, *>>
    : AppCompatActivity(), AACBaseHelp.IAACHelp<P> {

    abstract override val layout: Int

    abstract override fun createRepository(): P

    //  abstract override fun createModelPageListRepositoryFactory(repository: P): ViewModelProvider.Factory

    private lateinit var aacHelp: AACBaseHelp<B, V, P>

    lateinit var mViewModel: V

    lateinit var mDataBinding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aacHelp = AACBaseHelp(this, this)
        genericViewModelClassToInit()
        addStatusView()
    }

    lateinit var statusViewHelper: StatusViewHelper

    /**
     * 创建StatusView，并加入到activity中
     */
    private fun addStatusView() {
        statusViewHelper = StatusViewHelper(LayoutInflater.from(this), mDataBinding.root as ViewGroup)
        val view: ViewGroup = this.findViewById(android.R.id.content)
        view.addView(statusViewHelper.baseStatusLayout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        statusViewHelper.setRefreshClick {
            mViewModel.refresh()
        }
    }

    /**
     * 通过设置NetworkState，动态改变界面的状态
     */
    fun settingStatusView(ns: NetworkState, haveLocalData: Boolean) {
        if (haveLocalData && Status.FAILED == ns.status) {
            statusViewHelper.setNs(NetworkState.LOADED)
            SnackbarUtil.showActionLong(mDataBinding.root, "数据获取失败", "点击重试", {
                mViewModel.refresh()
            }, Snackbar.LENGTH_LONG)
        } else {
            statusViewHelper.setNs(ns)
        }
    }

    private fun genericViewModelClassToInit() {
        val modelClass: Class<V>
        val genType = javaClass.genericSuperclass
        if (genType is ParameterizedType) {
            val params = genType.actualTypeArguments
            @Suppress("UNCHECKED_CAST")
            modelClass = params[1] as Class<V>

            mViewModel = aacHelp.createViewModel(this, modelClass)
            mDataBinding = aacHelp.getDataBinding(this).apply {
                setLifecycleOwner(this@AACBaseActivity)
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
