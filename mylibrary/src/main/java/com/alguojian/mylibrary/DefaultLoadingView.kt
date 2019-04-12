package com.alguojian.mylibrary

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * 默认的加载视图view
 *
 * @author alguojian
 * @date 2019/4/12
 */
@SuppressLint("ViewConstructor")
class DefaultLoadingView(context: Context, private val mOnClickListener: View.OnClickListener?) : LinearLayout(context) {

    private val mTextView: TextView
    private val mImageView: ImageView
    private val errorBoldView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.status_loading_view, this, true)
        mImageView = findViewById(R.id.image)
        mTextView = findViewById(R.id.text)
        errorBoldView = findViewById(R.id.errorBoldView)
        setBackgroundColor(-0xf0f10)
    }

    /**
     * 设置是否显示图示文案
     *
     * @param visible
     */
    fun setTextVisibility(visible: Boolean) {
        mTextView.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * 设置新状态的view信息
     *
     * @param status
     */
    fun setStatus(status: Int) {
        var show = true
        var image = R.drawable.status_loading
        var str = ""
        errorBoldView.visibility = if (status == StatusLayout.STATUSLAYOUT_STATUS_FAIL) View.VISIBLE else View.GONE

        println("-----------$status")

        when (status) {
            StatusLayout.STATUSLAYOUT_STATUS_SUCCESS -> show = false
            StatusLayout.STATUSLAYOUT_STATUS_LOADING -> {
                str = "加载中..."
            }
            StatusLayout.STATUSLAYOUT_STATUS_FAIL -> {
                str = "点击空白重试"
                image = R.drawable.status_ic_state_error
                setOnClickListener(mOnClickListener)
            }
            StatusLayout.STATUSLAYOUT_STATUS_EMPTY -> {
                str = "暂无数据"
                image = R.drawable.status_ic_state_empty
            }
        }
        mImageView.setImageResource(image)
        mTextView.text = str

        val layoutParams = mImageView.layoutParams as LinearLayout.LayoutParams
        layoutParams.bottomMargin=if (status == StatusLayout.STATUSLAYOUT_STATUS_LOADING) 30 else -90
        visibility = if (show) View.VISIBLE else View.GONE
    }
}
