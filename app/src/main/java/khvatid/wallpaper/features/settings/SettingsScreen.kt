package khvatid.wallpaper.features.settings

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import khvatid.androidAi.features.settings.SettingsScreenContract
import khvatid.androidAi.features.settings.SettingsScreenContract.Events
import khvatid.wallpaper.R
import khvatid.wallpaper.ui.components.ListSwitchButton

@Composable
fun SettingsScreen(viewModel: SettingsScreenViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    SettingsScreenUi(state = state, events = viewModel::obtainEvent)
}

@Composable
private fun SettingsScreenUi(
    state: SettingsScreenContract.State,
    events: (Events) -> Unit
) {
    Column {
        ListSwitchButton(
            label = stringResource(id = R.string.system_theme),
            isActive = state.isSystemTheme,
            onClick = { events(Events.UseSystemTheme) }
        )
        AnimatedVisibility(visible = !state.isSystemTheme) {
            ListSwitchButton(
                label = stringResource(id = R.string.dark_theme),
                isActive = state.isDarkTheme,
                onClick = { events(Events.UseDarkTheme) }
            )
        }
        AnimatedVisibility(visible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ListSwitchButton(
                label = stringResource(id = R.string.dynamic_theme),
                isActive = state.isDynamicTheme,
                onClick = { events(Events.UseDynamicTheme) }
            )
        }
    }
}
