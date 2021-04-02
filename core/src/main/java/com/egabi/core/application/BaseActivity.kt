package com.egabi.core.application

import android.content.res.Resources
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.egabi.core.constants.Constants
import java.util.*


abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val language = sharedPreferences.getString("language", "en")
        setLocale(language)
        Constants.language=language.toUpperCase()
    }

    private fun setUpToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    fun setLocale(newLanguage: String)  {

    val res: Resources = this.resources

    val dm = res.displayMetrics
    val conf = res.configuration
        conf.setLocale(Locale(newLanguage)) // API 17+ only.
    res.updateConfiguration(conf, dm)
    }


}