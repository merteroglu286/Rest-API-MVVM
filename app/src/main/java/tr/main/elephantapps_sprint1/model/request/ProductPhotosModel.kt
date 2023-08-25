package tr.main.elephantapps_sprint1.model.request

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

data class ProductPhotosModel(
    private val productId : Int,
    private val product: File,
)