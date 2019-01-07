package com.example.archer.contacts_13;

import com.example.archer.contacts_13.provider.Api;
import com.example.archer.contacts_13.provider.IAuthInterractor;
import com.example.archer.contacts_13.provider.IAuthRepository;
import com.example.archer.contacts_13.provider.LoginRepository;
import com.example.archer.contacts_13.provider.Store;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    @Singleton
    IAuthRepository provideAuthRepository(Store store, Api api){
        return new LoginRepository(api, store);
    }

    @Provides
    @Singleton
    IAuthInterractor provideAuthInterractor(IAuthRepository repository){
        return new LoginInterractor(repository);
    }

}
