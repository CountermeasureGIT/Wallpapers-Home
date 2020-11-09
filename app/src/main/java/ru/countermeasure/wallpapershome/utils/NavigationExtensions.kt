package ru.countermeasure.wallpapershome.utils

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.countermeasure.wallpapershome.R

const val defaultContainerId = R.id.container

fun FragmentManager.backTo(fragment: Class<*>?) {
    if (fragment == null) {
        popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    } else {
        popBackStack(fragment.canonicalName, 0)
    }
}

fun FragmentManager.navigateTo(
    fragment: Class<out Fragment>,
    args: Bundle? = null,
    containerId: Int = defaultContainerId,
    setupFragmentTransaction: ((FragmentTransaction) -> Unit)? = null
) {
    val fragmentTransaction = beginTransaction()
    setupFragmentTransaction?.invoke(fragmentTransaction)
    fragmentTransaction
        .replace(containerId, fragment, args)
        .addToBackStack(fragment.canonicalName)
        .setReorderingAllowed(true)
        .commit()
}

fun FragmentManager.replace(
    fragment: Class<out Fragment>,
    args: Bundle? = null,
    containerId: Int = defaultContainerId,
    setupFragmentTransaction: ((FragmentTransaction) -> Unit)? = null
) {
    if (backStackEntryCount > 0) {
        popBackStack()
        val fragmentTransaction = beginTransaction()
        setupFragmentTransaction?.invoke(fragmentTransaction)
        fragmentTransaction
            .replace(containerId, fragment, args)
            .addToBackStack(fragment.canonicalName)
            .setReorderingAllowed(true)
            .commit()
    } else {
        val fragmentTransaction = beginTransaction()
        setupFragmentTransaction?.invoke(fragmentTransaction)
        fragmentTransaction
            .replace(containerId, fragment, args)
            .setReorderingAllowed(true)
            .commit()
    }
}

fun FragmentManager.newRootScreen(
    fragment: Class<out Fragment>,
    args: Bundle? = null,
    setupFragmentTransaction: ((FragmentTransaction) -> Unit)? = null,
    containerId: Int = defaultContainerId
) {
    backTo(null)
    replace(fragment, args, containerId, setupFragmentTransaction)
}

fun FragmentManager.newRootChain(
    vararg fragments: Pair<Class<out Fragment>, Bundle?>,
    containerId: Int = defaultContainerId,
    setupFragmentTransaction: ((FragmentTransaction) -> Unit)? = null
) {
    backTo(null)
    if (fragments.isNotEmpty()) {
        replace(
            fragments[0].first,
            fragments[0].second,
            containerId,
            setupFragmentTransaction
        )
        for (i in 1 until fragments.size) {
            navigateTo(
                fragments[i].first,
                fragments[i].second,
                containerId,
                setupFragmentTransaction
            )
        }
    }
}

fun Fragment.registerOnBackPressedCallback(
    onBackPressed: OnBackPressedCallback.() -> Unit
) {
    requireActivity().onBackPressedDispatcher.addCallback(this) {
        onBackPressed()
    }
}

fun FragmentTransaction.setSlideAnimation() {
    setCustomAnimations(
        R.anim.slide_in_right,
        R.anim.slide_out_left,
        R.anim.slide_in_left,
        R.anim.slide_out_right
    )
}
