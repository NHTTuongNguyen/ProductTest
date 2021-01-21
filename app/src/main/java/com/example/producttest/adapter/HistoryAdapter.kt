package com.example.producttest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.producttest.R
import com.example.producttest.adapter.HistoryAdapter.ViewHolderHistory
import com.example.producttest.model.Cart
import java.text.NumberFormat
import java.util.*

class HistoryAdapter(var context: Context, var cartArrayList: MutableList<Cart>) : RecyclerView.Adapter<ViewHolderHistory>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false)
        return ViewHolderHistory(view)
    }

    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {
        val cart = cartArrayList[position]
        holder.txtCurrentTimeHistory.text = cart.time
        val locale = Locale("vi", "VN")
        val fmt = NumberFormat.getCurrencyInstance(locale)
        holder.txtTotalHistory.text = fmt.format(cart.total.toLong())
    }

    override fun getItemCount(): Int {
        return cartArrayList.size
    }

    inner class ViewHolderHistory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTotalHistory: TextView
        var txtCurrentTimeHistory: TextView

        init {
            txtCurrentTimeHistory = itemView.findViewById(R.id.txtCurrentTimeHistory)
            txtTotalHistory = itemView.findViewById(R.id.txtTotalHistory)
        }
    }
}