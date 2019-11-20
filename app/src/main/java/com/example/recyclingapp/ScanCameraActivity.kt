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
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
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
import kotlinx.android.synthetic.main.recycle_recycler_view.*
import java.io.FileInputStream
import java.lang.ref.WeakReference
import java.nio.ByteBuffer
import kotlin.concurrent.thread


class ScanCameraActivity : AppCompatActivity() {


    lateinit var recycleRepository: RecycleRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_camera)
        camera_btn.setOnClickListener { cameraClick() }
        recycleRepository = RecycleRepository()
        saw_label.isVisible = false
        confidence_lbl.isVisible = false
        disp_meth_lbl.isVisible = false
        saw_tv.isVisible = false
        confidence_Tv.isVisible = false
        disposal_meth_Tv.isVisible = false
        progressBar.visibility = View.GONE
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
                var credentials = BasicAWSCredentials("My", "AWS CREDENTIALS")
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

            override fun onPreExecute() {
                super.onPreExecute()
                act.get()?.progressBar?.visibility = View.VISIBLE
                act.get()?.saw_label?.isVisible = false
                act.get()?.confidence_lbl?.isVisible = false
                act.get()?.disp_meth_lbl?.isVisible = false
                act.get()?.saw_tv?.isVisible = false
                act.get()?.confidence_Tv?.isVisible = false
                act.get()?.disposal_meth_Tv?.isVisible = false
            }

            override fun onPostExecute(result: DetectLabelsResult?) {
                super.onPostExecute(result)
                var labels = result?.labels
                if (labels != null) {
                    for(myLabel: Label in labels){
                        val isFound = act.get()?.recycleRepository?.contains(myLabel.name)
                        if(isFound != null){
                            if(isFound){
                                act.get()?.saw_label?.isVisible = true
                                act.get()?.confidence_lbl?.isVisible = true
                                act.get()?.disp_meth_lbl?.isVisible = true
                                act.get()?.saw_tv?.isVisible = true
                                act.get()?.confidence_Tv?.isVisible = true
                                act.get()?.disposal_meth_Tv?.isVisible = true
                                act.get()?.saw_tv?.text = myLabel.name
                                act.get()?.confidence_Tv?.text = myLabel.confidence.toString().subSequence(0, 5)
                                act.get()?.disposal_meth_Tv?.text = act.get()?.recycleRepository?.getRecycleMethod(myLabel.name)
                                act.get()?.progressBar?.visibility = View.GONE
                                break
                            }
                        }
                    }
                }
            }

        }
    }

}

