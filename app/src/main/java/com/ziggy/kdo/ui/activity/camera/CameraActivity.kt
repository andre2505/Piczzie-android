package com.ziggy.kdo.ui.activity.camera

import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ziggy.kdo.R
import com.ziggy.kdo.ui.activity.register.RegisterViewModel
import com.ziggy.kdo.ui.base.BaseActivity
import com.ziggy.kdo.ui.fragment.camera.CameraFragment
import com.ziggy.kdo.utils.CameraPreview

const val CONFIGURATION_PROFILE = "configuration_profile"

class CameraActivity : BaseActivity() {

    private lateinit var mNavDestination: NavController

    private lateinit var mCameraViewModel: CameraViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        PROFILE_CONFIG = intent.getBooleanExtra(CONFIGURATION_PROFILE, false)
        // config top view
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setSupportActionBar(findViewById(R.id.toolbar_camera_layout))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Get controller navigation
        mNavDestination = Navigation.findNavController(this@CameraActivity, R.id.camera_navigation)

        // Get CameraView model for fragment
        mCameraViewModel = ViewModelProviders.of(this@CameraActivity, mViewModeFactory).get(CameraViewModel::class.java)

    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onSupportNavigateUp(): Boolean {
        val destination = mNavDestination.currentDestination
        if (destination!!.id == R.id.cameraFragment) {
            finish()
        }
        return Navigation.findNavController(this@CameraActivity, R.id.camera_navigation).navigateUp()
    }

    override fun onBackPressed() {
        val destination = mNavDestination.currentDestination
        if (destination!!.id == R.id.cameraFragment) {
            finish()
        }
        super.onBackPressed()
    }

    companion object {

        var PROFILE_CONFIG = false

        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, CameraActivity::class.java)
            context.startActivity(intent)
        }
    }

}
