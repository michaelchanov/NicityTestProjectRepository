package com.nicity.nicitytestproject.utils.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nicity.nicitytestproject.R

fun FragmentManager.navigateToFragment(
    fragment: Fragment, addToBackStack: Boolean = false,
    primaryContainer: Boolean = true
) {
    val fragmentTransaction: FragmentTransaction?

    if (primaryContainer) {
        fragmentTransaction = this.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
    } else {
        fragmentTransaction = this.beginTransaction()
            .replace(R.id.navigationFragmentContainer, fragment)
    }

    if (addToBackStack) {
        fragmentTransaction.addToBackStack(null).commit()
    } else {
        fragmentTransaction.commit()
    }
}