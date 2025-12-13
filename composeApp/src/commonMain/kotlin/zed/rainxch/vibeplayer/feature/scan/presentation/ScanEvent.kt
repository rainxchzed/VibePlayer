package zed.rainxch.vibeplayer.feature.scan.presentation

sealed interface ScanEvent {
    data class SnackbarMessage(val message: String) : ScanEvent
    data object NavigateBack : ScanEvent
}
