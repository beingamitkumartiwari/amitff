package com.amtee.friendsfinder;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.amtee.friendsfinder.pojo.AlreadyResistered_Pojo;
import com.amtee.friendsfinder.retofitwork.Rest_Interface;
import com.amtee.friendsfinder.retofitwork.Service_Generator;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Amit Kumar Tiwar on 22/07/16.
 */
public class Splash_Activity extends AppCompatActivity {

    //    ProgressBar progressBar;
    AnimatedCircleLoadingView animatedCircleLoadingView;
    int progress = 0;
    Handler handler = new Handler();

    Rest_Interface RI_checkAlreadyResistered;
    // for permission check
    public static int WRITE_PERMISSION_REQ_CODE = 1234;
    final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.amnimatedprogressBar);

        RI_checkAlreadyResistered = Service_Generator.createService(Rest_Interface.class, Rest_Interface.BASE_URL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck();
        } else {
            checkAlreadyResistered();
        }

    }


    /////
// this used for checking user status for resisterd or not
    /////

    public void checkAlreadyResistered() {
        startLoading();
        //startPercentMockThread();

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("deviceId", App_Constant.get_IMEI_Number(Splash_Activity.this));
        Call<AlreadyResistered_Pojo> call = RI_checkAlreadyResistered.getAlreadyResister(jsonObject);
        call.enqueue(new Callback<AlreadyResistered_Pojo>() {
            @Override
            public void onResponse(Response<AlreadyResistered_Pojo> response, Retrofit retrofit) {
                System.out.println("Ashu page ");
                if (response.isSuccess()) {

                    AlreadyResistered_Pojo responseCode = response.body();
                    System.out.println("Amit page " + responseCode.getResponseCode());
                    if (responseCode.getResponseCode().equals("1")) {
                        System.out.println("Amit page " + responseCode.getResponseCode());

//                        Runnable runnable = new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Thread.sleep(11500);
//                                    for (progress = 0; progress <= 100; progress++) {
//                                        Thread.sleep(65);
//                                        changePercent(progress);
//                                        if (progress == 100) {
//                                            animatedCircleLoadingView.stopOk();
//                                        }
//
//                                        if (progress == 100) {
//                                            Thread.sleep(11000);
                        Intent intent = new Intent(Splash_Activity.this, Main_Activity.class);
                        startActivity(intent);
                        finish();
//                                        }
//                                    }
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        };
//                        new Thread(runnable).start();

                    }

                    if (responseCode.getResponseCode().equals("0")) {
//                        System.out.println("Amit page " + responseCode.getResponseCode());
//                        // resistration work
//                        Runnable runnable = new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Thread.sleep(4500);
//                                    for (progress = 0; progress <= 100; progress++) {
//                                        Thread.sleep(25);
//                                        changePercent(progress);
//                                        if (progress == 100) {
//                                            animatedCircleLoadingView.stopOk();
//                                        }
//
//                                        if (progress == 100) {
//                                            Thread.sleep(6000);
                        Intent intent = new Intent(Splash_Activity.this, Resistration_Activity.class);
                        startActivity(intent);
                        finish();
//                                        }
//                                    }
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        };
//                        new Thread(runnable).start();
//
//
//
                    } else if (responseCode.getResponseCode().equals("3")) {
                        // try again latter
                        animatedCircleLoadingView.stopFailure();
                    }

                }

            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Ashu page1 ");

                animatedCircleLoadingView.stopFailure();

            }
        });
    }


    //////
// this method is used for checking permission for marshmallow
    //////

    @TargetApi(Build.VERSION_CODES.M)
    private void permissionCheck() {


        int location_Permission = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
        int currentloc_Permission = checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int writeexternal = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readexternal = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int readphonestate = checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
        int readcontac = checkSelfPermission(android.Manifest.permission.READ_CONTACTS);


        List<String> permissions = new ArrayList<String>();


        if (location_Permission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (currentloc_Permission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (writeexternal != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readexternal != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (readphonestate != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (readcontac != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.READ_CONTACTS);
        }


        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
        } else {
            checkAlreadyResistered();

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SOME_FEATURES_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                                && checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                            if (i == permissions.length - 1) {

                                checkAlreadyResistered();
                            }
                        }
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WRITE_PERMISSION_REQ_CODE) {
            if (!Settings.System.canWrite(this)) {
                Toast.makeText(getApplicationContext(), "Permission Not Granted", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //////////////////////
    /////
    /////////////////////

    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    private void startPercentMockThread() {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1500);
//                    for (progress = 0; progress <= 100; progress++) {
//                        Thread.sleep(65);
//                        changePercent(progress);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        new Thread(runnable).start();
    }

    private void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
            }
        });
    }

//    public void resetLoading() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                animatedCircleLoadingView.resetLoading();
//            }
//        });
//    }

}



