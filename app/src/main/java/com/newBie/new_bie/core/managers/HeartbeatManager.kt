package com.newBie.new_bie.core.managers

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object HeartbeatManager {
    private var job: Job? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun start(roomId: String) {
        if (job != null) return // already running

        job = scope.launch {
            while (isActive) {
                sendHeartbeat(roomId)
                delay(30_000) // 30초
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }

    private suspend fun sendHeartbeat(roomId: String) {
        Log.d("Heartbeat", "heartBeat 작동함")

        try {

        } catch (e: Exception) {
            Log.e("Heartbeat", "에러 발생", e)
        }
    }
}