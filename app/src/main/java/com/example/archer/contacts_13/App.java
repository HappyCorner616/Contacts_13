package com.example.archer.contacts_13;

import android.app.Application;

import com.example.archer.contacts_13.dagpack.components.AppComponent;
import com.example.archer.contacts_13.dagpack.components.AuthComponent;
import com.example.archer.contacts_13.dagpack.components.DaggerAppComponent;
import com.example.archer.contacts_13.dagpack.modules.AuthModule;
import com.example.archer.contacts_13.dagpack.modules.MainModule;
import com.example.archer.contacts_13.provider.StoreProvider;

public class App extends Application {

    public static final String MY_TAG = "MY_TAG";
    public static  App app;

    private AppComponent mainComponent;
    private AuthComponent authComponent;

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
        mainComponent = DaggerAppComponent.builder()
                .mainModule(new MainModule(this))
                .build();

    }

    public AuthComponent authComponent(){
        if(authComponent == null){
            authComponent = mainComponent.plus(new AuthModule());
        }
        return authComponent;
    }


}
