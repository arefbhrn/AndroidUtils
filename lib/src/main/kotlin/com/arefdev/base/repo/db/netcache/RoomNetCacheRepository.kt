package com.arefdev.base.repo.db.netcache

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.arefdev.base.repo.db.BaseRepository

/**
 * Updated on 25/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class RoomNetCacheRepository(context: Context) : BaseRepository<RoomNetCache>() {

    @Dao
    interface DaoAccess {
        @Query("SELECT * FROM $tableName")
        fun getAll(): List<RoomNetCache>

        @Query("SELECT * FROM $tableName WHERE url = :url")
        operator fun get(url: String): RoomNetCache?

        @Query("SELECT COUNT(*) FROM $tableName")
        fun getCount(): Int

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertAll(vararg models: RoomNetCache): LongArray

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertAll(models: List<RoomNetCache>): LongArray

        @Update
        fun update(model: RoomNetCache)

        @Delete
        fun delete(model: RoomNetCache)

        @Delete
        fun deleteAll(vararg model: RoomNetCache)

        @Delete
        fun deleteAll(model: List<RoomNetCache>)

        @Query("DELETE FROM $tableName")
        fun deleteAll()

        companion object {
            const val tableName = "RoomNetCache"
        }
    }
    @Database(entities = [RoomNetCache::class], version = 1, exportSchema = false)
    abstract class RoomNetCacheDatabase : RoomDatabase() {
        abstract fun daoAccess(): DaoAccess
    }

    override val DB_NAME = RoomNetCache::class.java.simpleName.lowercase()
    private val mDatabase: RoomNetCacheDatabase

    init {
        val builder: RoomDatabase.Builder<RoomNetCacheDatabase> = Room.databaseBuilder(
            context, RoomNetCacheDatabase::class.java, DB_NAME
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
        mDatabase = builder.build()
    }

    override fun insert(model: RoomNetCache): Long {
        return mDatabase.daoAccess().insertAll(model)[0]
    }

    override fun insertAll(models: List<RoomNetCache>): LongArray {
        return mDatabase.daoAccess().insertAll(models)
    }

    override fun update(model: RoomNetCache) {
        mDatabase.daoAccess().update(model)
    }

    override fun delete(id: Int) {
        get(id)?.let { mDatabase.daoAccess().delete(it) }
    }

    override fun delete(model: RoomNetCache) {
        mDatabase.daoAccess().delete(model)
    }

    override fun deleteAll(vararg model: RoomNetCache) {
        mDatabase.daoAccess().deleteAll(*model)
    }

    override fun deleteAll(models: List<RoomNetCache>) {
        mDatabase.daoAccess().deleteAll(models)
    }

    override fun deleteAll() {
        mDatabase.daoAccess().deleteAll()
    }

    override fun get(url: Int): RoomNetCache? {
        return mDatabase.daoAccess()[url.toString()]
    }

    override fun get(url: String): RoomNetCache? {
        return mDatabase.daoAccess()[url]
    }

    override val all: List<RoomNetCache>
        get() = mDatabase.daoAccess().getAll()

    override val count: Int
        get() = mDatabase.daoAccess().getCount()

    override fun close() {
        mDatabase.close()
    }
}
