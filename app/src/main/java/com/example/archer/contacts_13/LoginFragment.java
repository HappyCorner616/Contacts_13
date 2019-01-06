package com.example.archer.contacts_13;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginFragment extends Fragment {

    @BindView(R.id.login_input) EditText loginInput;
    @BindView(R.id.password_input) EditText passwordInput;
    @BindView(R.id.register_btn) Button regBtn;
    @BindView(R.id.login_btn) Button loginBtn;

    private LoginFragmentListener listener;
    private Unbinder unbinder;

    public void setListener(LoginFragmentListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @OnClick(R.id.register_btn)
    void regBtnClick(){
        if(listener != null){
            Log.d("MY_TAG", "regBtnClick: ");
            listener.regBtnClick(loginInput.getText().toString(), passwordInput.getText().toString());
        }
    }

    @OnClick(R.id.login_btn)
    void loginBtnClick(){
        if(listener != null) {
            listener.loginBtnClick(loginInput.getText().toString(), passwordInput.getText().toString());
        }
    }

    public interface LoginFragmentListener{
        void regBtnClick(String email, String password);
        void loginBtnClick(String email, String password);
    }
}
