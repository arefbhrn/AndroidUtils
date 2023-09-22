package com.arefdev.base.repo.db.netcache

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Updated on 25/05/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
@Entity
@Keep
data class RoomNetCache(
    @PrimaryKey
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0,
    @ColumnInfo(name = "statusCode")
    var statusCode: Int = 0,
    @ColumnInfo(name = "response")
    var response: String? = null
)
