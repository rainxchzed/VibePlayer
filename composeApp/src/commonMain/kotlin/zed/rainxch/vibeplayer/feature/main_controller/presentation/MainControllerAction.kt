package zed.rainxch.vibeplayer.feature.main_controller.presentation

import zed.rainxch.vibeplayer.feature.main_controller.presentation.model.MainControllerTabs

sealed interface MainControllerAction {
    data class OnSwitchTab(val tab: MainControllerTabs) : MainControllerAction
}