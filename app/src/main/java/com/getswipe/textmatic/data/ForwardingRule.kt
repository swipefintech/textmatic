package com.getswipe.textmatic.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forwarding_rules")
data class ForwardingRule(

    @PrimaryKey val uid: Int,

    @ColumnInfo(name = "match_pattern") val matchPattern: String?,

    @ColumnInfo(name = "webhook_url") val webhookUrl: String?
)
