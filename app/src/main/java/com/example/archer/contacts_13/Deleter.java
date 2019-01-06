package com.example.archer.contacts_13;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.archer.contacts_13.dto.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deleter<T> {

    private static final int DELETE_CODE = 1;

    private Snackbar snackbar;
    private SnackbarListener listener;
    private List<TrashItem<T>> trashBox;
    private Handler handler;

    public Deleter(View container, final SnackbarListener listener){
        this.listener = listener;
        trashBox = new ArrayList<>();
        snackbar = Snackbar.make(container, "", BaseTransientBottomBar.LENGTH_SHORT)
                .setAction("cansel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.snackbarCancelClick();
                    }
                });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == DELETE_CODE){
                    if(trashBox.size() > 0){
                        List<T> forDeleting = new ArrayList<>();
                        for(TrashItem<T> ti : trashBox){
                            forDeleting.add(ti.val);
                        }
                        trashBox.clear();
                        snackbar.dismiss();
                        listener.removeItems(forDeleting);
                    }
                }
                return false;
            }
        });
    }


    public void add(int position, T item){
        trashBox.add(new TrashItem<T>(position, item));
        snackbar.setText("deleted " + trashBox.size());
        if(!snackbar.isShown()){
            snackbar.show();
        }
        handler.removeMessages(DELETE_CODE);
        handler.sendEmptyMessageDelayed(DELETE_CODE, 3000);
    }

    public List<TrashItem<T>> restore(){
        List<TrashItem<T>> restored = new ArrayList<>(trashBox);
        trashBox.clear();
        Collections.reverse(restored);
        return restored;
    }

    public class TrashItem<T>{
        private int position;
        private T val;

        public TrashItem(int position, T val) {
            this.position = position;
            this.val = val;
        }

        public int getPosition() {
            return position;
        }

        public T getVal() {
            return val;
        }
    }
    public interface SnackbarListener<T>{
        void snackbarCancelClick();
        void removeItems(List<T> items);
    }


}
