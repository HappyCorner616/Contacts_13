package com.example.archer.contacts_13.provider;

import android.content.Context;

import com.example.archer.contacts_13.dagpack.interfaces.IStore;

public class StoreProvider implements IStore {

    private static StoreProvider instance = null;

    private static final String SP_AUTH = "ST_AUTH";
    private static final String TOKEN_KEY = "TOKEN_KEY";

    private Context context;

    private StoreProvider(){};

    public static StoreProvider getInstance(){
        if(instance == null){
            instance = new StoreProvider();
        }
        return instance;
    }

    @Override
    public void setContext(Context context) {
        instance.context = context;
    }

    public void saveToket(String token){
        context.getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE)
                .edit()
                .putString(TOKEN_KEY, token)
                .apply();
    }

    public String getToken(){
        return context.getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE)
                .getString(TOKEN_KEY, null);
    }

    public void clearToken(){
        context.getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE)
                .edit()
                .remove(TOKEN_KEY)
                .apply();
    }
}
