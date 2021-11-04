package com.getswipe.textmatic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.getswipe.textmatic.data.ForwardingRule

class ForwardingRuleViewModelFactory(
    private var forwardingRule: ForwardingRule?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ForwardingRule::class.java)
            .newInstance(forwardingRule)
    }
}
