package com.example.producttest.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.producttest.R;
import com.example.producttest.Share.SharedPreferences_Utils;
import com.example.producttest.activity.HomeActivity;
import com.example.producttest.adapter.ProductAdapter;
import com.example.producttest.database.DatabaseHelper;
import com.example.producttest.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductListFragment extends Fragment implements View.OnClickListener {
    private View view;
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
    private EditText edtName, edtPrice, edtDescription,edtCount;
    private Button btnHistory,btnProduct;
    private SharedPreferences_Utils sharedPreferences_utils;
    private MaterialSearchBar materialSearchBar;
    private List<Product> suggesList = new ArrayList<>();
    private   FragmentTransaction fragmentTransaction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view =  inflater.inflate(R.layout.fragment_product_list, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        sharedPreferences_utils = new SharedPreferences_Utils(getActivity());
        initView();
        hasdatainlist();
        searchProduct();
        setTotalTextView();


        return view;
    }


    public void setTotalTextView() {
        int total = totalProduct();
        Locale locale = new Locale("vi","VN");
        NumberFormat fmt =NumberFormat.getCurrencyInstance(locale);
        txtTotalProduct.setText(fmt.format(total));
    }

    private int totalProduct(){
        int total = 0;
        for(Product product : productList)
            total += product.getPrice() * product.getCount();
        return total;

    }
    private void initView() {
        materialSearchBar = view.findViewById(R.id.materialSearchBar);
        search_barView = view.findViewById(R.id.search_barView);
        txtTotalProduct = view.findViewById(R.id.txtTotalProduct);
        recyclerViewProduct = view.findViewById(R.id.recyclerViewProduct);
        txtNoData = view.findViewById(R.id.txtNoData);
        view.findViewById(R.id.txtNoData).setOnClickListener(this);
        btnProduct = view.findViewById(R.id.btnProduct);
        view.findViewById(R.id.btnHistory).setOnClickListener(this);
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
                    sharedPreferences_utils.setSaveCartProduct(getActivity(), total, curTime);
                    databaseHelper.removeAll();

                    ((HomeActivity)getActivity()).getViewPager().setCurrentItem(1);
//                    setFragment(new TestFragment());
//                    Fragment fragment = new HistoryFragment();
//                    getChildFragmentManager().beginTransaction()
//                            .replace(R.id.container, fragment)
//                            .addToBackStack(null)
//                            .commit();

                }else{
                    Toast.makeText(getActivity(), "Your Cart Empty !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setFragment(Fragment fragment) {

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
        productAdapter = new ProductAdapter(getActivity(), productList,ProductListFragment.this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewProduct.setLayoutManager(linearLayoutManager);
        recyclerViewProduct.setHasFixedSize(true);
        recyclerViewProduct.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.selectImageview:
                chooseImage();
                break;
            case R.id.btnHistory:
                diaLogAddProduct();

//                startActivity(new Intent(getActivity(),HistoryActivity.class));
                break;
            case R.id.txtNoData:
                diaLogAddProduct();
            break;
        }
    }
    private void diaLogAddProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        edtCount = viewLayout.findViewById(R.id.edtCount);
        edtDescription = viewLayout.findViewById(R.id.edtDescription);
        Button btnAddProducts = viewLayout.findViewById(R.id.btnAddProduct);
        imageViewAdd =viewLayout.findViewById(R.id.selectImageview);
        btnAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                String price = edtPrice.getText().toString().trim();
                String count = edtCount.getText().toString().trim();
                String des = edtDescription.getText().toString().trim();
                // byte [] image =imageViewToByte(imageViewAdd);
                if(TextUtils.isEmpty(name)) {
                    edtName.setError("Please input name");
                }else if(TextUtils.isEmpty(des)) {
                    edtDescription.setError("Please input description");
                }
                int priceInt = 0;
                if (TextUtils.isEmpty(price)) {
                    edtPrice.setError("Please input price");
                }else {
                    try {
                        priceInt = Integer.parseInt(price);
                    }catch (NumberFormatException e ){
                        edtPrice.setError("Please input again");
                    }
                }
                int countInt = 0;
                if (TextUtils.isEmpty(count)){
                    edtCount.setError("Please input count");
                }else {
                    try {
                        countInt = Integer.parseInt(count);
                    }catch (NumberFormatException e){
                    }
                }
                if (priceInt > 0 && countInt > 0) {
                    Product product = new Product(name, priceInt, countInt, des);
                    if (!containss(product)) {
                        databaseHelper.insertProduct(product);
                        hasdatainlist();
                        setTotalTextView();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Please input name orther", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "number than 0", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean contains(Product product) {
        for(Product i : productList) {
            if(i.getName().equals(product.getName())) {
                return true;
            }
        }
        return false;
    }
    private boolean containss(Product product) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getName().equals(product.getName())) {
                return true;
            }
        }return false;

    }
    private int index (List<Product> productArrayList, String stringname){
        for (int i = 0;i<productArrayList.size();i++){
            if (productArrayList.get(i).getName() == stringname){
                return i;
            }
        }
        return -1;
    }
    private boolean  hasInList(List<Product> productArrayList, String stringname){
        for (int i = 0;i<productArrayList.size();i++){
            if (productArrayList.get(i).getName() == stringname){
                return true;
            }
        }
        return false;
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
                    String des  = product.getDes().toLowerCase();
                    String price = String.valueOf(product.getPrice()).toLowerCase();

                    String result = name +price +des;
                    if (result.contains(newText)){
                        newList.add(product);
                    }
                }
                setAdapter();
                productAdapter.setFilter(newList);


                return true;
            }
        });
    }
}