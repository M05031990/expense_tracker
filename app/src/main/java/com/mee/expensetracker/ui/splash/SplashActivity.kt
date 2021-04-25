package com.mee.expensetracker.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mee.expensetracker.R
import com.mee.expensetracker.base.BaseActivity
import com.mee.expensetracker.db.SharedPreferenceManager
import com.mee.expensetracker.ui.main.MainActivity
import com.mee.expensetracker.ui.start.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (sharedPreferenceManager.isInitialize())
            startActivity(Intent(this, MainActivity::class.java))
        else
            startActivity(Intent(this, StartActivity::class.java))

        finish()
    }
}