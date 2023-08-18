package tr.main.elephantapps_sprint1.util

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.model.request.Data
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

        fun getUserAccessToken(activity: Activity): String {
            val sharedPreferences = activity.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
            val json: String = sharedPreferences.getString("user", null) ?: return "" // Eğer json null ise boş bir string dön

            val gson = Gson()
            val userArrayListType = object : TypeToken<ArrayList<Data>>() {}.type
            val userArrayList: ArrayList<Data> = gson.fromJson(json, userArrayListType)

            if (userArrayList.isNotEmpty()) {
                return userArrayList[0].accessToken
            }

            return "" // Eğer userArrayList boşsa yine boş bir string dön
        }


    }


}