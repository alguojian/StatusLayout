package com.alguojian.statuslayout

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alguojian.mylibrary.DefaultStatusAdapter
import com.alguojian.mylibrary.StatusLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newAdapter.setOnClickListener {
            Main2Activity.start(this)
        }

        StatusLayout.getInstance().setDefaultAdapter(DefaultStatusAdapter())
        StatusLayout.setDebug()

//        supportFragmentManager.beginTransaction().replace(R.id.replace,SimpleFragment()).commitNow()

        StatusLayout.getInstance().attachView(this).onRetryClick(View.OnClickListener {
            requestData()
        })

        statusLayout.setOnClickListener {
            requestData()
        }
    }

    private fun requestData() {
        StatusLayout.getInstance().showLoading()
        StatusLayout.getInstance().showSuccess()
        StatusLayout.getInstance().showFailed()
        StatusLayout.getInstance().showEmpty()
        handler.postDelayed({ StatusLayout.getInstance().showFailed() }, 2000)
    }
}
