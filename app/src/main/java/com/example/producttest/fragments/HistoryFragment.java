package com.example.producttest.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.producttest.R;
import com.example.producttest.Share.SharedPreferences_Utils;
import com.example.producttest.activity.HistoryActivity;
import com.example.producttest.activity.HomeActivity;
import com.example.producttest.adapter.HistoryAdapter;
import com.example.producttest.model.Cart;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    private Toolbar toolbar_History;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerViewHistory;
    private ArrayList<Cart> cartArrayList;
    private SharedPreferences_Utils sharedPreferences_utils;
    private HistoryAdapter adapter;
    private TextView txtNoData;
    private Button btnRemove;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_history, container, false);
        sharedPreferences_utils = new SharedPreferences_Utils(getActivity());
        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        buidRecyclerView();
    }

    private void initView() {
        recyclerViewHistory = view.findViewById(R.id.recyclerViewHistory);
        txtNoData = view.findViewById(R.id.txtNoDataHistory);
        btnRemove = view.findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartArrayList.size()>0) {
                    sharedPreferences_utils.removeSaveCartProduct();
//                    finish();

//                    startActivity(getIntent());
//                    getActivity().onBackPressed();
                    ((HomeActivity)getActivity()).getViewPager().setCurrentItem(0);

                }else {
                    Toast.makeText(getActivity(),
                            "Not Data", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void buidRecyclerView() {
        cartArrayList =  sharedPreferences_utils.getSaveCartProduct(getActivity());
        if (cartArrayList.size()> 0){
            adapter = new HistoryAdapter(getActivity(),cartArrayList);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewHistory.setLayoutManager(linearLayoutManager);
            recyclerViewHistory.setHasFixedSize(true);
            recyclerViewHistory.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            recyclerViewHistory.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
        }else {
            recyclerViewHistory.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        }
    }
}