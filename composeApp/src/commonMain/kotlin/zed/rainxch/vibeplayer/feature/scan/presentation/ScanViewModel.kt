package zed.rainxch.vibeplayer.feature.scan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _event = Channel<ScanEvent>()
    val event = _event.receiveAsFlow()

    private val _state = MutableStateFlow(ScanState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ScanState()
        )

    fun onAction(action: ScanAction) {
        when (action) {
            is ScanAction.ChangeIgnoreDuration -> {
                _state.update { it.copy(ignoreDuration = action.ignoreDuration) }
            }

            is ScanAction.ChangeIgnoreSize -> {
                _state.update { it.copy(ignoreSize = action.ignoreSize) }
            }

            ScanAction.StartScan -> {
                startScan()
            }
        }
    }

    private fun startScan() {
        _state.update { it.copy(isScanning = true) }
        viewModelScope.launch {
            delay(1000)
            // TODO: scan here
            _event.send(ScanEvent.SnackbarMessage("Scan complete — 128 songs found."))
            _state.update { it.copy(isScanning = false) }
        }
    }

}