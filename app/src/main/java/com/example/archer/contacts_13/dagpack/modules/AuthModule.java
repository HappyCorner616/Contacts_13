package com.example.archer.contacts_13.dagpack.modules;

import com.example.archer.contacts_13.dagpack.interfaces.IAuthInteractor;
import com.example.archer.contacts_13.dagpack.interfaces.IAuthRepository;
import com.example.archer.contacts_13.dagpack.interfaces.IStore;
import com.example.archer.contacts_13.provider.Api;

import dagger.Module;
import dagger.Provides;

@Module
public class AuthModule {

    @Provides
    @AuthScope
    IAuthRepository provideAuthRepository(Api api, IStore store){
        return new AuthRepository(api, store);
    }

    @Provides
    @AuthScope
    IAuthInteractor provideAuthInteractor(IAuthRepository authRepository){
        return new AuthInteractor(authRepository);
    }


}
