package com.example.archer.contacts_13;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.archer.contacts_13.dto.Contact;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import butterknife.Unbinder;

public class ContactFragment extends Fragment {

    public static final int MODE_ADD = 1;
    public static final int MODE_INFO = 2;

    @BindView(R.id.waiting_bar) ProgressBar waitingBar;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.info_wrapper) LinearLayout infoWrapper;
    @BindView(R.id.name_info) TextView nameInfo;
    @BindView(R.id.last_name_info) TextView lastNameInfo;
    @BindView(R.id.email_info) TextView emailInfo;
    @BindView(R.id.phone_info) TextView phoneInfo;
    @BindView(R.id.address_info) TextView addressInfo;
    @BindView(R.id.description_info) TextView descriptionInfo;
    @BindView(R.id.id_info) TextView idInfo;

    @BindView(R.id.edit_wrapper) LinearLayout editWrapper;
    @BindView(R.id.name_edit) TextView nameEdit;
    @BindView(R.id.last_name_edit) TextView lastNameEdit;
    @BindView(R.id.email_edit) TextView emailEdit;
    @BindView(R.id.phone_edit) TextView phoneEdit;
    @BindView(R.id.address_edit) TextView addressEdit;
    @BindView(R.id.description_edit) TextView descriptionEdit;

    MenuItem itemDone, itemEdit, itemDelete;

    private Unbinder unbinder;
    private Contact contact;
    private int mode;
    private Fragment previousFragment;
    private ContactFragmentListener listener;

    private ContactFragment thisFragment(){
        return this;
    }

    public static ContactFragment newInstance(Contact contact, int mode){
        ContactFragment contactFragment = new ContactFragment();
        contactFragment.contact = contact;
        contactFragment.mode = mode;
        return contactFragment;
    }

    public static ContactFragment newInstance(Contact contact, int mode, Fragment previousFragment){
        ContactFragment contactFragment = new ContactFragment();
        contactFragment.contact = contact;
        contactFragment.mode = mode;
        contactFragment.previousFragment = previousFragment;
        return contactFragment;
    }

    public void setListener(ContactFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_edit_info_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        if(mode == MODE_ADD){
            fragmentEditMode();
        }else{
            fragmentInfoMode();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_menu, menu);
        itemDone = menu.findItem(R.id.item_done);
        itemDelete = menu.findItem(R.id.item_delete);
        itemEdit = menu.findItem(R.id.item_edit);
        if(mode == MODE_ADD){
            menuEditMode();
        }else{
            manuInfoMode();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mode == MODE_ADD){
            if(item.getItemId() == R.id.item_done){
                saveChanges();
                if(listener != null){
                    listener.saveContact(contact, this);
                }else{
                    setInfoMode();
                }
            }
        }else if(mode == MODE_INFO){
            if(item.getItemId() == R.id.item_edit){
                setEditMode();
            }else if(item.getItemId() == R.id.item_done){
                saveChanges();
                if(listener != null){
                    listener.updateContact(contact, this);
                }else{
                    Toast.makeText(getContext(), "Not updated", Toast.LENGTH_SHORT).show();
                    setInfoMode();
                }
            }else if(item.getItemId() == R.id.item_delete){
                new AlertDialog.Builder(getContext()).setTitle("Delete contact")
                        .setMessage("Are you sure?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(listener != null){
                                    listener.deleteContact(contact, thisFragment());
                                }else{
                                    Toast.makeText(getContext(), "Not deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).create().show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    private void saveChanges(){
        contact.setName(nameEdit.getText().toString());
        contact.setLastName(lastNameEdit.getText().toString());
        contact.setEmail(emailEdit.getText().toString());
        contact.setPhone(phoneEdit.getText().toString());
        contact.setAddress(addressEdit.getText().toString());
        contact.setDescription(descriptionEdit.getText().toString());
    }

    public void setEditMode(){
        fragmentEditMode();
        menuEditMode();
    }

    public void setInfoMode(){
        fragmentInfoMode();
        manuInfoMode();
    }

    public void fragmentEditMode(){
        nameEdit.setText(contact.getName());
        lastNameEdit.setText(contact.getLastName());
        emailEdit.setText(contact.getEmail());
        phoneEdit.setText(contact.getPhone());
        addressEdit.setText(contact.getAddress());
        descriptionEdit.setText(contact.getDescription());

        infoWrapper.setVisibility(View.GONE);
        editWrapper.setVisibility(View.VISIBLE);
    }

    public void fragmentInfoMode(){
        nameInfo.setText(contact.getName());
        lastNameInfo.setText(contact.getLastName());
        emailInfo.setText(contact.getEmail());
        phoneInfo.setText(contact.getPhone());
        addressInfo.setText(contact.getAddress());
        descriptionInfo.setText(contact.getDescription());
        idInfo.setText(String.valueOf(contact.getId()));

        infoWrapper.setVisibility(View.VISIBLE);
        editWrapper.setVisibility(View.GONE);
    }

    private void menuEditMode(){
        itemDone.setVisible(true);
        itemEdit.setVisible(false);
        itemDelete.setVisible(mode != MODE_ADD);
    }

    private void manuInfoMode(){
        itemDone.setVisible(false);
        itemEdit.setVisible(true);
        itemDelete.setVisible(false);
    }


    public void finishSaveContact(Contact contact){
        if(contact != null){
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            this.contact = contact;
            if(listener != null){
                listener.closeContactFragment(this, previousFragment);
            }
        }
    }

    public void finishUpdateContact(Contact contact){
        if(contact != null){
            Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            this.contact = contact;
            setInfoMode();
        }
    }

    public void finishDeleteContact(String status){
       if(status != null){
           Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
           if(listener != null){
               listener.closeContactFragment(this, previousFragment);
           }
       }
    }

    public interface ContactFragmentListener{
        void closeContactFragment(ContactFragment contactFragment, Fragment previousFragment);
        void saveContact(Contact contact, ContactFragment contactFragment);
        void updateContact(Contact contact, ContactFragment contactFragment);
        void deleteContact(Contact contact, ContactFragment contactFragment);
    }
}
