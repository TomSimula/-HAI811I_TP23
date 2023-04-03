package com.example.tp3v2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfRenderer
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text
import java.io.File


class DownloadCompletedReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.DOWNLOAD_COMPLETE"){
            android.widget.Toast.makeText(context, "Download completed", android.widget.Toast.LENGTH_SHORT).show()
            var path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/liste-jeux.pdf"

            val filep = File(path)


            if (filep.exists()) {
                lateinit var version: String
                lateinit var author: String
                lateinit var title: String
                lateinit var creation_date: String
                lateinit var producer: String


                val content = filep.readText()
                for(line in content.lines()){
                    if(line.startsWith("%PDF")) version = line.split("-")[1]
                    if(line.startsWith("/Author")) author = line.split(" ")[1].replace("(", "").replace(")", "")
                    if(line.startsWith("/Title")) title = line.substring(8, line.length - 1)
                    if(line.startsWith("/CreationDate")) creation_date = line.split(" ")[1].replace("(", "").replace(")", "")
                    if(line.startsWith("/Producer")) producer = line.substring(11, line.length - 1)
                }

                Log.d("version", version)
                Log.d("author", author)
                Log.d("title", title)
                Log.d("creation_date", creation_date)
                Log.d("producer", producer)

                val info = "Version: $version\nAuthor: $author\nTitle: $title\nCreation date: $creation_date\nProducer: $producer"
                var view = View.inflate(context, R.layout.fragment_inscription, null)
                view.findViewById<TextView>(R.id.inscrFragTextViewPDFInformation).text = info

            } else {
                Log.d("DownloadService", "File does not exist")
            }
        }
    }
}