package com.example.tp3v2

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.net.toUri

class InscriptionFragment : Fragment(), OnClickListener {

    private lateinit var listener: OnSubmitListener

    private var nom: String = ""
    private var prenom: String = ""
    private var dateAnniv: String = ""
    private var travail: String = ""
    private var telephone: String = ""
    private var interests: MutableList<String> = mutableListOf()
    private var option: Boolean = false

    public interface OnSubmitListener {
        fun onSubmit(nom: String, prenom: String, interests: List<String>, option: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            nom = savedInstanceState.getString("nom")!!
            prenom = savedInstanceState.getString("prenom")!!
            dateAnniv = savedInstanceState.getString("dateAnniv")!!
            travail = savedInstanceState.getString("travail")!!
            telephone = savedInstanceState.getString("telephone")!!
            interests = savedInstanceState.getStringArrayList("interests")!!
            option = savedInstanceState.getBoolean("option")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_inscription, container, false)

        view.findViewById<Button>(R.id.mainPageButtonValidate).setOnClickListener(this)
        view.findViewById<Button>(R.id.mainPageButtonDownload).setOnClickListener {

            var connectionManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            var networkInfo = connectionManager.activeNetwork
            if(networkInfo != null) {
                var url = "https://lafertemace.fr/liste-jeux.pdf"
                var downloadManager = requireContext().getSystemService(DownloadManager::class.java)
                var request = DownloadManager.Request(url.toUri())
                    .setMimeType("application/pdf")
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    .setTitle("liste-jeux.pdf")
                    .setDescription("Downloading file...")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "liste-jeux.pdf")
                downloadManager.enqueue(request)
                //notify user when download is completed
                var receiver = object : android.content.BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        android.widget.Toast.makeText(requireContext(), "Download completed", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                android.widget.Toast.makeText(requireContext(), "Network is not available", android.widget.Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSubmitListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSubmitListener")
        }
    }

    override fun onClick(p0: View?) {
        if(nom == "") {
            nom = (view?.findViewById<View>(R.id.mainPageEditTextFirstName) as TextView).text.toString()
        }
        if(prenom == "") {
            prenom = (view?.findViewById<View>(R.id.mainPageEditTextLastName) as TextView).text.toString()
        }
        if(interests.isEmpty()) {
            if ((view?.findViewById<View>(R.id.checkbox_sport) as CheckBox).isChecked) {
                interests.add("Sport")
            }
            if ((requireView().findViewById<View>(R.id.checkbox_musique) as CheckBox).isChecked) {
                interests.add("Cinema")
            }
            if ((requireView().findViewById<View>(R.id.checkbox_lecture) as CheckBox).isChecked) {
                interests.add("Musique")
            }
            if ((requireView().findViewById<View>(R.id.checkbox_serie) as CheckBox).isChecked) {
                interests.add("Jeux vidéo")
            }
        }

        option = (view?.findViewById<View>(R.id.inscrFragSyncCheck) as CheckBox).isChecked

        listener.onSubmit(nom, prenom, interests, option)
    }

}