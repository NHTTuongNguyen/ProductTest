package com.example.producttest.Share;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.producttest.model.Cart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferences_Utils {
    public static Context mcontext;

    public static SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefsSetting1111" ;
    public static final String Total = "total_Key";
    public static final String CART_KEY = "Cart_Key";

    public SharedPreferences_Utils(Context context){
        mcontext=context;
        sharedPreferences  = mcontext.getSharedPreferences(MyPREFERENCES,mcontext.MODE_PRIVATE);
    }

    public void setSaveTotal(int saveTotal){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Total,saveTotal);
        editor.commit();
    }
    public int getSaveTotal(){
        int saveTotal = sharedPreferences.getInt(Total,0);
        return saveTotal;
    }

    public void setSaveCartProduct(Context context, int total, String currentTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonStoryWatched = sharedPreferences.getString(CART_KEY, null);
        ArrayList<Cart> chartsModelArrayList = new ArrayList<>();
        if (jsonStoryWatched !=null){
            Type type = new TypeToken<ArrayList<Cart>>(){}.getType();/////luu mang
            chartsModelArrayList = gson.fromJson(jsonStoryWatched,type);
        }
        chartsModelArrayList.add(0,new Cart(total, currentTime));
        String json  =gson.toJson(chartsModelArrayList);
        editor.putString(CART_KEY,json);
        editor.commit();
    }

    public ArrayList<Cart> getSaveCartProduct(Context context) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CART_KEY, null);
        Type type = new TypeToken<ArrayList<Cart>>(){}.getType();
        ArrayList<Cart> chartsList = gson.fromJson(json, type);
        if (chartsList == null) {
            chartsList = new ArrayList<>();
        }
        return chartsList;
    }

    public void removeSaveCartProduct(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CART_KEY);
        editor.commit();
    }
}
