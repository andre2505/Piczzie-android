package com.ziggy.kdo.base.factory

import androidx.lifecycle.ViewModel
import com.ziggy.kdo.ui.activity.camera.CameraViewModel
import com.ziggy.kdo.ui.activity.login.LoginViewModel
import com.ziggy.kdo.ui.activity.main.MainViewModel
import com.ziggy.kdo.ui.activity.register.RegisterViewModel
import com.ziggy.kdo.ui.fragment.FragmentViewModel
import com.ziggy.kdo.ui.fragment.children.ChildViewModel
import com.ziggy.kdo.ui.fragment.friends.FriendViewModel
import com.ziggy.kdo.ui.fragment.home.HomeViewModel
import com.ziggy.kdo.ui.fragment.profile.ProfileViewModel
import com.ziggy.kdo.ui.fragment.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class])
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMyViewModel(myViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CameraViewModel::class)
    abstract fun bindCameraViewModel(cameraViewModel: CameraViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChildViewModel::class)
    abstract fun bindChildViewModel(childViewModel: ChildViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FriendViewModel::class)
    abstract fun bindFriendViewModel(friendViewModel: FriendViewModel): ViewModel

}