package zed.rainxch.vibeplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zed.rainxch.vibeplayer.core.presentation.utils.Permission
import zed.rainxch.vibeplayer.core.presentation.utils.PermissionChecker

class AppViewModel(
    private val permissionChecker: PermissionChecker
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(AppState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                viewModelScope.launch {
                    val isGranted = permissionChecker.isPermissionGranted(Permission.Audio)
                    _state.update {
                        it.copy(
                            isAudioPermissionGranted = isGranted,
                            isLoading = false
                        )
                    }
                }

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppState()
        )
}