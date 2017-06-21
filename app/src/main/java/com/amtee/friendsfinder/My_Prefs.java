package com.amtee.friendsfinder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Amit Kumar Tiwar on 29/07/16.
 */
public class My_Prefs {

    private final String DATABASE_NAME = "FriendFinderdb";

    private final String USER_REGISTERED_GCM_ID = "user_resistered_gcm_id";
    private final String REGISERED_GCM_ID_SAVED_IN_SERVER = "registered_gcm_saved_in_server";
    private final String REQUEST_STATUS = "request_status";
    SharedPreferences.Editor sp_editor;
    SharedPreferences sp;


    public My_Prefs(Context context)
    {
        sp = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
    }

    public String getUSER_REGISTERED_GCM_ID() {
        return sp.getString(USER_REGISTERED_GCM_ID, "");
    }

    public void setUSER_REGISTERED_GCM_ID(String user_resistered_gcm_id)
    {
        sp_editor = sp.edit();
        sp_editor.putString(USER_REGISTERED_GCM_ID, user_resistered_gcm_id);
        sp_editor.commit();
    }

    public boolean isREGISERED_GCM_ID_SAVED_IN_SERVER() {
        return sp.getBoolean(REGISERED_GCM_ID_SAVED_IN_SERVER, false);
    }

    public void setREGISERED_GCM_ID_SAVED_IN_SERVER(boolean registered_gcm_saved_in_server)
    {
        sp_editor = sp.edit();
        sp_editor.putBoolean(REGISERED_GCM_ID_SAVED_IN_SERVER, registered_gcm_saved_in_server);
        sp_editor.commit();
    }

    public String getREQUEST_STATUS() {
        return sp.getString(REQUEST_STATUS, "");
    }

    public void setREQUEST_STATUS(String request_status)
    {
        sp_editor = sp.edit();
        sp_editor.putString(REQUEST_STATUS, request_status);
        sp_editor.commit();
    }
}
