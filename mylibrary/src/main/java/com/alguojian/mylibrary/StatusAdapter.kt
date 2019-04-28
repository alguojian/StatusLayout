package com.alguojian.mylibrary

import android.view.View

/**
 *状态加载布局适配器
 *
 * @author alguojian
 * @date 2019/4/12
 */
interface StatusAdapter {
    /**
     * 加载管理类[StatusLayout]
     * 待加载的类[statusView]
     * 加载类的状态，用于更新待加载view的信息[status]
     */
    fun getView(statusHelper: StatusLayout.StatusHelper, statusView: View?, status: Int): View?
}