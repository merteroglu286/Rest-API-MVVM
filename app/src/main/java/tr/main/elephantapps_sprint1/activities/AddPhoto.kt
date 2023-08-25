package tr.main.elephantapps_sprint1.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.ProductPhotosAdapter
import tr.main.elephantapps_sprint1.databinding.ActivityAddPhotoBinding
import tr.main.elephantapps_sprint1.databinding.DialogLayoutAddProductBinding
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import tr.main.elephantapps_sprint1.model.response.ResponseModel
import tr.main.elephantapps_sprint1.util.Utils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddPhoto : BaseActivity() {

    companion object{
        const val TAG = "cameraX"
        val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        @SuppressLint("StaticFieldLeak")
        lateinit var addPhotoActivity: Activity
    }

    private lateinit var binding : ActivityAddPhotoBinding
    private var receivedProduct : ProductAddModel? = null
    private val photoList = ArrayList<Uri>()

    private var imageCapture : ImageCapture? = null
    private lateinit var outputDirectory : File
    private lateinit var cameraExecutor: ExecutorService
    private var productId : Int = 0
    private var success : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addPhotoActivity = this
        createToolbar()
        receivedProduct = intent.getParcelableExtra("product") as? ProductAddModel
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        productId = intent.getIntExtra("data",0)
        println("productId: " + productId.toString())

        if (allPermissionsGranted()){
            startCamera()

        }else{
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS,
                Constans.REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnTakePhoto.setOnClickListener {
            takePhoto()
        }

        binding.btnGallery.setOnClickListener {
            if (checkStoragePermission())
                pickImage()
            else storageRequestPermission()
        }

        binding.btnSave.setOnClickListener {
            var i = 0
            for (photoUri in photoList){
                i++
                photoUri.path?.let { it1 -> postProductPhotos(productId, it1,i) }
            }
            if (i == photoList.size){
                startActivity(Intent(this@AddPhoto,IlaniGazla::class.java))
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
            }
        }

    }

    private fun postProductPhotos(productId : Int, capturedPhotoPath: String,position:Int) {
        val productId = productId
        val productFile = File(capturedPhotoPath)

        val mediaType = "image/jpeg".toMediaTypeOrNull()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("ProductId", productId.toString())
            .addFormDataPart("Product", productFile.name, RequestBody.create(mediaType, productFile))
            .build()

        val request = Request.Builder()
            .url(Constans.BASE_URL + Constans.EXT_PRODUCT_PHOTOS)
            .post(requestBody)
            .header("Authorization", "Bearer ${Constans.ACCESS_TOKEN}")
            .header("ApiKey", Constans.API_KEY)
            .build()

        val client = OkHttpClient()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseData = response.body?.string()

                } else {
                    println("${position+1}. fotoğraf yüklemesi başarısız: " + response.message)
                }
            } catch (e: IOException) {
                println(e)
            }
        }
    }

    private fun setImageToAdapter(uri:Uri){
        photoList.add(uri)
        val productPhotoAdapter = ProductPhotosAdapter(this@AddPhoto,photoList)
        binding.rvProductPhoto.layoutManager = LinearLayoutManager(this@AddPhoto,LinearLayoutManager.HORIZONTAL,false)
        binding.rvProductPhoto.adapter = productPhotoAdapter
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun storageRequestPermission() = ActivityCompat.requestPermissions(
        this,
        arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), 1000
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {
                        setImageToAdapter(result.uri)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickImage() {
        this.let {
            CropImage.activity()
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle(resources.getString(R.string.crop_image_save_ok))
                .start(it)
        }
    }

    private fun createToolbar(){
        setSupportActionBar(binding.toolbarAddPhoto)

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarAddPhoto.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.toolbarAddPhoto.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

    }

    private fun takePhoto(){
        val imageCapture = imageCapture
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(Constans.FILE_NAME_FORMAT, Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg")

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(outputOption, ContextCompat.getMainExecutor(this),object :ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                setImageToAdapter(savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e(TAG,"onError:${exception.message}",exception)
            }

        })
    }


    private fun getOutputDirectory(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it,resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if(mediaDir != null && mediaDir.exists()){
            mediaDir
        }else{
            filesDir
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        try {
            when (requestCode) {
                1000 -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        pickImage()
                    } else {
                        Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
                Constans.REQUEST_CODE_PERMISSIONS -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startCamera()
                    } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun allPermissionsGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext,it
            ) == PackageManager.PERMISSION_GRANTED
        }


    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.imagePreview.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview,imageCapture)

            }catch (e: Exception){
                Log.d(TAG,"startCamera fail: ",e)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}