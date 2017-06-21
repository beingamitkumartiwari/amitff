package com.amtee.friendsfinder.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amtee.friendsfinder.R;
import com.amtee.friendsfinder.pojo.ContactPojo;

import java.util.ArrayList;
import java.util.Locale;

public class ContactsDetailsAdapter extends BaseAdapter {
    private ArrayList<ContactPojo> data;
    private ArrayList<ContactPojo> arraylist;
    private static LayoutInflater inflater = null;
    Context context;

    public ContactsDetailsAdapter(Context context, ArrayList<ContactPojo> data) {
        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arraylist = new ArrayList<ContactPojo>();
        this.arraylist.addAll(data);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.contact_details_layout, null);

        TextView txtName = (TextView) view.findViewById(R.id.name);
        TextView txtPhNum = (TextView) view.findViewById(R.id.ph_num);
        ImageView contactPic = (ImageView) view.findViewById(R.id.contact_picture);

        final ContactPojo appItem = data.get(position);

        txtName.setText(appItem.getName());
        txtPhNum.setText(appItem.getPhNumber());
        if (appItem.getPhotoUri() != null) {
            contactPic.setImageURI(appItem.getPhotoUri());
        } else {
            contactPic.setImageDrawable(context.getResources().getDrawable(R.drawable.profile));
        }

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(arraylist);
        } else {
            for (ContactPojo cp : arraylist) {
                if (cp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    data.add(cp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
