package com.alguojian.statuslayout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.alguojian.mylibrary.StatusLayoutDefaultAdapter
import com.alguojian.mylibrary.StatusLayout
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    private lateinit var view: StatusLayout
    lateinit var statusHelper: StatusLayout.StatusHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        view = StatusLayout.setNewAdapter(StatusLayoutDefaultAdapter())
        statusHelper=  view.attachView(this).onRetryClick {
            requestData()
        }

        statusLayout.setOnClickListener {
            requestData()
        }
    }

    private fun requestData() {
        statusHelper.showLoading()
        handler.postDelayed({ statusHelper.showFailed() }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        StatusLayout.clearNewAdapter()
    }

    companion object {
        val handler = Handler()
        fun start(context: Context) {
            val starter = Intent(context, Main2Activity::class.java)
            context.startActivity(starter)
        }
    }
}
