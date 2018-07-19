package com.avengers.zombiebase

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.design.internal.SnackbarContentLayout
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.FrameLayout
import com.avengers.zombielibrary.R

/**
 *
 *    SnackbarUtil.showActionLong(view,"我测试一下","撤销", {
                 ToastOneUtil.showToastShort("已撤销")
      }, Snackbar.LENGTH_INDEFINITE)
 *
 *    <dimen name="design_snackbar_elevation">0dp</dimen>
 *    控制了阴影大小
 *
 *    @author Jervis
 *    @date 20180718
 */
object SnackbarUtil {

    fun showShort(rootView: View, infoString: String) {
        showSnackbarText(rootView, infoString, Snackbar.LENGTH_SHORT)
    }

    fun showLong(rootView: View, infoString: String) {
        showSnackbarText(rootView, infoString, Snackbar.LENGTH_LONG)
    }

    fun showActionLong(rootView: View, infoString: String, actionStr: String, actionCallBack: () -> Unit, TOAST_LENGTH: Int = Snackbar.LENGTH_LONG) {
        showToastAction(rootView, infoString, actionStr, TOAST_LENGTH, actionCallBack)
    }

    private fun showSnackbarText(it: View, infoStr: String, TOAST_LENGTH: Int) {
        customSnackbarStyle(Snackbar.make(it, infoStr, TOAST_LENGTH)).show()
    }

    private val dp16 = BaseApplication.getMaApplication().resources.getDimensionPixelSize(R.dimen._16)

    @SuppressLint("RestrictedApi")
    private fun showToastAction(it: View, infoStr: String, actionStr: String, TOAST_LENGTH: Int, actionCallBack: () -> Unit) {
        val snackBar = customSnackbarStyle(Snackbar.make(it, infoStr, TOAST_LENGTH))
        //设置按钮文字颜色
        snackBar.setActionTextColor(Color.GREEN)
                //设置点击事件
                .setAction("撤销") {
                    actionCallBack.invoke()
                    snackBar.dismiss()
                }
                .addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(snackbar: Snackbar?, event: Int) {
                        super.onDismissed(snackbar, event)
                    }

                    override fun onShown(snackbar: Snackbar?) {
                        super.onShown(snackbar)
                    }
                })
                .show()
    }


    private fun customSnackbarStyle(snackBar: Snackbar): Snackbar {
        snackBar.view.setPadding(dp16, dp16, dp16, dp16)
        snackBar.view.setBackgroundResource(R.drawable.snack_radiu_bg)
        //设置SnackBar 文本颜色
        //   val contentLayout = (snackBar.view as FrameLayout).getChildAt(0) as SnackbarContentLayout
        //  val tv = contentLayout.messageView
        //   tv.setTextColor(Color.WHITE)
        //设置SnackBar背景颜色toast_radiu_background
        //  snackBar.view.setBackgroundResource(R.color.material_deep_teal_500)
        return snackBar

    }


}
