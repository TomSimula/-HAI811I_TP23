package com.example.tp3v2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), SyntheseFragment.OnValidateListener, InscriptionFragment.OnSubmitListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainActivityFrameLayoutFrangment, InscriptionFragment())
            commit()
        }
    }

    override fun onSubmit(
        nom: String,
        prenom: String,
        dateAnniv: String,
        travail: String,
        telephone: String,
        interests: List<String>,
        sync: Boolean
    ) {
        val syntheseFragment = SyntheseFragment()
        val bundle = Bundle()
        bundle.putString("nom", nom)
        bundle.putString("prenom", prenom)
        bundle.putString("dateAnniv", dateAnniv)
        bundle.putString("travail", travail)
        bundle.putString("telephone", telephone)
        bundle.putStringArrayList("interests", ArrayList(interests))
        bundle.putBoolean("sync", sync)
        syntheseFragment.arguments = bundle

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainActivityFrameLayoutFrangment, syntheseFragment)
            setReorderingAllowed(true)
            addToBackStack(null)
            commit()
        }
    }

    override fun onValidate(
        nom: String,
        prenom: String,
        dateAnniv: String,
        travail: String,
        telephone: String,
        interests: List<String>,
        sync: Boolean
    ) {
        var FILENAME = "informations.json"

        var json = JSONObject()
        json.put("nom", nom)
        json.put("prenom", prenom)
        json.put("dateAnniv", dateAnniv)
        json.put("travail", travail)
        json.put("telephone", telephone)

        var array = JSONArray()
        for (interest in interests) {
            array.put(interest)
        }

        json.put("interests", array)


        //write to file
        openFileOutput(FILENAME, Context.MODE_PRIVATE).use {
            it.write(json.toString().toByteArray())
        }


        val file = getFileStreamPath(FILENAME)
        if(file.exists()) {
            Toast.makeText(this, "Informations enregistrées", Toast.LENGTH_SHORT).show()
            Log.d("infos", file.readText())
        }

    }
}