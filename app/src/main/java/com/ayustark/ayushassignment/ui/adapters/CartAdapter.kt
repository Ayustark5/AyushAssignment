package com.ayustark.ayushassignment.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ayustark.ayushassignment.R
import com.ayustark.ayushassignment.database.CartEntity
import com.ayustark.ayushassignment.databinding.ItemMenuBinding
import com.ayustark.ayushassignment.ui.dashboard.DashboardViewModel

class CartAdapter(private val context: Context, private val cartList: ArrayList<CartEntity>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var viewModel: DashboardViewModel =
        ViewModelProvider(context as FragmentActivity)[DashboardViewModel::class.java]

    inner class CartViewHolder(val bind: ItemMenuBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            ItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val bind = holder.bind
        val cart = cartList[position]
        bind.btnAdd.visibility = View.GONE
        bind.quantityLayout.visibility = View.VISIBLE
        bind.quantity.text = cart.quantity.toString()
        if (cart.quantity < 1) {
            updateItem(cart)
            cartList.remove(cart)
            notifyItemRemoved(position)
        }
        bind.txtFoodName.text = cart.name
        bind.txtSerial.text = context.getString(R.string.item_serial, position + 1)
        bind.txtFoodPrice.text = context.getString(R.string.item_cost, cart.costForOne)
        bind.btnPlus.setOnClickListener {
            ++cart.quantity
            bind.quantity.text = cart.quantity.toString()
            updateItem(cart)
        }
        bind.btnMin.setOnClickListener {
            --cart.quantity
            bind.quantity.text = cart.quantity.toString()
            updateItem(cart)
            if (cart.quantity < 1) {
                val index = cartList.indexOf(cart)
                cartList.removeAt(index)
                notifyItemRemoved(index)
                Log.d("Cart Remove", "$position ${cartList.size} $index  ${cart.id}")
            }
        }

    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private fun updateItem(cart: CartEntity) {
        viewModel.updateCartItem(cart)
        viewModel.updateTotalPrice(cartList)
    }


}