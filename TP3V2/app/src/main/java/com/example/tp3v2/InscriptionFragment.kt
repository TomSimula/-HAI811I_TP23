package com.example.tp3v2


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.app.ActivityCompat

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
        view.findViewById<Button>(R.id.mainPageButtonDownload).setOnClickListener(this)
            // ask for permission to donwload

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
        when (p0?.id) {
            R.id.mainPageButtonValidate -> {
                submit()
            }
            R.id.mainPageButtonDownload -> {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                    return
                }
                Intent(requireContext(), DownloadService::class.java).also { intent -> context?.startService(intent) }
            }
        }
    }

    private fun submit() {
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