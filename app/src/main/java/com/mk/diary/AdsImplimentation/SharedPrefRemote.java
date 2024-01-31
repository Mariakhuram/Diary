package com.mk.diary.AdsImplimentation;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefRemote {
    public String remoteValue(String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("CalculatorPrefs", MODE_PRIVATE);
        // Retrieving data
        String id=null;
         id = sharedPreferences.getString(key, "");
             return id;
    }
}