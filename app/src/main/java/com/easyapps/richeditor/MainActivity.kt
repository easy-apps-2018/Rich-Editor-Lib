package com.easyapps.richeditor

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.easyapps.richeditorlib.widgets.RichEditText
import com.easyapps.richeditorlib.widgets.StyleBar

class MainActivity : AppCompatActivity() {

    private lateinit var styleBar: StyleBar
    private lateinit var richEditText: RichEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        styleBar = findViewById(R.id.styleBar)
        richEditText = findViewById(R.id.richEditText)
        richEditText.setStyleBar(styleBar)
        styleBar.setEditText(richEditText)

        val html = "<html>\n" +
                "    <body>\n" +
                "    <p><span style=\"font-size:18px\";><span style=\"color:#000000;\">Simple Rich Editor:</span></span></p>\n" +
                "    <br>\n" +
                "    <p><b><span style=\"font-size:18px\";><span style=\"color:#000000;\">Bold</span></span></b></p>\n" +
                "    <p><i><span style=\"font-size:18px\";><span style=\"color:#000000;\">Italic</span></span></i></p>\n" +
                "    <p><u><span style=\"font-size:18px\";><span style=\"color:#000000;\">Undeline</span></span></u></p>\n" +
                "    <p><span style=\"text-decoration:line-through;\"><span style=\"font-size:18px\";><span style=\"color:#000000;\">Strike through</span></span></span></p>\n" +
                "    <p><span style=\"font-size:26px\";><span style=\"color:#000000;\">Text Size</span></span></p>\n" +
                "    <p><span style=\"color:#000000;\"><span style=\"font-size:18px\";>Colored text like </span></span><span style=\"color:#FF0000;\"><span style=\"font-size:18px\";>red </span></span><span style=\"color:#FFEB3B;\"><span style=\"font-size:18px\";>yellow </span></span><span style=\"font-size:18px\";><span style=\"color:#03A9F4;\">...</span></span></p>\n" +
                "    <br>\n" +
                "    <p><b><i><u><span style=\"text-decoration:line-through;\"><span style=\"font-size:32px\";><span style=\"color:#03A9F4;\">All </span></span></span></u></i></b><b><i><u><span style=\"text-decoration:line-through;\"><span style=\"font-size:32px\";><span style=\"color:#FF0000;\">in </span></span></span></u></i></b><b><i><u><span style=\"text-decoration:line-through;\"><span style=\"font-size:32px\";><span style=\"color:#607D8B;\">one </span></span></span></u></i></b><b><i><u><span style=\"text-decoration:line-through;\"><span style=\"font-size:32px\";><span style=\"color:#CDDC39;\">text!</span></span></span></u></i></b></p>\n" +
                "    </body>\n" +
                "    </html>"
        richEditText.setHtml(html)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_show -> {
                if (richEditText.text.toString().isNotEmpty()) {
                    val html = richEditText.getHtml()
                    startActivity(Intent(this, ShowActivity::class.java).apply {
                        putExtra("html", html)
                    })
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}