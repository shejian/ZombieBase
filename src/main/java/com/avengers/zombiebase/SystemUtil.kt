package com.avengers.zombiebase

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Build.*
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager.LayoutParams
import com.avengers.zombiebase.widget.SystemBarTintManager
import com.avengers.zombielibrary.R
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * @author Jervis
 * @date 20180719
 */
object SystemUtil {

    /**
     * 控制状态栏文字主题的Android M 23以上和MIUI有效
     */
    fun setSystemStatusBar(window: Window, isLightingText: Boolean = false) {
        when {
            isLightingText -> StatusBarUtil.showLightingStatusText(window)
            else -> StatusBarUtil.showDarkStatusText(window)
        }
    }

    /**
     * 获取系统属性
     */
    fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            //Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    //     Log.e(TAG, "Exception while closing InputStream", e);
                }

            }
        }
        return line
    }


    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @return true 前台 false 后台
     */
    fun isApplicationForeground(context: Context): Boolean {
        return !TextUtils.isEmpty(getActivePackagesName(context)) && context.packageName == getActivePackagesName(context)
    }

    private fun getActivePackagesName(context: Context): String? {
        var packageName: String? = null
        val activityManager = context.getSystemService(Context
                .ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val activePackages = Vector<String>()
            val processInfos = activityManager.runningAppProcesses
            for (processInfo in processInfos) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    activePackages.addAll(Arrays.asList(*processInfo.pkgList))
                }
            }
            if (activePackages.size > 0) {
                packageName = activePackages[0]
            }
        } else {
            val taskInfoList = activityManager.getRunningTasks(1)
            val componentName = taskInfoList[0].topActivity
            packageName = componentName.packageName
        }
        return packageName
    }


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager?.activeNetworkInfo
        if (networkInfo != null) {
            if (networkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun isWifiAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager?.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (networkInfo != null) {
            if (networkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }


    /**
     * 手机是否开启了定位服务
     *
     * @param context
     * @return
     */
    fun isEnableLocation(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快） 
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位） 
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    fun getVersion(): String {
        var version = ""
        try {
            val manager = BaseApplication.getMaApplication().packageManager
            val info = manager.getPackageInfo(BaseApplication.getMaApplication().packageName, 0)
            version = info.versionName
            return version
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return version
    }

    /**
     * 获取当前进程的名称
     *
     * @param context
     * @return
     */
    fun getCurProcessName(context: Context): String? {
        val pid = android.os.Process.myPid()
        val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        if (null != mActivityManager!!.getRunningAppProcesses()) {
            for (appProcess in mActivityManager!!.getRunningAppProcesses()) {

                if (appProcess.pid == pid) {
                    return appProcess.processName
                }
            }
        }
        return null
    }


}

object StatusBarUtil {

    //白色的状态字
    fun showLightingStatusText(window: Window) {
        when {
        //MIUI的沉浸式
            MIUIUtil.CAN_SET_MIUIBAR -> MIUIUtil.setMiuiWhiteSystemBar(window)
        //原生AndroidM
            else -> setAndroidMLightStatusText(window)
        }
    }


    //黑色状态字
    fun showDarkStatusText(window: Window) {
        when {
        // MIUI的沉浸式
            MIUIUtil.CAN_SET_MIUIBAR -> MIUIUtil.setMiuiDarkSystemBar(window)
        //原生AndroidM
            else -> setAndroidMDarkStatusText(window)

        }
    }

    private fun setAndroidMDarkStatusText(window: Window) {
        when {
            VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP -> window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS)
        when {
            VERSION.SDK_INT >= VERSION_CODES.M -> window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setAndroidMLightStatusText(window: Window) {
        when {
            VERSION.SDK_INT >= VERSION_CODES.M -> {
                window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }


    //4.4上的状态栏
    fun setKStatusBar(mActivity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val decorView = mActivity.window.decorView
            if (decorView.tag == null) {
                mActivity.window.decorView.tag = ""
                val tintManager = SystemBarTintManager(mActivity)
                tintManager.isStatusBarTintEnabled = true
                tintManager.setStatusBarTintResource(R.color.black_20_color)
            }

            return true
        }
        return true
    }


}


object MIUIUtil {

    /**
     * MIUI版本
     */
    var miuiV = ""
    var CAN_SET_MIUIBAR = false

    fun initMIUIInfo() {
        val vString = MANUFACTURER
        when (vString) {
            "Xiaomi" -> {// 是小米设备
                miuiV = SystemUtil.getSystemProperty("ro.miui.ui.version.name")!!
                CAN_SET_MIUIBAR = canSetMIUIbar()
            }
        }
    }


    private fun canSetMIUIbar(): Boolean {
        return when (MANUFACTURER) {
            "Xiaomi" -> // 是小米设备
                when (miuiV) {
                    "V6", "V7", "V8" -> true
                    else -> false
                }
            else -> false
        }
    }


    /**
     *
     * 设置Miui的沉浸式
     * Method name: miuiDarkSystemBar <BR></BR>
     * Description: miuiDarkSystemBar <BR></BR>
     * Remark: <BR></BR>
     *
     * @param
     */
    fun setMiuiDarkSystemBar(window: Window) {
        val clazz = window.javaClass
        try {
            var tranceFlag = 0
            var darkModeFlag = 0
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")

            var field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT")
            tranceFlag = field.getInt(layoutParams)

            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            darkModeFlag = field.getInt(layoutParams)

            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            //只需要状态栏透明
            //    extraFlagField.invoke(window, tranceFlag, tranceFlag);
            //或
            //状态栏透明且黑色字体
            //  extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
            //状态栏有设定的颜色且黑色字体
            extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            //清除黑色字体
            // extraFlagField.invoke(window, 0, darkModeFlag);
            //  MyApplication.isMIUIv6 = true;
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }


    fun setMiuiWhiteSystemBar(window: Window) {

        val clazz = window.javaClass
        try {
            var tranceFlag = 0
            var darkModeFlag = 0
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")

            var field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT")
            tranceFlag = field.getInt(layoutParams)

            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            darkModeFlag = field.getInt(layoutParams)

            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            //只需要状态栏透明
            //   extraFlagField.invoke(window, tranceFlag, tranceFlag);
            //或
            //状态栏透明且黑色字体
            //  extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
            //清除黑色字体
            extraFlagField.invoke(window, 0, darkModeFlag)
            //     MyApplication.isMIUIv6 = true;
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }


}


