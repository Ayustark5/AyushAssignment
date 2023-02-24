package com.ayustark.ayushassignment.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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

    private var bind: FragmentMenuBinding? = null
    lateinit var viewModel: DashboardViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bind = FragmentMenuBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        (activity as DashboardActivity).supportActionBar?.apply {
            title = "Menu"
            setDisplayHomeAsUpEnabled(false)
        }
        createMenu()
        subscribeToObservers()
        viewModel.getRestaurantList()
        return bind?.root
    }

    override fun onDestroyView() {
        bind = null
        super.onDestroyView()
    }

    private fun subscribeToObservers() {
        viewModel.restaurant.observe(viewLifecycleOwner) {
            when (val restaurantResponse = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(restaurantResponse.message)
                }
                is Resource.Loading -> {
                    Log.d("RestaurantObserverResponse", "Loading")
                }
                is Resource.Success -> {
                    if (restaurantResponse.data?.data != null)
                        setupUI(restaurantResponse.data.data)
                    else
                        showSnackBar("Some Error Occurred")
                }
                null -> {
                    Log.d("RestaurantObserverResponse", "Received null")
                }
            }
        }

        viewModel.menu.observe(viewLifecycleOwner) {
            when (val menu = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(menu.message)
                }
                is Resource.Loading -> {
                    Log.d("MenuObserverResponse", "Loading")
                }
                is Resource.Success -> {
                    if (menu.data?.data != null) {
                        (bind?.menuRecycler?.adapter as MenuAdapter).setMenuList(menu.data.data.data)
                    } else {
                        showSnackBar("Some Error Occurred")
                    }
                }
                null -> {
                    Log.d("MenuObserverResponse", "received null")
                }
            }
        }
        viewModel.cart.observe(viewLifecycleOwner) {
            when (val cart = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(cart.message)
                    bind?.menuRecycler?.layoutManager = LinearLayoutManager(context)
                    bind?.menuRecycler?.adapter = MenuAdapter(
                        requireActivity(),
                        arrayListOf<CartEntity>().apply {
                            cart.data?.let { cartList ->
                                addAll(
                                    cartList
                                )
                            }
                        })
                    viewModel.getMenu()
                }
                is Resource.Loading -> {
                    Log.d("CartObserverResponse", "Loading")
                }
                is Resource.Success -> {
                    bind?.menuRecycler?.layoutManager = LinearLayoutManager(context)
                    bind?.menuRecycler?.adapter = MenuAdapter(requireActivity(),
                        arrayListOf<CartEntity>().apply { cart.data?.let { it1 -> addAll(it1) } }
                    )
                    viewModel.getMenu()
                }
                null -> {
                    Log.d("CartObserverResponse", "null")
                }

            }
        }
    }

    private fun setupUI(data: RestaurantData) {
        bind?.apply {
            val restaurant = data.data[2]
            name.text = restaurant.name
            forOne.text = getString(R.string.cost_for_one, restaurant.costForOne)
            rating.text = restaurant.rating
            Picasso.get().load(restaurant.imageUrl).fit().centerCrop().error(R.drawable.logo)
                .into(logo)
        }
        viewModel.getCartItems()
    }

    private fun createMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.profile_menu, menu)
                menuInflater.inflate(R.menu.cart_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.profile -> {
                        bind?.let {
                            Navigation.findNavController(it.root).navigate(R.id.menu_to_profile)
                        }
                        true
                    }
                    R.id.cart -> {
                        bind?.let {
                            Navigation.findNavController(it.root).navigate(R.id.menu_to_cart)
                        }
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showSnackBar(msg: String?) {
        if (msg != null) {
            bind?.let { Snackbar.make(it.root, msg, Snackbar.LENGTH_SHORT).show() }
        }
    }

}