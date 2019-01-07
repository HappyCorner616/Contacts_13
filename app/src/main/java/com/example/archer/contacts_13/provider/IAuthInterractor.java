package com.example.archer.contacts_13.provider;

import com.example.archer.contacts_13.AuthInterractorCallback;

public interface IAuthInterractor {
    void registration(String email, String password, AuthInterractorCallback callback);
}
