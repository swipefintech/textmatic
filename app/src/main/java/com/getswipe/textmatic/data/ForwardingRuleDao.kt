package com.getswipe.textmatic.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ForwardingRuleDao {

    @Query("SELECT * FROM forwarding_rules")
    fun getAll(): List<ForwardingRule>

    @Insert
    fun insertAll(vararg forwardingRules: ForwardingRule)

    @Delete
    fun delete(user: ForwardingRule)
}
