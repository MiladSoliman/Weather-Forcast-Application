package com.example.wetharapplication.alert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.FragmentAlertBinding


class AlertFragment : Fragment() {
    lateinit var binding:FragmentAlertBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.alertFAB.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.Alert_To_Map)
        }
    }
}