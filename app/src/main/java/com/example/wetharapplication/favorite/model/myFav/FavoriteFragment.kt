package com.example.wetharapplication.favorite.model.myFav

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
import com.example.wetharapplication.network.InternetCheck
import com.example.wetharapplication.network.WeatherClient
import com.google.android.material.snackbar.Snackbar


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
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            requireActivity().getString(R.string.favouriteFragment)
        var context:Context = requireContext()
        favFactory =
            FavViewModelFactory (Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource.getInstance(context)) )
        favModel =
            ViewModelProvider(requireActivity(),  favFactory).get(FavVeiwModel::class.java)
       binding.favFAB.setOnClickListener {
           if (InternetCheck.getConnectivity(context)==true){
               Navigation.findNavController(view).navigate(R.id.Fav_to_map)
           }else{
               val snakbar = Snackbar.make(
                   view,
                   context.resources.getString(R.string.no_internet),
                   Snackbar.LENGTH_LONG
               ).setAction("Action", null)
               snakbar.show()
           }

       }

       favModel.getFavouriteCountries()

       favModel._FavWeathers.observe(viewLifecycleOwner){

         myFavWeather = it

           if (it.size==0){
               binding.favLottie.visibility = View.VISIBLE
               binding.favLottie.animate()
           }else{
               binding.favLottie.visibility = View.GONE
           }

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