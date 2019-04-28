package com.alguojian.statuslayout

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alguojian.mylibrary.StatusLayout

/**
 * ${Descript}
 *
 * @author alguojian
 * @date 2019/4/13
 */
class SimpleFragment : Fragment() {

    lateinit var statusHelper: StatusLayout.StatusHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val inflate: ViewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_simple, container, false)
        statusHelper= StatusLayout.attachView(inflate.root).onRetryClick {
            requestData()
        }
        inflate.root.findViewById<View>(R.id.statusLayout).setOnClickListener {
            requestData()
        }
        return StatusLayout.getRootView()
    }

    private fun requestData() {
        statusHelper.showLoading()
        MainActivity.handler.postDelayed({statusHelper.showFailed() }, 2000)
    }
}
