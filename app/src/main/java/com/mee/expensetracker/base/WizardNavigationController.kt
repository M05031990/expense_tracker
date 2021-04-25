package com.mee.expensetracker.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.mee.expensetracker.R

interface NavigationController {
    fun next(fragment: Fragment)
    fun previous()
    fun up()
    fun finishNC()
}

interface NCTagable {
    fun getNCTag(): String
}

interface Validator {
    fun isValid(): Boolean
}

class Navigator(private val supportFragmentManager: FragmentManager, @IdRes private val containerRes: Int,
                homeFragment: Fragment? = null, savedInstanceState: Bundle? = null) {

    private var backStackHandler = BackStackHandler(supportFragmentManager)


    init {

        if (savedInstanceState == null) {
            homeFragment?.let {
                home(it)
            }
        }
    }

    /**
     * go next by activity
     */
    fun next(fragment: androidx.fragment.app.Fragment, skip: Boolean = false, newTag: String = "") {
        navigateTo(fragment, skip, newTag)
    }

    // call  when implement backStack Fix
    fun previous() {
        backStackHandler.previous()?.let {
            supportFragmentManager.popBackStack(it, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun home(fragment: Fragment, skipBackStack: Boolean = false) {

        var skip = skipBackStack

        supportFragmentManager
                .beginTransaction()
                .replace(containerRes, fragment, fragment.javaClass.name)
                .commit()

//        if (fragment is OTPFragment) {
//            skip = true
//        }

        backStackHandler.home(fragment, skip)
    }

    /**
     * set up default fragment
     */
    fun up() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        backStackHandler.clear()
    }

    /**
     * navigate to fragments
     */
    private fun navigateTo(fragment: Fragment, _skipBackStack: Boolean, newTag: String = "") {
        var skipBackStack = _skipBackStack

//        if (fragment is OTPFragment) {
//            skipBackStack = true
//        }

        val container = containerRes

        val tags = backStackHandler.addFragment(fragment, skipBackStack, newTag)

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.activity_enter_start, R.anim.activity_enter_end,
                        R.anim.activity_exit_start, R.anim.activity_exit_end)

        fragmentTransaction.replace(container, fragment, tags.fragmentTag)

        if(backStackHandler.getHomeStackEntry() != tags && (backStackHandler.getHomeStackEntry()?.skipBack == false || backStackHandler.getHomeStackEntry()?.skipBack == true && backStackHandler.getSkipEntries().count() < backStackHandler.size()-1))
            fragmentTransaction.addToBackStack(tags.transactionTag)

        fragmentTransaction.commit()
    }

    fun isBackStackEmpty() = backStackHandler.isEmpty()
}


private class BackStackHandler(val supportFragmentManager: FragmentManager) {

    private val stack = mutableListOf<StackEntry>()
    private val stackMap = mutableMapOf<String, String>()

    fun clear() {
        stackMap.clear()
        if (stack.size > 1)
            stack.subList(1, stack.lastIndex).clear()
    }

    fun home(fragment: Fragment, skip: Boolean = false) {
        stack.add(StackEntry(fragment.javaClass.name, skipBack = skip))
    }

    fun getHomeStackEntry() = stack.getOrNull(0)

    fun getLastStackEntry() = stack.lastOrNull()

    fun getHomeTag() = stack.getOrNull(0)?.fragmentTag

    fun addFragment(nextFragment: Fragment, skipBackStack: Boolean, newTag: String = ""): StackEntry {

        var newFragmentTag = nextFragment.javaClass.name

        val similarTags = stack.filter {
            stackEntry -> stackEntry.fragmentTag.startsWith(newFragmentTag)
        }

        if (similarTags.isNotEmpty()){
            newFragmentTag = nextFragment.javaClass.name.plus(similarTags.size)
        }

        if (newTag.isNotEmpty()) {
            newFragmentTag = newTag
        }

        val currentFragmentTag = supportFragmentManager.fragments.filter { it.isVisible }.getOrNull(0)?.tag
        val transactionTag = "to:$newFragmentTag"

        currentFragmentTag?.let {
            stackMap[it] = transactionTag
        }

        val stackEntry = StackEntry(newFragmentTag, transactionTag, currentFragmentTag, skipBackStack)

        stack.add(stackEntry)

        return stackEntry

    }

    fun previous(): String? {
        if (stack.lastIndex > -1) {
            stack.removeAt(stack.lastIndex)

            while (stack.lastOrNull()?.skipBack == true) {
                stack.removeAt(stack.lastIndex)
            }

            if (stack.count() >= 1) {
                val lastIndex = stack.count() - 1
                val top = stack[lastIndex]
                return stackMap[top.fragmentTag]
            }
        }
        return null
    }

    fun isEmpty() = supportFragmentManager.backStackEntryCount == 0

    fun size() = stack.size

    fun getSkipEntries() = stack.filter { it.skipBack }

    data class StackEntry(val fragmentTag: String, val transactionTag: String? = null, val mapEntryKey: String? = null, val skipBack: Boolean = false)
}