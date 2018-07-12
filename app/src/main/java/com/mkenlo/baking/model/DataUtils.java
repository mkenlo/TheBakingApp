package com.mkenlo.baking.model;

import com.google.gson.Gson;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static com.mkenlo.baking.R.raw.baking;

public class DataUtils {

    Context mContext;
    List<Recipe> mData;

    public DataUtils(Context context) {
        mContext = context;
        setData();
    }


    private void setData(){
        String json;
        mData = null;
        try {

            InputStream is = mContext.getResources().openRawResource(baking);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            mData = new ArrayList<>(Arrays.asList(gson.fromJson(json, Recipe[].class)));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<Recipe> getData(){
        return mData;
    }
}
