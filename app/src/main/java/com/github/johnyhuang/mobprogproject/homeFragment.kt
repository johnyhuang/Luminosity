package com.github.johnyhuang.mobprogproject

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

class homeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set on click listener for start game
        val gameBtn = view.findViewById<Button>(R.id.startGameButton)
        gameBtn.setOnClickListener{
            val intent = Intent(activity, GameActivity::class.java)
            startActivity(intent)
        }
        //Set on click listener to navigate to rules fragment
        val ruleBtn = view.findViewById<Button>(R.id.rulesButton)
        ruleBtn.setOnClickListener{
            view.findNavController().navigate(R.id.action_homeFragment_to_rulesFragment)
        }
    }

}
