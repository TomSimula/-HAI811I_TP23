package com.example.tp3v2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import java.io.File

class DownloadCompletedReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.DOWNLOAD_COMPLETE"){
            android.widget.Toast.makeText(context, "Download completed", android.widget.Toast.LENGTH_SHORT).show()
            var path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/liste-jeux.pdf"

            val filep = File(path)

            Log.d("file path", "${filep.absoluteFile}")


            if (filep != null) {
                if (filep.exists()) {
                    filep.readText()
                    Log.d("DownloadService", "${filep.readText()}")
                } else {
                    Log.d("DownloadService", "File does not exist")
                }
            }
        }
    }
}