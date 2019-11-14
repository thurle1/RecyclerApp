package com.example.recyclingapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import kotlinx.android.synthetic.main.activity_scan_camera.*
import java.io.File
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import com.amazonaws.services.rekognition.AmazonRekognition
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.amazonaws.services.rekognition.model.DetectLabelsRequest
import com.amazonaws.services.rekognition.model.DetectLabelsResult
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.services.rekognition.model.Label
import com.amazonaws.util.IOUtils
import java.io.FileInputStream
import java.lang.ref.WeakReference
import java.nio.ByteBuffer
import kotlin.concurrent.thread


class ScanCameraActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_camera)
        camera_btn.setOnClickListener { cameraClick() }
    }

    private fun cameraClick(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try{
                    createImageFile()
                } catch(ex: IOException){
                    null
                }
                photoFile?.also {
                    val photoUri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(takePictureIntent, CAMERA_INTENT)
                }
            }
        }
    }
    var task: MyTask? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMERA_INTENT -> {
                if(resultCode == Activity.RESULT_OK){
                    task = MyTask(WeakReference(this))
                    task?.execute(currentPhotoPath)
                }
            }
        }
    }



    var currentPhotoPath: String? = null

    private fun createImageFile(): File{
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    companion object{
        val CAMERA_INTENT = 1
        class MyTask(var act: WeakReference<ScanCameraActivity>) : AsyncTask<String, Int, DetectLabelsResult>(){
            override fun doInBackground(vararg params: String?): DetectLabelsResult {
                var credentials = BasicAWSCredentials("AKIA3VBM2BXF4FRUO2GQ", "a6MZ/QLOsDNJexBls5S7gm8FzutDr0PvD0Fpwe2C")
                var client = AmazonRekognitionClient(credentials)
                var imageBytes: ByteBuffer
                var request = DetectLabelsRequest()
                val inputStream = FileInputStream(File(params[0]))
                imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream))
                request.withImage(Image().withBytes(imageBytes))
                    .withMaxLabels(100)
                    .withMinConfidence(10F)
                var result: DetectLabelsResult
                result = client.detectLabels(request)

                return result
            }

            override fun onPostExecute(result: DetectLabelsResult?) {
                super.onPostExecute(result)
                var labels = result?.labels
                if (labels != null) {
                    for(myLabel: Label in labels){
                        if(myLabel.name == "Bottle"){
                            act.get()?.textViewResult?.text = myLabel.name
                        }

                    }
                }
            }

        }
    }

}

