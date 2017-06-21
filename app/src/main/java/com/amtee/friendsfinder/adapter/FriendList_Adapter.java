package com.amtee.friendsfinder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amtee.friendsfinder.App_Constant;
import com.amtee.friendsfinder.FriendList_Activity;
import com.amtee.friendsfinder.R;
import com.amtee.friendsfinder.pojo.Delete_Friend_Pojo;
import com.amtee.friendsfinder.pojo.FriendListDetail_Pojo;
import com.amtee.friendsfinder.retofitwork.Rest_Interface;
import com.amtee.friendsfinder.retofitwork.Service_Generator;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Amit Kumar Tiwar on 12/08/16.
 */
public class FriendList_Adapter extends BaseAdapter {

    Context context;
    ArrayList<FriendListDetail_Pojo> rowitem;
    Call<Delete_Friend_Pojo> deleteFriend;
    Rest_Interface ri_deleteFriend;

    public FriendList_Adapter(Context context, ArrayList<FriendListDetail_Pojo> rowitem) {
        this.context = context;
        this.rowitem = rowitem;
    }

    @Override
    public int getCount() {return rowitem.size();}

    @Override
    public Object getItem(int position) {return rowitem.size();}

    @Override
    public long getItemId(int position) {return rowitem.indexOf(position);}

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.friend_list_layout, null);
            MyFriendListViewHolder myFriendListViewHolder = new MyFriendListViewHolder(convertView);
            myFriendListViewHolder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            myFriendListViewHolder.tv_phonenumber = (TextView) convertView.findViewById(R.id.tv_phonenumber);
            myFriendListViewHolder.btn_deleteFriend = (ImageButton) convertView.findViewById(R.id.btn_deleteFriend);
            convertView.setTag(myFriendListViewHolder);
        }


        MyFriendListViewHolder holder = (MyFriendListViewHolder) convertView.getTag();
        holder.tv_username.setText(rowitem.get(position).getSenderuserName());
        holder.tv_phonenumber.setText(rowitem.get(position).getSenderphoneNo());
        holder.btn_deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ri_deleteFriend = Service_Generator.createService(Rest_Interface.class, Rest_Interface.BASE_URL);
                DeleteFriend(position);

                Intent i2 = new Intent(context.getApplicationContext(), FriendList_Activity.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i2);
            }
        });

        return convertView;
    }


    private class MyFriendListViewHolder {
        TextView tv_username;
        TextView tv_phonenumber;
        ImageButton btn_deleteFriend;

        public MyFriendListViewHolder(View items) {
            tv_username = (TextView) items.findViewById(R.id.tv_username);
            tv_phonenumber = (TextView) items.findViewById(R.id.tv_phonenumber);
            btn_deleteFriend = (ImageButton) items.findViewById(R.id.btn_deleteFriend);
        }
    }


    public void DeleteFriend(int position) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("deviceId", App_Constant.get_IMEI_Number(context));
        jsonObject.addProperty("acptdeviceId", rowitem.get(position).getSenderDeviceid());
        deleteFriend = ri_deleteFriend.getdeleteFriend(jsonObject);
        deleteFriend.enqueue(new Callback<Delete_Friend_Pojo>() {
            @Override
            public void onResponse(Response<Delete_Friend_Pojo> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    System.out.println("google " + response.body().getResponseCode());
                    Delete_Friend_Pojo deleteFriendPojo = response.body();
                    if (deleteFriendPojo.getResponseCode().equals("1")) {

                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
