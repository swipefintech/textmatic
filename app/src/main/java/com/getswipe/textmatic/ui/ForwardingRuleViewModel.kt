package com.getswipe.textmatic.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ForwardingRuleViewModel : ViewModel() {

    val matchPattern: LiveData<String> = MutableLiveData()

    val webhookUrl: LiveData<String> = MutableLiveData()
}
