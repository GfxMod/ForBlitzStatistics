package ru.forblitz.statistics.helpers

import android.content.Intent

class ActivityResultActionManager {

    var action: ((result: Intent) -> Unit)? = null

    fun invoke(result: Intent?) {
        if (result != null) {
            action?.invoke(result)
        }
    }

}