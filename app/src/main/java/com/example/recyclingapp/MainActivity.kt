package com.example.recyclingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var awsSyncClient : AWSAppSyncClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scan_btn.setOnClickListener { launchScanActivity() }
        search_btn.setOnClickListener { launchSearchActivity() }
        awsSyncClient = AWSAppSyncClient.builder().context(applicationContext).awsConfiguration(
            AWSConfiguration(applicationContext)
        ).build()
        

    }
    private fun launchScanActivity(){
        val intent = Intent(this, ScanCameraActivity::class.java)
        startActivity(intent)
    }

    fun launchSearchActivity(){
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }


}
