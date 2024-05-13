package com.workmanagerimagedownloading

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class MainActivity : AppCompatActivity() {
    val imageUrls = listOf(
        "https://i.imgur.com/CzXTtJV.jpg",
        "https://i.imgur.com/OB0y6MR.jpg",
        "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg",
        "https://farm4.staticflickr.com/3075/3168662394_7d7103de7d_z_d.jpg",
        "https://farm9.staticflickr.com/8505/8441256181_4e98d8bff5_z_d.jpg",
        "https://i.imgur.com/OnwEDW3.jpg",
        "https://farm3.staticflickr.com/2220/1572613671_7311098b76_z_d.jpg",
        "https://farm6.staticflickr.com/5590/14821526429_5c6ea60405_z_d.jpg",
        "https://farm7.staticflickr.com/6089/6115759179_86316c08ff_z_d.jpg",
        "https://farm2.staticflickr.com/1090/4595137268_0e3f2b9aa7_z_d.jpg",
        "https://farm4.staticflickr.com/3224/3081748027_0ee3d59fea_z_d.jpg",
        "https://farm8.staticflickr.com/7377/9359257263_81b080a039_z_d.jpg",
        "https://farm9.staticflickr.com/8295/8007075227_dc958c1fe6_z_d.jpg",
        "https://farm2.staticflickr.com/1449/24800673529_64272a66ec_z_d.jpg",
        "https://farm4.staticflickr.com/3752/9684880330_9b4698f7cb_z_d.jpg",
        "https://farm4.staticflickr.com/3827/11349066413_99c32dee4a_z_d.jpg"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.btnDownloadImages).setOnClickListener {
            DownloadImageWorker.downloadImages(context = baseContext,imageUrls)
        }
    }


}