package com.luojianping.xloghelper

import android.app.Application
import android.os.Environment
import com.luojianping.xlog.LogLevel
import com.luojianping.xlog.LogModel
import com.luojianping.xlog.XLogHelper
import java.io.File

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val path = Environment.getExternalStorageDirectory().absolutePath + "/XLogHelper/"

        // this is necessary, or may cash for SIGBUS
        val cachePath = this.filesDir.toString() + "/xlog"
        File(path).mkdirs()
        XLogHelper.create(this)
            .setModel(LogModel.Async)
            .setTag("TAG")
            .setDebug(true)
            .setConsoleLogOpen(true)
            .setLogLevel(LogLevel.LEVEL_INFO)
            .setNamePreFix("log")
            .setCachePath(cachePath)
            .setLogPath(path)
            .setPubKey("e0a5125824e9fb61dee137126b100562b7271c0e1fc43a4baea19cd55726c896d91f961b516cb5fba6dc05da5280abfb6389e18b7791a035167144473e319444")
            .setMaxFileSize(1f)
            .setOneFileEveryday(true)
            .setCacheDays(0)
            .setMaxAliveTime(2)
            .init()
    }
}