package com.oakkub.pointpoker

/**
 * Created by oakkub on 7/28/2017 AD.
 */


inline fun <T, R> T.letIf(predicate: (T) -> Boolean, block: (T) -> R): R? =
        if (predicate(this))
            block(this)
        else
            null
