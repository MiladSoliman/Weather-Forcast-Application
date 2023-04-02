package com.example.wetharapplication.dialog.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.wetharapplication.MainActivity
import com.example.wetharapplication.R
import com.example.wetharapplication.databinding.FragmentIntialDialogBinding
import com.example.wetharapplication.dialog.viewmodel.IntialViewModel
import com.example.wetharapplication.dialog.viewmodel.IntialViewModelFactory
import com.example.wetharapplication.model.Location



class IntialDialogFragment : DialogFragment() {
    lateinit var binding: FragmentIntialDialogBinding
    lateinit var intialFactory: IntialViewModelFactory
    lateinit var intialModel: IntialViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntialDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var context: Context = requireContext()
        intialFactory = IntialViewModelFactory(Location(context))
        intialModel =
            ViewModelProvider(requireActivity(), intialFactory).get(IntialViewModel::class.java)

        binding.startSave.setOnClickListener {
            var selectedItem = binding.IntialradioGroup.checkedRadioButtonId
            if (selectedItem == R.id.radio_gps) {
                intialModel.getMyLocation()
            } else if (selectedItem == R.id.radio_map) {
                activity?.getSharedPreferences("My Shared", MODE_PRIVATE)?.edit()?.apply() {
                    putBoolean("Map", true)
                    putString("language", "en")
                    putString("units", "standard")
                    apply()
                }
                dialog?.dismiss()
                startHomeActivity()
            }else{
                Toast.makeText(requireContext(),"Please Choose Your Location", Toast.LENGTH_SHORT ).show()
            }
        }


        intialModel.observLocation().observe(viewLifecycleOwner){
            if (it[0]!=0.0 && it[1]!=0.0){
                saveInSharedPrefernce(it[0],it[1])
            }
        }

    }

        private fun saveInSharedPrefernce(lat: Double, lon: Double) {
            requireActivity().getSharedPreferences("My Shared", MODE_PRIVATE).edit().apply(){
                putBoolean("Map",false)
                putFloat("lat", lat.toFloat())
                putFloat("long", lon.toFloat())
                putString("language", "en")
                putString("units", "metrice")
                apply()
            }
            dialog?.dismiss()
            startHomeActivity()
        }

        private fun startHomeActivity() {
            var intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

    override fun onStart() {
        super.onStart()
       dialog?.setCanceledOnTouchOutside(false)
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


}
