package com.digywood.cineauditions.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.cineauditions.Fragments.ItemsFragment;
import com.digywood.cineauditions.Fragments.ListOfAdvtsFragment;
import com.digywood.cineauditions.Pojo.SingleInterest;
import com.digywood.cineauditions.R;
import com.digywood.cineauditions.ViewAdvtInfo;

import java.util.ArrayList;

/**
 * Created by Shashank on 22-01-2018.
 */

public class InterestsBaseAdapter extends BaseAdapter {

    private static final String TAG = "INTERESTS_BASE_ADAPTER";

    ArrayList<SingleInterest> Interestlist;

    Context context;

    public InterestsBaseAdapter(Context c, ArrayList<SingleInterest> Interestlist ) {
        context = c;
        this.Interestlist = Interestlist;
    }

    @Override
    public int getCount() {
        return Interestlist.size();
    }

    @Override
    public Object getItem(int i) {
        return Interestlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

//        ItemsFragment.ContactsBaseAdapter.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.popup_item, parent, false);
        final ViewHolder holder = new ViewHolder();
        holder.name = (TextView) rowView.findViewById(R.id.interest_name);
        holder.email = (TextView) rowView.findViewById(R.id.interest_email);
        holder.contact = (TextView) rowView.findViewById(R.id.interest_contact);
        holder.comment = (TextView) rowView.findViewById(R.id.interest_comment);

        int []_intAdvtlist = new int[Interestlist.size()];
        //Log.d("BrochuresInfoList.size", "comes:" + _intAdvtlist.length);
        for (int x = 0; x < _intAdvtlist.length; x++) {

            /*holder.name.setText(Interestlist.get(i).getName());
            holder.email.setText(Interestlist.get(i).getEmailId());
            holder.contact.setText(Interestlist.get(i).getContactNumber());*/
            holder.comment.setText(Interestlist.get(i).getComment());

        }
        final int final_i = i;

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Direct to user MyFolio
            }
        });
        return rowView;
    }

    public class ViewHolder
    {
        public TextView name,email,contact,comment;
    }
}
