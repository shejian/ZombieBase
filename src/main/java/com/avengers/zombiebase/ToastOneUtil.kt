package com.avengers.zombiebase

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.avengers.zombielibrary.R

/**
 * 一个可以手动取消的Toast
 */
object ToastOneUtil {

    private var toast: Toast? = null

    open fun showToastShort(infoString: String) {
        showToastText(infoString, Toast.LENGTH_SHORT)
    }

    fun showToastLong(infoString: String) {
        showToastText(infoString, Toast.LENGTH_LONG)
    }


    fun cancel() {
        if (toast == null) {
            return
        }
        toast!!.cancel()
        toast = null
    }

    private fun setToast(toast2Set: Toast) {
        toast = toast2Set
    }

    private fun getToast(): Toast {
        if (toast == null) {
            toast = Toast(BaseApplication.getMaApplication())
            val inflate = BaseApplication.getMaApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v = inflate.inflate(R.layout.common_radius_toast, null)
            toast!!.view = v
            setToast(toast!!)
        }
        toast!!.duration = Toast.LENGTH_SHORT
        return toast!!
    }

    private fun showToastText(infoStr: String, TOAST_LENGTH: Int) {
        if (TextUtils.isEmpty(infoStr)) {
            return
        }
        toast = getToast()
        val textView = toast!!.view.findViewById<TextView>(R.id.toast_message)
        textView.text = infoStr
        toast!!.duration = TOAST_LENGTH
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.show()
    }

}
