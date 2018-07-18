package com.avengers.zombiebase.matisse

import android.app.Activity
import com.zhihu.matisse.Matisse
import android.content.pm.ActivityInfo
import android.support.v4.app.Fragment
import com.avengers.zombielibrary.R
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy


/**
 * Created by duo.chen on 2018/7/18
 */
object MatisseChooser {

    fun choose(activity: Activity,REQUEST_CODE_CHOOSE: Int) {
        choose(Matisse.from(activity),
                activity.resources.getDimensionPixelSize(R.dimen.grid_expected_size),
                activity.packageName,
                REQUEST_CODE_CHOOSE)
    }

    fun choose(fragment: Fragment,REQUEST_CODE_CHOOSE: Int) {
        choose(Matisse.from(fragment),
                fragment.resources.getDimensionPixelSize(R.dimen.grid_expected_size),
                fragment.activity!!.packageName,
                REQUEST_CODE_CHOOSE)
    }

    private fun choose(matisse: Matisse,size: Int,providerPre: String,REQUEST_CODE_CHOOSE: Int) {

        matisse.choose(MimeType.ofAll(),false)
                .countable(true)
                .maxSelectable(9)
                .gridExpectedSize(size)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .capture(true)
                .captureStrategy(CaptureStrategy(true,"$providerPre.matisse.fileprovider"))
                .forResult(REQUEST_CODE_CHOOSE)
    }
}