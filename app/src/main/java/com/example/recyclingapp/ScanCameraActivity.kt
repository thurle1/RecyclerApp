package com.example.recyclingapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_scan_camera.*
import java.io.File
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMERA_INTENT -> {
                if(resultCode == Activity.RESULT_OK){
                    setPic()
                }
            }
        }
    }

    private fun setPic(){
        var imgFile = File(currentPhotoPath)
        if(imgFile.exists()){
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            camera_iv.setImageBitmap(myBitmap)
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
    }

}

