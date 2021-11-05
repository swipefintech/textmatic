package com.getswipe.textmatic.spy

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.getswipe.textmatic.data.ForwardingRule
import com.getswipe.textmatic.ui.ForwardingRuleViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.lang.Exception
import java.util.regex.Pattern

class TextMessageContentObserver(
    private val appContext: Context,
    private val forwardingRules: Array<ForwardingRule>,
    handler: Handler
) : ContentObserver(handler) {

    private var lastSmsTimestamp: Long
        get() = sharedPreferences.getLong("lastSmsTimestamp", 0)
        set(value) {
            sharedPreferences.edit()
                .putLong("lastSmsTimestamp", value)
                .apply()
        }

    private val sharedPreferences =
        appContext.getSharedPreferences("TextMessageContentObserver", Context.MODE_PRIVATE)

    override fun onChange(selfChange: Boolean) {
        Timber.v("Looks like something changed in %s since %d", URI, lastSmsTimestamp)
        try {
            appContext.contentResolver.query(
                URI, PROJECTION, "date > ?", arrayOf("$lastSmsTimestamp"),
                "date DESC"
            ).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    lastSmsTimestamp =
                        cursor.getLong(cursor.getColumnIndexOrThrow("date"))
                    do processRow(cursor) while (cursor.moveToNext())
                } else {
                    Timber.w("No new data found in %s.", URI)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Could not observe change in %s.", URI)
        }
    }

    private fun matchesRule(forwardingRule: ForwardingRule, textMessage: TextMessage) : Boolean {
        if (forwardingRule.direction != ForwardingRuleViewModel.DIRECTION_BOTH
            && forwardingRule.direction != textMessage.direction) {
            return false
        }

        if (!forwardingRule.participantPattern.isNullOrEmpty()
            && !Pattern.matches(forwardingRule.participantPattern!!, textMessage.participant)) {
            return false
        }

        if (!forwardingRule.contentPattern.isNullOrEmpty()
            && !Pattern.matches(forwardingRule.contentPattern!!, textMessage.content)) {
            return false
        }

        return true
    }

    private fun notifyWebhook(forwardingRule: ForwardingRule, textMessage: TextMessage) {
        val textMessageNotifyRequest: WorkRequest =
            OneTimeWorkRequestBuilder<TextMessageNotifyWorker>()
                .setInputData(workDataOf(
                    TextMessageNotifyWorker.DATA_URL to forwardingRule.webhookUrl,
                    TextMessageNotifyWorker.DATA_TEXT_MESSAGE to Json.encodeToString(textMessage),
                ))
                .build()
        WorkManager
            .getInstance(appContext)
            .enqueue(textMessageNotifyRequest)
    }

    private fun processRow(cursor: Cursor) {
        val direction = if (cursor.getInt(0) == 1)
            ForwardingRuleViewModel.DIRECTION_INCOMING
        else
            ForwardingRuleViewModel.DIRECTION_OUTGOING
        val participant = cursor.getString(1)
        Timber.v("Processing %s message from/to %s.", direction, participant)
        val content = cursor.getString(2)
        val date = cursor.getLong(3)
        val textMessage = TextMessage(direction, participant, content, date)
        forwardingRules.onEach {
            val match = matchesRule(it, textMessage)
            Timber.v("If matches with %s, %b", it.name, match)
            if (match) notifyWebhook(it, textMessage)
        }
    }

    companion object {

        val PROJECTION = arrayOf("type", "address", "body", "date")
        val URI: Uri = Uri.parse("content://sms")
    }
}
