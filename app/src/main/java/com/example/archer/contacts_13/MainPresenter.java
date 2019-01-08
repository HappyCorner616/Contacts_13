package com.example.archer.contacts_13;

import android.os.AsyncTask;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.archer.contacts_13.dagpack.interfaces.IAuthInteractor;
import com.example.archer.contacts_13.dagpack.interfaces.ILoginInteractorCallback;
import com.example.archer.contacts_13.dagpack.interfaces.IRegistrationInteractorCallback;
import com.example.archer.contacts_13.dto.Contact;
import com.example.archer.contacts_13.dto.ContactsDto;
import com.example.archer.contacts_13.provider.HttpProvider;
import com.example.archer.contacts_13.provider.HttpProvider_2;
import com.example.archer.contacts_13.provider.StoreProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@InjectViewState
public class MainPresenter extends MvpPresenter<IMainActivity> {

    @Inject
    IAuthInteractor authInteractor;

    public MainPresenter(){
        App.get().authComponent().inject(this);
    }

    public void registration(String email,String password){
        new RegistrationTask(email, password).execute();
    }

    public void login(String email, String password){
        new LoginTask(email, password).execute();
    }

    public void refreshContactsList(ListFragment listFragment){
        new RefreshContactsListTack(listFragment).execute();
    }

    public void exitFromAccount(){
        new ExitFromAccountTask().execute();
    }

    public void clearAllContacts(ListFragment listFragment){
        new ClearAllContactsTask(listFragment).execute();
    }

    public void saveContact(Contact contact, ContactFragment contactFragment){
        new SaveContactTask(contact, contactFragment).execute();
    }

    public void updateContact(Contact contact, ContactFragment contactFragment){
        new UpdateContactTask(contact, contactFragment).execute();
    }

    public void deleteContact(Contact contact, ContactFragment contactFragment){
        new DeleteContactTask(contact, contactFragment).execute();
    }

    public void deleteContacts(List<Contact> contacts, ListFragment listFragment){
        new DeleteContactsTask(contacts, listFragment).execute();
    }

    class RegistrationTask extends AsyncTask<Void, Void, Void>{

        private String email, password;
        private boolean isSuccessful;
        private String res;

        public RegistrationTask(String email, String password) {
            this.email = email;
            this.password = password;
            isSuccessful = true;
        }

        @Override
        protected void onPreExecute() {
            getViewState().setWaitingMode();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            authInteractor.registration(email, password, new IRegistrationInteractorCallback() {
               @Override
               public void successful() {
                   res = "Done";
               }

               @Override
               public void failure(String error) {
                   isSuccessful = false;
                   res = error;
               }
           });
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            getViewState().desetWaitingMode();
            if(isSuccessful){
                getViewState().showToast(res);
            }else{
                getViewState().showError(res);
            }
        }
    }

    class LoginTask extends AsyncTask<Void, Void, Void>{

        private String email, password;
        private boolean isSuccessful;
        private String res;

        public LoginTask(String email, String password) {
            this.email = email;
            this.password = password;
            isSuccessful = true;
        }

        @Override
        protected void onPreExecute() {
            getViewState().setWaitingMode();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            authInteractor.login(email, password, new ILoginInteractorCallback() {
                @Override
                public void successful() {

                }

                @Override
                public void failure(String error) {
                    isSuccessful = false;
                    res = error;
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            getViewState().desetWaitingMode();
            if(isSuccessful){
                getViewState().startListFragment();
            }else{
                getViewState().showError(res);
            }
        }
    }

    class RefreshContactsListTack extends  AsyncTask<Void, Void, String>{

        private ListFragment listFragment;
        private boolean isSuccessful;
        private List<Contact> list;

        public RefreshContactsListTack(ListFragment listFragment) {
            this.listFragment = listFragment;
            isSuccessful = true;
        }

        @Override
        protected void onPreExecute() {
            getViewState().setWaitingMode();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String token = StoreProvider.getInstance().getToken();
                ContactsDto contactsDto = HttpProvider.getInstance().getContacts(token);
                //ContactsDto contactsDto = HttpProvider_2.getInstance().getAllContacts(token);
                list = contactsDto.getContacts();
                return "Done";
            } catch (Exception e) {
                e.printStackTrace();
                return  e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            getViewState().desetWaitingMode();
            if(list == null){
                list = new ArrayList<>();
            }
            if(isSuccessful){
                listFragment.finishRefresList(list);
            }else{
                getViewState().showError(s);
                listFragment.finishRefresList(new ArrayList<Contact>());
            }
        }
    }

    class ExitFromAccountTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            getViewState().setWaitingMode();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StoreProvider.getInstance().clearToken();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getViewState().desetWaitingMode();
            getViewState().finishExitFromAccount();
        }
    }

