package com.ziggy.kdo.ui.activity.hub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ziggy.kdo.network.configuration.UserSession
import com.ziggy.kdo.ui.activity.login.LoginActivity
import com.ziggy.kdo.ui.activity.main.MainActivity

class HubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (UserSession.getUserToken(this@HubActivity) != "" && UserSession.getUserRefreshToken(this@HubActivity) != "") {
            startActivity(Intent(this@HubActivity, MainActivity::class.java))
        } else {
            startActivity(Intent(this@HubActivity, LoginActivity::class.java))
        }
        finish()
    }
}
