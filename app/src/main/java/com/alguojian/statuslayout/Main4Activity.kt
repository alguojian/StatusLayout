package com.alguojian.statuslayout

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.alguojian.mylibrary.StatusLayout
import kotlinx.android.synthetic.main.activity_main4.*

class Main4Activity : AppCompatActivity() {

    lateinit var statusHelper: StatusLayout.StatusHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        statusHelper = StatusLayout.attachView(this).onRetryClick {
            requestData()
        }

        statusLayout.setOnClickListener {
            requestData()
        }
    }

    companion object {
        val handler = Handler()
        fun start(context: Context) {
            val starter = Intent(context, Main4Activity::class.java)
            context.startActivity(starter)
        }
    }

    private fun requestData() {
        statusHelper.showLoading()
        handler.postDelayed({ statusHelper.showEmpty() }, 2000)
    }

}
