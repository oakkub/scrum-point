package com.oakkub.pointpoker.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.oakkub.pointpoker.extensions.fullWidthWrapHeight
import com.oakkub.pointpoker.extensions.wrapWidthWrapHeight
import com.oakkub.pointpoker.helpers.createToolbarTextView
import com.oakkub.pointpoker.ui.settings.color.ChangeColorActivity
import com.oakkub.simplepoker.R

/**
 * Created by oakkub on 8/8/2017 AD.
 */
// TODO: Use this activity later when settings has more than change color ability
class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createContainer())
    }

    fun createContainer() = LinearLayout(this).fullWidthWrapHeight().apply {
        orientation = LinearLayout.VERTICAL

        val toolbar = createToolbarTextView(this@SettingsActivity).apply {
            text = getString(R.string.settings_title)
        }
        addView(toolbar)
        addView(createChangeColorButton())
    }

    fun createChangeColorButton() = Button(this).wrapWidthWrapHeight().apply {
        text = getString(R.string.settings_change_button_text)
        textSize = resources.getDimension(R.dimen.settings_button_text_size)
        setAllCaps(false)

        setOnClickListener {
            val intent = Intent(this@SettingsActivity, ChangeColorActivity::class.java)
            startActivity(intent)
        }
    }

}