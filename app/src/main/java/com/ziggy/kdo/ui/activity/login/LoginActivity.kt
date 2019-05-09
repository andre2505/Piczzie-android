package com.ziggy.kdo.ui.activity.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.ActivityLoginBinding
import com.ziggy.kdo.network.configuration.UserSession
import com.ziggy.kdo.ui.activity.main.MainActivity
import com.ziggy.kdo.ui.activity.register.RegisterActivity
import com.ziggy.kdo.ui.base.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

@Suppress("DEPRECATION")
class LoginActivity : BaseActivity() {

    lateinit var mLoginBinding: ActivityLoginBinding

    lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLoginBinding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)

        mLoginViewModel = ViewModelProviders.of(this@LoginActivity, mViewModeFactory).get(LoginViewModel::class.java)

        //LiveData with Binding
        mLoginBinding.loginViewModel = mLoginViewModel
        mLoginBinding.setLifecycleOwner(this@LoginActivity)

        //Observe identification
        mLoginViewModel.mSucessAuthenticated.observe(this, Observer {
            if (it == true) {

                val token = mLoginViewModel.mToken?.token
                val refreshToken = mLoginViewModel.mToken?.tokenRefresh

                UserSession.createUserToken(this@LoginActivity, token, refreshToken, mLoginViewModel.mToken?.uid)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
            if (it == false) {
                RegisterActivity.newInstance(this@LoginActivity)
            }
        })
    }

    fun toRegister(view: View) {
        RegisterActivity.newInstance(this@LoginActivity)
    }

    companion object {
        fun newInstance(context: Context) {
            val app = context
            val intent = Intent(app, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            app.startActivity(intent)
        }
    }

}
