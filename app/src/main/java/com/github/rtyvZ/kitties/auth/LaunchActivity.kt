package com.github.rtyvZ.kitties.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.App
import com.github.rtyvZ.kitties.extentions.toggleVisibility
import com.github.rtyvZ.kitties.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.launch_activity.*

class LaunchActivity : AppCompatActivity(R.layout.launch_activity) {

    private val viewModel: LaunchViewModel by viewModels()

    private val sessionStorage = App.SessionStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.SessionStorage.restoreSession()
        progress.toggleVisibility()


        if (sessionStorage.hasSession()) {
            progress.toggleVisibility()
            startMainActivity()
        } else {
            stateLaunch.text = this.getString(R.string.auth)
            viewModel.requestUid()
        }

        viewModel.getUserUid.observe(this, {
            progress.toggleVisibility()
            stateLaunch.text = this.getString(R.string.success)
            if (!sessionStorage.hasSession())
                sessionStorage.saveSession(UserSession(it))
            startMainActivity()
        })

        viewModel.error.observe(this, {
            it?.let { throwable ->
                val charSequence: CharSequence = throwable.message.toString()
                progress.toggleVisibility()
                stateLaunch.text = this.getString(R.string.empty)
                Snackbar.make(progress, charSequence, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun startMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}