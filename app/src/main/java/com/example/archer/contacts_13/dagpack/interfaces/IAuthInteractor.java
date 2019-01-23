package com.example.archer.contacts_13.dagpack.interfaces;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface IAuthInteractor {
    Completable registration(String email, String password) throws Exception;
    void login(String email, String password);
}
