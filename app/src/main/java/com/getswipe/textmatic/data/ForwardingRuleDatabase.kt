package com.getswipe.textmatic.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ForwardingRule::class], version = 1)
abstract class ForwardingRuleDatabase : RoomDatabase() {

    abstract fun forwardingRuleDao(): ForwardingRuleDao
}
