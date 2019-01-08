package com.example.archer.contacts_13.dagpack.components;

import com.example.archer.contacts_13.MainActivity;
import com.example.archer.contacts_13.MainPresenter;
import com.example.archer.contacts_13.dagpack.modules.AuthModule;
import com.example.archer.contacts_13.dagpack.modules.AuthScope;

import dagger.Subcomponent;

@Subcomponent(modules ={AuthModule.class})
@AuthScope
public interface AuthComponent {
    void inject(MainPresenter presenter);
}