    class ClearAllContactsTask extends AsyncTask<Void, Void, String>{

        private ListFragment listFragment;
        private boolean isSuccessful;

        public ClearAllContactsTask(ListFragment listFragment) {
            this.listFragment = listFragment;
            isSuccessful = true;
        }

        @Override
        protected void onPreExecute() {
            getViewState().setWaitingMode();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String token = StoreProvider.getInstance().getToken();
            try {
                return HttpProvider.getInstance().clearContacts(token);
            } catch (Exception e) {
                e.printStackTrace();
                isSuccessful = false;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            getViewState().desetWaitingMode();
            if(isSuccessful){
                getViewState().showToast(s);
                listFragment.finishRefresList(new ArrayList<Contact>());
            }else{
                getViewState().showError(s);
            }
        }
    }

    class SaveContactTask extends AsyncTask<Void, Void, String>{

        private Contact contact;
        private ContactFragment contactFragment;
        private boolean isSuccessful;

        public SaveContactTask(Contact contact, ContactFragment contactFragment) {
            this.contact = contact;
            this.contactFragment = contactFragment;
            isSuccessful = true;
        }

        @Override
        protected void onPreExecute() {
            getViewState().setWaitingMode();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String token = StoreProvider.getInstance().getToken();
                contact = HttpProvider.getInstance().addContact(contact, token);
                return "Done";
            } catch (Exception e) {
                e.printStackTrace();
                isSuccessful = false;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            getViewState().desetWaitingMode();
            if(isSuccessful){
                contactFragment.finishSaveContact(contact);
            }else{
                getViewState().showError(s);
                contactFragment.finishSaveContact(null);
            }
        }
    }

    class UpdateContactTask extends AsyncTask<Void, Void, String>{

        private Contact contact;
        private ContactFragment contactFragment;
        private boolean isSuccessful;

        public UpdateContactTask(Contact contact, ContactFragment contactFragment) {
            this.contact = contact;
            this.contactFragment = contactFragment;
            isSuccessful = true;
        }

        @Override
        protected void onPreExecute() {
            getViewState().setWaitingMode();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String token = StoreProvider.getInstance().getToken();
                contact = HttpProvider.getInstance().updateContact(contact, token);
                return "Done";
            } catch (Exception e) {
                e.printStackTrace();
                isSuccessful = false;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            getViewState().desetWaitingMode();
            if(isSuccessful){
                contactFragment.finishUpdateContact(contact);
            }else{
                getViewState().showError(s);
                contactFragment.finishUpdateContact(null);
            }
        }
    }

    class DeleteContactTask extends AsyncTask<Void, Void, String>{

        private Contact contact;
        private ContactFragment contactFragment;
        private boolean isSuccessful;

        public DeleteContactTask(Contact contact, ContactFragment contactFragment) {
            this.contact = contact;
            this.contactFragment = contactFragment;
            isSuccessful = true;
        }

        @Override
        protected void onPreExecute() {
            getViewState().setWaitingMode();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String token = StoreProvider.getInstance().getToken();
                return HttpProvider.getInstance().deleteContact(contact, token);
            } catch (Exception e) {
                e.printStackTrace();
                isSuccessful = false;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            getViewState().desetWaitingMode();
            if(isSuccessful){
                contactFragment.finishDeleteContact(s);
            }else{
                getViewState().showError(s);
                contactFragment.finishUpdateContact(null);
            }
        }
    }

    class DeleteContactsTask extends AsyncTask<Void, Void, String>{

        private List<Contact> contacts;
        private ListFragment listFragment;
        private boolean isSuccessful;

        public DeleteContactsTask(List<Contact> contacts, ListFragment listFragment) {
            this.contacts = contacts;
            this.listFragment = listFragment;
            isSuccessful = true;
        }

        @Override
        protected void onPreExecute() {
            getViewState().setWaitingMode();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String token = StoreProvider.getInstance().getToken();
                for(Contact c : contacts){
                    HttpProvider.getInstance().deleteContact(c, token);
                }
                return "deleted";
            } catch (Exception e) {
                e.printStackTrace();
                isSuccessful = false;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            getViewState().desetWaitingMode();
            if(isSuccessful){
                getViewState().showToast(s);
            }else{
                getViewState().showError(s);
            }
        }
    }


}
