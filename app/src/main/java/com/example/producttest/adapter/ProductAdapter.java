package com.example.producttest.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.producttest.MainActivity;
import com.example.producttest.R;
import com.example.producttest.activity.ProductDetailActivity;
import com.example.producttest.database.DatabaseHelper;
import com.example.producttest.model.Product;

import java.util.ArrayList;
import java.util.List;

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
        holder.txtName.setText("Name: "+product.getName());
        holder.txtPrice.setText("Price: "+product.getPrice()+" vnđ");
        holder.txtDes.setText("Description: "+product.getDes());
        if (position %2 ==0){
            holder.img_Productss.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_storage_24));

        }else {
            holder.img_Productss.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_rate_review_24));

        }
//        byte [] imageProduct = product.getImage();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(imageProduct,0,imageProduct.length);
//        holder.imgProduct.setImageBitmap(bitmap);

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeProduct(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolderProduct extends  RecyclerView.ViewHolder {
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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                   CharSequence [] item  = {"Update","Delete"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Choose an action");
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


    }

    private void removeProduct(int position) {
        Product product = productList.get(position);
        String id = product.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.deleteProduct(id);
        ((MainActivity)context).hasdatainlist();
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
                String price = edtPrice.getText().toString();
                String des = edtDes.getText().toString();


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
                    edtDes.setError("Please input description");
                    return;
                }
                ////update
                Product product = productList.get(position);
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                databaseHelper.updateProducts(new Product(product.getId(),name,price,des));
                ((MainActivity)context).hasdatainlist();
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
