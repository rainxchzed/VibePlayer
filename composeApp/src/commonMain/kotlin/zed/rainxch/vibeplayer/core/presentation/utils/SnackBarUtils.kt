package zed.rainxch.vibeplayer.core.presentation.utils

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun CoroutineScope.showSnackBar(snackBarHostState: SnackbarHostState, message: String) {
    this.launch {
        snackBarHostState.currentSnackbarData?.dismiss()
        snackBarHostState.showSnackbar(
            message = message,
        )
    }
}