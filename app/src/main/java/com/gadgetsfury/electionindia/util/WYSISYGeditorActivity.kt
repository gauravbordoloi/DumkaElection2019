package com.gadgetsfury.electionindia.util

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.activity_wysisygeditor.*
import android.app.Activity
import android.content.Intent
import android.util.Log

class WYSISYGeditorActivity : AppCompatActivity() {

    private var selected = -1
    private lateinit var buttons: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wysisygeditor)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener { onBackPressed() }

        buttons = listOf(formatH1, formatH2, formatH3, formatBold,
            formatItalics, formatUnderline, formatUnorderedList, formatOrderedList)

        if (intent.hasExtra("content")) {
            editor.html = intent.getStringExtra("content")
        }
        editor.setPlaceholder(getString(R.string.insert_text_here))
        editor.setOnTextChangeListener {
            Log.e("Text", it)
            if (it.endsWith("<div><br></div>")) {
                updateBtn(-1)
            }
        }

        formatClear.setOnClickListener {
            editor.html = null
            selected = -1
        }

        formatUndo.setOnClickListener {
            editor.undo()
        }

        formatRedo.setOnClickListener {
            editor.redo()
        }

        formatH1.setOnClickListener {
            updateBtn(0)
            editor.setHeading(1)
        }

        formatH2.setOnClickListener {
            updateBtn(1)
            editor.setHeading(2)
        }

        formatH3.setOnClickListener {
            updateBtn(2)
            editor.setHeading(3)
        }

        formatBold.setOnClickListener {
            updateBtn(3)
            editor.setBold()
        }

        formatItalics.setOnClickListener {
            updateBtn(4)
            editor.setItalic()
        }

        formatUnderline.setOnClickListener {
            updateBtn(5)
            editor.setUnderline()
        }

        formatUnorderedList.setOnClickListener {
            updateBtn(6)
            editor.setBullets()
        }

        formatOrderedList.setOnClickListener {
            updateBtn(7)
            editor.setNumbers()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.menu_done) {
            val returnIntent = Intent()
            returnIntent.putExtra("content", editor.html)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateBtn(index: Int) {
        selected = index
        var i = 0
        for (iv in buttons) {
            if (i == selected) {
                buttons[i].setBackgroundResource(R.drawable.wisisyg_item_seletced)
                buttons[i].setColorFilter(Color.WHITE)
            } else {
                buttons[i].setBackgroundResource(R.color.white)
                buttons[i].setColorFilter(Color.BLACK)
            }
            i += 1
        }
    }

}
