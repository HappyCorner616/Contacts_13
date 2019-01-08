package com.example.archer.contacts_13.dagpack.modules;

import com.example.archer.contacts_13.dagpack.interfaces.IAuthInteractor;
import com.example.archer.contacts_13.dagpack.interfaces.IAuthRepository;
import com.example.archer.contacts_13.dagpack.interfaces.ILoginInteractorCallback;
import com.example.archer.contacts_13.dagpack.interfaces.ILoginRepositoryCallback;
import com.example.archer.contacts_13.dagpack.interfaces.IRegistrationInteractorCallback;
import com.example.archer.contacts_13.dagpack.interfaces.IRegistrationRepositoryCallback;

public class AuthInteractor implements IAuthInteractor {

    private IAuthRepository authRepository;

    public AuthInteractor(IAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void registration(String email, String password, final IRegistrationInteractorCallback callback) {
        try {
            checkEmail(email);
            checkPassword(password);
        } catch (PasswordException e) {
            callback.failure(e.getMessage());
            return;
        } catch (EmailException e) {
            callback.failure(e.getMessage());
            return;
        }
        authRepository.registration(email, password, new IRegistrationRepositoryCallback() {
            @Override
            public void successful() {
                callback.successful();
            }

            @Override
            public void failure(String error) {
                callback.failure(error);
            }
        });
    }

    @Override
    public void login(String email, String password, final ILoginInteractorCallback callback) {
        authRepository.login(email, password, new ILoginRepositoryCallback() {
            @Override
            public void successful() {
                callback.successful();
            }

            @Override
            public void failure(String error) {
                callback.failure(error);
            }
        });
    }

    private void checkEmail(String email) throws EmailException {
        int length = email.length();
        int atPosition = email.indexOf("@");
        if(atPosition == -1){
            throw new EmailException("email hasn't '@'");
        }
        if(atPosition != email.lastIndexOf("@")){
            throw new EmailException("to much '@");
        }
        if(email.lastIndexOf(".") > length - 3){
            throw new EmailException("wrong email format");
        }
    }

    private void checkPassword(String password) throws PasswordException {
        boolean length = password.length() >= 8;
        boolean symbol = false;
        boolean upperLetter = false;
        boolean lowerLetter = false;
        boolean number = false;
        char[] chars = password.toCharArray();
        for(char c : chars){
            if(!Character.isLetterOrDigit(c)){
                symbol = true;
            }
            if(Character.isUpperCase(c)){
                upperLetter = true;
            }
            if(Character.isLowerCase(c)){
                lowerLetter = true;
            }
            if(Character.isDigit(c)){
                number = true;
            }
        }
        if(!length || !symbol || !upperLetter || !lowerLetter || !number){
            throw new PasswordException("too much easy password");
        }
    }

    private class EmailException extends Exception{
        public EmailException(String message){
            super(message);
        }
    }

    private class PasswordException extends Exception{
        public PasswordException(String message){
            super(message);
        }
    }
}
