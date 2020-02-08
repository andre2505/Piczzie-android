package com.ziggy.kdo.base.factory

import com.ziggy.kdo.repository.ChildRepository_Factory
import com.ziggy.kdo.ui.fragment.children.AddChildFragment
import com.ziggy.kdo.ui.fragment.children.ChildProfileFragment
import com.ziggy.kdo.ui.fragment.children.ChildrenFragment
import com.ziggy.kdo.ui.fragment.children.UpdateChildFragment
import com.ziggy.kdo.ui.fragment.home.HomeFragment
import com.ziggy.kdo.ui.fragment.profile.base.MyGiftFragment
import com.ziggy.kdo.ui.fragment.profile.base.MyReservationFragment
import com.ziggy.kdo.ui.fragment.profile.base.ProfileFragment
import com.ziggy.kdo.ui.fragment.profile.detail.MyGiftDetailFragment
import com.ziggy.kdo.ui.fragment.profile.detail.MyReservationDetailFragment
import com.ziggy.kdo.ui.fragment.friends.FriendsFragment
import com.ziggy.kdo.ui.fragment.gallery.CropFragment
import com.ziggy.kdo.ui.fragment.search.SearchFragment
import com.ziggy.kdo.ui.fragment.settings.edit_profile.user_information.UserInformationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.20
 */
@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindHomeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindProfileFragment(): ProfileFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindMyGiftFragment(): MyGiftFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindMyReservationFragment(): MyReservationFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindMyGiftDetailFragment(): MyGiftDetailFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindMyReservationDetailFragment(): MyReservationDetailFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindFriendsFragment(): FriendsFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindChildrenFragment(): ChildrenFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindSearchFragment(): SearchFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindAddChildFragment(): AddChildFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindChildProfileFragment(): ChildProfileFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindUpdateChildFragment(): UpdateChildFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindCropFragment(): CropFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindUserInformationFragement(): UserInformationFragment
}