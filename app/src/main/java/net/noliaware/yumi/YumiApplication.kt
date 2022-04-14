package net.noliaware.yumi

import android.app.Application
import net.noliaware.yumi.commun.di.appModule
import net.noliaware.yumi.commun.di.loginFragmentVMModule
import net.noliaware.yumi.commun.di.voucherListFragmentVMModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class YumiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@YumiApplication)
            modules(
                listOf(
                    appModule,
                    loginFragmentVMModule,
                    voucherListFragmentVMModule
                )
            )
        }
    }
}