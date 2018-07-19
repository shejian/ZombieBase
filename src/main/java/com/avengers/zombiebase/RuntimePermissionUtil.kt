package com.avengers.zombiebase

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.avengers.zombielibrary.R
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission

/**
 * @author Jervis
 * 运行时权限的调用
 *
 */
object RuntimePermissionUtil {

    fun requestPermission(context: Context, onSuccess: () -> Unit, vararg permissions: String) {
        AndPermission.with(context)
                .runtime()
                .permission(*permissions)
                .rationale(RuntimeRationale())
                .onGranted {
                    onSuccess()
                }
                .onDenied { permission ->
                    if (AndPermission.hasAlwaysDeniedPermission(context, permission)) {
                        showSettingDialog(context, permission)
                    } else {
                        showDeniedDialog(context, onSuccess, *permissions)
                    }
                }.start()
    }


    /**
     * 轻度拒绝后做提示
     */
    private fun showDeniedDialog(context: Context, onSuccess: () -> Unit, vararg permissions: String) {
        DialogBuilder.buildAlertDialog(context)
                .setTitle("请授权")
                .setMessage("需要一些权限才能正常使用该功能")
                .setPositiveButton("继续") { _, _ ->
                    requestPermission(context, onSuccess, *permissions)
                }
                .setNegativeButton("拒绝") { _, _ ->

                }
                .setCancelable(false)
                .show()
    }

    /**
     * Display setting dialog.
     */
    private fun showSettingDialog(context: Context, permissions: List<String>) {
        val permissionNames = Permission.transformText(context, permissions)
        val message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames))

        DialogBuilder.buildAlertDialog(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting) { _, _ -> setPermission(context) }
                .show()
    }

    private fun setPermission(context: Context) {
        AndPermission.with(context)
                .runtime()
                .setting()
                .onComeback {} //Toast.makeText(context, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show() }
                .start()
    }
}
