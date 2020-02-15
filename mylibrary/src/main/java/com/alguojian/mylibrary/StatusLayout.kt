package com.alguojian.mylibrary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
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

@SuppressLint("StaticFieldLeak")
object StatusLayout {
    const val STATUS_LAYOUT_STATUS_LOADING = 0//加载中
    const val STATUS_LAYOUT_STATUS_SUCCESS = 1//加载成功
    const val STATUS_LAYOUT_STATUS_FAIL = 2//加载失败
    const val STATUS_LAYOUT_STATUS_EMPTY = 3//加载空数据
    private var TAG = "statusLayout"

    @JvmStatic
    private var isDebug = false//是否debug模式，开启日志

    private var defaultAdapter: StatusAdapter? = null//适配器对象，用于获得待加载的view
    private var newAdapter: StatusAdapter? = null//设置适配器新对象，用于获得待加载的view
    private var viewGroup: ViewGroup? = null//承载加载布局的viewGroup

    //当在fragment中或者动态引入中的view中使用时，需要判断当前的view是否为根view，如果不是需要返回根view供使用者调用
    private var isRootView = false
    private lateinit var rootView: View

    @JvmOverloads
    @JvmStatic
    fun setDebug(boolean: Boolean = true, tag: String? = null) {
        isDebug = boolean
        if (!tag.isNullOrBlank())
            this@StatusLayout.TAG = tag
    }

    /**
     * 设置新的适配器，需要重新创建对象
     */
    @JvmStatic
    fun setNewAdapter(statusAdapter: StatusAdapter): StatusLayout {
        this@StatusLayout.newAdapter = statusAdapter
        return StatusLayout
    }

    /**
     * 页面关闭时需要清除之前设置的新适配器
     */
    @JvmStatic
    fun clearNewAdapter() {
        this@StatusLayout.newAdapter = null
    }

    /**
     * 返回根view
     */
    fun getRootView(): View {
        return if (isRootView) viewGroup ?: rootView else rootView
    }

    /**
     * 设置适配器
     */
    fun setDefaultAdapter(statusAdapter: StatusAdapter) {
        this@StatusLayout.defaultAdapter = statusAdapter
    }

    private fun showLog(string: String?) {
        if (isDebug && !string.isNullOrBlank()) Log.d(TAG, string)
    }

    /**
     * 绑定activity
     */
    fun attachView(activity: Activity): StatusHelper {
        val view: View = activity.findViewById(R.id.statusLayout) ?: return StatusHelper(null, null, null)
        return structureViewGroup(activity, view)
    }

    /**
     * 绑定fragment，fragment中入如果绑定根view，在onCreateView()需要返回getRootView()
     */
    fun attachView(rootView: View): StatusHelper {
        this@StatusLayout.rootView = rootView
        this@StatusLayout.isRootView = false
        val view: View = rootView.findViewById(R.id.statusLayout) ?: return StatusHelper(null, null, null)
        this@StatusLayout.isRootView = true
        return structureViewGroup(view.context, view)
    }

    /**
     * 构造新的管理布局
     */
    private fun structureViewGroup(context: Context, view: View): StatusHelper {
        this@StatusLayout.viewGroup = FrameLayout(context)
        val layoutParams = view.layoutParams
        if (null != layoutParams) {
            viewGroup!!.layoutParams = layoutParams
        }

        if (null != view.parent) {
            val parent = view.parent as ViewGroup
            val indexOfChild = parent.indexOfChild(view)
            parent.removeView(view)
            parent.addView(viewGroup, indexOfChild)
            this@StatusLayout.isRootView = false
        }
        viewGroup!!.addView(view, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        return StatusHelper(context, newAdapter ?: defaultAdapter, viewGroup)
    }

    /**
     * 其他的数据[data]
     * 页面状态[status]，默认是成功状态
     * 承载加载布局的[viewGroup]
     */
    class StatusHelper(
        var context: Context?,
        var statusAdapter: StatusAdapter? = null,
        private var viewGroup: ViewGroup? = null
    ) {
        private val mStatusViews = SparseArray<View?>(4)//存储加载过得的view
        private var statusView: View? = null//待加载的view
        private var status: Int = STATUS_LAYOUT_STATUS_SUCCESS
        var click: (() -> Unit?)? = null
        var data: String? = null

        /**
         * 失败的时候，点击重试事件
         */
        fun onRetryClick(click: () -> Unit): StatusHelper {
            this@StatusHelper.click = click
            return this@StatusHelper
        }

        /**
         * 设置其他的数据
         */
        fun setDate(string: String): StatusHelper {
            this@StatusHelper.data = string
            return this@StatusHelper
        }

        fun showLoading() {
            showLoadingStatus(STATUS_LAYOUT_STATUS_LOADING)
        }

        fun showSuccess() {
            showLoadingStatus(STATUS_LAYOUT_STATUS_SUCCESS)
        }

        fun showFailed() {
            showLoadingStatus(STATUS_LAYOUT_STATUS_FAIL)
        }

        fun showEmpty() {
            showLoadingStatus(STATUS_LAYOUT_STATUS_EMPTY)
        }

        /**
         * 展示新的展示状态
         */
        private fun showLoadingStatus(status: Int = STATUS_LAYOUT_STATUS_LOADING) {
            if (this@StatusHelper.status == status || null == statusAdapter || null == context || null == viewGroup) {
                if (isDebug) {
                    showLog("statusAdapter--context--viewGroup--其中一个状态为null")
                }
                if (this@StatusHelper.status != status) {
                    throw Exception("-----------检查是否初始化stateLayout的adapter-------xml中是否使用id---statusLayout")
                }
                return
            }
            this@StatusHelper.status = status
            //获得当前状态中存储的view
            var view = mStatusViews.get(status)
            if (null == view) {
                //如果没有存储该view，就为待加载的view
                view = statusView
            }

            try {
                val currentView = statusAdapter!!.getView(this@StatusHelper, view, status)
                if (null == currentView) {
                    showLog("adapter 中 view没有加载完全")
                    return
                }
                if (currentView != statusView || viewGroup!!.indexOfChild(currentView) < 0) {
                    if (null != statusView) viewGroup!!.removeView(statusView)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) currentView.elevation = Float.MAX_VALUE
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
    }
}
