package com.dipanshu.videoplayerdemo

import android.app.Application
import com.dipanshu.videoplayerdemo.utils.Prefs

class VideoPlayerDemoApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Prefs.init(this)
    }
}