package zed.rainxch.vibeplayer.core.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun ImagePicker(
    show: Boolean,
    onImageSelected: (imagePath: String?) -> Unit
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            onImageSelected(uri?.toString())
        }
    )

    LaunchedEffect(show) {
        if (show) {
            launcher.launch("image/*")
        }
    }
}