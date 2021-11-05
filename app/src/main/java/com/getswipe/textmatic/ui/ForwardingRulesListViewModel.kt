package com.getswipe.textmatic.ui

import androidx.lifecycle.ViewModel
import com.getswipe.textmatic.MainApplication
import com.getswipe.textmatic.data.ForwardingRuleDatabase

class ForwardingRulesListViewModel : ViewModel() {

    val forwardingRules = MainApplication.CONTAINER[ForwardingRuleDatabase::class.java]
        .forwardingRuleDao()
        .getAllAsLiveData()
}
