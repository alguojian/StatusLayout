package com.alguojian.statuslayout

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.alguojian.mylibrary.StatusLayoutDefaultAdapter
import com.alguojian.mylibrary.StatusLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val handler = Handler()
    }

    lateinit var statusHelper: StatusLayout.StatusHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newAdapter.setOnClickListener {
            Main2Activity.start(this)
        }
        fragment.setOnClickListener {
            Main3Activity.start(this)
        }

        newActivity.setOnClickListener {
            Main4Activity.start(this)
        }

        statusLayout.setOnClickListener {
            requestData()
        }
        StatusLayout.setDefaultAdapter(StatusLayoutDefaultAdapter())
        statusHelper = StatusLayout.attachView(this).onRetryClick {
            requestData()
        }
    }

    private fun requestData() {
        statusHelper.showLoading()
        handler.postDelayed({ statusHelper.showSuccess() }, 2000)
    }
}
