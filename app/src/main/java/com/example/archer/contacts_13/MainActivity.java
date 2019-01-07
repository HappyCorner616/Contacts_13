package com.example.archer.contacts_13;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.archer.contacts_13.dto.Contact;
import com.example.archer.contacts_13.provider.Api;
import com.example.archer.contacts_13.provider.IAuthInterractor;
import com.example.archer.contacts_13.provider.IAuthRepository;
import com.example.archer.contacts_13.provider.LoginRepository;
import com.example.archer.contacts_13.provider.SharedStore;
import com.example.archer.contacts_13.provider.Store;
import com.example.archer.contacts_13.provider.StoreProvider;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends MvpAppCompatActivity implements IMainActivity, LoginFragment.LoginFragmentListener, ListFragment.ListFragmentListener, ContactFragment.ContactFragmentListener {

    @BindView(R.id.container) FrameLayout container;
    @BindView(R.id.waiting_layout) FrameLayout waitingLayout;

    @InjectPresenter
    MainPresenter presenter;

    @Inject
    IAuthInterractor authInterractor;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.get().component().inject(this);

        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

        /*Store store = new SharedStore(this);
        OkHttpClient client = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://contacts-telran.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        IAuthRepository authRepository = new LoginRepository(api, store);
        authInterractor = new LoginInterractor(authRepository);*/


        if(StoreProvider.getInstance().getToken() != null){
            startListFragment();
        }else{
            startLoginFragment();
        }

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void setWaitingMode() {
        waitingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void desetWaitingMode() {
        waitingLayout.setVisibility(View.GONE);
    }

    @Override
    public void startLoginFragment(){
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, loginFragment)
                .commit();
    }

    @Override
    public void startListFragment(){
        ListFragment listFragment = new ListFragment();
        listFragment.setListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, listFragment)
                .commit();
    }

    @Override
    public void startContactFragment(Contact contact, int mode, Fragment previousFragment) {
        ContactFragment contactFragment = ContactFragment.newInstance(contact, mode, previousFragment);
        contactFragment.setListener(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(previousFragment != null){transaction.detach(previousFragment);}
        transaction.add(R.id.container, contactFragment)
                .addToBackStack("CONTACT_FRAGMENT")
                .commit();

    }

    @Override
    public void showError(String error) {
        new AlertDialog.Builder(this).setTitle("error")
                .setMessage(error)
                .setPositiveButton("OK", null)
                .create().show();
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishExitFromAccount() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, loginFragment)
                .commit();
    }

    // LOGIN
    @Override
    public void regBtnClick(String email, String password) {
        //presenter.registration(email, password);
        setWaitingMode();
        try{
            authInterractor.registration(email, password, new AuthInterractorCallback() {
                @Override
                public void registratioSuccess() {
                    desetWaitingMode();
                    showToast("Done");
                }

                @Override
                public void registrationFailure(String error) {
                    desetWaitingMode();
                    showError(error);
                }
            });
        }catch (IllegalArgumentException e){
            showError(e.getMessage());
            desetWaitingMode();
        }
    }

    @Override
    public void loginBtnClick(String email, String password) {
        presenter.login(email, password);
    }

    //LIST
    @Override
    public void refreshList(ListFragment listFragment) {
        presenter.refreshContactsList(listFragment);
    }

    @Override
    public void exitFromAccount(ListFragment listFragment) {
        presenter.exitFromAccount();
    }

    @Override
    public void clearAllContacts(ListFragment listFragment) {
        presenter.clearAllContacts(listFragment);
    }

    @Override
    public void deleteContacts(List<Contact> contacts, ListFragment listFragment) {
        presenter.deleteContacts(contacts, listFragment);
    }

    //CONTACT
    @Override
    public void closeContactFragment(ContactFragment contactFragment, Fragment previousFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction = transaction.detach(contactFragment).remove(contactFragment);
        if(previousFragment != null){
            transaction = transaction.attach(previousFragment);
        }
        transaction.commit();
        if(previousFragment instanceof ListFragment){
            ((ListFragment)previousFragment).refreshList();
        }
    }

    @Override
    public void saveContact(Contact contact, ContactFragment contactFragment) {
        presenter.saveContact(contact, contactFragment);
    }

    @Override
    public void updateContact(Contact contact, ContactFragment contactFragment) {
        presenter.updateContact(contact, contactFragment);
    }

    @Override
    public void deleteContact(Contact contact, ContactFragment contactFragment) {
        presenter.deleteContact(contact, contactFragment);
    }
}
