package com.example.archer.contacts_13.dagpack.modules;

import android.content.Context;

import com.example.archer.contacts_13.dagpack.interfaces.IStore;
import com.example.archer.contacts_13.provider.Api;
import com.example.archer.contacts_13.provider.StoreProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MainModule {

    private static final String BASE_URL_GRISHA = "http://contacts-telran.herokuapp.com/";
    private static final String BASE_URL_ME = "http://192.168.1.11:8080/ForAndroid/";

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
    IStore provideStore(Context context){
        IStore store = StoreProvider.getInstance();
        store.setContext(context);
        return store;
    }

    @Provides
    @Singleton
    OkHttpClient provideClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    Api provideApi(OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_ME)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Api.class);
    }
}
