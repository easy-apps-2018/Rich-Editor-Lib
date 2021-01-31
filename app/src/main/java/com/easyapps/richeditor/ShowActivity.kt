package com.easyapps.richeditor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.easyapps.richeditorlib.widgets.RichTextView

class ShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        var html: String? = ""

        intent?.let {
            html = it.getStringExtra("html")
        }

        val show: RichTextView = findViewById(R.id.richTextView)
        html?.let { h ->
            show.setHtml(h)
        }
    }
}