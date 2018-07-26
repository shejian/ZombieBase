package com.avengers.zombiebase.aacbase.paging

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
 * 辅助fragment构建针对PagedList的aac的基础框架代码
 * @author Jervis
 * @date 20180726
 */
abstract class AACPagedListFragment<B : ViewDataBinding, V : ViewModel, P : PagedListRepository<*, *>>
    : Fragment(), AACPageListHelp.IAACPageListHelp<P> {

    abstract override val layout: Int

    abstract override fun createRepository(): P

    private lateinit var aacHelp: AACPageListHelp<B, V, P>

    lateinit var mViewModel: V

    lateinit var mDataBinding: B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        aacHelp = AACPageListHelp(this, this)
        genericViewModelClassToInit(inflater, container)
        return mDataBinding.root
    }

    private fun genericViewModelClassToInit(inflater: LayoutInflater, container: ViewGroup?) {
        val modelClass: Class<V>
        val genType = javaClass.genericSuperclass
        if (genType is ParameterizedType) {
            val params = genType.actualTypeArguments
            @Suppress("UNCHECKED_CAST")
            modelClass = params[1] as Class<V>

            mViewModel = aacHelp.createViewModel(modelClass)
            mDataBinding = aacHelp.getDataBinding(inflater, container).apply {
                setLifecycleOwner(this@AACPagedListFragment)
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

