package com.alguojian.mylibrary

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * 多种状态切换的view管理
 *
 * @author alguojian
 * @date 2019/4/12
 */
class StatusLayout {
    private var statusAdapter: StatusAdapter? = null//适配器对象，用于获得待加载的view
    private var context: Context? = null
    private var viewGroup: ViewGroup? = null//承载加载布局的viewGroup
    private var statusView: View? = null//待加载的view
    private var status = STATUSLAYOUT_STATUS_SUCCESS//页面加载的状态
    private val mStatusViews = SparseArray<View?>(4)//存储加载过得的view
    private var data: String? = null//其他的数据
    private var click: View.OnClickListener? = null

    companion object {
        const val STATUSLAYOUT_STATUS_LOADING = 0//加载中
        const val STATUSLAYOUT_STATUS_SUCCESS = 1//加载成功
        const val STATUSLAYOUT_STATUS_FAIL = 2//加载失败
        const val STATUSLAYOUT_STATUS_EMPTY = 3//加载空数据
        private var TAG = "asdfghjkl"

        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        private var statusLayout: StatusLayout? = null

        @JvmStatic
        private var isDebug = false//是否debug模式，开启日志

        @JvmOverloads
        @JvmStatic
        fun setDebug(boolean: Boolean=true, string: String?=null) {
            isDebug = boolean
            if (!TextUtils.isEmpty(string))
                TAG = string!!
        }

        /**
         * 返回自身对象
         */
        @JvmStatic
        fun getThis(): StatusLayout {
            if (statusLayout == null) {
                synchronized(StatusLayout::class.java) {
                    statusLayout = StatusLayout()
                }
            }
            return statusLayout!!
        }

        /**
         * 设置新的适配器，需要重新创建对象
         */
        @JvmStatic
        fun setNewAdapter(statusAdapter: StatusAdapter): StatusLayout {
            val statusLayout = StatusLayout()
            statusLayout.statusAdapter = statusAdapter
            return statusLayout
        }
    }

    /**
     * 返回上下文
     */
    fun getContext(): Context {
        return context!!
    }

    /**
     * 返回根view
     */
    fun getViewGroup(): ViewGroup? {
        return viewGroup
    }

    /**
     * 返回点击重试
     */
    fun getRetryClick(): View.OnClickListener? {
        return click
    }

    /**
     * 返回data数据
     */
    fun getData(): String? {
        return data
    }


    /**
     * 绑定activity
     */
    fun attachView(activity: Activity): StatusLayout {
        context = activity
        viewGroup = activity.findViewById(R.id.content)
        return this@StatusLayout
    }

    /**
     * 绑定某一个view，fragment中入如果绑定根view，在onCreateView()需要返回该viewGroup
     */
    fun attachView(view: View): StatusLayout {
        context = view.context
        viewGroup = FrameLayout(context)
        val layoutParams = view.layoutParams

        if (null != layoutParams) {
            viewGroup!!.layoutParams = layoutParams
        }

        if (null != view.parent) {
            val parent = view.parent as ViewGroup
            val indexOfChild = parent.indexOfChild(view)
            parent.removeView(view)
            parent.addView(viewGroup, indexOfChild)
        }
        viewGroup!!.addView(
            view,
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        )
        return this@StatusLayout
    }

    /**
     * 失败的时候，点击重试事件
     */
    fun onRetryClick(click: View.OnClickListener) {
        this@StatusLayout.click = click
    }

    /**
     * 设置其他的数据
     */
    fun setDate(string: String) {
        this@StatusLayout.data = string
    }

    /**
     * 设置适配器
     */
    fun setDefaultAdapter(statusAdapter: StatusAdapter) {
        this@StatusLayout.statusAdapter = statusAdapter
    }


    fun showLoading() {
        showLoadingStatus(STATUSLAYOUT_STATUS_LOADING)
    }

    fun showLoadSuccess() {
        showLoadingStatus(STATUSLAYOUT_STATUS_SUCCESS)
    }

    fun showLoadFailed() {
        showLoadingStatus(STATUSLAYOUT_STATUS_FAIL)
    }

    fun showEmpty() {
        showLoadingStatus(STATUSLAYOUT_STATUS_EMPTY)
    }

    /**
     * 展示新的展示状态
     */
    private fun showLoadingStatus(status: Int = STATUSLAYOUT_STATUS_LOADING) {
        if (this@StatusLayout.status == status || null == statusAdapter || null == context || null == viewGroup) {
            if (this@StatusLayout.status != status && isDebug) {
                showLog("statusAdapter--context--viewGroup--其中状态为null")
            }
            return
        }
        this@StatusLayout.status = status
        //获得当前状态中存储的view
        var view = mStatusViews.get(status)
        if (null == view) {
            //如果没有存储该view，就为待加载的view
            view = statusView
        }

        try {
            val currentView = statusAdapter!!.getView(this@StatusLayout, view, status)

            if (null == currentView) {
                showLog("adapter 中 view没有加载完全")
                return
            }

            if (currentView != statusView || viewGroup!!.indexOfChild(currentView) < 0) {
                if (null != statusView) viewGroup!!.removeView(statusView)

                viewGroup!!.addView(currentView)

                val layoutParams = currentView.layoutParams
                if (null != layoutParams) {
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                }

            } else if (viewGroup!!.indexOfChild(currentView) != viewGroup!!.childCount - 1) {
                currentView.bringToFront()
            }
            statusView = currentView
            mStatusViews.put(status, currentView)
        } catch (e: Exception) {
            e.printStackTrace()
            showLog(e.message)
        }

    }


    private fun showLog(string: String?) {
        if (isDebug && !TextUtils.isEmpty(string)) Log.d(TAG, string)
    }
}
