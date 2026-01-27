package zed.rainxch.vibeplayer.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import java.awt.FileDialog
import java.io.File
import javax.swing.JFrame

@Composable
actual fun ImagePicker(
    show: Boolean,
    onImageSelected: (imagePath: String?) -> Unit
) {
    LaunchedEffect(show) {
        if (show) {
            val fileDialog = FileDialog(JFrame(), "Select an image", FileDialog.LOAD)
            fileDialog.isVisible = true
            val file = fileDialog.file
            val directory = fileDialog.directory
            if (file != null && directory != null) {
                onImageSelected(File(directory, file).toURI().toString())
            } else {
                onImageSelected(null) // User cancelled
            }
        }
    }
}