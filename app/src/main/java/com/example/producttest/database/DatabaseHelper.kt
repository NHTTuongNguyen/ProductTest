package com.example.producttest.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.producttest.model.Product
import java.util.*

class DatabaseHelper : SQLiteOpenHelper {
    private val PRODUCT = "PRODUCT"
    private var mContext: Context? = null
    private var mDatabase: SQLiteDatabase? = null

    constructor(context: Context?, name: String?, factory: CursorFactory?, version: Int) : super(context, name, factory, version) {}
    constructor(context: Context?) : super(context, tablesqlite, null, 1) {
        mContext = context
        mDatabase = mDatabase
    }

    fun removeAll() {
        val db = this.writableDatabase // helper is object extends SQLiteOpenHelper
        db.delete(PRODUCT, null, null)
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
    fun insertProduct(product: Product): Long {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, product.name)
        contentValues.put(COLUMN_PRICE, product.price)
        contentValues.put(COLUMN_COUNT, product.count)
        contentValues.put(COLUMN_DES, product.des)
        //        contentValues.put(COLUMN_image,product.getImage());
        return sqLiteDatabase.insert(PRODUCT, null, contentValues)
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
    fun updateProducts(product: Product) {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        //        contentValues.put(COLUMN_ID,product.getId());
        contentValues.put(COLUMN_NAME, product.name)
        contentValues.put(COLUMN_PRICE, product.price)
        contentValues.put(COLUMN_COUNT, product.count)
        contentValues.put(COLUMN_DES, product.des)
        sqLiteDatabase.update(PRODUCT, contentValues, COLUMN_ID + "=?", arrayOf(product.id))
    }

    fun deleteProduct(id: String): Long {
        val sqLiteDatabase = this.writableDatabase
        return sqLiteDatabase.delete(PRODUCT, COLUMN_ID + "=?", arrayOf(id)).toLong()
    }

    //                product.setImage(cursor.getBlob(4));
    val allProduct: MutableList<Product>
        get() {
            val productArrayList: MutableList<Product> = ArrayList()
            val SELECT = "SELECT * FROM $PRODUCT"
            val db = this.readableDatabase
            val cursor = db.rawQuery(SELECT, null)
            if (cursor.moveToFirst()) {
                do {
                    val product = Product()
                    product.id = cursor.getString(0)
                    product.name = cursor.getString(1)
                    product.price = cursor.getInt(2)
                    product.count = cursor.getInt(3)
                    product.des = cursor.getString(4)
                    //                product.setImage(cursor.getBlob(4));
                    productArrayList.add(product)
                } while (cursor.moveToNext())
            }
            db.close()
            return productArrayList
        }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE " + PRODUCT + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY," +
                "" + COLUMN_NAME + " VARCHAR," +
                "" + COLUMN_PRICE + " INTEGER," +
                "" + COLUMN_COUNT + " INTEGER," +
                "" + COLUMN_DES + " VARCHAR," +
                "" + COLUMN_image + " BLOG)"
        sqLiteDatabase.execSQL(CREATE_TABLE)
        Log.d("CREATE_TABLE", CREATE_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}

    companion object {
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_DES = "des"
        const val COLUMN_COUNT = "count"
        const val COLUMN_image = "image"
        private const val tablesqlite = "Product.sqlite"
    }
}