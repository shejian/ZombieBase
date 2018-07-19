package com.avengers.zombiebase

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.avengers.zombielibrary.R
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission

abstract class BasePremissionActivity : AppCompatActivity() {

    var toSet = false

    abstract fun toMainActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermission()
    }


    /**
     * 默认会获取本地存储权限
     * //可以加入启动权限，设备读写权限，运行时权限定位，相机，
     * 如果需要增加权限的，请传递权限参数
     */
    open fun initPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            toMainActivity()
            finish()
            return
        }
        requestPermission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
    }


    /**
     * Request permissions.
     */
    open fun requestPermission(vararg permissions: String) {
        var rationale = RuntimeRationale()
        AndPermission.with(this)
                .runtime()
                .permission(*permissions)
                .rationale(rationale)
                .onGranted {
                    toMainActivity()
                    finish()
                }
                .onDenied { permissions ->
                    if (AndPermission.hasAlwaysDeniedPermission(this@BasePremissionActivity, permissions)) {
                        showSettingDialog(this@BasePremissionActivity, permissions)
                    } else {
                        showDeniedDialog()
                    }
                }.start()
    }


    /**
     * 轻度拒绝后做提示
     */
    private fun showDeniedDialog() {
        DialogBuilder.buildAlertDialog(this@BasePremissionActivity)
                .setTitle("请授权")
                .setMessage("需要一些权限才能正常使用App")
                .setPositiveButton("继续") { _, _ ->
                    initPermission()
                }
                .setNegativeButton("退出") { _, _ ->
                    finish()
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
                .setPositiveButton(R.string.setting) { _, _ -> setPermission() }
                .setNegativeButton(R.string.cancel) { _, _ -> finish() }
                .show()
    }

    /**
     * Set permissions.
     */
    private fun setPermission() {
        toSet = true
        AndPermission.with(this)
                .runtime()
                .setting()
                .onComeback { Toast.makeText(this@BasePremissionActivity, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show() }
                .start()
    }


    /**
     * 处理当设置界面授权后的情况
     */
    override fun onResume() {
        super.onResume()
        if (toSet) {
            initPermission()
            toSet = false
        }

    }


}
