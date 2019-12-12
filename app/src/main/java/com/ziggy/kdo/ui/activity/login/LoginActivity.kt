package com.ziggy.kdo.ui.activity.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ziggy.kdo.R
import com.ziggy.kdo.databinding.ActivityLoginBinding
import com.ziggy.kdo.network.configuration.UserSession
import com.ziggy.kdo.ui.activity.main.MainActivity
import com.ziggy.kdo.ui.activity.register.RegisterActivity
import com.ziggy.kdo.ui.base.BaseActivity
import java.util.concurrent.Executor

class LoginActivity : BaseActivity() {

    lateinit var mLoginBinding: ActivityLoginBinding

    lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLoginBinding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)

        mLoginViewModel = ViewModelProviders.of(this@LoginActivity, mViewModeFactory).get(LoginViewModel::class.java)

        //LiveData with Binding
        mLoginBinding.loginViewModel = mLoginViewModel
        mLoginBinding.lifecycleOwner = this@LoginActivity

        //Observe identification
        mLoginViewModel.mSucessAuthenticated.observe(this, Observer {
            if (it == true) {

                val token = mLoginViewModel.mUserLogin.value?.token
                val refreshToken = mLoginViewModel.mUserLogin.value?.tokenRefresh
                val id = mLoginViewModel.mUserLogin.value?.id
                val mail = mLoginViewModel.mUserLogin.value?.email
                val firstname = mLoginViewModel.mUserLogin.value?.firstname
                val lastname = mLoginViewModel.mUserLogin.value?.lastname
                val photo = mLoginViewModel.mUserLogin.value?.photo

                UserSession.createUserToken(
                    this@LoginActivity,
                    token,
                    refreshToken,
                    id,
                    mail,
                    firstname,
                    lastname,
                    photo
                )

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
            if (it == false) {
                Toast.makeText(this, R.string.network_error_return, Toast.LENGTH_LONG).show()
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
