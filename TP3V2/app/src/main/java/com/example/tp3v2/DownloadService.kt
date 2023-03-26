package com.example.tp3v2

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContentProviderCompat.requireContext

class DownloadService : IntentService("DownloadService"){

    override fun onHandleIntent(p0: Intent?) {

        Log.d("Download", "Download started")


        var connectionManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        var networkInfo = connectionManager.activeNetwork
        if (networkInfo != null) {
            var url = "https://lafertemace.fr/liste-jeux.pdf"
            var downloadManager =
                getSystemService(Context.DOWNLOAD_SERVICE) as android.app.DownloadManager
            var request = android.app.DownloadManager.Request(android.net.Uri.parse(url))
            request.setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI or android.app.DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle("Download")
            request.setDescription("Downloading file...")
            request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                android.os.Environment.DIRECTORY_DOWNLOADS,
                "${System.currentTimeMillis()}"
            )
            downloadManager.enqueue(request)
            //notify user when download is completed
            var receiver = object : android.content.BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    android.widget.Toast.makeText(
                        context,
                        "Download completed",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}
