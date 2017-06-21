package com.amtee.friendsfinder.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amtee.friendsfinder.R;
import com.amtee.friendsfinder.pojo.NotificationList_Pojo;

import java.util.ArrayList;

/**
 * Created by Amit Kumar Tiwar on 22/08/16.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private Context context;
    private ArrayList<NotificationList_Pojo> notificationList;

    public NotificationAdapter(Context context, ArrayList<NotificationList_Pojo> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView;
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationviewholder_layout, parent, false);
        NotificationViewHolder notificationViewHolder = new NotificationViewHolder(convertView);
        return notificationViewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {

        NotificationList_Pojo notificationList_pojo = notificationList.get(position);
        holder.notificationMsg.setText(notificationList_pojo.getMessage());
        if(notificationList_pojo.getMsgReadStatus().equals("unread")){
            Drawable notificationDrawableIcon=context.getResources().getDrawable(R.drawable.friendlist);
            notificationDrawableIcon.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            holder.notificationIcon.setImageDrawable(notificationDrawableIcon);
            holder.unreadStatus.setVisibility(View.VISIBLE);
        }else{
            Drawable notificationDrawableIcon=context.getResources().getDrawable(R.drawable.friendlist);
            notificationDrawableIcon.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            holder.notificationIcon.setImageDrawable(notificationDrawableIcon);
            holder.unreadStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != notificationList ? notificationList.size() : 0);
    }
}
