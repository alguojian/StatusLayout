package com.alguojian.mylibrary

import android.view.View

/**
 * 默认的适配器
 *
 * @author alguojian
 * @date 2019/4/12
 */
class StatusLayoutDefaultAdapter : StatusAdapter {
    override fun getView(statusHelper: StatusLayout.StatusHelper, statusView: View?, status: Int): View? {
        var defaultLoadingView: DefaultLoadingView? = null
        if (statusView != null && statusView is DefaultLoadingView) defaultLoadingView = statusView
        if (defaultLoadingView == null) {
            defaultLoadingView = DefaultLoadingView(statusHelper.context!!, statusHelper.click)
        }
        defaultLoadingView.setStatus(status)
        return defaultLoadingView

    }

}
