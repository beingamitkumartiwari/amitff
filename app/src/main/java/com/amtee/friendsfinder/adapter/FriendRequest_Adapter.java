package com.amtee.friendsfinder.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.amtee.friendsfinder.R;
import com.amtee.friendsfinder.interfaces.OnRecyclerItemClickListener;
import com.amtee.friendsfinder.pojo.FriendRequestDetail_Pojo;

import java.util.ArrayList;

/**
 * Created by Amit Kumar Tiwar on 06/08/16.
 */
public class FriendRequest_Adapter extends BaseAdapter {


    Context context;
    ArrayList<FriendRequestDetail_Pojo> rowitem;
    OnRecyclerItemClickListener itemClickListener;

    public FriendRequest_Adapter(Context context, ArrayList<FriendRequestDetail_Pojo> rowitem, OnRecyclerItemClickListener itemClickListener) {
        this.context = context;
        this.rowitem = rowitem;
        this.itemClickListener=itemClickListener;
    }

    @Override
    public int getCount() {
        return rowitem.size();
    }

    @Override
    public Object getItem(int position) {
        return rowitem.size();
    }

    @Override
    public long getItemId(int position) {
        return rowitem.indexOf(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        System.out.println("dell " + rowitem.size());
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.friend_request_layout, null);
            MyFriendRequestViewHolder myFriendRequestViewHolder = new MyFriendRequestViewHolder(convertView);
            myFriendRequestViewHolder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            myFriendRequestViewHolder.btn_accept = (Button) convertView.findViewById(R.id.btn_accept);
            convertView.setTag(myFriendRequestViewHolder);
        }

        MyFriendRequestViewHolder holder = (MyFriendRequestViewHolder) convertView.getTag();
//        FriendRequestDetail_Pojo friendRequestDetail_pojo = rowitem.get(position);
        System.out.println("radhe1 " + rowitem.get(position).getSenderuserName());
        holder.tv_username.setText(rowitem.get(position).getSenderuserName());
        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(position);

                //((Activity) context).finish();

            }
        });
        return convertView;
    }


    private class MyFriendRequestViewHolder {
        TextView tv_username;
        Button btn_accept;

        public MyFriendRequestViewHolder(View items) {
            tv_username = (TextView) items.findViewById(R.id.tv_username);
            btn_accept = (Button) items.findViewById(R.id.btn_accept);
        }
    }



}
