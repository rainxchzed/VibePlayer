package zed.rainxch.vibeplayer.app.di

import org.koin.core.module.Module
import org.koin.dsl.module
import zed.rainxch.vibeplayer.core.data.local.db.initDatabase

actual val platformModule: Module = module {
    single {
        initDatabase()
    }
}