package com.example.archer.contacts_13;

import android.app.Application;

import com.example.archer.contacts_13.provider.StoreProvider;

public class App extends Application {

    public static final String MY_TAG = "MY_TAG";
    public static  App app;

    private AppComponent component;

    public static App get(){
        return  app;
    }

    public  App(){
        app = this;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        StoreProvider.getInstance().setContext(this);
        component = DaggerAppComponent.builder()
                .mainModule(new MainModule(this))
                .loginModule(new LoginModule())
                .build();
    }

    public AppComponent component(){
        return component;
    }
}
