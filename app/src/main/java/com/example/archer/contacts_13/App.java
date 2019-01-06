package com.example.archer.contacts_13;

import android.app.Application;

import com.example.archer.contacts_13.provider.StoreProvider;

public class App extends Application {

    public static final String MY_TAG = "MY_TAG";

    @Override
    public void onCreate() {
        super.onCreate();
        StoreProvider.getInstance().setContext(this);
    }
}
