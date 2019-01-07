package com.example.archer.contacts_13;

import android.content.Context;

import com.example.archer.contacts_13.provider.Api;
import com.example.archer.contacts_13.provider.SharedStore;
import com.example.archer.contacts_13.provider.Store;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MainModule {

    private Context context;

    public MainModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return context;
    }

    @Provides
    @Singleton
    Store provideStore(Context context){
        return new SharedStore(context);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    Api provideApi(OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl("http://contacts-telran.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Api.class);
    }

}
