package com.example.letstravel.api

import com.example.letstravel.api.geo_model.ReverseGeoResponse
import com.example.letstravel.util.CLog
import com.example.letstravel.util.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Protocol
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException

class RetrofitApiManager {
    fun Build(): Retrofit {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .client(getUnsafeOkHttpClient().build())
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // 안전하지 않음으로 HTTPS를 통과
    fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        return try {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            //                HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); //OkHttp 사용해서 로그 보기
            val logging = RetrofitLogInterceptor { message ->
                if (CLog.isRetrofitLog) {
                    try {
                        // JSON Format이 아닌경우 Exception
                        JSONObject(message)
                        CLog.d(
                            """
                                 RESPONSE JSON ->
                                 ${CLog.getPretty(message)}
                                 """.trimIndent()
                        )
                    } catch (e: JSONException) {
                        CLog.d(message)
                    }
                }
            }
            logging.level = RetrofitLogInterceptor.Level.BODY
            builder.addInterceptor(logging)
            builder.sslSocketFactory(
                sslSocketFactory,
                (trustAllCerts[0] as X509TrustManager)
            )
            builder.hostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String?, session: SSLSession?): Boolean {
                    return true
                }
            })

            // 네트워크 연결 상태 TimeOut 추가 - 15초? 30초?
            builder.connectTimeout(RETROFIT_TIME_OUT.toLong(), TimeUnit.SECONDS)
            builder.readTimeout(RETROFIT_TIME_OUT.toLong(), TimeUnit.SECONDS)
            builder.protocols(Collections.singletonList(Protocol.HTTP_1_1))
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }


    fun requestReverseGeoAddress(
        latLng: String,
        language: String,
        key: String,
        retrofitInterface: RetrofitInterface
    ) {
        Build().create(ApiService::class.java).getReverseGeoAddress(latLng, language, key)
            .enqueue(object : Callback<ReverseGeoResponse?> {
                override fun onResponse(
                    call: Call<ReverseGeoResponse?>,
                    response: Response<ReverseGeoResponse?>
                ) {
                    retrofitInterface.onResponse(response)
                }

                override fun onFailure(call: Call<ReverseGeoResponse?>, t: Throwable) {
                    retrofitInterface.onFailure(t)
                }

            })
    }

    companion object {
        private var instance = RetrofitApiManager()
        private const val RETROFIT_TIME_OUT = 10

        fun getInstance(): RetrofitApiManager {
            if (instance == null) {
                instance = RetrofitApiManager()
            }
            return instance
        }
    }
}

