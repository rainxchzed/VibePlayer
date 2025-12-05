package zed.rainxch.vibeplayer.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import zed.rainxch.vibeplayer.AppViewModel
import zed.rainxch.vibeplayer.feature.main.presentation.MainViewModel

val sharedModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::MainViewModel)
}