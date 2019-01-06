package com.example.archer.contacts_13;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.archer.contacts_13.dto.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;


public class ListFragment extends Fragment implements RecyclerAdapter.ContactCellListener, Deleter.SnackbarListener<Contact> {

    @BindView(R.id.contact_list) RecyclerView contactList;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Unbinder unbinder;
    private ListFragmentListener listener;
    private Deleter<Contact> deleter;

    public ListFragment() {
        // Required empty public constructor
    }

    private ListFragment thisFragment(){
        return this;
    }

    public void setListener(ListFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        contactList.setLayoutManager(manager);
        //RecyclerAdapter adapter = new RecyclerAdapter(new ArrayList<Contact>());
        //adapter.setListener(this);
        //contactList.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ListFragmentTouchHelper());
        helper.attachToRecyclerView(contactList);

        refreshList();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.item_add){
            if(listener != null){
                listener.startContactFragment(new Contact(), ContactFragment.MODE_ADD, this);
            }
        }else if(item.getItemId() == R.id.item_refresh){
            if(listener != null){
                listener.refreshList(this);
            }
        }else if(item.getItemId() == R.id.item_clearAll){
            new AlertDialog.Builder(getContext()).setTitle("Clear all contacts")
                    .setMessage("Are you sure?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(listener != null){
                                listener.clearAllContacts(thisFragment());
                            }
                        }
                    }).create().show();
        }else if(item.getItemId() == R.id.item_exit){
            if(listener != null){
                listener.exitFromAccount(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    public void refreshList(){
        if(listener != null){
            listener.refreshList(this);
        }else{
            finishRefresList(new ArrayList<Contact>());
        }
    }

    public void finishRefresList(List<Contact> list){
        RecyclerAdapter adapter = new RecyclerAdapter(list);
        adapter.setListener(this);
        contactList.setAdapter(adapter);
        deleter = new Deleter<>(contactList, this);

    }

    @Override
    public void onCellClick(int position) {
        Contact contact = ((RecyclerAdapter)contactList.getAdapter()).getItem(position);
        if(listener != null){
            listener.startContactFragment(contact, ContactFragment.MODE_INFO, this);
        }
    }

    @Override
    public void snackbarCancelClick() {
        List<Deleter<Contact>.TrashItem<Contact>> restored = deleter.restore();
        RecyclerAdapter adapter = (RecyclerAdapter) contactList.getAdapter();
        for(Deleter<Contact>.TrashItem<Contact> ti : restored){
            adapter.add(ti.getPosition(), ti.getVal());
        }
    }

    @Override
    public void removeItems(List<Contact> items) {
        if(listener != null){
            listener.deleteContacts(items, this);
        }
    }

    class ListFragmentTouchHelper extends ItemTouchHelper.Callback{

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            int pos = viewHolder.getAdapterPosition();
            deleter.add(pos, ((RecyclerAdapter)contactList.getAdapter()).remove(pos));
        }
    }

    public interface ListFragmentListener{
        void refreshList(ListFragment listFragment);
        void startContactFragment(Contact contact, int mode, Fragment previousFragment);
        void exitFromAccount(ListFragment listFragment);
        void clearAllContacts(ListFragment listFragment);
        void deleteContacts(List<Contact> contacts, ListFragment listFragment);
    }
}
