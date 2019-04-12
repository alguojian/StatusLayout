package com.alguojian.statuslayout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alguojian.mylibrary.DefaultStatusAdapter
import com.alguojian.mylibrary.StatusLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        StatusLayout.getThis().setDefaultAdapter(DefaultStatusAdapter())
        StatusLayout.setDebug()

        StatusLayout.getThis().attachView(this).onRetryClick(View.OnClickListener {
            println("------------000000")
        })

        aaaa.setOnClickListener {
            StatusLayout.getThis().showEmpty()
        }
    }
}
