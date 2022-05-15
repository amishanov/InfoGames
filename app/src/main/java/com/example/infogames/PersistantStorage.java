package com.example.infogames;

import android.content.Context;
import android.content.SharedPreferences;


public class PersistantStorage {
    public static final String STORAGE_NAME = "StorageName";

    private static SharedPreferences storage = null;
    private static SharedPreferences.Editor editor = null;
    private static Context context = null;

    public static void init(Context cntxt){
        context = cntxt;
    }

    private void init(String storageName){
        storage = context.getSharedPreferences(storageName, Context.MODE_PRIVATE);
        editor = storage.edit();
    }

    public static void addProperty( String name, String value ){
        if( storage == null ){
            init();
        }
        editor.putString( name, value );
        editor.apply();
    }

    public static String getProperty( String name ){
        if( storage == null ){
            init();
        }
        return storage.getString( name, null );
    }
}
