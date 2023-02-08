package com.ayustark.ayushassignment.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
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

    private var binding: FragmentCartBinding? = null
    private val bind get() = binding!!
    lateinit var viewModel: DashboardViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        (activity as DashboardActivity).supportActionBar?.apply {
            title = "Cart"
            setDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)
        subscribeToObservers()
        setEventListeners()
        viewModel.getCartItems()
        return binding?.root
    }

    private fun subscribeToObservers() {
        viewModel.cart.observe(viewLifecycleOwner) {
            when (val cart = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(cart.message)
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    Log.d("GetCartResponse 2", "${cart.data?.size}")
                    bind.cartRecycler.layoutManager = LinearLayoutManager(context)
                    bind.cartRecycler.adapter = CartAdapter(requireActivity(),
                        arrayListOf<CartEntity>().apply { addAll(cart.data!!) }
                    )
                    viewModel.updateTotalPrice(arrayListOf<CartEntity>().apply { addAll(cart.data!!) })
                }
                null -> {

                }

            }
        }

        viewModel.total.observe(viewLifecycleOwner) {
            bind.btnPlaceOrder.text = "Place Order(Total: Rs. $it)"
            Log.d("GetCartResponse 3", "$it")
            bind.btnPlaceOrder.isEnabled = it >= 1
            bind.btnPlaceOrder.visibility = if (it < 1) View.GONE else View.VISIBLE
//                bind.btnPlaceOrder.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.disabledDarkGreen))
//                bind.btnPlaceOrder.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.text_color_hint))
            bind.rlEmpty.visibility = if (it < 1) View.VISIBLE else View.GONE
        }
    }

    private fun setEventListeners() {
        bind.btnPlaceOrder.setOnClickListener {
            bind.btnPlaceOrder.visibility = View.GONE
            bind.rlFinal.visibility = View.VISIBLE
            viewModel.emptyCart()
        }
        bind.btnOK.setOnClickListener {
            Navigation.findNavController(bind.root).popBackStack()
        }
        bind.goShop.setOnClickListener {
            Navigation.findNavController(bind.root).popBackStack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                Navigation.findNavController(bind.root).navigate(R.id.cart_to_profile)
                return false
            }
        }
        return super.onOptionsItemSelected(item)
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