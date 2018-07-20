package com.avengers.zombiebase.widget

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import com.avengers.zombielibrary.R

class CustomToolBar : Toolbar {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet) : super(context, attributes)
    constructor(context: Context, attributes: AttributeSet, defaultAttr: Int) : super(context, attributes, defaultAttr)

    lateinit var mTxtMiddleTitle: TextView

    init {
        initView()
    }

    private fun initView() {
        setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        setTitleTextColor(ContextCompat.getColor(context, R.color.black_87_color))
        setBackgroundColor(Color.WHITE)
        mTxtMiddleTitle = View.inflate(context, R.layout.toolbar_text_view, null) as TextView
        val vp = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        vp.gravity = Gravity.CENTER
        addView(mTxtMiddleTitle, vp)
    }


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