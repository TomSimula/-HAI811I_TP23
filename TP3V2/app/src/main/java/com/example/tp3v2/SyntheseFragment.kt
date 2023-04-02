package com.example.tp3v2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class SyntheseFragment : Fragment() {

    private lateinit var listener: OnValidateListener

    public interface OnValidateListener {
        fun onValidate(nom: String, prenom: String, dateAnniv: String, travail: String, telephone: String, interests: List<String>, sync: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_synthese, container, false)
        val nom = arguments?.getString("nom")
        val prenom = arguments?.getString("prenom")
        val dateAnniv = arguments?.getString("dateAnniv")
        val travail = arguments?.getString("travail")
        val telephone = arguments?.getString("telephone")
        val interests = arguments?.getStringArrayList("interests")
        val sync = arguments?.getBoolean("sync")

        var nomView = view?.findViewById<TextView>(R.id.synthFragTextViewLastNameRes)
        var prenomView = view?.findViewById<TextView>(R.id.synthFragTextViewFirstNameRes)
        var dateAnnivView = view?.findViewById<TextView>(R.id.synthFragTextViewBirthdayRes)
        var travailView = view?.findViewById<TextView>(R.id.synthFragTextViewJobRes)
        var telephoneView = view?.findViewById<TextView>(R.id.synthFragTextViewPhoneRes)

        nomView?.text = nom
        prenomView?.text = prenom
        dateAnnivView?.text = dateAnniv
        travailView?.text = travail
        telephoneView?.text = telephone

        var sInterests = ""
        for(interest in interests!!) {
            sInterests += interest + "\n"
        }
        view?.findViewById<TextView>(R.id.synthFragTextViewInterestRes)?.text = sInterests

        view?.findViewById<Button>(R.id.synthFragButtonValidate)
            ?.setOnClickListener { listener.onValidate(nom!!, prenom!!, dateAnniv!!, travail!!, telephone!!, interests, sync!!) }

        view?.findViewById<Button>(R.id.synthFragButtonReturn)
            ?.setOnClickListener { requireActivity().onBackPressed() }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnValidateListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSubmitListener")
        }
    }
}