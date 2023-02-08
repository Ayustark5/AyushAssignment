package com.ayustark.ayushassignment.ui.dashboard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayustark.ayushassignment.R
import com.ayustark.ayushassignment.database.CartEntity
import com.ayustark.ayushassignment.databinding.FragmentMenuBinding
import com.ayustark.ayushassignment.network.responses.RestaurantData
import com.ayustark.ayushassignment.ui.adapters.MenuAdapter
import com.ayustark.ayushassignment.utils.Resource
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class MenuFragment : Fragment() {

    private var binding: FragmentMenuBinding? = null
    private val bind get() = binding!!
    lateinit var viewModel: DashboardViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        (activity as DashboardActivity).supportActionBar?.apply {
            title = "Menu"
            setDisplayHomeAsUpEnabled(false)
        }
        setHasOptionsMenu(true)
        subscribeToObservers()
        viewModel.getRestaurantList()
        return binding?.root
    }

    private fun subscribeToObservers() {
        viewModel.restaurant.observe(viewLifecycleOwner) {
            when (val restr = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(restr.message)
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (restr.data?.data != null)
                        setupUI(restr.data.data)
                    else
                        showSnackBar("Some Error Occurred")
                }
                null -> {

                }
            }
        }

        viewModel.menu.observe(viewLifecycleOwner) {
            when (val menu = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(menu.message)
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (menu.data?.data != null) {
                        (bind.menuRecycler.adapter as MenuAdapter).setMenuList(menu.data.data.data)
                    } else {
                        showSnackBar("Some Error Occurred")
                    }
                }
                null -> {

                }
            }
        }
        viewModel.cart.observe(viewLifecycleOwner) {
            when (val cart = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(cart.message)
                    bind.menuRecycler.layoutManager = LinearLayoutManager(context)
                    bind.menuRecycler.adapter = MenuAdapter(
                        requireActivity(),
                        arrayListOf<CartEntity>().apply { addAll(cart.data!!) })
                    viewModel.getMenu()
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    bind.menuRecycler.layoutManager = LinearLayoutManager(context)
                    bind.menuRecycler.adapter = MenuAdapter(requireActivity(),
                        arrayListOf<CartEntity>().apply { addAll(cart.data!!) }
                    )
                    viewModel.getMenu()
                }
                null -> {

                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        inflater.inflate(R.menu.cart_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                Navigation.findNavController(bind.root).navigate(R.id.menu_to_profile)
                return false
            }
            R.id.cart -> {
                Navigation.findNavController(bind.root).navigate(R.id.menu_to_cart)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupUI(data: RestaurantData) {
        val restaurant = data.data[2]
        bind.name.text = restaurant.name
        bind.forOne.text = "Cost for one: ₹ ${restaurant.costForOne}"
        bind.rating.text = restaurant.rating
        Picasso.get().load(restaurant.imageUrl).fit().centerCrop().error(R.drawable.logo)
            .into(bind.logo)
        viewModel.getCartItems()
    }

    private fun showSnackBar(msg: String?) {
        if (msg != null) {
            Snackbar.make(bind.root, msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}