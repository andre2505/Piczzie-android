package com.ziggy.kdo.utils

import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.05
 */
class CustomNavHostFragment : NavHostFragment() {
    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return CustomNavigator(requireContext(), childFragmentManager, id)
    }
}