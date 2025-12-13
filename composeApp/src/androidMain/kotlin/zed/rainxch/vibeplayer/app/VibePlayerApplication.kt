package zed.rainxch.vibeplayer.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import zed.rainxch.vibeplayer.app.di.initKoin

class VibePlayerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@VibePlayerApplication)
        }
    }
}