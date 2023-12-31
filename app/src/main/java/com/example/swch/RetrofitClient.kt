import com.google.api.Http
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL:String = "https://us-central1-coupowning.cloudfunctions.net/widgets/"
    private val retrofit: Retrofit.Builder by lazy{
        val interceptor = HttpLoggingInterceptor()
        val clientBuilder: okhttp3.OkHttpClient.Builder = okhttp3.OkHttpClient.Builder().addInterceptor(interceptor)
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
        var gson: Gson = GsonBuilder().setLenient().create()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(clientBuilder.build())
    }
    /*
    val apiService: Network by lazy{ // retrofit 객체 생성
        retrofit.build().create(Network::class.java)
    }

     */
}