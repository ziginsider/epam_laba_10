package io.github.ziginsider.epam_laba_10.utils

import android.content.res.TypedArray

inline fun <R> TypedArray.use(block: (TypedArray) -> R): R {
    return block(this).also {
        recycle()
    }
}