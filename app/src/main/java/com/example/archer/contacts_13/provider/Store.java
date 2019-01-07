package com.example.archer.contacts_13.provider;

import android.content.Context;

public interface Store {
    void setContext(Context context);
    void saveToken(String token);
    String getToken();
    void clearToken();
}
