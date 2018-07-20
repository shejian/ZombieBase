package com.avengers.zombiebase.widget

import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.widget.TextView
import com.avengers.zombielibrary.R

class CustomToolBar : Toolbar {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet) : super(context, attributes)
    constructor(context: Context, attributes: AttributeSet, defaultAttr: Int) : super(context, attributes, defaultAttr)

    lateinit var mTxtMiddleTitle: TextView

    override fun onFinishInflate() {
        super.onFinishInflate()
        mTxtMiddleTitle = findViewById(R.id.center_title_view)
    }

    open fun setMiddleText(txt: String) {
        title = " "
        mTxtMiddleTitle.text = txt
    }

    open fun setLeftText(txt: String) {
        title = txt
        mTxtMiddleTitle.text = ""
    }

    open fun setGoBack(listener: OnClickListener) {
        setNavigationOnClickListener(listener)
    }

    fun hideBack() {
        navigationIcon = null
    }


}