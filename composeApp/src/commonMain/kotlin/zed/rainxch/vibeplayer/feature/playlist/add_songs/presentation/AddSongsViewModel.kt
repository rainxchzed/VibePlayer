package zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddSongsViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddSongsState())
    val state = _state.asStateFlow()

    fun onAction(action: AddSongsAction) {
        if (action is AddSongsAction.OnSongSelected) {
            val musicId = action.musicId
            val isSelected = action.isSelected

            _state.update { currentState ->
                val newSelectedIds = currentState.selectedMusicIds.toMutableSet()

                if (isSelected)
                    newSelectedIds.add(musicId)
                else
                    newSelectedIds.remove(musicId)

                currentState.copy(selectedMusicIds = newSelectedIds)
            }

        } else if (action is AddSongsAction.OnSelectedAllSongs){
            val isSelected = action.isSelected
            val musicsList = action.musics

            _state.update { currentState ->
                val allIds = if (isSelected) {
                    musicsList.map { it.id }.toSet()
                } else
                    emptySet()

                currentState.copy(selectedMusicIds = allIds)
            }
        }
    }
}