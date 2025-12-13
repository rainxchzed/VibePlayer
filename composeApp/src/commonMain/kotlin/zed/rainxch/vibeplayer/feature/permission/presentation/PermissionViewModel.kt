package zed.rainxch.vibeplayer.feature.permission.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PermissionViewModel : ViewModel() {

    private val _state = MutableStateFlow(PermissionState())
    val state = _state.asStateFlow()

    fun onAction(action: PermissionAction) {
        when (action) {
            PermissionAction.OnDialogPermissionDoneClick -> {
                _state.update { it.copy(
                    isPermissionDeniedDialogVisible = false
                ) }
            }

            PermissionAction.OnPermissionDenied -> {
                _state.update { it.copy(
                    didDenyPermission = true,
                    isPermissionDeniedDialogVisible = true
                ) }
            }

            PermissionAction.OnDialogPermissionTryAgainClick -> {
                /* Handled in composable */
            }
            PermissionAction.OnPermissionGranted -> {
                /* Handled in composable */
            }

            PermissionAction.OnAllowAccessClick -> {
                /* Handled in composable */
            }
        }
    }

}