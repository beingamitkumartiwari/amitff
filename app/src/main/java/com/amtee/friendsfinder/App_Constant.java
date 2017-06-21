package com.amtee.friendsfinder;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by Amit Kumar Tiwar on 25/07/16.
 */
public class App_Constant {

    //this is sender id which is used here as project id
    //public static final String GOOGLE_PROJECT_ID = "276953562068";
    public static final String GOOGLE_PROJECT_ID = "879766485045";

    public static String get_IMEI_Number(Context context)
    {
        String identifier = null;
        TelephonyManager telephonyManager = null;
        if(context!=null){
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        if (telephonyManager != null)
            identifier = telephonyManager.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println("jigra " + identifier);
        return identifier;

    }

}
