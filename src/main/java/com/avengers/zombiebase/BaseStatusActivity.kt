package com.avengers.zombiebase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.avengers.zombiebase.aacbase.NetworkState
import com.avengers.zombiebase.aacbase.StatusViewHelper


abstract class BaseStatusActivity : AppCompatActivity() {
    private var view: View? = null
    private var myContentView: View? = null


    lateinit var sViewHelper: StatusViewHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = findViewById(android.R.id.content)
       // sViewHelper = StatusViewHelper(LayoutInflater.from(this))
     //   sViewHelper.networkStateDataBinding?.ns = getNetworkState()

       // val frameLayout = view as ViewGroup
       // myContentView = frameLayout.getChildAt(0)
       // frameLayout.addView(sViewHelper.baseStatusLayout)
    }


    abstract fun getNetworkState(): NetworkState

}
