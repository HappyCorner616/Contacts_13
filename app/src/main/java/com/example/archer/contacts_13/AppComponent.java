package com.example.archer.contacts_13;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {MainModule.class, LoginModule.class})
@Singleton
public interface AppComponent {

    void inject(MainActivity activity);

}
