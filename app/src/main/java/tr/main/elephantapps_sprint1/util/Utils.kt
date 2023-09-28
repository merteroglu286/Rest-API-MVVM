package tr.main.elephantapps_sprint1.util

import android.R.id
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.enums.ToastType
import tr.main.elephantapps_sprint1.model.request.Data
import tr.main.elephantapps_sprint1.service.ApiService
import java.io.ByteArrayOutputStream
import java.time.Duration


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

        @SuppressLint("InflateParams")
        fun showToast(context:Context, message: String?,duration: Int,type:ToastType) {
            val view: View = LayoutInflater.from(context)
                .inflate(R.layout.custom_toast_layout,null)
            val toast = Toast(context)

            if (message != null){
                val tvMessage = view.findViewById<TextView>(R.id.tv_toast_message)
                tvMessage.text = message
            }
            val imageview = view.findViewById<ImageView>(R.id.iv_toast)

            when (type) {
                ToastType.Red -> {
                    imageview.setImageResource(R.drawable.baseline_cancel_24)
                    view.setBackgroundResource(R.drawable.red_border_for_toast)
                }
                ToastType.Green -> {
                    imageview.setImageResource(R.drawable.baseline_check_circle_24)
                    view.setBackgroundResource(R.drawable.green_border_for_toast)

                }
                else -> {
                    imageview.setImageResource(R.drawable.baseline_info_24)
                    view.setBackgroundResource(R.drawable.yellow_border_for_toast)

                }
            }

            toast.duration = duration
            toast.view = view
            toast.show()
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