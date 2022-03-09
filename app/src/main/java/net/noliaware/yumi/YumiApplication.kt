package net.noliaware.yumi

import android.app.Application
import net.noliaware.yumi.model.DataManager

class YumiApplication : Application() {

    private lateinit var dataManager: DataManager

    override fun onCreate() {
        super.onCreate()
        DataManager.initialize(this)
        dataManager = DataManager.get()
    }
}