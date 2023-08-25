package tr.main.elephantapps_sprint1.util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.model.request.Data
import tr.main.elephantapps_sprint1.service.ApiService
import java.io.ByteArrayOutputStream

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


        fun prepareFilePart(partName: String, fileUri: Uri, contentResolver: ContentResolver): MultipartBody.Part {
            val fileName = getFileNameFromUri(fileUri, contentResolver)
            val mimeType = getMimeType(contentResolver, fileUri)
            val fileByteArray = readFileToByteArray(fileUri, contentResolver)

            val mediaType = mimeType?.toMediaTypeOrNull()

            val requestBody = fileByteArray.toRequestBody(mediaType)
            return MultipartBody.Part.createFormData(partName, fileName, requestBody)
        }

        private fun getFileNameFromUri(uri: Uri, contentResolver: ContentResolver): String? {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst()) {
                    return cursor.getString(nameIndex)
                }
            }
            return null
        }

        private fun getMimeType(contentResolver: ContentResolver, uri: Uri): String? {
            val mime = MimeTypeMap.getSingleton()
            return mime.getExtensionFromMimeType(contentResolver.getType(uri))
        }

        private fun readFileToByteArray(uri: Uri, contentResolver: ContentResolver): ByteArray {
            val outputStream = ByteArrayOutputStream()
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
            }
            return outputStream.toByteArray()
        }

    }


}