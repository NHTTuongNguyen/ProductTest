package com.example.producttest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.producttest.model.Product;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String PRODUCT = "PRODUCT";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DES = "des";
    public static final String COLUMN_COUNT = "count";

    public static final String COLUMN_image = "image";
    private Context mContext;
    private static final String tablesqlite = "Product.sqlite";
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DatabaseHelper(Context context) {
        super(context, tablesqlite, null, 1);
        this.mContext = context;
        this.mDatabase = mDatabase;

    }


    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(PRODUCT, null, null);
    }
//    public void insertData (String name,String price,String des, byte [] image){
//        SQLiteDatabase database = getWritableDatabase();
//        String sql = "INSERT INTO "+PRODUCT+" VALUES (NULL,?,?,?,?)";
//        SQLiteStatement sqLiteStatement = database.compileStatement(sql);
//        sqLiteStatement.clearBindings();
//        sqLiteStatement.bindString(1,name);
//        sqLiteStatement.bindString(2,price);
//        sqLiteStatement.bindString(3,des);
//        sqLiteStatement.bindBlob(4,image);
//        sqLiteStatement.executeInsert();
//
//    }
    public long insertProduct(Product product){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,product.getName());
        contentValues.put(COLUMN_PRICE,product.getPrice());
        contentValues.put(COLUMN_COUNT,product.getCount());
        contentValues.put(COLUMN_DES,product.getDes());
//        contentValues.put(COLUMN_image,product.getImage());
        long result = sqLiteDatabase.insert(PRODUCT,null,contentValues);
        return result;
    }
//    public long updateProduct(Product product){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COLUMN_ID,product.getId());
//        contentValues.put(COLUMN_NAME,product.getName());
//        contentValues.put(COLUMN_PRICE,product.getPrice());
//        contentValues.put(COLUMN_DES,product.getDes());
//        long result = sqLiteDatabase.update(PRODUCT,contentValues,
//                COLUMN_ID+"=?",new String[]{product.getId()});
//        return result;
//    }
    public void updateProducts(Product product){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(COLUMN_ID,product.getId());
        contentValues.put(COLUMN_NAME,product.getName());
        contentValues.put(COLUMN_PRICE,product.getPrice());
        contentValues.put(COLUMN_COUNT,product.getCount());
        contentValues.put(COLUMN_DES,product.getDes());
       sqLiteDatabase.update(PRODUCT,contentValues,COLUMN_ID+"=?",new String[]{product.id});

    }
    public long deleteProduct(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(PRODUCT,COLUMN_ID+"=?",new String[]{id});
    }
    public List<Product> getAllProduct(){
        List<Product> productArrayList = new ArrayList<>();
        String SELECT = "SELECT * FROM " + PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT,null);
        if (cursor.moveToFirst()){
            do {
                Product product = new Product();
                product.setId(cursor.getString(0));
                product.setName(cursor.getString(1));
                product.setPrice(cursor.getInt(2));
                product.setCount(cursor.getInt(3));
                product.setDes(cursor.getString(4));
//                product.setImage(cursor.getBlob(4));
                productArrayList.add(product);
            }while (cursor.moveToNext());
        }
        db.close();
        return productArrayList;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE =
                "CREATE TABLE "+PRODUCT+"("+COLUMN_ID+
                        " INTEGER PRIMARY KEY," +
                        "" + COLUMN_NAME+" VARCHAR," +
                        "" + COLUMN_PRICE+" INTEGER," +
                        "" + COLUMN_COUNT+" INTEGER," +
                        "" + COLUMN_DES+" VARCHAR," +
                        "" + COLUMN_image+" BLOG)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
        Log.d("CREATE_TABLE",CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
