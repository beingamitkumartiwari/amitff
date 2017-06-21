package com.amtee.friendsfinder;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPref {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;

    private final String DB_NAME = "Multi Window";
    private final String WINDOW_THEME = "windowTheme";
    private final String BAR_SPEED = "barSpeed";
    private final String POLICY_READ = "policyRead";
    private final String ADDMOB = "addmob";
    private final String ADDBUDDIES = "addbuddies";
    private final String ADDBANNER = "addbanner";
    private final String PROMOTEDAPPIMAGE = "promotedappimage";
    private final String PROMOTEDAPPURL = "promotedappurl";
    private final String SHOWBACKADS = "showbackads";
    private final String BACKCAMPAIGNAPPID = "backCampaignAppId";
    public final String PROMOTEDAPPDESC = "promotedappdesc";

    private final String INSTALLEDDATE = "installeddate";
    private final String ISFIRSTTIME = "isfirsttime";

    public final String CHECKFORADS = "checkforads";

    int windowTheme;
    int barSpeed;
    boolean policyRead;

    public MyPref(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(DB_NAME,
                Context.MODE_PRIVATE);
    }

    public int getWindowTheme() {
        return sharedPreferences.getInt(WINDOW_THEME, 3);
    }

    public void setWindowTheme(int windowTheme) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(WINDOW_THEME, windowTheme);
        spEditor.commit();
    }

    public int getBarSpeed() {
        return sharedPreferences.getInt(BAR_SPEED, 2);
    }

    public void setBarSpeed(int barSpeed) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(BAR_SPEED, barSpeed);
        spEditor.commit();
    }

    public boolean isPolicyRead() {
        return sharedPreferences.getBoolean(POLICY_READ, false);
    }

    public void setPolicyRead(boolean policyRead) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(POLICY_READ, policyRead);
        spEditor.commit();
    }

    public String getAddmob() {
        return sharedPreferences.getString(ADDMOB, "ca-app-pub-6210235444865574/8604502047");//"ca-app-pub-3223616616608757/3114139820");
    }

    public void setAddmob(String addmob) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(ADDMOB, addmob);
        spEditor.commit();
    }


    public String getAddbuddies() {
        return sharedPreferences.getString(ADDBUDDIES, "");//"5651fe0d-00b3-422b-81f4-7e26d38853b3");
    }

    public void setAddbuddies(String addbuddies) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(ADDBUDDIES, addbuddies);
        spEditor.commit();
    }


    public String getAddbanner() {
        return sharedPreferences.getString(ADDBANNER, "ca-app-pub-6210235444865574/7127768841");// "ca-app-pub-3223616616608757/1637406624");
    }

    public void setAddbanner(String addbanner) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(ADDBANNER, addbanner);
        spEditor.commit();
    }

    public String getShowbackads() {
        return sharedPreferences.getString(SHOWBACKADS, "");
    }

    public void setShowbackads(String showbackads) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(SHOWBACKADS, showbackads);
        spEditor.commit();
    }

    public String getPromotedappimage() {
        return sharedPreferences.getString(PROMOTEDAPPIMAGE, "http://mpaisa.info/images/bannercallrecorder.png");
    }

    public void setPromotedappimage(String promotedappimage) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(PROMOTEDAPPIMAGE, promotedappimage);
        spEditor.commit();
    }

    public String getPromotedappdesc() {
        return sharedPreferences.getString(PROMOTEDAPPDESC, "For recording your calls,Download the app Now!");

    }

    public void setPromotedappdesc(String promotedappdesc) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(PROMOTEDAPPDESC, promotedappdesc);
        spEditor.commit();
    }

    public String getPromotedappurl() {
        return sharedPreferences.getString(PROMOTEDAPPURL, "https://play.google.com/store/apps/details?id=com.mimi.callrecorder&hl=en");

    }

    public void setPromotedappurl(String promotedappurl) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(PROMOTEDAPPURL, promotedappurl);
        spEditor.commit();
    }

    public String getBackCampaignAppId() {
        return sharedPreferences.getString(BACKCAMPAIGNAPPID, "1");

    }

    public void setBackCampaignAppId(String backCampaignAppId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(BACKCAMPAIGNAPPID, backCampaignAppId);
        spEditor.commit();
    }

    public String getINSTALLEDDATE() {
        return sharedPreferences.getString(INSTALLEDDATE, "date");
    }

    public void setINSTALLEDDATE(String installeddate) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(INSTALLEDDATE, installeddate);
        spEditor.commit();
    }

    public void setCHECKFORADS(boolean date) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(CHECKFORADS, date);
        spEditor.commit();

    }

    public boolean getCHECKFORADS() {
        return sharedPreferences.getBoolean(CHECKFORADS, false);
    }

    public boolean isFirstTime() {
        return sharedPreferences.getBoolean(ISFIRSTTIME, true);
    }

    public void setFirstTime(boolean firstTime) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(ISFIRSTTIME, firstTime);
        spEditor.commit();
    }
}
