package com.example.archer.contacts_13.provider;

import com.example.archer.contacts_13.AuthRepsitoryCallback;

public interface IAuthRepository {
    void registration(String email, String password, AuthRepsitoryCallback callback);
}
