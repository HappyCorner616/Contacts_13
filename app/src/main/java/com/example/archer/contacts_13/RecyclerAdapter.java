package com.example.archer.contacts_13;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.archer.contacts_13.dto.Contact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Contact> list;
    private ContactCellListener listener;

    public RecyclerAdapter(){
        list = new ArrayList<>();
    }

    public RecyclerAdapter(List<Contact> list) {
        this.list = list;
    }

    public void setListener(ContactCellListener listener) {
        this.listener = listener;
    }

    public Contact getItem(int position){
        return list.get(position);
    }

    public Contact remove(int position){
        Contact contact = list.remove(position);
        notifyDataSetChanged();
        return  contact;
    }

    public void add(int position, Contact contact){
        list.add(position, contact);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == 0){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_cell_odd, viewGroup, false);
            return new OddContactViewHolder(view);
        }else{
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_cell, viewGroup, false);
            return new ContactViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Contact c = list.get(i);
        if(i % 2 == 0){
            ((OddContactViewHolder)viewHolder).nameTxt.setText(c.getName());
            ((OddContactViewHolder)viewHolder).phoneTxt.setText(c.getPhone());
        }else{
            ((ContactViewHolder)viewHolder).nameTxt.setText(c.getName());
            ((ContactViewHolder)viewHolder).phoneTxt.setText(c.getPhone());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{

        TextView nameTxt;
        TextView phoneTxt;

        public ContactViewHolder(@NonNull final View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.name_txt);
            phoneTxt = itemView.findViewById(R.id.phone_txt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onCellClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    class OddContactViewHolder extends RecyclerView.ViewHolder{

        TextView nameTxt;
        TextView phoneTxt;

        public OddContactViewHolder(@NonNull final View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.name_txt);
            phoneTxt = itemView.findViewById(R.id.phone_txt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onCellClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface ContactCellListener{
        void onCellClick(int position);
    }

}
