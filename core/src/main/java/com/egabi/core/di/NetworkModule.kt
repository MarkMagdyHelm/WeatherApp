package com.egabi.core.di

import android.content.Context
import com.egabi.core.BuildConfig
import com.egabi.core.constants.Constants
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.*
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException


@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.API_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(okHttpClient)

            .build()
    }


    @Provides
    @Singleton
    fun providesOkHttpClient(cache: Cache, context: Context): OkHttpClient {
        val obj = UploadInterceptor()
        val client = OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(70, TimeUnit.MINUTES)
            .writeTimeout(70, TimeUnit.MINUTES)
            .readTimeout(70, TimeUnit.MINUTES)
//            .sslSocketFactory(getSSLConfig(context)?.socketFactory, gettestmanaber())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
//            .addNetworkInterceptor(obj)
        return client.build()
    }
    @Provides
    @Singleton
    fun providesOkhttpCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun providesRxJavaCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    private fun gettestmanaber(): X509TrustManager {
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + Arrays.toString(
                trustManagers
            )
        }
        val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(trustManager), null)
        // val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        return trustManager
    }

    @Throws(
        CertificateException::class,
        IOException::class,
        KeyStoreException::class,
        NoSuchAlgorithmException::class,
        KeyManagementException::class
    )
    private fun getSSLConfig(context: Context): SSLContext? {

        // Loading CAs from an InputStream
        var cf: CertificateFactory? = null
        cf = CertificateFactory.getInstance("X.509")
        var ca: Certificate? = null
        context.resources.openRawResource(com.egabi.core.R.raw.ca)
            .use { cert ->
                ca = cf.generateCertificate(cert)


            }

        // Creating a KeyStore containing our trusted CAs
        val keyStoreType: String = KeyStore.getDefaultType()
        val keyStore: KeyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
        val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        // Creating an SSLSocketFactory that uses our TrustManager
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)
        return sslContext
    }
}

interface OnAttachmentListener {
    fun onAttachmentUploadedSuccess()
    fun onAttachmentDownloadedSuccess()
    fun onAttachmentDownloadedError()
    fun onAttachmentDownloadedFinished()
    fun onAttachmentDownloadUpdate(percent: Int)
}

private class ProgressResponseBody(
    responseBody: ResponseBody
) :
    ResponseBody() {
    private val responseBody: ResponseBody = responseBody
    private var bufferedSource: BufferedSource? = null
    override fun contentType(): MediaType {
        return responseBody.contentType()!!
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer?, byteCount: Long): Long {
                val bytesRead: Long = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                val percent =
                    if (bytesRead == -1L) 100f else totalBytesRead.toFloat() / responseBody.contentLength() as Float * 100
                EventBus.getDefault().post(UploadEvent.instance(percent.toInt()))
                return bytesRead
            }
        }
    }

}