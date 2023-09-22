package com.arefdev.base.repo.network.retrofit

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.arefdev.base.BaseApp
import com.arefdev.base.BuildConfig
import com.arefdev.base.extensions.observeOnComputationThread
import com.arefdev.base.extensions.subscribeOnIOThread
import com.arefdev.base.extensions.subscribeVia
import com.arefdev.base.repo.network.retrofit.ApiExceptionManager.isPersian
import com.arefdev.base.utils.NetworkUtils.isOnline
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.util.Objects
import java.util.concurrent.TimeUnit

/**
 * Updated on 14/08/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
open class RetrofitRequest<R> @JvmOverloads constructor(
    cacheEnabled: Boolean = false,
    cacheAge: Int = DEFAULT_CACHE_ONLINE_MAX_AGE
) : Single<RetrofitResponse<R>>() {

    companion object {
        const val CACHE_CONTROL = "Cache-Control"
        const val CACHE_CONTROL_NO_CACHE = "Cache-Control: no-cache"
        private const val DEFAULT_CACHE_ONLINE_MAX_AGE = 10 // Set maximum online cache age to 10 seconds
        private const val DEFAULT_CACHE_OFFLINE_MAX_AGE = 7 // Set maximum offline cache age to 7 days
        private val DEFAULT_CACHE_AGE_TIME_UNIT = TimeUnit.SECONDS
        private val IS_DEBUG = BuildConfig.DEBUG
    }


    @Suppress("UNREACHABLE_CODE")
    private fun onError(e: Throwable, statusCode: Int) {
        return
        Timber.e(e, "${e.message} - status code: $statusCode")
        val context = BaseApp.context
        val defaultMsg = context.getString(com.arefdev.base.R.string.error_happened)
        var msg: String? = defaultMsg
        try {
            val jsonError = JsonParser.parseString(Objects.requireNonNull(e.message)).asJsonObject
            if (jsonError.has("code") && jsonError.has("detail")) {
                val code = jsonError["code"].asString
                val detail = jsonError["detail"].asString
                msg = ApiExceptionManager.getPersianMessage(context, code)
                if (msg == null) {
                    msg = if (IS_DEBUG)
                        detail
                    else if (isPersian(detail))
                        detail
                    else
                        defaultMsg
                }
            }
            if (IS_DEBUG)
                msg = jsonError.toString()
//            GeneralUtils.showToast(context, msg!!, Toast.LENGTH_SHORT)

        } catch (err: Exception) {
//            GeneralUtils.showToast(context, defaultMsg, Toast.LENGTH_SHORT)
        }
    }

    private var retrofitClient: Retrofit? = null
    private var owner: LifecycleOwner? = null
    private val ownerLifecycleEventObserver: LifecycleEventObserver
    private var api: Single<Response<R>>? = null
    private var disposableObserver: Disposable? = null
    private var isCacheEnabled: Boolean
    private var cacheOnlineMaxAge: Int
    private var cacheOfflineMaxAge = DEFAULT_CACHE_OFFLINE_MAX_AGE
    val headers: Map<String, String> = HashMap()
    private var observer: SingleObserver<in RetrofitResponse<R>>? = null
    var mResponse: RetrofitResponse<R>? = null

    init {
        ownerLifecycleEventObserver = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    source.lifecycle.removeObserver(this)
                    cancel()
                }
            }
        }
        isCacheEnabled = cacheEnabled
        cacheOnlineMaxAge = cacheAge
        subscribeOnIOThread()
        observeOnComputationThread()
    }

    protected fun getRetrofitClient(): Retrofit {
        return retrofitClient ?: createNewRetrofitClient()
    }

    private fun createNewRetrofitClient(): Retrofit {
        return RetrofitApiClient.retrofit.newBuilder()
            .client(
                RetrofitApiClient.okHttpClient.newBuilder()
                    .addInterceptor(if (isCacheEnabled) networkCacheInterceptor else baseNetworkInterceptor)
                    .addNetworkInterceptor(if (isCacheEnabled) networkCacheInterceptor else baseNetworkInterceptor)
                    .build()
            )
            .build()
            .also { retrofitClient = it }
    }

    /**
     * @param cls Service interface
     */
    fun <T> getApiService(cls: Class<T>): T {
        return getRetrofitClient().create(cls)
    }

    val baseApiService: BaseApiService
        get() = getApiService(BaseApiService::class.java)

    fun setApi(api: Single<Response<R>>): RetrofitRequest<R> {
        this.api = api
        return this
    }

    fun setCacheEnabled(enabled: Boolean): RetrofitRequest<R> {
        isCacheEnabled = enabled
        createNewRetrofitClient()
        return this
    }

    fun setCacheOnlineMaxAge(seconds: Int): RetrofitRequest<R> {
        cacheOnlineMaxAge = seconds
        createNewRetrofitClient()
        return this
    }

    fun setCacheOfflineMaxAge(seconds: Int): RetrofitRequest<R> {
        cacheOfflineMaxAge = seconds
        createNewRetrofitClient()
        return this
    }

    fun addHeader(name: String, value: String): RetrofitRequest<R> {
        (headers as MutableMap)[name] = value
        return this
    }

    fun addHeaders(headers: Map<String, String>): RetrofitRequest<R> {
        val mHeaders = this.headers as MutableMap
        for (item in headers) {
            mHeaders[item.key] = item.value
        }
        return this
    }

    fun removeHeader(name: String): RetrofitRequest<R> {
        (headers as MutableMap).remove(name)
        return this
    }

    fun setToken(token: String): RetrofitRequest<R> {
        return setAuthorization("Token $token")
    }

    fun setAuthorization(token: String): RetrofitRequest<R> {
        addHeader("Authorization", token)
        return this
    }

    fun setOwner(owner: LifecycleOwner?): RetrofitRequest<R> {
        this.owner = owner
        return this
    }

    fun get(url: String): RetrofitRequest<R> {
        setApi(baseApiService.generalGet(url))
        return this
    }

    fun post(url: String, body: JsonElement? = null): RetrofitRequest<R> {
        setApi(baseApiService.generalPost(url, body))
        return this
    }

    private fun call(): RetrofitRequest<R> {
        if (api == null)
            return this

        disposableObserver = api
            ?.subscribeOnIOThread()
            ?.observeOnComputationThread()
            ?.map { response ->
                if (observer is Disposable && (observer as Disposable).isDisposed) {
                    cancel()
                    return@map
                }
                val errorBody: String? = response.errorBody()?.string()
                val statusCode = response.code()
                try {
                    val resp = response.body() as R
                    mResponse = RetrofitResponse(resp, statusCode)
                    if (response.code() in 200..299) {
                        observer?.onSuccess(mResponse!!)
                    } else {
                        if (errorBody == null) {
                            val e = Throwable("status code: $statusCode -> Response code not valid")
                            onError(e, statusCode)
                            observer?.onError(e)
                        } else {
                            val e = Throwable("status code: $statusCode -> $errorBody")
                            onError(e, statusCode)
                            observer?.onError(e)
                        }
                    }
                } catch (e: Exception) {
                    onError(e, statusCode)
                    observer?.onError(Throwable("status code: $statusCode", e))
                }
                cancel()
            }
            ?.doOnError { e ->
                if (observer is Disposable && (observer as Disposable).isDisposed) {
                    cancel()
                    return@doOnError
                }
                val statusCode = 0
                onError(e, statusCode)
                observer?.onError(Throwable("status code: $statusCode", e))
                cancel()
            }
            ?.subscribeVia()

        owner?.lifecycle?.addObserver(ownerLifecycleEventObserver)

        return this
    }

    override fun subscribeActual(observer: SingleObserver<in RetrofitResponse<R>>) {
        this.observer = observer
        call()
    }

    fun cancel() {
        owner?.lifecycle?.removeObserver(ownerLifecycleEventObserver)
        if (disposableObserver?.isDisposed != true)
            disposableObserver?.dispose()
    }

    private val baseNetworkInterceptor: Interceptor
        get() = Interceptor { chain: Interceptor.Chain ->
            // Get the request from the chain.
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            for ((key, value) in headers)
                requestBuilder.addHeader(key, value)

            if (isCacheEnabled.not())
                requestBuilder.cacheControl(CacheControl.FORCE_NETWORK)

            // response
            chain.proceed(requestBuilder.build()).newBuilder().build()
        }

    private val networkCacheInterceptor: Interceptor
        get() = Interceptor { chain: Interceptor.Chain ->
            // Get the request from the chain.
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            for ((key, value) in headers)
                requestBuilder.addHeader(key, value)
            var request: Request = requestBuilder.build()

            /*
             *  We initialize the request and change its header depending on whether
             *  the device is connected to Internet or not.
             */
            if (!isCacheEnabled || request.cacheControl.noCache || request.cacheControl.noStore) {
                request = request.newBuilder()
                    .removeHeader("pragma")
                    .removeHeader("Etag")
                    .removeHeader("If-None-Match")
                    .build()

                val response = chain.proceed(request)
                response.newBuilder()
                    .removeHeader("pragma")
                    .removeHeader("Etag")
                    .removeHeader(CACHE_CONTROL)
                    .build()

            } else {
                val cacheControl: CacheControl = if (isOnline(BaseApp.context)) {
                    /*
                 *  If there is Internet, get the cache that was stored {cacheOnlineMaxAge} seconds ago.
                 *  If the cache is older than {cacheOnlineMaxAge} seconds, then discard it,
                 *  and indicate an error in fetching the response.
                 *  The 'max-age' attribute is responsible for this behavior.
                 */
                    CacheControl.Builder()
                        .maxAge(cacheOnlineMaxAge, DEFAULT_CACHE_AGE_TIME_UNIT)
                        .maxStale(cacheOnlineMaxAge, DEFAULT_CACHE_AGE_TIME_UNIT)
                        .build()
                } else {
                    /*
                 *  If there is no Internet, get the cache that was stored within last {cacheOfflineMaxAge} seconds.
                 *  If the cache is older than {cacheOfflineMaxAge} seconds, then discard it,
                 *  and indicate an error in fetching the response.
                 *  The 'max-stale' attribute is responsible for this behavior.
                 *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
                 */
                    CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(cacheOfflineMaxAge, TimeUnit.DAYS)
                        .build()
                }

                // request
                request = request.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader(CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()

                // response
                val resp = chain.proceed(request)
                resp.newBuilder()
                    .removeHeader("pragma")
                    .removeHeader("Etag")
                    .removeHeader(CACHE_CONTROL)
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .build()
            }
        }
}