package com.example.archer.contacts_13;

import com.example.archer.contacts_13.provider.IAuthInterractor;
import com.example.archer.contacts_13.provider.IAuthRepository;

public class LoginInterractor implements IAuthInterractor {

    private IAuthRepository repository;

    public LoginInterractor(IAuthRepository repository) {
        this.repository = repository;
    }

    @Override
    public void registration(String email, String password, final AuthInterractorCallback callback) {
        if(!email.contains("@") || password.length() < 0){
            throw new IllegalArgumentException("Wrong email or password!");
        }
        repository.registration(email, password, new AuthRepsitoryCallback() {
            @Override
            public void registrationSuccess() {
                callback.registratioSuccess();
            }

            @Override
            public void registrationFailure(String error) {
                callback.registrationFailure(error);
            }
        });
    }
}
