package com.luojianping.xloghelper

import android.app.Application
import com.luojianping.xlog.LogLevel
import com.luojianping.xlog.LogModel
import com.luojianping.xlog.XLogHelper

class MyApp :Application(){

    override fun onCreate() {
        super.onCreate()
        XLogHelper.create(this)
            .setModel(LogModel.Async)
            .setTag("TAG")
            .setDebug(true)
            .setConsoleLogOpen(true)
            .setLogLevel(LogLevel.LEVEL_INFO)
            .setNamePreFix("log")
            .setPubKey("572d1e2710ae5fbca54c76a382fdd44050b3a675cb2bf39feebe85ef63d947aff0fa4943f1112e8b6af34bebebbaefa1a0aae055d9259b89a1858f7cc9af9df1")
            .setMaxFileSize(1f)
            .setOneFileEveryday(true)
            .setCacheDays(0)
            .setMaxAliveTime(2)
            .init()
    }
}