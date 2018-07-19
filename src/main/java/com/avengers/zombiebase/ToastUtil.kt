/**
 *
 */
package com.avengers.zombiebase

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.avengers.zombielibrary.R


object ToastUtil {

    fun show(context: Context, info: String) {
        if (TextUtils.isEmpty(info)) {
            return
        }
        Toast.makeText(context, info, Toast.LENGTH_LONG).show()
    }

    fun show(info: String) {
        if (TextUtils.isEmpty(info)) {
            return
        }
        Toast.makeText(BaseApplication.getMaApplication(), info, Toast.LENGTH_LONG).show()
    }


    fun show(context: Context, resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
    }

    fun showInCenter(message: String) {
        ToastOneUtil.showToastShort(message)
    }

    fun showInRectangleCenter(context: Context, message: String) {
        if (TextUtils.isEmpty(message)) {
            return
        }
        val toast = Toast(context)
        val view = LayoutInflater.from(context).inflate(R.layout.common_radius_toast, null)
        val tv = view.findViewById<TextView>(R.id.toast_message)
        tv.text = message
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun showInBottom(context: Context, message: String): Toast? {
        if (TextUtils.isEmpty(message)) {
            return null
        }
        val toast = Toast(context)
        val view = LayoutInflater.from(context).inflate(R.layout.common_radius_toast, null)
        val tv = view.findViewById<TextView>(R.id.toast_message)
        tv.text = message
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.BOTTOM, 0, 120)
        toast.show()
        return toast
    }

    fun showInBottomLong(message: String) {
        if (TextUtils.isEmpty(message)) {
            return
        }
        val toast = Toast(BaseApplication.getMaApplication())
        val inflate = BaseApplication.getMaApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflate.inflate(R.layout.common_radius_toast, null)
        toast.view = v
        val textView = toast.view.findViewById<TextView>(R.id.toast_message)
        textView.text = message
        toast.duration = Toast.LENGTH_LONG
        toast.setGravity(Gravity.BOTTOM, 0, 120)
        toast.show()
    }


    fun showInCenterLong(context: Context, message: String) {
        if (TextUtils.isEmpty(message)) {
            return
        }
        val toast = Toast(context)
        val view = LayoutInflater.from(context).inflate(R.layout.common_radius_toast, null)
        val tv = view.findViewById<TextView>(R.id.toast_message)
        tv.text = message
        toast.view = view
        toast.duration = Toast.LENGTH_LONG
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun showInCenterLong(context: Context, view: View) {
        val toast = Toast(context)
        toast.view = view
        toast.duration = Toast.LENGTH_LONG
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}
