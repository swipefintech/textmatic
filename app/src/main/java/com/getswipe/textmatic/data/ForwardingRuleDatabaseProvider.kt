package com.getswipe.textmatic.data

import android.content.Context
import androidx.room.Room
import com.vaibhavpandey.katora.contracts.MutableContainer
import com.vaibhavpandey.katora.contracts.Provider

class ForwardingRuleDatabaseProvider : Provider {

    override fun provide(container: MutableContainer) {
        container.factory(ForwardingRuleDatabase::class.java) {
            Room.databaseBuilder(
                it.get(Context::class.java),
                ForwardingRuleDatabase::class.java, "forwarding-rules"
            ).build()
        }
    }
}
