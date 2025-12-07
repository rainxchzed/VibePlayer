package zed.rainxch.vibeplayer.app.di

import org.koin.core.module.Module
import org.koin.dsl.module
import zed.rainxch.vibeplayer.core.data.local.db.AppDatabase
import zed.rainxch.vibeplayer.core.data.local.db.initDatabase
import zed.rainxch.vibeplayer.core.presentation.utils.PermissionChecker
import zed.rainxch.vibeplayer.core.presentation.utils.JvmPermissionChecker
import zed.rainxch.vibeplayer.core.data.data_source.MusicsDataStore
import zed.rainxch.vibeplayer.feature.main.data.data_store.JvmMusicsDataStore

actual val platformModule: Module = module {
    single<PermissionChecker> {
        JvmPermissionChecker()
    }

    single<AppDatabase> {
        initDatabase()
    }

    single<MusicsDataStore> {
        JvmMusicsDataStore()
    }
}