package khvatid.androidAi.features.settings

import android.os.Build
import androidx.annotation.RequiresApi

interface SettingsScreenContract{

    data class State(
        val isDynamicTheme: Boolean = false,
        val isDarkTheme: Boolean = false,
        val isSystemTheme: Boolean = false
    )

    sealed class Events{
        @RequiresApi(Build.VERSION_CODES.S)
        object UseDynamicTheme: Events()
        object UseDarkTheme: Events()
        object UseSystemTheme: Events()

    }
}