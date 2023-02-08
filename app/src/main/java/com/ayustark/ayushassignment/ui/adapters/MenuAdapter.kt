package com.ayustark.ayushassignment.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ayustark.ayushassignment.database.CartEntity
import com.ayustark.ayushassignment.databinding.ItemMenuBinding
import com.ayustark.ayushassignment.network.responses.MenuDataX
import com.ayustark.ayushassignment.ui.dashboard.DashboardViewModel

class MenuAdapter(val context: Context, val cartList: ArrayList<CartEntity>?) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private var viewModel: DashboardViewModel =
        ViewModelProvider(context as FragmentActivity)[DashboardViewModel::class.java]

    private val menuList = arrayListOf<MenuDataX>()

    inner class MenuViewHolder(val bind: ItemMenuBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            ItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val bind = holder.bind
        val menu = menuList[position]
        bind.quantityLayout.visibility = View.GONE
        bind.btnAdd.visibility = View.VISIBLE
        if (menu.quantity > 0) {
            bind.btnAdd.visibility = View.GONE
            bind.quantityLayout.visibility = View.VISIBLE
            bind.quantity.text = menu.quantity.toString()
        }
        bind.txtFoodName.text = menu.name
        bind.txtSerial.text = "${position + 1}"
        bind.txtFoodPrice.text = "₹ ${menu.costForOne}"
        bind.btnAdd.setOnClickListener {
            bind.btnAdd.visibility = View.GONE
            bind.quantityLayout.visibility = View.VISIBLE
            ++menu.quantity
            bind.quantity.text = menu.quantity.toString()
            updateItem(menu)
        }
        bind.btnPlus.setOnClickListener {
            ++menu.quantity
            bind.quantity.text = menu.quantity.toString()
            updateItem(menu)
        }
        bind.btnMin.setOnClickListener {
            --menu.quantity
            bind.quantity.text = menu.quantity.toString()
            if (menu.quantity < 1) {
                bind.btnAdd.visibility = View.VISIBLE
                bind.quantityLayout.visibility = View.GONE
            }
            updateItem(menu)
        }

    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    fun setMenuList(menu: List<MenuDataX>) {
        menuList.clear()
        menuList.addAll(menu)
        if (cartList?.isNotEmpty() == true)
            menuList.forEach { m ->
                for (i in cartList.indices) {
                    if (m.id == cartList[i].id) {
                        m.quantity = cartList[i].quantity
                        cartList.removeAt(i)
                        break
                    }
                }
            }
        notifyDataSetChanged()
    }

    private fun updateItem(menu: MenuDataX) {
        val item = CartEntity()
        item.id = menu.id
        item.name = menu.name
        item.quantity = menu.quantity
        item.costForOne = menu.costForOne
        item.restaurantId = menu.restaurantId
        viewModel.updateCartItem(item)
    }


}