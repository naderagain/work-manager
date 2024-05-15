package com.workmanagerimagedownloading

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class DownloadImageWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d(TAG, "doWork: ")
        val imageUrl = inputData.getString(KEY_IMAGE_URL)

        if (imageUrl.isNullOrEmpty()) {
            Log.e(TAG, "Invalid input data")
            return@withContext Result.failure()
        }
        try {
            val imageName = getImageNameFromUrl(imageUrl)
            Log.d(TAG, "doWork: imageName: $imageName")
            val imageFile = File(applicationContext.filesDir, imageName)
            if (imageFile.exists()) {
                Log.d(TAG, "Image already exists: $imageName")
                return@withContext Result.success()
            }
            downloadImage(imageUrl, imageFile)
            Log.d(TAG, "Image downloaded: $imageName")
            return@withContext Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading image: ${e.message}")
            return@withContext Result.failure()
        }
    }
    private fun getImageNameFromUrl(imageUrl: String): String {
        val lastIndex = imageUrl.lastIndexOf('/')
        return if (lastIndex != -1) {
            imageUrl.substring(lastIndex + 1)
        } else {
            "image_${System.currentTimeMillis()}"
        }
    }
    private fun downloadImage(imageUrl: String, imageFile: File) {
        val inputStream = URL(imageUrl).openStream()
        val outputStream = FileOutputStream(imageFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
    }
    companion object {
        private const val TAG = "DownloadImageWorker"
        const val KEY_IMAGE_URL = "image_url"

        private fun createWorkRequest(imageUrl: String): OneTimeWorkRequest {
            val inputData = Data.Builder()
                .putString(KEY_IMAGE_URL, imageUrl)
                .build()

            return OneTimeWorkRequestBuilder<DownloadImageWorker>()
                .setInputData(inputData)
                .build()
        }

        fun downloadImages(context: Context, imageUrls: List<String>) {
            val workManager = WorkManager.getInstance(context)
            val workRequests = mutableListOf<OneTimeWorkRequest>()

            for (i in imageUrls.indices) {
                val workRequest = createWorkRequest(imageUrls[i])
                workRequests.add(workRequest)
            }

            workManager.enqueue(workRequests)
        }
    }
}