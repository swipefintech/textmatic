package com.getswipe.textmatic.spy

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.OkHttpClient
import timber.log.Timber
import java.lang.Exception

class TextMessageNotifyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val urlInput =
            inputData.getString(DATA_URL) ?: return Result.failure()
        val textMessageInput: String =
            inputData.getString(DATA_TEXT_MESSAGE) ?: return Result.failure()
        Timber.v("Notifying new message to %s", urlInput)
        var code = -1
        try {
            code = postJson(urlInput, textMessageInput)
        } catch (e: Exception) {
            Timber.e(e, "Failed to notify %s", urlInput)
        }

        Timber.v("URL returned %d status", code)
        return if (code in 200..299) Result.success() else Result.failure()
    }

    private fun postJson(url: String, json: String): Int {
        val request: Request = Request.Builder()
            .url(url)
            .post(json.toRequestBody(MEDIATYPE_JSON))
            .build()
        OkHttpClient()
            .newCall(request)
            .execute().use { response -> return response.code }
    }

    companion object {

        const val DATA_TEXT_MESSAGE = "text_message"
        const val DATA_URL = "url"

        private val MEDIATYPE_JSON = "application/json; charset=utf-8".toMediaType()
    }
}
