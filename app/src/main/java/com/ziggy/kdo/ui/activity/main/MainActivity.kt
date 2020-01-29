package com.ziggy.kdo.ui.activity.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.ziggy.kdo.BuildConfig
import com.ziggy.kdo.R
import com.ziggy.kdo.enums.Error
import com.ziggy.kdo.model.Gift
import com.ziggy.kdo.model.User
import com.ziggy.kdo.network.configuration.UserSession
import com.ziggy.kdo.ui.activity.camera.CameraActivity
import com.ziggy.kdo.ui.activity.gallery.GalleryActivity
import com.ziggy.kdo.ui.base.BaseActivity
import com.ziggy.kdo.ui.fragment.home.HomeViewModel
import com.ziggy.kdo.ui.fragment.profile.base.ACTION_PHOTO_USER
import com.ziggy.kdo.ui.fragment.profile.base.EXTRA_USER
import com.ziggy.kdo.ui.fragment.search.SearchViewModel
import com.ziggy.kdo.utils.ProgressRequestBody
import com.ziggy.kdo.utils.setupWithNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File


class MainActivity : BaseActivity(), NavController.OnDestinationChangedListener,
    MenuItem.OnMenuItemClickListener,
    ProgressRequestBody.ProgressListener, androidx.appcompat.widget.SearchView.OnQueryTextListener,
    NavigationView.OnNavigationItemSelectedListener {

    private val NAVIGATION_FRAGMENT_SEARCH: String = "fragment_search"

    private val NAVIGATION_FRAGMENT_HOME: String = "fragment_home"

    private val NAVIGATION_PROFILE: String = "fragment_profile"

    private val NAVIGATION_MY_GIFT_DETAIL: String = "MyGiftDetailFragment"

    private val NAVIGATION_MY_RESERVATION_DETAIL: String = "MyReservationDetailFragment"

    private val NAVIGATION_MY_FRIENDS: String = "FriendsFragment"

    private val NAVIGATION_CHILDREN: String = "ChildrenFragment"

    private val NAVIGATION_ADD_CHILDREN: String = "AddChildFragment"

    private val NAVIGATION_CHILD_PROFILE: String = "ChildProfileFragment"

    private val NAVIGATION_CHILD_UPDATE: String = "UpdateChildFragment"

    private val NAVIGATION_EDIT_PROFILE: String = "edit_profile"

    private lateinit var mBottomNavigationView: BottomNavigationView

    private lateinit var mToolbar: Toolbar

    private lateinit var mMainViewModel: MainViewModel

    private lateinit var mSearchViewModel: SearchViewModel

    private lateinit var mProgressBar: ProgressBar

    private lateinit var mTextViewUpload: TextView

    private lateinit var mLinearLayout: LinearLayout

    private lateinit var mLabelFragment: String

    private lateinit var mSearchView: androidx.appcompat.widget.SearchView

    private lateinit var appBarConfig: AppBarConfiguration

    lateinit var mDrawerLayout: DrawerLayout

    private lateinit var mNavigationView: NavigationView

    private lateinit var controller: LiveData<NavController>

    private lateinit var mToolbarTitle: TextView

    private lateinit var drawerImage: ImageView

    private var currentNavController: LiveData<NavController>? = null

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                RECEIVER_UPLOAD -> {
                    mLinearLayout.visibility = View.VISIBLE

                    val file = File(intent.getStringExtra(EXTRA_FILE))
                    val requestFile =
                        ProgressRequestBody(this@MainActivity, file, this@MainActivity)

                    mMainViewModel.mGift.value = intent.getSerializableExtra(EXTRA_GIFT) as Gift
                    mMainViewModel.mMultipartBody.value =
                        MultipartBody.Part.createFormData("image", file.name, requestFile)

                    mMainViewModel.uploadGift()
                }
                ACTION_PHOTO_USER -> {
                    Glide
                        .with(this@MainActivity)
                        .load(BuildConfig.ENDPOINT + UserSession.getPhoto(this@MainActivity))
                        .apply(
                            RequestOptions
                                .circleCropTransform()
                        )
                        .into(drawerImage)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBottomNavigationView = findViewById(R.id.bottom_navigation)
        mToolbar = findViewById(R.id.toolbar_home)
        mProgressBar = findViewById(R.id.main_progress_bar)
        mTextViewUpload = findViewById(R.id.main_textview_upload)
        mLinearLayout = findViewById(R.id.main_linear_upload)
        mSearchView = mToolbar.findViewById(R.id.toolbar_searchview)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavigationView = findViewById(R.id.nav_view)
        mToolbarTitle = mToolbar.findViewById(R.id.toolbar_title)

        mNavigationView.setNavigationItemSelectedListener(this)

        val selectedItem = mBottomNavigationView.menu.findItem(R.id.camera)

        //Add custom bar
        setSupportActionBar(mToolbar)

        //Drawer layout
        initDrawerLayout()

        //SearchManager
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (mSearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        mSearchView.setOnQueryTextListener(this@MainActivity)

        mMainViewModel = ViewModelProviders.of(this@MainActivity, mViewModeFactory)
            .get(MainViewModel::class.java)
        ViewModelProviders.of(this@MainActivity, mViewModeFactory).get(HomeViewModel::class.java)
        mSearchViewModel = ViewModelProviders.of(this@MainActivity, mViewModeFactory)
            .get(SearchViewModel::class.java)

        mMainViewModel.mValidationSuccess.observe(this@MainActivity, Observer { error ->
            when (error!!) {
                Error.NO_ERROR -> {
                    Handler().postDelayed({
                        mLinearLayout.visibility = View.GONE
                    }, 3000)
                }
                Error.ERROR_REQUEST -> {
                    showAlertDialogError()
                }
                Error.ERROR_NETWORK -> {
                    showAlertDialogError()
                }
            }
        })

        //Graph navigation
        val navGraphIds =
            listOf(
                R.navigation.navigation_home,
                R.navigation.navigation_search,
                R.navigation.navigation_notification,
                R.navigation.navigation_profile
            )

        // Setup the bottom navigation view with a list of navigation graphs
        controller = mBottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.home_navigation,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            NavigationUI.setupActionBarWithNavController(
                this,
                navController
            )
            navController.addOnDestinationChangedListener(this)
        })
        currentNavController = controller

        //Open add photo
        selectedItem.setOnMenuItemClickListener(this@MainActivity)

        //broadcast receiver upload
        val filter = IntentFilter()
        filter.addAction(RECEIVER_UPLOAD)
        filter.addAction(ACTION_PHOTO_USER)
        LocalBroadcastManager.getInstance(this@MainActivity).registerReceiver(mReceiver, filter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        when (mLabelFragment) {
            NAVIGATION_FRAGMENT_HOME -> {
                menuInflater.inflate(R.menu.menu_home, menu)
            }
            NAVIGATION_PROFILE -> {
                menuInflater.inflate(R.menu.menu_profile, menu)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_camera -> CameraActivity.newInstance(this@MainActivity)
            R.id.action_drawer -> mDrawerLayout.openDrawer(GravityCompat.END)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        mLabelFragment = destination.label.toString()
        supportActionBar?.setDisplayShowTitleEnabled(false)

        when (mLabelFragment) {
            NAVIGATION_FRAGMENT_HOME -> {
                mSearchView.visibility = View.GONE
                mToolbarTitle.visibility = View.VISIBLE
                mToolbarTitle.text = getText(R.string.app_name)
            }
            NAVIGATION_PROFILE -> {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                mToolbarTitle.visibility = View.VISIBLE
                mSearchView.visibility = View.GONE
                mToolbarTitle.text = getText(R.string.profile_title)
            }
            NAVIGATION_FRAGMENT_SEARCH -> {
                mToolbarTitle.visibility = View.GONE
                mSearchView.visibility = View.VISIBLE
            }
            NAVIGATION_MY_GIFT_DETAIL -> {
                mToolbarTitle.visibility = View.GONE
                supportActionBar?.title = getString(R.string.navigation_gift_detail)
                supportActionBar?.setDisplayShowTitleEnabled(true)
            }
            NAVIGATION_MY_RESERVATION_DETAIL -> {
                mToolbarTitle.visibility = View.GONE
                supportActionBar?.setDisplayShowTitleEnabled(true)
            }
            NAVIGATION_MY_FRIENDS -> {
                mToolbarTitle.visibility = View.GONE
                supportActionBar?.title = getString(R.string.navigation_friends_list)
                supportActionBar?.setDisplayShowTitleEnabled(true)
            }
            NAVIGATION_CHILDREN -> {
                mToolbarTitle.visibility = View.GONE
                supportActionBar?.title = getString(R.string.navigation_children_list)
                supportActionBar?.setDisplayShowTitleEnabled(true)
            }
            NAVIGATION_ADD_CHILDREN -> {
                mToolbarTitle.visibility = View.GONE
                mToolbar.setNavigationIcon(R.drawable.ic_close_edit)
                supportActionBar?.title = getString(R.string.navigation_add_children)
                supportActionBar?.setDisplayShowTitleEnabled(true)
            }
            NAVIGATION_CHILD_PROFILE -> {
                mToolbarTitle.visibility = View.GONE
                supportActionBar?.title = getString(R.string.navigation_children_profile)
                supportActionBar?.setDisplayShowTitleEnabled(true)
            }
            NAVIGATION_CHILD_UPDATE -> {
                mToolbarTitle.visibility = View.GONE
                mToolbar.setNavigationIcon(R.drawable.ic_close_edit)
                supportActionBar?.title = getString(R.string.navigation_children_update)
                supportActionBar?.setDisplayShowTitleEnabled(true)
            }
            NAVIGATION_EDIT_PROFILE -> {
                mToolbarTitle.visibility = View.GONE
                supportActionBar?.title = getString(R.string.navigation_edit_profile)
                supportActionBar?.setDisplayShowTitleEnabled(true)
            }
        }

        if (mLabelFragment != NAVIGATION_PROFILE) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

        if (mLabelFragment != NAVIGATION_FRAGMENT_SEARCH) {
            mSearchView.visibility = View.GONE
        }

        invalidateOptionsMenu()
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        GalleryActivity.newInstance(this@MainActivity)
        return true
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.nav_edit_profile -> {
                currentNavController?.value!!.navigate(R.id.action_profile_to_navigation_setting)
            }
        }
        return true
    }

    override fun onUploadProgress(progressInPercent: Int, totalBytes: Long) {
        runOnUiThread {
            mProgressBar.progress = progressInPercent

            mTextViewUpload.text = String.format("%d %%", progressInPercent)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        GlobalScope.launch(Dispatchers.Main) {
            delay(500)
            newText?.let { theNewText ->
                mSearchViewModel.getSearchUser(theNewText)
            }
        }
        return true
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this@MainActivity).unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp()
            ?: currentNavController?.value!!.navigateUp(appBarConfig)
    }


    override fun onBackPressed() {
        if (currentNavController?.value?.popBackStack() != true) {
            super.onBackPressed()
        }
    }

    private fun initDrawerLayout() {

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val header = navigationView.getHeaderView(0)
        drawerImage = header.findViewById<ImageView>(R.id.drawer_header)
        val drawerName = header.findViewById<TextView>(R.id.drawer_name)
        val drawerMail = header.findViewById<TextView>(R.id.drawer_mail)

        Glide
            .with(this)
            .load(BuildConfig.ENDPOINT + UserSession.getPhoto(this))
            .apply(
                RequestOptions
                    .circleCropTransform()
            )
            .into(drawerImage)

        if (UserSession.getFirstname(this) != null && UserSession.getLastname(this) != null) {
            val firstname: String = UserSession.getFirstname(this)!!
            val lastname: String = UserSession.getLastname(this)!!

            drawerName.text = "$firstname $lastname"
        }

        drawerMail.text = UserSession.getMail(this)
    }

    private fun showAlertDialogError() {

        val dialog = Dialog(this@MainActivity)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.view_dialog_error)

        val dialogButton: Button = dialog.findViewById(R.id.btn_dialog)
        val dialogButtonRetry: Button = dialog.findViewById(R.id.btn_dialog_retry)
        val alertSubText: TextView = dialog.findViewById(R.id.text_dialog_sub_title)

        alertSubText.text = getString(R.string.main_alert_dialog_text)

        dialogButton.setOnClickListener {
            dialog.dismiss()
            mLinearLayout.visibility = View.GONE
        }

        dialogButtonRetry.setOnClickListener {
            dialog.dismiss()
            mMainViewModel.uploadGift()
        }

        dialog.show()
    }

    companion object {
        const val RECEIVER_UPLOAD = "receiver_upload"
        const val EXTRA_FILE = "extra_file"
        const val EXTRA_GIFT = "extra_gift"
    }

}
