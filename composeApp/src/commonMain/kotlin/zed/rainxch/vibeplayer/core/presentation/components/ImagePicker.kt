package zed.rainxch.vibeplayer.core.presentation.components

import androidx.compose.runtime.Composable

@Composable
expect fun ImagePicker(
    show: Boolean,
    onImageSelected: (imagePath: String?) -> Unit
)