package zed.rainxch.vibeplayer.feature.main_controller.presentation

import zed.rainxch.vibeplayer.feature.main_controller.presentation.model.MainControllerTabs

data class MainControllerState(
    val selectedTab: MainControllerTabs = MainControllerTabs.Songs
)