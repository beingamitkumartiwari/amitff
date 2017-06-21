package com.amtee.friendsfinder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Amit Kumar Tiwar on 22/07/16.
 */
public class PrefrenceManagerForFirsttime
{
    SharedPreferences sp;
    SharedPreferences.Editor speditor;
    Context _context;

    //Sharedpref mode
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "WelcomeManager";
    private static final String IS_FIRST_TIME_LAUNCH = "isfirsttimelaunch";


    public PrefrenceManagerForFirsttime(Context context)
    {
        this._context=context;
        sp =_context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        speditor = sp.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime)
    {
        speditor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        speditor.commit();
    }

    public boolean isFirstTimeLaunch()
    {
        return sp.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
