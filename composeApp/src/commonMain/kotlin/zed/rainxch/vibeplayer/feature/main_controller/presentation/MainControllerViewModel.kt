package zed.rainxch.vibeplayer.feature.main_controller.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainControllerViewModel : ViewModel() {
    private val _state = MutableStateFlow(MainControllerState())
    val state = _state.asStateFlow()

    fun onAction(action: MainControllerAction) {
        when (action) {
            is MainControllerAction.OnSwitchTab -> {
                _state.update { it.copy(
                    selectedTab = action.tab
                ) }
            }
        }
    }

}