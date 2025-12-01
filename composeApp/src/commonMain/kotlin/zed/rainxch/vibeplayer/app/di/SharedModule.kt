package zed.rainxch.vibeplayer.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import zed.rainxch.vibeplayer.AppViewModel

val sharedModule = module {
    viewModelOf(::AppViewModel)
}