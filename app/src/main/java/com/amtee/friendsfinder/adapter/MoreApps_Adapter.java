package com.amtee.friendsfinder.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amtee.friendsfinder.R;
import com.amtee.friendsfinder.pojo.FriendRequestDetail_Pojo;
import com.amtee.friendsfinder.pojo.MoreApps_Pojo;
import com.amtee.friendsfinder.pojo.OfferList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Amit on 27-08-2016.
 */
public class MoreApps_Adapter extends BaseAdapter {

    Context context;
    ArrayList<OfferList> rowitem;

    public MoreApps_Adapter(Context context, ArrayList<OfferList> rowitem) {
        this.context = context;
        this.rowitem = rowitem;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.moreapps_layout, null);
            MoreAppsViewHolder moreAppsViewHolder = new MoreAppsViewHolder(convertView);
            moreAppsViewHolder.appname = (TextView) convertView.findViewById(R.id.appname);
            moreAppsViewHolder.appdescription = (TextView) convertView.findViewById(R.id.appdescription);
            moreAppsViewHolder.imgurl = (ImageView) convertView.findViewById(R.id.imgurl);
            moreAppsViewHolder.btndecription = (Button) convertView.findViewById(R.id.btndecription);
            convertView.setTag(moreAppsViewHolder);
        }
        OfferList offerList = rowitem.get(position);
        MoreAppsViewHolder holder = (MoreAppsViewHolder) convertView.getTag();
        holder.appname.setText(offerList.getAppName());
        holder.appdescription.setText(offerList.getAppDescription());
        holder.btndecription.setText(offerList.getButtonDescription());
        Picasso.with(context)
                .load(offerList.getImgageUrl())
                .placeholder(R.mipmap.ic_boy)
                .error(R.mipmap.ic_girl)
                .into(holder.imgurl);
        return convertView;
    }


    private class MoreAppsViewHolder {
        ImageView imgurl;
        TextView appname, appdescription;
        Button btndecription;

        public MoreAppsViewHolder(View items) {
            appname = (TextView) items.findViewById(R.id.appname);
            appdescription = (TextView) items.findViewById(R.id.appdescription);

            imgurl = (ImageView) items.findViewById(R.id.imgurl);
            btndecription = (Button) items.findViewById(R.id.btndecription);
        }
    }
}
