package com.amtee.friendsfinder.datastorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amtee.friendsfinder.pojo.NotificationList_Pojo;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Amit Kumar Tiwar on 02/08/16.
 */
public class FriendsFinderDataBase extends SQLiteOpenHelper {

    public static final String FRIENDSFINDER_DATABASE = "friendsfinderdatabase.db";
    public static final String FRIENDSFINDER_TABLE_NAME = "alerts";
    public static final String GCM_MESSAGE = "message";
    public static final String ALERT_TIME_STAMP = "time_stamp";
    public static final String MSG_READ_STATUS = "read_status";

    public FriendsFinderDataBase(Context context) {super(context, FRIENDSFINDER_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table alerts " + "(message TEXT,time_stamp INT8,read_status TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS alerts");
        onCreate(db);

    }

    public boolean deleteAllNotification()
    {
        boolean isDeleted = false;
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(FRIENDSFINDER_TABLE_NAME, null, null);
            db.close();
            isDeleted=true;
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
        return isDeleted;

    }

    public void insertNewMessage(String msg, long timeStamp, String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GCM_MESSAGE, msg);
        contentValues.put(ALERT_TIME_STAMP, timeStamp);
        contentValues.put(MSG_READ_STATUS, status);
        db.insert(FRIENDSFINDER_TABLE_NAME,null,contentValues);
        db.close();
    }

    public int getMessageCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int numberofrows = (int) DatabaseUtils.queryNumEntries(db, FRIENDSFINDER_TABLE_NAME);
        return numberofrows;
    }

    public void updateMessage(String msg, long timeStamp, String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GCM_MESSAGE, msg);
        contentValues.put(ALERT_TIME_STAMP, timeStamp);
        contentValues.put(MSG_READ_STATUS, status);
        db.update(FRIENDSFINDER_TABLE_NAME, contentValues, "time_stamp=(SELECT MIN(time_stamp) From alerts)", null);
        db.close();
    }

    public void updateMessageReadStatus()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MSG_READ_STATUS, "read");
        db.update(FRIENDSFINDER_TABLE_NAME, contentValues, null, null);
        db.close();
    }

    //    public ArrayList<String> getAlertMessages() {
//        ArrayList<String> arrayList = new ArrayList<>();
//        String selectetionQuery = "SELECT * FROM " + FRS_TABLE_NAME;
//        SQLiteDatabase database = this.getReadableDatabase();
//        Cursor cursor = database.rawQuery(selectetionQuery, null);
//        cursor.moveToFirst();
//        while (cursor.isAfterLast() == false) {
//            arrayList.add(cursor.getString(cursor.getColumnIndex(GCM_MESSAGE)));
//            cursor.moveToNext();
//        }
//        database.close();
//        return arrayList;
//    }

    public ArrayList<NotificationList_Pojo> getNotificationData() {
        ArrayList<NotificationList_Pojo> listOfData = new ArrayList<NotificationList_Pojo>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from alerts", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            NotificationList_Pojo notificationList_pojo = new NotificationList_Pojo();
            notificationList_pojo.setMessage(res.getString(res.getColumnIndex(GCM_MESSAGE)));
            notificationList_pojo.setMsgReadStatus(res.getString(res.getColumnIndex(MSG_READ_STATUS)));
            notificationList_pojo.setTiming(String.valueOf(res.getInt(res.getColumnIndex(ALERT_TIME_STAMP))));
            listOfData.add(notificationList_pojo);
            res.moveToNext();
        }
        db.close();
        Collections.reverse(listOfData);
        return listOfData;
    }

    public int unreadMessagesCount() {
        int count = 0;
        String selectetionQuery = "SELECT * FROM " + FRIENDSFINDER_TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectetionQuery, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            if (cursor.getString(cursor.getColumnIndex(MSG_READ_STATUS)).equals("unread")) {
                count++;
            }
            cursor.moveToNext();
        }
        database.close();
        return count;
    }
}
