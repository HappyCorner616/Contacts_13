package com.example.archer.contacts_13;

import com.arellomobile.mvp.MvpView;

public interface IMainActivity extends MvpView {
    void setWaitingMode();
    void desetWaitingMode();
    void startLoginFragment();
    void startListFragment();
    void showError(String error);
    void showToast(String toast);
    void finishExitFromAccount();
}
