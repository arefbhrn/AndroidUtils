package com.arefdev.base.repo.db.netcache

import android.content.Context
import com.arefdev.base.repo.db.BaseDBHolder

/**
 * Updated on 25/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class RoomNetCacheDBHolder(context: Context) : BaseDBHolder<RoomNetCache, RoomNetCacheRepository>(context) {
    override fun init(context: Context) {
        if (repository == null) {
            repository = RoomNetCacheRepository(context)
        }
    }
}
