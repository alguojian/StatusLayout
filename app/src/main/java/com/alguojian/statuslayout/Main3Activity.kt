package com.alguojian.statuslayout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.alguojian.mylibrary.StatusLayout
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        supportFragmentManager.beginTransaction().replace(R.id.statusLayout,SimpleFragment()).commit()
    }

    companion object {
        val handler = Handler()
        fun start(context: Context) {
            val starter = Intent(context, Main3Activity::class.java)
            context.startActivity(starter)
        }
    }
}
