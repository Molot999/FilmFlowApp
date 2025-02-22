package com.blacksun.filmflow.ui

import android.content.Context
import kotlin.math.roundToInt

fun Int.dpToPx(context: Context): Int =
    (this * context.resources.displayMetrics.density).roundToInt()