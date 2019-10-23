package com.example.recyclingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scan_btn.setOnClickListener { scan_btn_click() }
        search_btn.setOnClickListener { search_btn_click() }
    }
    private fun scan_btn_click(){
        val intent = Intent(this, ScanCameraActivity::class.java)
        startActivity(intent)
    }

    private fun search_btn_click(){
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }


}
