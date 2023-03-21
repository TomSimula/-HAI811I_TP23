package com.example.tp3v2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class SyntheseFragment : Fragment() {

    private lateinit var listener: OnValidateListener

    public interface OnValidateListener {
        fun onValidate(nom: String, prenom: String, interests: List<String>, option: Boolean)
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
        val interests = arguments?.getStringArrayList("interests")
        val option = arguments?.getBoolean("option")

        var nomView = view?.findViewById<TextView>(R.id.s_nom)
        var prenomView = view?.findViewById<TextView>(R.id.s_prenom)

        nomView?.text = nom
        prenomView?.text = prenom

        for(interest in interests!!) {
            val interest_view = TextView(requireContext())
            interest_view.text = interest
            interest_view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            view?.findViewById<LinearLayout>(R.id.interests)?.addView(interest_view)
        }

        view?.findViewById<Button>(R.id.validate)
            ?.setOnClickListener { listener.onValidate(nom!!, prenom!!, interests, option!!) }

        view?.findViewById<Button>(R.id.info_return)
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