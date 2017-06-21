package com.amtee.friendsfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amtee.friendsfinder.R;

/**
 * Created by Amit Kumar Tiwar on 22/08/16.
 */
public class NotificationViewHolder extends RecyclerView.ViewHolder {

    protected TextView notificationMsg;
    protected ImageView unreadStatus;
    protected ImageView notificationIcon;

    public NotificationViewHolder(View itemView) {
        super(itemView);

        notificationMsg= (TextView) itemView.findViewById(R.id.notificationMsg);
        unreadStatus= (ImageView) itemView.findViewById(R.id.unreadMsg);
        notificationIcon= (ImageView) itemView.findViewById(R.id.notificationIcon);
    }
}
