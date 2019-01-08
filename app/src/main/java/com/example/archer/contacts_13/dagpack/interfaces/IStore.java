package com.example.archer.contacts_13.dagpack.interfaces;

import android.content.Context;

public interface IStore {

    void setContext(Context context);
    void saveToket(String token);
    String getToken();
    void clearToken();

}
