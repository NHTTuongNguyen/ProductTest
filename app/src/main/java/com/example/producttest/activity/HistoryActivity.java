package com.example.producttest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.producttest.R;
import com.example.producttest.Share.SharedPreferences_Utils;
import com.example.producttest.adapter.HistoryAdapter;
import com.example.producttest.model.Cart;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private Toolbar toolbar_History;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerViewHistory;
    private ArrayList<Cart> cartArrayList;
    private SharedPreferences_Utils sharedPreferences_utils;
    private HistoryAdapter adapter;
    private TextView txtNoData;
    private Button btnRemove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        actionToolBar();
        sharedPreferences_utils = new SharedPreferences_Utils(this);
        initView();
        cartArrayList =  sharedPreferences_utils.getSaveCartProduct(this);
        buidRecyclerView();

    }

    private void initView() {
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        txtNoData = findViewById(R.id.txtNoDataHistory);
        btnRemove = findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartArrayList.size()>0) {
                    sharedPreferences_utils.removeSaveCartProduct();
                    finish();
                    startActivity(getIntent());
                }else {
                    Toast.makeText(HistoryActivity.this, "Not Data", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void buidRecyclerView() {
        if (cartArrayList.size()> 0){
            adapter = new HistoryAdapter(HistoryActivity.this,cartArrayList);
            linearLayoutManager = new LinearLayoutManager(this);
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

    private void actionToolBar() {
        toolbar_History = findViewById(R.id.toolbar_History);
        setSupportActionBar(toolbar_History);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_History.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}