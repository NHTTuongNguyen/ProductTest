package com.example.producttest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.producttest.adapter.ProductAdapter;
import com.example.producttest.database.DatabaseHelper;
import com.example.producttest.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerViewProduct;
    private FloatingActionButton floatAddProduct;
    private final  int PICK_IMAGE_REQUEST = 71;
    private  ImageView imageViewAdd;
    private  DatabaseHelper databaseHelper;
    private  AlertDialog alertDialog;
    private  LinearLayoutManager linearLayoutManager;
    private  ProductAdapter productAdapter;
    private  List<Product> productList;
    private  TextView txtNoData;
    private SearchView search_barView;

    String name;
    String price;
    String des;

    EditText edtName;
    EditText edtPrice;
    EditText edtDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        databaseHelper = new DatabaseHelper(this);
        hasdatainlist();
        searchProduct();
    }

    private void initView() {
        recyclerViewProduct = findViewById(R.id.recyclerViewProduct);
        search_barView = findViewById(R.id.search_barView);
        txtNoData = findViewById(R.id.txtNoData);
        findViewById(R.id.floatAddProduct).setOnClickListener(this);
    }

    private void searchProduct() {
        search_barView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Product>newList = new ArrayList<>();
                for (Product product : productList){
                    String name = product.getName().toLowerCase();
                    if (name.contains(newText)){
                        newList.add(product);
                    }
                }
                setAdapter();
                productAdapter.setFilter(newList);
                return true;
            }
        });
    }
    public void hasdatainlist() {
        productList = databaseHelper.getAllProduct();
        if (productList.size()>0) {
            setAdapter();
            recyclerViewProduct.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
        }else {
            recyclerViewProduct.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter() {
        productAdapter = new ProductAdapter(this, productList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewProduct.setLayoutManager(linearLayoutManager);
        recyclerViewProduct.setHasFixedSize(true);
        recyclerViewProduct.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.floatAddProduct:
                diaLogAddProduct();
                break;
            case R.id.selectImageview:
                chooseImage();
                break;
        }
    }



    private void diaLogAddProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View viewLayout = inflater.inflate(R.layout.dialog_addproduct,null);
        viewLayout.findViewById(R.id.selectImageview).setOnClickListener(this);
        viewLayout.findViewById(R.id.btnAddProduct).setOnClickListener(this);
        initViewLayoutDiaLog(viewLayout);
        builder.setView(viewLayout);
        builder.setCancelable(true);
         alertDialog = builder.create();
        alertDialog.show();
    }

    private void initViewLayoutDiaLog(View viewLayout) {
         edtName = viewLayout.findViewById(R.id.edtName);
         edtPrice = viewLayout.findViewById(R.id.edtPrice);
         edtDescription = viewLayout.findViewById(R.id.edtDescription);
        Button btnAddProducts = viewLayout.findViewById(R.id.btnAddProduct);
        imageViewAdd =viewLayout.findViewById(R.id.selectImageview);

        btnAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 name = edtName.getText().toString().trim();
                 price = edtPrice.getText().toString().trim();
                 des = edtDescription.getText().toString().trim();
                // byte [] image =imageViewToByte(imageViewAdd);

                if (name.length()>0 && price.length()>0 && des.length()>0){
                    alertDialog.dismiss();
                }
                if(TextUtils.isEmpty(name)) {
                    edtName.setError("Please input name");
                    return;
                }else if(TextUtils.isEmpty(price)) {
                    edtPrice.setError("Please input price");
                    return;
                }else if(TextUtils.isEmpty(des)) {
                    edtDescription.setError("Please input description");
                    return;
                }
//
                Product product = new Product(name,price,des);
                databaseHelper.insertProduct(product);
                productList.addAll(databaseHelper.getAllProduct());
                hasdatainlist();
            }
        });
    }

    private byte[] imageViewToByte(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte []  byteArray = stream.toByteArray();
        return byteArray;

    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData() != null)
        {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
                imageViewAdd.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }






}