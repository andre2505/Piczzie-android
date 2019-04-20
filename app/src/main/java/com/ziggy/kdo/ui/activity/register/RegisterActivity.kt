package com.ziggy.kdo.ui.activity.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.ziggy.kdo.R
import com.ziggy.kdo.ui.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar_register.*
import androidx.navigation.NavDestination


class RegisterActivity : BaseActivity() {

    private lateinit var mRegisterViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        mRegisterViewModel =
                ViewModelProviders.of(this@RegisterActivity, mViewModeFactory).get(RegisterViewModel::class.java)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navDestination =
            Navigation.findNavController(this@RegisterActivity, R.id.register_navigation).currentDestination
        if (navDestination!!.id == R.id.registerPartOneFragment) {
            finish()
        }
        return Navigation.findNavController(this@RegisterActivity, R.id.register_navigation).navigateUp()
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}
