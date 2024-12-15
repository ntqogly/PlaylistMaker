package com.example.playlistmaker.domain.threads

interface ThreadExecutor {
    fun executeInBackground(task: () -> Unit)
    fun postToMainThread(task: () -> Unit)
}