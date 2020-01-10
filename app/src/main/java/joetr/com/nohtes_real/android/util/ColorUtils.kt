package joetr.com.nohtes_real.android.util

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import androidx.annotation.AttrRes

import androidx.annotation.ColorInt
import androidx.annotation.NonNull


object ColorUtils {
    /**
     * Queries the theme of the given `context` for a theme color.
     *
     * @param context   the context holding the current theme.
     * @param attrResId the theme color attribute to resolve.
     * @return the theme color
     */
    @ColorInt
    fun getThemeColor(@NonNull context: Context, @AttrRes attrResId: Int): Int {
        val a: TypedArray = context.obtainStyledAttributes(intArrayOf(attrResId))
        return try {
            a.getColor(0, Color.MAGENTA)
        } finally {
            a.recycle()
        }
    }
}