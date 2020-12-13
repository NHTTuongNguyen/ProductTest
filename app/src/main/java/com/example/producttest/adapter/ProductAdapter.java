package com.example.producttest.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.producttest.activity.MainActivity;
import com.example.producttest.R;
import com.example.producttest.activity.ProductDetailActivity;
import com.example.producttest.database.DatabaseHelper;
import com.example.producttest.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolderProduct> {
    Context context;
    List<Product> productList;
    private final  int PICK_IMAGE_REQUEST = 71;
    EditText edtName;
    EditText edtPrice;
    EditText edtDes;
    TextView textViewId;
    AlertDialog alertDialog;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product,parent,false);
        return new ViewHolderProduct(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProduct holder, int position) {
        Product product = productList.get(position);
//        holder.txtId.setText("Id "+product.getId());
        holder.txtName.setText(product.getName());
        Locale locale = new Locale("vi","VN");
        NumberFormat fmt =NumberFormat.getCurrencyInstance(locale);
        holder.txtPrice.setText(fmt.format(product.getPrice()));
        holder.txtDes.setText(product.getDes());
//
//        byte [] imageProduct = product.getImage();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(imageProduct,0,imageProduct.length);
//        holder.imgProduct.setImageBitmap(bitmap);

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setTitle("Delete Cart");
                builder.setMessage("Do you want to delete: "+productList.get(position).getName()+"");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int positions) {
                        removeProduct(position);
                        ((MainActivity)context).setTotalTextView();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();



            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    private int totalProduct(){
        int total = 0;
        for(Product product : productList)
            total+=product.getPrice();
        Log.d("sd",total+"");
        return total;
    }
    public class ViewHolderProduct extends  RecyclerView.ViewHolder  {
        public TextView txtName,txtPrice,txtDes,txtId;
        public ImageView imgProduct,imgDelete;
        public ImageView img_Productss;

        public ViewHolderProduct(@NonNull View itemView) {
            super(itemView);
//            txtId = itemView.findViewById(R.id.txtId);
            img_Productss = itemView.findViewById(R.id.img_Productss);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDes = itemView.findViewById(R.id.txtDes);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgDelete = itemView.findViewById(R.id.delete_cart);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("ProductKey",productList.get(getPosition()));
                    context.startActivity(intent);
                }
            });
            ///
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    CharSequence [] item  = {"Update","Delete"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Choose an action " );
                    builder.setItems(item, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i==0){
                                dialogUpdate(getPosition());
                            }else {
                                removeProduct(getPosition());
                            }
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }


//        @Override
//        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            contextMenu.setHeaderTitle("Select  the action");
//            contextMenu.add(0,view.getId(),0,"UPDATE");
//            contextMenu.add(0,view.getId(),0,"DELETE");
//        }
    }

    private void removeProduct(int position) {
        Product product = productList.get(position);
        String id = product.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.deleteProduct(id);
        ((MainActivity)context).hasdatainlist();
        ((MainActivity)context).setTotalTextView();

    }

    private void dialogUpdate(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.dialog_addproduct,null);
        builder.setView(viewLayout);
        textViewId =viewLayout.findViewById(R.id.edtID);
        edtName =viewLayout.findViewById(R.id.edtName);
        edtPrice= viewLayout.findViewById(R.id.edtPrice);
        edtDes =viewLayout.findViewById(R.id.edtDescription);
        ImageView img= viewLayout.findViewById(R.id.selectImageviewUpdate);
        Button btn =viewLayout.findViewById(R.id.btnAddProduct);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String priceString = edtPrice.getText().toString();
                String des = edtDes.getText().toString();
              if(TextUtils.isEmpty(name)) {
                    edtName.setError("Please input name");
                    return;
                }else if(TextUtils.isEmpty(des)) {
                  edtDes.setError("Please input description");
                    return;
                }
                int priceInteger = 0;
                if (TextUtils.isEmpty(priceString)) {
                    edtPrice.setError("Please input price");
                }else {
                    try {
                        priceInteger = Integer.parseInt(priceString);
                    }catch (NumberFormatException e){
                        edtPrice.setError("Please input is number");
                    }


                }
                ////update
                if (priceInteger>0) {
                    Product product = productList.get(position);
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    databaseHelper.updateProducts(new Product(product.getId(), name, priceInteger, des));
                    ((MainActivity) context).hasdatainlist();
                    ((MainActivity)context).setTotalTextView();
                    alertDialog.dismiss();
                }else {
                    edtPrice.setError("Please input again");

                }
            }
        });
         alertDialog = builder.create();
        alertDialog.show();
    }

    public void setFilter(List<Product>newList){
        productList = new ArrayList<>();
        productList.addAll(newList);
        notifyDataSetChanged();
    }

}
