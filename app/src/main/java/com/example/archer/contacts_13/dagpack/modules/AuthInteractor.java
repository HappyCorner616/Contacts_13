package com.example.archer.contacts_13.dagpack.modules;

import com.example.archer.contacts_13.dagpack.interfaces.IAuthInteractor;
import com.example.archer.contacts_13.dagpack.interfaces.IAuthRepository;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class AuthInteractor implements IAuthInteractor {

    private IAuthRepository authRepository;

    public AuthInteractor(IAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Completable registration(String email, String password) throws Exception {
        try {
            checkEmail(email);
            checkPassword(password);
        } catch (PasswordException | EmailException e) {
            return Completable.error(e);
        }

        return authRepository.registration(email, password);
    }

    @Override
    public void login(String email, String password) {
        authRepository.login(email, password);
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
