package edu.quinnipiac.workouttracker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.quinnipiac.workouttracker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.red.setOnClickListener {
            (activity as MainActivity).changeBackgroundColor(Color.rgb(189,46,50))
        }
        binding.green.setOnClickListener {
            (activity as MainActivity).changeBackgroundColor(Color.rgb(70,142,89))
        }
        binding.blue.setOnClickListener {
            (activity as MainActivity).changeBackgroundColor(Color.rgb(37,124,202))
        }
        binding.white.setOnClickListener {
            (activity as MainActivity).changeBackgroundColor(Color.rgb(255,255,255))
        }

        return view
    }

}