package com.example.recyclingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scan_btn.setOnClickListener { launchScanActivity() }
        search_btn.setOnClickListener { launchSearchActivity() }
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
