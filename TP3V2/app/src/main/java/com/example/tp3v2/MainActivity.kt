package com.example.tp3v2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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
        interests: List<String>,
        option: Boolean
    ) {
        val syntheseFragment = SyntheseFragment()
        val bundle = Bundle()
        bundle.putString("nom", nom)
        bundle.putString("prenom", prenom)
        bundle.putStringArrayList("interests", ArrayList(interests))
        bundle.putBoolean("option", option)
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
        interests: List<String>,
        option: Boolean
    ) {
        var FILENAME = "informations.json"
        //encode to json
        var json = "{\"nom\": \"$nom\", \"prenom\": \"$prenom\""
        json += ", \"interests\": ["
        for(interest in interests) {
            json += "\"$interest\""
            if(interest != interests.last()) json += ", "
        }
        json += "]}"



        //write to file
        openFileOutput(FILENAME, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }
}