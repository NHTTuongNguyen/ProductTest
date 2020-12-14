package com.example.producttest.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.Toast;

import com.example.producttest.R;
import com.example.producttest.Share.SharedPreferences_Utils;
import com.example.producttest.adapter.ProductAdapter;
import com.example.producttest.database.DatabaseHelper;
import com.example.producttest.model.Cart;
import com.example.producttest.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerViewProduct;
    private FloatingActionButton floatAddProduct;
    private final  int PICK_IMAGE_REQUEST = 71;
    private ImageView imageViewAdd;
    private DatabaseHelper databaseHelper;
    private AlertDialog alertDialog;
    private LinearLayoutManager linearLayoutManager;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private TextView txtNoData;
    private TextView txtTotalProduct ;
    private SearchView search_barView;
    private Toolbar toolbar_Product;
    private String name, price, des;
    private EditText edtName, edtPrice, edtDescription;
    private Button btnHistory,btnProduct;
    private SharedPreferences_Utils sharedPreferences_utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences_utils = new SharedPreferences_Utils(MainActivity.this);
        initView();

        hasdatainlist();

        searchProduct();
        setTotalTextView();
    }



    public void setTotalTextView() {
        int total = totalProduct();
        Locale locale = new Locale("vi","VN");
        NumberFormat fmt =NumberFormat.getCurrencyInstance(locale);
//        sharedPreferences_utils.setSaveTotal(total);
        txtTotalProduct.setText(fmt.format(total));
    }

    private int totalProduct(){
        int total = 0;
        for(Product product : productList)
            total+=product.getPrice();
        return total;
    }
    private void initView() {
        search_barView = findViewById(R.id.search_barView);
        txtTotalProduct = findViewById(R.id.txtTotalProduct);
        recyclerViewProduct = findViewById(R.id.recyclerViewProduct);
        txtNoData = findViewById(R.id.txtNoData);
        btnProduct = findViewById(R.id.btnProduct);
        findViewById(R.id.btnHistory).setOnClickListener(this);
        findViewById(R.id.floatAddProduct).setOnClickListener(this);
        int iii = sharedPreferences_utils.getSaveTotal();
        Log.d("HHHH",iii+"");

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productList.size() > 0) {

                    int total = totalProduct();
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String date = simpleDateFormat.format(calendar.getTime());
                    String curTime = hours + "h" + ":" + minutes + "m" + ":" + seconds + "s" +" - "+ date;
                    Log.d("curTimecurTime", curTime + "");
                    sharedPreferences_utils.setSaveCartProduct(MainActivity.this, total, curTime);


                    ////
                    databaseHelper.removeAll();
//                    startActivity(new Intent(MainActivity.this,HistoryActivity.class));
                    finish();
                    startActivity(getIntent());
                    startActivity(new Intent(MainActivity.this,HistoryActivity.class));

                }else{
                    Toast.makeText(MainActivity.this, "Your Cart Empty !!!", Toast.LENGTH_SHORT).show();
                }
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
            case R.id.btnHistory:
                startActivity(new Intent(MainActivity.this,HistoryActivity.class));
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
                String price = edtPrice.getText().toString().trim();
                des = edtDescription.getText().toString().trim();
                // byte [] image =imageViewToByte(imageViewAdd);
                if(TextUtils.isEmpty(name)) {
                    edtName.setError("Please input name");
                    return;
                }else if(TextUtils.isEmpty(des)) {
                    edtDescription.setError("Please input description");
                    return;
                }
                int prisss = 0;
                if (TextUtils.isEmpty(price)) {
                    edtPrice.setError("Please input price");
                }else {
                    try {
                        prisss = Integer.parseInt(price);
                    }catch (NumberFormatException e ){
                        edtPrice.setError("Please input again");
                    }
                }
                if (prisss >0) {
                    Product product = new Product(name, prisss, des);
                    databaseHelper.insertProduct(product);
                    productList.addAll(databaseHelper.getAllProduct());
                    hasdatainlist();
                    setTotalTextView();
                    alertDialog.dismiss();
                }else {
                    edtPrice.setError("Please input again");
                }
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

    private void displayCurrentTextView() {
        Thread myThread;
        Runnable runnable = new CountDownRunner();
        myThread= new Thread(runnable);
        myThread.start();
    }

    private class CountDownRunner implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    setRunOnUiThread();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }
    private void setRunOnUiThread() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
//                    setTotalTextView();
                }catch (Exception e) {}
            }
        });
    }
}