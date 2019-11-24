package com.github.johnyhuang.mobprogproject

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController


class rulesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rules, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set on click listener for return to home fragment
        val homeBtn = view.findViewById<Button>(R.id.backToHomeBtn)
        homeBtn.setOnClickListener{
            view.findNavController().navigate(R.id.action_rulesFragment_to_homeFragment)
        }
    }

}
