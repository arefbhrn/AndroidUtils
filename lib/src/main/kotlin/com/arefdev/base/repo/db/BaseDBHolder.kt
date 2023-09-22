package com.arefdev.base.repo.db

import android.content.Context
import com.arefdev.base.extensions.subscribeOnIOThread
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Updated on 25/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
abstract class BaseDBHolder<M : Any, R : BaseRepository<M>>(context: Context) {

    @JvmField
    var repository: R? = null

    init {
        init(context)
    }

    abstract fun init(context: Context)

    private fun <T : Any> single(func: (it: R) -> T): Single<T> {
        return (repository?.let {
            Single.fromCallable { func(it) }
        } ?: Single.error(Throwable("Empty repository")))
            .subscribeOnIOThread()
    }

    private fun completable(func: (it: R) -> Unit): Completable {
        return (repository?.let {
            Completable.fromCallable { func(it) }
        } ?: Completable.error(Throwable("Empty repository")))
            .subscribeOnIOThread()
    }

    fun insert(model: M): Single<Long> {
        return single { it.insert(model) }
    }

    fun insertAll(models: List<M>): Single<LongArray> {
        return single { it.insertAll(models) }
    }

    operator fun get(id: Int): Single<M> {
        return single { it[id]!! }
    }

    operator fun get(id: String): Single<M> {
        return single { it[id]!! }
    }

    fun getAll(): Single<List<M>> {
        return single { it.all }
    }

    fun getCount(): Single<Int> {
        return single { it.count }
    }

    fun update(model: M): Completable {
        return completable { it.update(model) }
    }

    fun delete(id: Int): Completable {
        return completable { it.delete(id) }
    }

    fun delete(model: M): Completable {
        return completable { it.delete(model) }
    }

    fun deleteAll(vararg model: M): Completable {
        return completable { it.deleteAll(*model) }
    }

    fun deleteAll(models: List<M>): Completable {
        return completable { it.deleteAll(models) }
    }

    fun deleteAll(): Completable {
        return completable { it.deleteAll() }
    }

    fun close() {
        repository?.close()
        repository = null
    }
}
