package com.emiliaengberg.sqliteinlamningsuppgift;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Coworker> mCoworkerList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView shiftTextView;
        TextView phoneNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Set up the views for the rows in the RecyclerView
            nameTextView = itemView.findViewById(R.id.insertNamnTextView);
            shiftTextView = itemView.findViewById(R.id.insertShiftTextView);
            phoneNumberTextView = itemView.findViewById(R.id.insertPhoneNumberTextView);
        }
    }

    //Constructor that accepts an array as in parameter
    public RecyclerAdapter(ArrayList<Coworker> coworkerList) {
        mCoworkerList = coworkerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create inflater and viewholder that inflates the view in the viewholder
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.coworker_list_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Binds the data to the viewholder for each row
        Coworker coworker = mCoworkerList.get(position);
        holder.nameTextView.setText(coworker.getName());
        holder.shiftTextView.setText("Skift " + coworker.getShiftNumber());
        holder.phoneNumberTextView.setText(coworker.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        //Total number of rows
        return mCoworkerList.size();
    }
}
