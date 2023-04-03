package com.example.tp3v2


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import org.json.JSONObject

class InscriptionFragment : Fragment(), OnClickListener {

    private lateinit var listener: OnSubmitListener

    private var nom: String = ""
    private var prenom: String = ""
    private var dateAnniv: String = ""
    private var travail: String = ""
    private var telephone: String = ""
    private var interests: MutableList<String> = mutableListOf()
    private var sync: Boolean = false
    private var FILENAME = "informations.json"

    public interface OnSubmitListener {
        fun onSubmit(nom: String, prenom: String, dateAnniv: String, travail: String, telephone: String, interests: List<String>, sync: Boolean)
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
            sync = savedInstanceState.getBoolean("sync")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_inscription, container, false)

        view.findViewById<Button>(R.id.inscrFragButtonsubmit).setOnClickListener(this)
        view.findViewById<Button>(R.id.inscrFragButtonDownload).setOnClickListener(this)
        view.findViewById<Button>(R.id.inscrFragButtonLoad).setOnClickListener(this)

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
            R.id.inscrFragButtonsubmit -> {
                submit()
            }
            R.id.inscrFragButtonDownload -> {
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

            R.id.inscrFragButtonLoad -> {


                // open json file and parse it
                val file = requireContext().getFileStreamPath(FILENAME)
                if (file.exists()) {
                    val json = requireContext().openFileInput(FILENAME).bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(json)

                    // get values from json
                    nom = jsonObject.getString("nom")
                    prenom = jsonObject.getString("prenom")
                    dateAnniv = jsonObject.getString("dateAnniv")
                    travail = jsonObject.getString("travail")
                    telephone = jsonObject.getString("telephone")

                    val interests_array = jsonObject.getJSONArray("interests")

                    for (i in 0 until interests_array.length()) {
                        interests.add(interests_array.getString(i))
                    }

                    // set values to the view
                    (view?.findViewById<View>(R.id.inscrFragEditTextLastName) as TextView).text = nom
                    (view?.findViewById<View>(R.id.inscrFragEditTextFirstName) as TextView).text = prenom
                    (view?.findViewById<View>(R.id.inscrFragEditTextBirthday) as TextView).text = dateAnniv
                    (view?.findViewById<View>(R.id.inscrFragEditTextJob) as TextView).text = travail
                    (view?.findViewById<View>(R.id.inscrFragEditTextPhone) as TextView).text = telephone

                    if (interests.contains("Sport")) {
                        (view?.findViewById<View>(R.id.inscrFragCheckboxSport) as CheckBox).isChecked = true
                    }
                    if (interests.contains("Musique")) {
                        (view?.findViewById<View>(R.id.inscrFragCheckboxMusique) as CheckBox).isChecked = true
                    }
                    if (interests.contains("Lecture")) {
                        (view?.findViewById<View>(R.id.inscrFragCheckboxLecture) as CheckBox).isChecked = true
                    }
                    if (interests.contains("Séries")) {
                        (view?.findViewById<View>(R.id.inscrFragCheckboxSerie) as CheckBox).isChecked = true
                    }

                    Toast.makeText(requireContext(), "Chargement réussi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun submit() {
        nom = (view?.findViewById<View>(R.id.inscrFragEditTextLastName) as TextView).text.toString()
        prenom = (view?.findViewById<View>(R.id.inscrFragEditTextFirstName) as TextView).text.toString()
        dateAnniv = (view?.findViewById<View>(R.id.inscrFragEditTextBirthday) as TextView).text.toString()
        travail = (view?.findViewById<View>(R.id.inscrFragEditTextJob) as TextView).text.toString()
        telephone = (view?.findViewById<View>(R.id.inscrFragEditTextPhone) as TextView).text.toString()

        if ((view?.findViewById<View>(R.id.inscrFragCheckboxSport) as CheckBox).isChecked) {
            if(!interests.contains("Sport")) interests.add("Sport")
        } else {
            interests.remove("Sport")
        }

        if ((requireView().findViewById<View>(R.id.inscrFragCheckboxMusique) as CheckBox).isChecked) {
            if(!interests.contains("Musique")) interests.add("Musique")
        } else {
            interests.remove("Musique")
        }

        if ((requireView().findViewById<View>(R.id.inscrFragCheckboxLecture) as CheckBox).isChecked) {
            if(!interests.contains("Lecture")) interests.add("Lecture")
        } else {
            interests.remove("Lecture")
        }

        if ((requireView().findViewById<View>(R.id.inscrFragCheckboxSerie) as CheckBox).isChecked) {
            if(!interests.contains("Séries")) interests.add("Séries")
        } else {
            interests.remove("Séries")
        }

        sync = (view?.findViewById<View>(R.id.inscrFragCheckboxSync) as CheckBox).isChecked

        listener.onSubmit(nom, prenom, dateAnniv, travail, telephone, interests, sync)
    }



}