package com.arefdev.base.repo.network.retrofit

import android.annotation.SuppressLint
import android.content.Context
import com.arefdev.base.repo.SharedPrefs
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Updated on 28/26/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object RetrofitApiClient {

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FIELD)
    annotation class ExcludeFromGson

    /**
     * @return Newest set base url
     */
    /**
     * Sets base url for future [.getRetrofitClient] calls
     */
    var baseUrl = ""

    @JvmStatic
    var token = SharedPrefs.token
        set(token) {
            field = if (token.isNullOrBlank()) null else token
        }

    val isLoggedIn: Boolean
        get() = token.isNullOrBlank().not()

    @JvmStatic
    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().let { builder ->
            builder.addNetworkInterceptor(
                HttpLoggingInterceptor(PrettyLogger())
                    .apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
            )
                .connectTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(HeaderInterceptor())

            // Set maximum cache size to 10MB
            val cacheSize = (50 * 1024 * 1024).toLong()
            val myCache = Cache(cacheDir, cacheSize)
            builder.cache(myCache)

            builder.build().also {
                it.dispatcher.maxRequestsPerHost = 64
            }
        }
    }

    /**
     * Get Retrofit object. This is a singleton instance.
     * Instance has 30s ReadTimeout, 30s ConnectTimeout and uses [okhttp3.Interceptor] to set Authorization header,
     * Base url based on flavor, [GsonConverterFactory.create] as converter factory and
     * [RxJava3CallAdapterFactory.create] as call adapter factory
     *
     * @return Retrofit instance
     */
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl.let { if (it.endsWith("/")) it else "$it/" })
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    /**
     * Get a gson object. This is a singleton instance.
     * This instance has [FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES] as field naming policy and
     * excludes fields with [ExcludeFromGson] annotation
     *
     * @return Gson instance
     */
    val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setExclusionStrategies(object : ExclusionStrategy {
                override fun shouldSkipField(field: FieldAttributes): Boolean {
                    return field.getAnnotation(ExcludeFromGson::class.java) != null
                }

                override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                    return false
                }
            })
            .create()
    }

    private lateinit var cacheDir: File

    @JvmStatic
    fun init(context: Context, baseUrl: String) {
        cacheDir = File(context.applicationContext.externalCacheDir, "http")
        RetrofitApiClient.baseUrl = baseUrl
    }

    @get:SuppressLint("TrustAllX509TrustManager")
    private val unsafeClientBuilder: OkHttpClient.Builder
        get() = try {
            // Create a trust manager that does not validate certificate chains
            @SuppressLint("CustomX509TrustManager")
            val trustManager: X509TrustManager = object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
            val trustAllCerts = arrayOf<TrustManager>(trustManager)

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustManager)
            builder.hostnameVerifier { _: String?, _: SSLSession? -> true }

            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}