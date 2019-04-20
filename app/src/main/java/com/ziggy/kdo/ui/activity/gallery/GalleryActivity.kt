package com.ziggy.kdo.ui.activity.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ziggy.kdo.R
import com.ziggy.kdo.ui.activity.camera.CameraActivity
import com.ziggy.kdo.ui.activity.camera.CameraViewModel
import com.ziggy.kdo.ui.base.BaseActivity
import com.ziggy.kdo.ui.fragment.gallery.GalleryFragment


class GalleryActivity : BaseActivity() {

    private lateinit var mNavDestination: NavController

    private lateinit var mCameraViewModel: CameraViewModel

    private lateinit var mToolbar: Toolbar

    private lateinit var mButtonToolbar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        mToolbar = findViewById(R.id.toolbar_camera_layout)

        mButtonToolbar = mToolbar.findViewById(R.id.gallery_button_validation)

        // config top view
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //viewModel
        mCameraViewModel = ViewModelProviders.of(this@GalleryActivity, mViewModeFactory).get(CameraViewModel::class.java)

        // Get controller navigation
        mNavDestination = Navigation.findNavController(this@GalleryActivity, R.id.gallery_navigation)
    }

    override fun onSupportNavigateUp(): Boolean {
        val destination = mNavDestination.currentDestination
        if (destination!!.id == R.id.galleryFragment) {
            finish()
        } else {
            mButtonToolbar.visibility = View.VISIBLE
        }
        return Navigation.findNavController(this@GalleryActivity, R.id.gallery_navigation).navigateUp()
    }

    override fun onBackPressed() {
        val destination = mNavDestination.currentDestination
        if (destination!!.id == R.id.galleryFragment) {
            finish()
        } else {
            mButtonToolbar.visibility = View.VISIBLE
        }
        super.onBackPressed()
    }

    fun toCrop(view: View) {
        GalleryFragment.toCrop()
        mButtonToolbar.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, GalleryActivity::class.java)
            context.startActivity(intent)
        }
    }
}
