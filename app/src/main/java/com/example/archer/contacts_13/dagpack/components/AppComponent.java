package com.example.archer.contacts_13.dagpack.components;

import com.example.archer.contacts_13.dagpack.modules.AuthModule;
import com.example.archer.contacts_13.dagpack.modules.MainModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {MainModule.class })
@Singleton
public interface AppComponent {
    AuthComponent plus(AuthModule module);
}
