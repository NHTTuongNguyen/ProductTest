package com.example.producttest.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.producttest.R;
import com.example.producttest.activity.ProductDetailActivity;
import com.example.producttest.model.Cart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolderHistory> {
    Context context;
    ArrayList<Cart> cartArrayList;

    public HistoryAdapter(Context context, ArrayList<Cart> cartArrayList) {
        this.context = context;
        this.cartArrayList = cartArrayList;
    }

    @NonNull
    @Override
    public ViewHolderHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history,parent,false);
        return new ViewHolderHistory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderHistory holder, int position) {
        Cart cart = cartArrayList.get(position);
        holder.txtCurrentTimeHistory.setText(cart.getTime());
        Locale locale = new Locale("vi","VN");
        NumberFormat fmt =NumberFormat.getCurrencyInstance(locale);
        holder.txtTotalHistory.setText(fmt.format(cart.getTotal()));
    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    public class ViewHolderHistory extends  RecyclerView.ViewHolder  {
        public TextView txtTotalHistory,txtCurrentTimeHistory;
        public ViewHolderHistory(@NonNull View itemView) {
            super(itemView);
            txtCurrentTimeHistory = itemView.findViewById(R.id.txtCurrentTimeHistory);
            txtTotalHistory = itemView.findViewById(R.id.txtTotalHistory);


        }


    }
}
