package com.getswipe.textmatic.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.getswipe.textmatic.R
import com.getswipe.textmatic.data.ForwardingRule

class ForwardingRuleViewModel(forwardingRule: ForwardingRule?) : ViewModel() {

    val name = MutableLiveData<String>(forwardingRule?.name)

    val direction = MutableLiveData(forwardingRule?.direction ?: DIRECTION_BOTH)

    var directionRadio: Int
        get() {
            return when (direction.value) {
                DIRECTION_INCOMING -> R.id.direction_incoming_radio
                DIRECTION_OUTGOING -> R.id.direction_outgoing_radio
                else -> R.id.direction_both_radio
            }
        }
        set(value) {
            direction.value = when (value) {
                R.id.direction_incoming_radio -> DIRECTION_INCOMING
                R.id.direction_outgoing_radio -> DIRECTION_OUTGOING
                else -> DIRECTION_BOTH
            }
        }

    val participantPattern = MutableLiveData<String>(forwardingRule?.participantPattern)

    val contentPattern = MutableLiveData<String>(forwardingRule?.contentPattern)

    val webhookUrl = MutableLiveData<String>(forwardingRule?.webhookUrl)

    companion object {

        const val DIRECTION_BOTH = "both"
        const val DIRECTION_INCOMING = "incoming"
        const val DIRECTION_OUTGOING = "outgoing"
    }
}
