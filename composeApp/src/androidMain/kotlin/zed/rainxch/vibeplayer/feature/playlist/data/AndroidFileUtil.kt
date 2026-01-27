package zed.rainxch.vibeplayer.feature.playlist.data

import android.content.Context
import androidx.core.net.toUri
import java.io.File

class AndroidFileUtil (
    private val context: Context
) : FileUtil {
    override fun getAbsolutePathFromUri(uri: String): String {
        val fileName = "cover_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        context.contentResolver.openInputStream(uri.toUri())?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath

    }
}