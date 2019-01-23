package com.example.archer.contacts_13.dagpack.interfaces;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface IAuthRepository {
    Completable registration(String email, String password);
    void login(String email, String password);
}
