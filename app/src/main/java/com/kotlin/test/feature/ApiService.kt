package com.kotlin.test.feature

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

val serviceModule = module {
    single<ApiService> { ApiServiceImpl() }
}

data class Response(    
    val id: String,
    val name: String,
    val ticker: String,
    val instrument_type: String,
    val current_price: Double,
    val previous_price: Double,
    val description: String
    
)

interface ApiService{
    @GET("instruments.json")
    suspend fun getData(): List<Response>
}

class ApiServiceImpl() : ApiService {
    
    private val apiService : ApiService
    
    init {
        val client = OkHttpClient.Builder().build()
         apiService = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/dns-mcdaid/b248c852b743ad960616bac50409f0f0/raw/6921812bfb76c1bea7868385adf62b7f447048ce/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
        }

    override suspend fun getData(): List<Response> {
        return apiService.getData()
    }

}