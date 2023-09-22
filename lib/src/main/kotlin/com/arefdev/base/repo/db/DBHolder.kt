package com.arefdev.base.repo.db

import com.arefdev.base.BaseApp
import com.arefdev.base.repo.db.netcache.RoomNetCacheDBHolder
import io.reactivex.rxjava3.core.Completable

/**
 * Updated on 16/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object DBHolder {

    private val dbHolders: MutableList<BaseDBHolder<*, *>?> = mutableListOf()

    val netCaches: RoomNetCacheDBHolder by lazy { RoomNetCacheDBHolder(BaseApp.context).also { dbHolders.add(it) } }

    fun openAll() {
        for (dbHolder in dbHolders) {
            dbHolder?.getCount()
        }
    }

    fun deleteAll(): Completable {
        val list = mutableListOf<Completable>()
        for (dbHolder in dbHolders) {
            dbHolder?.deleteAll()?.let { list.add(it) }
        }
        return Completable.concatArray(*list.toTypedArray())
    }

    fun closeAll() {
        for (dbHolder in dbHolders) {
            dbHolder?.close()
        }
    }
}
