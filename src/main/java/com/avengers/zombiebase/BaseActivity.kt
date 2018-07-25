package com.avengers.zombiebase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewSwitcher
import com.avengers.zombiebase.ui.LaeView
import com.avengers.zombielibrary.R


abstract class BaseActivity : AppCompatActivity(),LaeView {
    private var haveData = false
    private var view: View? = null
    private var myContentView: View? = null
    private var loadView: ViewSwitcher? = null
    private var errorSwitchLayout: ViewSwitcher? = null

    abstract override fun reloadData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = findViewById(android.R.id.content)
    }

    /**
     * 加载界面
     */
    override fun showLoadView() {
        if (haveData) {
            return
        }
        if (!initErrorLayout()) {
            return
        }
        errorSwitchLayout!!.visibility = View.VISIBLE
        errorSwitchLayout!!.displayedChild = 0
        loadView!!.displayedChild = 0
        loadView!!.setBackgroundResource(android.R.color.white)
    }

    override fun showLoadTransView() {
        if (!initErrorLayout()) {
            return
        }
        errorSwitchLayout!!.visibility = View.VISIBLE
        errorSwitchLayout!!.displayedChild = 0
        loadView!!.displayedChild = 0
        loadView!!.setBackgroundResource(android.R.color.transparent)
    }

    /**
     * 初始化异常处理界面
     */
    override fun initErrorLayout(): Boolean {
        if (errorSwitchLayout == null) {
            val frameLayout = view as FrameLayout ?: return false
            myContentView = frameLayout.getChildAt(0)
            errorSwitchLayout = View.inflate(this, R.layout.loading_select, null) as ViewSwitcher

            frameLayout.addView(errorSwitchLayout)
            val topViewHeight = resources.getDimensionPixelOffset(R.dimen.dp_48)
            errorSwitchLayout!!.setPadding(0, topViewHeight, 0, 0)
            //errorSwitchLayout.setPadding(0, _48dp, 0, 0);
            loadView = errorSwitchLayout!!.findViewById(R.id.loading_viewswitch)
        }
        return true
    }

    /**
     * 显示业务主界面
     */
    override fun showContentView() {
        if (errorSwitchLayout == null) {
            return
        }
        haveData = true
        errorSwitchLayout!!.visibility = View.GONE
        myContentView!!.visibility = View.VISIBLE
    }

    /**
     * 请求失败，显示请求错误界面
     *
     * @param error
     */
    override fun showErrorView(error: String) {
        var errorInfo = error
        if (!initErrorLayout()) {
            return
        }
        if (errorSwitchLayout == null) {
            return
        }
        if (haveData) {
            //ToastUtil.showInBottom(getActivity(), errorInfo)
            errorSwitchLayout!!.visibility = View.GONE
            myContentView!!.visibility = View.VISIBLE
        } else {
            errorSwitchLayout!!.visibility = View.VISIBLE
            val errorViewSwitch: ViewSwitcher = errorSwitchLayout!!.findViewById(R.id.error_viewswitch)
            (loadView!!.findViewById(R.id.error_text) as TextView).text = errorInfo
            val imageView: ImageView = loadView!!.findViewById(R.id.error_img)
            imageView.setImageResource(R.drawable.no_internet)
            if (TextUtils.isEmpty(errorInfo)) {
                errorInfo = loadView!!.resources.getString(R.string.service_exception)
            }
            (loadView!!.findViewById(R.id.error_text) as TextView).text = errorInfo
            val errorBtn: View = loadView!!.findViewById(R.id.error_btn)
            errorBtn.setOnClickListener {
                reloadData()
            }
            errorSwitchLayout!!.displayedChild = 0
            loadView!!.displayedChild = 1
            errorViewSwitch.displayedChild = 0
        }
    }

}
