package com.amtee.friendsfinder.retofitwork;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.Window;

/**
 * Created by Amit Kumar Tiwar on 25/07/16.
 */
public class Connection_Check {

    public Connection_Check(Context applicationContext) {
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((localConnectivityManager.getActiveNetworkInfo() == null)
                || (!localConnectivityManager.getActiveNetworkInfo().isAvailable())
                || (!localConnectivityManager.getActiveNetworkInfo().isConnected())
                )

            return false;
        else
            return true;

    }

    public static void alertDialog(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_DeviceDefault_Light_Dialog));
        alertDialogBuilder.setTitle("Internet unavailable");
        alertDialogBuilder.setMessage("Check your Internet connection and try again.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        alertDialog.show();
    }

}
