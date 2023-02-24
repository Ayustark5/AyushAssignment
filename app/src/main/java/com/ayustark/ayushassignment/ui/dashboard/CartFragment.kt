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
import com.ayustark.ayushassignment.databinding.FragmentCartBinding
import com.ayustark.ayushassignment.ui.adapters.CartAdapter
import com.ayustark.ayushassignment.utils.Resource
import com.google.android.material.snackbar.Snackbar


class CartFragment : Fragment() {

    private var bind: FragmentCartBinding? = null
    private var viewModel: DashboardViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bind = FragmentCartBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        (activity as DashboardActivity).supportActionBar?.apply {
            title = "Cart"
            setDisplayHomeAsUpEnabled(true)
        }
        createMenu()
        subscribeToObservers()
        setEventListeners()
        viewModel?.getCartItems()
        return bind?.root
    }

    override fun onDestroyView() {
        bind = null
        super.onDestroyView()
    }

    private fun subscribeToObservers() {
        viewModel?.cart?.observe(viewLifecycleOwner) {
            when (val cart = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(cart.message)
                }
                is Resource.Loading -> {
                    Log.d("CartObserverResponse", "Loading")
                }
                is Resource.Success -> {
                    Log.d("GetCartResponse 2", "${cart.data?.size}")
                    bind?.cartRecycler?.layoutManager = LinearLayoutManager(context)
                    bind?.cartRecycler?.adapter = CartAdapter(requireActivity(),
                        arrayListOf<CartEntity>().apply {
                            cart.data?.let { cartList ->
                                addAll(
                                    cartList
                                )
                            }
                        }
                    )
                    viewModel?.updateTotalPrice(arrayListOf<CartEntity>().apply {
                        cart.data?.let { cartList ->
                            addAll(
                                cartList
                            )
                        }
                    })
                }
                null -> {
                    Log.d("CartObserverResponse", "null")
                }

            }
        }

        viewModel?.total?.observe(viewLifecycleOwner) {
            bind?.apply {
                btnPlaceOrder.text = getString(R.string.place_order, it)
                Log.d("GetCartResponse 3", "$it")
                btnPlaceOrder.isEnabled = it >= 1
                btnPlaceOrder.visibility = if (it < 1) View.GONE else View.VISIBLE
                rlEmpty.visibility = if (it < 1) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setEventListeners() {
        bind?.apply {
            btnPlaceOrder.setOnClickListener {
                btnPlaceOrder.visibility = View.GONE
                rlFinal.visibility = View.VISIBLE
                viewModel?.emptyCart()
            }
            btnOK.setOnClickListener {
                Navigation.findNavController(root).popBackStack()
            }
            btnGoShop.setOnClickListener {
                Navigation.findNavController(root).popBackStack()
            }
        }
    }

    private fun createMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.profile_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.profile -> {
                        bind?.let {
                            Navigation.findNavController(it.root).navigate(R.id.cart_to_profile)
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