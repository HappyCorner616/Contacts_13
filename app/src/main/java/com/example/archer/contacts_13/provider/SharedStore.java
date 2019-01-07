package com.example.archer.contacts_13.provider;

import android.content.Context;

public class SharedStore implements Store {

    private static final String SP_AUTH = "ST_AUTH";
    private static final String TOKEN_KEY = "TOKEN_KEY";

    private Context context;

    public SharedStore(Context context) {
        this.context = context;
    }

    @Override
    public void saveToken(String token) {
        context.getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE)
                .edit()
                .putString(TOKEN_KEY, token)
                .apply();
    }

    @Override
    public String getToken() {
        return context.getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE)
                .getString(TOKEN_KEY, null);
    }

    @Override
    public void clearToken() {
        context.getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE)
                .edit()
                .remove(TOKEN_KEY)
                .apply();
    }
}
