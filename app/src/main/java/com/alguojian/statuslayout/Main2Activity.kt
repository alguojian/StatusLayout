package com.alguojian.statuslayout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alguojian.mylibrary.DefaultStatusAdapter
import com.alguojian.mylibrary.StatusLayout
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    private lateinit var view: StatusLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        view = StatusLayout.setNewAdapter(DefaultStatusAdapter())
        view.attachView(this).onRetryClick(View.OnClickListener {
            requestData()
        })

        statusLayout.setOnClickListener {
            requestData()
        }
    }

    private fun requestData() {
        view.showLoading()
        handler.postDelayed({ view.showFailed() }, 2000)
    }

    companion object {
        val handler = Handler()
        fun start(context: Context) {
            val starter = Intent(context, Main2Activity::class.java)
            context.startActivity(starter)
        }
    }
}
