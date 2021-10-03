package yuri.dyachenko.popularlibs01.util

import android.text.Editable
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.google.android.material.textfield.TextInputLayout

fun ViewGroup.showOnly(views: Array<View>) {
    children.forEach {
        if (it is ViewGroup && it !is TextInputLayout) {
            it.showOnly(views)
        } else {
            it.visibility = if (views.contains(it)) View.VISIBLE else View.GONE
        }
    }
}

fun sometimes(): Boolean = (System.currentTimeMillis() / 1_000 % 2 == 0L)

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
