package com.arefdev.base.repo.db

/**
 * Updated on 25/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
abstract class BaseRepository<M> {

    protected val mustEncrypt = false

    abstract val DB_NAME: String

    abstract fun insert(model: M): Long

    abstract fun insertAll(models: List<M>): LongArray

    abstract operator fun get(id: Int): M?

    abstract operator fun get(id: String): M?

    abstract val all: List<M>

    abstract val count: Int

    abstract fun update(model: M)

    abstract fun delete(id: Int)

    abstract fun delete(model: M)

    abstract fun deleteAll(vararg model: M)

    abstract fun deleteAll(models: List<M>)

    abstract fun deleteAll()

    abstract fun close()
}
