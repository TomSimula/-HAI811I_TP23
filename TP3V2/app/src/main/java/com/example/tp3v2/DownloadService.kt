package com.example.tp3v2

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import android.app.DownloadManager
import android.os.Environment
import androidx.core.net.toUri
import android.net.ConnectivityManager
import android.content.BroadcastReceiver

class DownloadService : IntentService("DownloadService"){

    override fun onHandleIntent(p0: Intent?) {

        Log.d("Download", "Download started")


        var connectionManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo = connectionManager.activeNetwork
        if (networkInfo != null) {
            var url = "https://lafertemace.fr/liste-jeux.pdf"
            var downloadManager =
                getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            var request = DownloadManager.Request(url.toUri())
                .setMimeType("application/pdf")
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setTitle("liste-jeux.pdf")
                .setDescription("Downloading file...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "liste-jeux.pdf")
            downloadManager.enqueue(request)
            //notify user when download is completed
            var receiver = object : BroadcastReceiver() {
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
