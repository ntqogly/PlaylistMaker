package com.example.playlistmaker.data.threads

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.threads.ThreadExecutor

class AndroidThreadExecutor : ThreadExecutor {
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun executeInBackground(task: () -> Unit) {
        Thread(task).start()
    }

    override fun postToMainThread(task: () -> Unit) {
        mainHandler.post(task)
    }
}