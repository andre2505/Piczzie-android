package com.ziggy.kdo.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.concurrent.fixedRateTimer

/**
 * The class description here.
 *
 * @author thomas
 * @since 2019.03.05
 */
@Navigator.Name("fragment")
class CustomNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {
    private var popBackStackListener: ((Int) -> Unit)? = null

    private val backStack = Stack<Int>()

    private val onBackStackChangedListener = FragmentManager.OnBackStackChangedListener {

        if (backStack.count() > manager.backStackEntryCount) {
            backStack.pop()
            popBackStackListener?.invoke(backStack.peek())
        }
    }

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {

        val tag = destination.id.toString()
        val transaction = manager.beginTransaction()

        val currentFragment = manager.primaryNavigationFragment
        if (currentFragment != null) {
            transaction.remove(currentFragment)
        }

        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            val className = destination.className
            fragment = instantiateFragment(context, manager, className, args)
            transaction.add(containerId, fragment, tag)
        } else {
            transaction.show(fragment)
        }

        transaction.setPrimaryNavigationFragment(currentFragment)
        transaction.setReorderingAllowed(true)
        transaction.commit()

        return destination
    }

    fun setPopBackStackListener(listener: (Int) -> Unit) {
        popBackStackListener = listener
    }

    // For rotation
    override fun onSaveState(): Bundle? {
        val bundle = super.onSaveState() ?: Bundle()
        bundle.putIntArray(KEY_BACK_STACK_IDS, backStack.toIntArray())
        return bundle
    }

    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)
        savedState?.getIntArray(KEY_BACK_STACK_IDS)?.forEach {
            backStack.push(it)
        }
    }

    companion object {
        private const val KEY_BACK_STACK_IDS = "back_stack_ids"
    }

    fun BottomNavigationView.setupWithNavController(navController: NavController, navigator: CustomNavigator) {
        setOnNavigationItemSelectedListener {
            try {
                navController.navigate(it.itemId)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Change tab
            (0 until menu.size()).forEach {
                val item = menu.getItem(it)
                if (item.itemId == destination.id) {
                    item.isChecked = true
                }
            }
        }

        navigator.setPopBackStackListener { id ->
            // Press back key
            (0 until menu.size()).forEach {
                val item = menu.getItem(it)
                if (item.itemId == id) {
                    item.isChecked = true
                }
            }
        }
    }
}