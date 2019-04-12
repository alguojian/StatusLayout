package com.alguojian.mylibrary

import android.view.View

/**
 * 默认的适配器
 *
 * @author alguojian
 * @date 2019/4/12
 */
class DefaultStatusAdapter : StatusAdapter {
    override fun getView(statusLayout: StatusLayout, statusView: View?, status: Int): View? {

        var defaultLoadingView: DefaultLoadingView? = null
        if (statusView != null && statusView is DefaultLoadingView) defaultLoadingView = statusView
        if (defaultLoadingView == null) {
            defaultLoadingView = DefaultLoadingView(statusLayout.getContext(), statusLayout.getRetryClick())
        }
        defaultLoadingView.setStatus(status)
        return defaultLoadingView
    }
}
