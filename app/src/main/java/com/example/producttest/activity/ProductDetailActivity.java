package com.example.producttest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.producttest.R;
import com.example.producttest.model.Product;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView txtNameDetail,
    txtPriceDetail,txtDescriptionDetail;
    private Product product;
    private Toolbar toolbar_ProductDetail;
    private ImageView imgDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getInTent();
        initView();
        actionToolBar();

    }
    private void actionToolBar() {
        toolbar_ProductDetail = findViewById(R.id.toolbar_ProductDetail);
        setSupportActionBar(toolbar_ProductDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_ProductDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getInTent() {
        Intent intent = getIntent();
        if (intent!=null){
            if (intent.hasExtra("ProductKey")){
                product = (Product) intent.getSerializableExtra("ProductKey");

            }
        }
    }

    private void initView() {
//        imgDetail = findViewById(R.id.imgDetail);
        txtNameDetail = findViewById(R.id.txtNameDetail);
        txtDescriptionDetail = findViewById(R.id.txtDescriptionDetail);
        txtPriceDetail = findViewById(R.id.txtPriceDetail);
        toolbar_ProductDetail = findViewById(R.id.toolbar_ProductDetail);
        toolbar_ProductDetail.setTitle(product.getName());
        //setText
        txtNameDetail.setText(product.getName());
        Locale locale = new Locale("vi","VN");
        NumberFormat fmt =NumberFormat.getCurrencyInstance(locale);
        txtPriceDetail.setText(fmt.format(product.getPrice()));
        txtDescriptionDetail.setText(product.getDes());
//        imgDetail

    }
}