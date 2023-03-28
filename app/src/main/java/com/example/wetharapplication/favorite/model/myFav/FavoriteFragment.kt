package com.example.wetharapplication.favorite.model.myFav

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wetharapplication.R
import com.example.wetharapplication.database.ConcreteLocalSource
import com.example.wetharapplication.databinding.FragmentFavoriteBinding
import com.example.wetharapplication.favorite.viewmodel.myFav.FavVeiwModel
import com.example.wetharapplication.favorite.viewmodel.myFav.FavViewModelFactory
import com.example.wetharapplication.model.MyResponse
import com.example.wetharapplication.model.Repository
import com.example.wetharapplication.network.WeatherClient


class FavoriteFragment : Fragment() , OnRemove ,OnClick{
    lateinit var favFactory: FavViewModelFactory
    lateinit var favModel: FavVeiwModel
    lateinit var binding: FragmentFavoriteBinding
    lateinit var se :SharedPreferences
    private var lat:Double =0.0
    private var long :Double =0.0
    lateinit var myFavWeather : List<MyResponse>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      /*  se =  activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        lat = se.getFloat("lat",31.0f).toDouble()
        long = se.getFloat("lon",31.0f).toDouble()*/
        super.onViewCreated(view, savedInstanceState)
        var context:Context = requireContext()
        favFactory =
            FavViewModelFactory (Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource.getInstance(context)) )
        favModel =
            ViewModelProvider(requireActivity(),  favFactory).get(FavVeiwModel::class.java)
       binding.favFAB.setOnClickListener {
           Navigation.findNavController(view).navigate(R.id.Fav_to_map)
       }

      //favModel.insertWeather(lat,long)

       favModel.getFavouriteCountries()
       favModel._FavWeathers.observe(viewLifecycleOwner){
         myFavWeather = it
         binding.FavRecyclerView.apply {
             adapter = FavAdapter(myFavWeather,context,this@FavoriteFragment,this@FavoriteFragment)
             layoutManager = LinearLayoutManager(context)
         }
       }

    }

    override fun deleteCountry(response: MyResponse) {
        favModel.deletCountry(response)
    }

    override fun showDetails(lat: Double, long: Double) {
        var action =FavoriteFragmentDirections.fromFavToDetails(lat.toFloat(),long.toFloat())
        Navigation.findNavController(requireView()).navigate(action)
    }


}