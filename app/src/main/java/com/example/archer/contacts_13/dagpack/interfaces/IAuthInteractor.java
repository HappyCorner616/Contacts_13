package com.example.archer.contacts_13.dagpack.interfaces;

public interface IAuthInteractor {
    void registration(String email, String password, IRegistrationInteractorCallback callback);
    void login(String email, String password, ILoginInteractorCallback callback);
}
