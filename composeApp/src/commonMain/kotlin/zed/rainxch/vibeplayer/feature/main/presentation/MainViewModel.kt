package zed.rainxch.vibeplayer.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zed.rainxch.vibeplayer.core.data.local.db.AppDatabase
import zed.rainxch.vibeplayer.feature.main.domain.repository.MainRepository

class MainViewModel(
    private val mainRepository: MainRepository,
    private val appDatabase: AppDatabase
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MainState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadMusics()

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainState()
        )

    private fun loadMusics() {
        viewModelScope.launch {
            val musics = mainRepository.getMusicsWithMetadata()

            _state.update {
                it.copy(
                    scanResultState = ScanResultState.Ready,
                    musics = musics
                )
            }

            println(appDatabase.musicDao.getMusics())
        }
    }

    fun onAction(action: MainAction) {
        when (action) {
            MainAction.OnScanAgainClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            scanResultState = ScanResultState.Loading
                        )
                    }

                    val musics = mainRepository.getMusicsWithMetadata()

                    _state.update {
                        it.copy(
                            scanResultState = ScanResultState.Ready,
                            musics = musics
                        )
                    }
                }

            }

            is MainAction.OnMusicItemClick -> {
                /* Handled in composable */
            }
        }
    }

}