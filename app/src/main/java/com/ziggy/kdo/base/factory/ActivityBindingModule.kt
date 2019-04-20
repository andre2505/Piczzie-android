package com.ziggy.kdo.base.factory

import com.ziggy.kdo.ui.activity.camera.CameraActivity
import com.ziggy.kdo.ui.activity.gallery.GalleryActivity
import com.ziggy.kdo.ui.activity.login.LoginActivity
import com.ziggy.kdo.ui.activity.main.MainActivity
import com.ziggy.kdo.ui.activity.register.RegisterActivity
import com.ziggy.kdo.ui.fragment.register.RegisterPartOneFragment
import com.ziggy.kdo.ui.fragment.register.RegisterPartTwoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindRegisterActivity(): RegisterActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindCameraActivity(): CameraActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindGalleryActivity(): GalleryActivity

}