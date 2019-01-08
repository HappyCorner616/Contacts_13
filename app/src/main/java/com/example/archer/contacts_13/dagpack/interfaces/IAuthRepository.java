package com.example.archer.contacts_13.dagpack.interfaces;

public interface IAuthRepository {
    void registration(String email, String password, IRegistrationRepositoryCallback callback);
    void login(String email, String password, ILoginRepositoryCallback callback);
}
