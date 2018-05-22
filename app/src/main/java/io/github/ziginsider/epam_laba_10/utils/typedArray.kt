package io.github.ziginsider.epam_laba_10.utils

import android.content.res.TypedArray

/**
 * Extension for TypedArray. Executes the given [block] function on this TypedArray and
 * then recycles it.
 *
 * @see kotlin.io.use
 */
inline fun <R> TypedArray.use(block: (TypedArray) -> R): R {
    return block(this).also {
        recycle()
    }
}
