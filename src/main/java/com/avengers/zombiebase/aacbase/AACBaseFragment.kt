package com.avengers.zombiebase.aacbase

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType

/**
 * 辅助Fragment构建针对基础界面的aac的基础框架代码
 * @author Jervis
 * @date 20180726
 */
abstract class AACBaseFragment<B : ViewDataBinding, V : ViewModel, P : Repository<*, *>>
    : Fragment(), AACBaseHelp.IAACHelp<P> {

    abstract override val layout: Int

    abstract override fun createRepository(): P

    //  abstract override fun createModelPageListRepositoryFactory(repository: P): ViewModelProvider.Factory

    private lateinit var aacHelp: AACBaseHelp<B, V, P>

    lateinit var mViewModel: V

    lateinit var mDataBinding: B


    lateinit var sViewHelper: StatusViewHelper


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        aacHelp = AACBaseHelp(this, this)
        genericViewModelClassToInit(inflater, container)
        sViewHelper = StatusViewHelper(LayoutInflater.from(context), container)
        (mDataBinding.root as ViewGroup).addView(sViewHelper.baseStatusLayout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        return mDataBinding.root
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
