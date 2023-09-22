package com.arefdev.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.arefdev.base.repo.Constants
import com.arefdev.base.receivers.NetworkChangeReceiver
import com.arefdev.base.repo.db.DBHolder
import com.arefdev.base.repo.network.retrofit.RetrofitApiClient
import com.arefdev.base.utils.TimberLogTree
import com.tonyodev.fetch2.AbstractFetchListener
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2.FetchListener
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import timber.log.Timber
import java.io.File
import java.util.Locale

/**
 * Updated on 13/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
open class BaseApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var context: Context
            private set

        @JvmStatic
        val networkAvailability: LiveData<Boolean>
            get() = NetworkChangeReceiver.isOnline

        lateinit var fetch: Fetch
            private set

        val cachedDownloadsDir: File by lazy { File(context.externalCacheDir!!.path + "/downloads") }

        val doneCachedDownloadsDir: File by lazy { File(cachedDownloadsDir.path + "/done") }
    }


    override fun onCreate() {
        super.onCreate()

        context = this

        Timber.plant(TimberLogTree(BuildConfig.DEBUG))

        RetrofitApiClient.init(context, Constants.API_BASE_URL)

        RxJavaPlugins.setErrorHandler { throwable: Throwable ->
            val e = throwable.cause
            if (e !is InterruptedException) throw RuntimeException(e)
        }

        NetworkChangeReceiver.init(context)

        setLocale()

        initFetch()
    }

    private fun setLocale() {
        val locale = Locale("en")
        Locale.setDefault(locale)
        val config = resources.configuration
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    private fun initFetch() {
        val fetchConfiguration = FetchConfiguration.Builder(context)
            .preAllocateFileOnCreation(false)
            .createDownloadFileOnEnqueue(false)
            .setProgressReportingInterval(200)
            .setDownloadConcurrentLimit(Int.MAX_VALUE)
            .build()
        fetch = Fetch.getInstance(fetchConfiguration)
        val fetchListener: FetchListener = object : AbstractFetchListener() {
            override fun onCompleted(download: Download) {
                fetch.remove(download.id)
                val doneFile = File(doneCachedDownloadsDir.path + "/" + File(download.file).name)
                doneFile.parentFile?.mkdirs()
                File(download.file).renameTo(doneFile)
            }

            override fun onError(download: Download, error: Error, throwable: Throwable?) {
                fetch.delete(download.id)
                File(download.file).delete()
                Toast.makeText(context, download.error.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(download: Download) {
                fetch.delete(download.id)
                File(download.file).delete()
            }
        }
        fetch.addListener(fetchListener)
    }

    override fun onTerminate() {
        DBHolder.closeAll()

        super.onTerminate()
    }
}
