package tr.main.elephantapps_sprint1.util

import android.app.Dialog
import android.content.Context
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.model.response.ResponseModel
import tr.main.elephantapps_sprint1.service.ApiService

class Utils {

    companion object{


        fun createRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Constans.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun createApiService() : ApiService{
            return  createRetrofit().create(ApiService::class.java)
        }

    }


}