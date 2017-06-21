package com.amtee.friendsfinder;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amtee.friendsfinder.pojo.FriendSearchDetails_Pojo;
import com.amtee.friendsfinder.pojo.FriendSearch_Pojo;
import com.amtee.friendsfinder.pojo.SendFriendRequest_Pojo;
import com.amtee.friendsfinder.retofitwork.Rest_Interface;
import com.amtee.friendsfinder.retofitwork.Service_Generator;
import com.amtee.friendsfinder.voolyfile.ApplicationFL;
import com.amtee.friendsfinder.voolyfile.ConnectionCheck;
import com.amtee.friendsfinder.voolyfile.CustomJsonObjectRequest;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.appnext.ads.interstitial.Interstitial;
import com.appnext.core.callbacks.OnAdClicked;
import com.appnext.core.callbacks.OnAdClosed;
import com.appnext.core.callbacks.OnAdError;
import com.appnext.core.callbacks.OnAdLoaded;
import com.appnext.core.callbacks.OnAdOpened;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.JsonObject;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Amit Kumar Tiwar on 28/07/16.
 */
public class FriendSearch_Activity extends AppCompatActivity implements View.OnClickListener {

    Rest_Interface ri_verification;
    Call<FriendSearch_Pojo> search_friend_pojoCall;
    Call<SendFriendRequest_Pojo> send_friend_request_pojoCall;

    EditText et_number_search;
    Button search_btn;
    Button conatact_btn;
    String username_search, usermobilenumber_search;

    LinearLayout ll_search_reasult;
    Button frient_request_btn;
    TextView tv_searchname, tv_searchmobilenumber, tv_searchgender;
    String search_deviceId;

    TextView tv_requestsend;
    int PHONE_NUM_REQUEST_CODE = 27;

    Interstitial interstitial_Ad;
    //  ads content

    MyPref myprefs;
    LinearLayout myAppLayoutl, progressBar;
    Dialog myDialog;
    InterstitialAd mInterstitialAd, interstitialAd1;
    AdRequest adRequest, adRequest1;
    String urlJsonArray = "http://www.mpaisa.info/MtechAppsPromotion.asmx/appsAppId?appName=AirCallAnswer";
    Handler addMobHandler = new Handler();
    Handler handler = new Handler();
    AlertDialog alertDialog;
    View view1;
    ImageView iv_promotedappimage;
    RelativeLayout rl_promotionback;
    TextView tv_promotedappdesc;
    Button bt_yes;
    Button bt_no;
    String playstorelink;
    String backcampid;
    LinearLayout myAdd;
    SimpleDateFormat simpleDateFormat;
    boolean check = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        interstitial_Ad = new Interstitial(this, "6a4424cd-0e36-49a5-a353-c03c4caa0420");
        interstitial_Ad.loadAd();
        interstitial_Ad.showAd();
        interstitial_Ad.setOnAdLoadedCallback(new OnAdLoaded() {
            @Override
            public void adLoaded() {
//                interstitial_Ad.showAd();
            }
        });
// Get callback for ad opened
        interstitial_Ad.setOnAdOpenedCallback(new OnAdOpened() {
            @Override
            public void adOpened() {

            }
        });
// Get callback for ad clicked
        interstitial_Ad.setOnAdClickedCallback(new OnAdClicked() {
            @Override
            public void adClicked() {

            }
        });
// Get callback for ad closed
        interstitial_Ad.setOnAdClosedCallback(new OnAdClosed() {
            @Override
            public void onAdClosed() {
                System.out.println("dineshclosed");
                interstitial_Ad.loadAd();
            }
        });
// Get callback for ad error
        interstitial_Ad.setOnAdErrorCallback(new OnAdError() {
            @Override
            public void adError(String error) {

            }
        });
        ri_verification = Service_Generator.createService(Rest_Interface.class, Rest_Interface.BASE_URL);
        myprefs=new MyPref(this);
        //et_name_search = (EditText) findViewById(R.id.et_name_search);
        et_number_search = (EditText) findViewById(R.id.et_number_search);

        search_btn = (Button) findViewById(R.id.search_btn);
        conatact_btn = (Button) findViewById(R.id.conatact_btn);

        search_btn.setOnClickListener(this);
        conatact_btn.setOnClickListener(this);

        ll_search_reasult = (LinearLayout) findViewById(R.id.ll_search_reasult);
        tv_searchname = (TextView) findViewById(R.id.tv_searchname);
        tv_searchmobilenumber = (TextView) findViewById(R.id.tv_searchmobilenumber);
        tv_searchgender = (TextView) findViewById(R.id.tv_searchgender);

        frient_request_btn = (Button) findViewById(R.id.frient_request_btn);
        frient_request_btn.setOnClickListener(this);

        tv_requestsend = (TextView) findViewById(R.id.tv_requestsend);
//        connectionCheck();

//        adMobBannerAd();


    }
    Runnable mShowFullPageAdTask = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    System.out.println("dineshrun");
                    interstitial_Ad.loadAd();
                    if(interstitial_Ad.isAdLoaded())
                        interstitial_Ad.showAd();
                    interstitial_Ad.setOnAdLoadedCallback(new OnAdLoaded() {
                        @Override
                        public void adLoaded() {
//                            System.out.println("dineshshow");
                            if(interstitial_Ad.isAdLoaded())
                                interstitial_Ad.showAd();
                        }
                    });
// Get callback for ad opened
                    interstitial_Ad.setOnAdOpenedCallback(new OnAdOpened() {
                        @Override
                        public void adOpened() {

                        }
                    });
// Get callback for ad clicked
                    interstitial_Ad.setOnAdClickedCallback(new OnAdClicked() {
                        @Override
                        public void adClicked() {

                        }
                    });
// Get callback for ad closed
                    interstitial_Ad.setOnAdClosedCallback(new OnAdClosed() {
                        @Override
                        public void onAdClosed() {
                        }
                    });
// Get callback for ad error
                    interstitial_Ad.setOnAdErrorCallback(new OnAdError() {
                        @Override
                        public void adError(String error) {

                        }
                    });
                }
            });
        }
    };

    @Override
    public void onClick(View view) {

        if (view == search_btn) {
            //username_search = et_name_search.getText().toString();
            usermobilenumber_search = et_number_search.getText().toString();

            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("deviceId", App_Constant.get_IMEI_Number(FriendSearch_Activity.this));
            jsonObject.addProperty("Name", username_search);
            jsonObject.addProperty("PhoneNo", usermobilenumber_search);

            search_friend_pojoCall = ri_verification.getSearchFriend(jsonObject);
            search_friend_pojoCall.enqueue(new Callback<FriendSearch_Pojo>() {
                @Override
                public void onResponse(Response<FriendSearch_Pojo> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        if (response.body().getSearchDetails().size() > 0) {
//                            adMobFullPageAd();
                            FriendSearch_Pojo friend_Search_pojo = response.body();
                            FriendSearchDetails_Pojo friend_search_details_pojo = new FriendSearchDetails_Pojo();

                            for (int i = 0; i < friend_Search_pojo.getSearchDetails().size(); i++) {
                                friend_search_details_pojo.setPhoneNo(friend_Search_pojo.getSearchDetails().get(i).getPhoneNo());
                                friend_search_details_pojo.setResponseCode(friend_Search_pojo.getSearchDetails().get(i).getResponseCode());
                                friend_search_details_pojo.setSearchDeviceId(friend_Search_pojo.getSearchDetails().get(i).getSearchDeviceId());
                                friend_search_details_pojo.setSex(friend_Search_pojo.getSearchDetails().get(i).getSex());
                                friend_search_details_pojo.setUserName(friend_Search_pojo.getSearchDetails().get(i).getUserName());
                            }

                            ll_search_reasult.setVisibility(View.VISIBLE);
                            tv_searchname.setText(friend_search_details_pojo.getUserName());
                            tv_searchmobilenumber.setText(friend_search_details_pojo.getPhoneNo());
                            tv_searchgender.setText(friend_search_details_pojo.getSex());
                            search_deviceId = friend_search_details_pojo.getSearchDeviceId();

                        } else {
                            Toast.makeText(getApplicationContext(), "No Result Found", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }


        if (view == frient_request_btn) {
            if (App_Constant.get_IMEI_Number(this).equals(search_deviceId)) {
                Toast.makeText(getApplicationContext(), "This is your profile", Toast.LENGTH_LONG).show();
            } else {


                final JsonObject jsonObject1 = new JsonObject();
                jsonObject1.addProperty("deviceId", App_Constant.get_IMEI_Number(this));
                jsonObject1.addProperty("searchdeviceId", search_deviceId);

                send_friend_request_pojoCall = ri_verification.getSendFriendRequest(jsonObject1);
                send_friend_request_pojoCall.enqueue(new Callback<SendFriendRequest_Pojo>() {
                    @Override
                    public void onResponse(Response<SendFriendRequest_Pojo> response, Retrofit retrofit) {

                        if (response.isSuccess()) {
                            if (response.body().getResponseCode().equals("1")) {
                                Toast.makeText(getApplicationContext(), "Friend Request Send", Toast.LENGTH_LONG).show();
                                //frient_request_btn.setVisibility(View.GONE);
                                tv_requestsend.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Try After Some Time", Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        }

        if (view == conatact_btn) {
            Intent intentPhoneBook = new Intent(FriendSearch_Activity.this, AdressBookActivity.class);
            startActivityForResult(intentPhoneBook, PHONE_NUM_REQUEST_CODE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PHONE_NUM_REQUEST_CODE) {
            if (data.hasExtra("phoneNumber")) {
                String phNumb = data.getExtras().getString("phoneNumber");
                phNumb = phNumb.replaceAll(" ", "");
                if (phNumb != "") {
                    int length = phNumb.length();
                    if (length > 10) {
                        et_number_search.setText(phNumb.substring(length - 10));
                    } else {
                        et_number_search.setText(phNumb);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        Intent i2 = new Intent(this, Main_Activity.class);
        startActivity(i2);
        finish();
    }




    //// this medhod used for ads

    private void setimageForBack() {
        alertDialog = new AlertDialog.Builder(this).create();
        view1 = getLayoutInflater().inflate(R.layout.exit_dialog, null);
        alertDialog.setView(view1);
        iv_promotedappimage = (ImageView) view1.findViewById(R.id.imageview_promotedappimage);
        tv_promotedappdesc = (TextView) view1.findViewById(R.id.textview_promoteddappdesc);
        rl_promotionback = (RelativeLayout) view1.findViewById(R.id.relative_promotionback);
        bt_yes = (Button) view1.findViewById(R.id.button_yes);
        bt_no = (Button) view1.findViewById(R.id.button_no);
        ImageLoader m = ApplicationFL.getInstance().getImageLoader();
        m.get(myprefs.getPromotedappimage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                iv_promotedappimage.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        playstorelink = myprefs.getPromotedappurl();
        backcampid = myprefs.getBackCampaignAppId();
        tv_promotedappdesc.setText(myprefs.getPromotedappdesc());
    }

    //request for all ad id's
    private void requestforAdMobID() {
        ApplicationFL.getInstance().addToRequestQueue(requestforAdmob(urlJsonArray), "GETID");
    }

    private CustomJsonObjectRequest requestforAdmob(String url) {
        CustomJsonObjectRequest customJsonobjectRequest = new CustomJsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String AdBuddizFullPage = response.getString("AdBuddizFullPage");
                    myprefs.setAddbuddies(AdBuddizFullPage);
                    System.out.println("AdBuddizFullPage" + AdBuddizFullPage);

                    String AdmobFullPage = response.getString("AdmobFullPage");
                    myprefs.setAddmob(AdmobFullPage);
                    System.out.println("AdMobFullPage" + AdmobFullPage);

                    String BannerAd = response.getString("BannerAd");
                    myprefs.setAddbanner(BannerAd);
                    System.out.println("BannerAd" + BannerAd);
                    myprefs.setShowbackads(response.getString("ShowNativAds"));
                    if (response.getString("ShowNativAds").equals("1")) {
                        System.out.println("response code " + response.getString("promotedappid"));
                        myprefs.setBackCampaignAppId(response.getString("promotedappid"));
                        myprefs.setPromotedappimage(response.getString("promotedappimage"));
                        myprefs.setPromotedappurl(response.getString("promotedappurl"));
                        myprefs.setPromotedappdesc(response.getString("promotedappdesc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ankitatest error" + error);
            }
        });
        customJsonobjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        return customJsonobjectRequest;
    }

    private void policyAlert() {
        myDialog = new Dialog(FriendSearch_Activity.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.policy_dialog);
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
        Window window = myDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        final CheckBox doNotShowAgain = (CheckBox) myDialog
                .findViewById(R.id.dontshowagainpolicy);
        TextView policyRead = (TextView) myDialog.findViewById(R.id.policy_tv);
        String text = "When you click on advertisements delivered by Micro Mini Apps, you will be directed to a third party’s webpage and we may pass certain of your information to the third parties operating or hosting these pages, including your email address, phone number and a list of the apps on your device. This policy will tell you what data we collect and how we use it for our games and apps. For more information on how we collect, use and share your data please read Privacy policy. If you do not wish to receive these ads delivered by Micro Mini Apps you can delete the apps any time.";
        int start = text.indexOf("Privacy policy");
        SpannableString spannableString = new SpannableString(text);

        spannableString.setSpan(new MyClickableSpan(), start, start + 14,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
        spannableString.setSpan(italicSpan, start, start + 14,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan, start, start + 14,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        policyRead.setText(spannableString);
        policyRead.setMovementMethod(LinkMovementMethod.getInstance());
        Button accept = (Button) myDialog.findViewById(R.id.acceptPolicy);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                if (doNotShowAgain.isChecked()) {
                    myprefs.setPolicyRead(true);
                }
            }
        });

        Button decline = (Button) myDialog.findViewById(R.id.declinePolicy);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                finish();
            }
        });
    }


    private class MyClickableSpan extends ClickableSpan {
        public void onClick(View textView) {
            myDialog.dismiss();
            Intent iPolicy = new Intent(getApplicationContext(),
                    PrivacyPolicy_Activity.class);
            startActivity(iPolicy);
            finish();
        }

        @Override
        public void updateDrawState(TextPaint textPaint) {
            textPaint.setColor(Color.BLUE);
            textPaint.setUnderlineText(true);
        }
    }

    public void connectionCheck() {
        ConnectionCheck connectionCheck = new ConnectionCheck(getApplicationContext());
        interstitialAd1 = new InterstitialAd(getApplicationContext());
        if (connectionCheck.isConnectionAvailable()) {
            interstitialAd1.setAdUnitId(myprefs.getAddmob());
            System.out.println("Admob getting " + myprefs.getAddmob());
            adRequest1 = new AdRequest.Builder().build();
            interstitialAd1.loadAd(adRequest1);
            addAdmobAdListner1();
//            adMobBannerAd();
//            if (addMobHandler != null) {
//                addMobHandler.postDelayed(mAddIsLoaded, 8 * 1000);
//            } else {
//                addMobHandler = new Handler();
//                addMobHandler.postDelayed(mAddIsLoaded, 8 * 1000);
//            }
        } else {
            myAppLayoutl.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

//    Runnable mAddIsLoaded = new Runnable() {
//        @Override
//        public void run() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (!interstitialAd1.isLoaded()) {
//                        myAppLayoutl.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
//                    }
//                }
//            });
//        }
//    };

    //dcgiudgsfoiuasgdiuagd

    public void adMobFullPageAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(myprefs.getAddmob());
        requestNewInterstitial();
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

//    Runnable mShowFullPageAdTask = new Runnable() {
//        @Override
//        public void run() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (myprefs.getCHECKFORADS()) {
//                        if (mInterstitialAd.isLoaded())
//                            mInterstitialAd.show();
//                    }
//                }
//            });
//        }
//    };


    private void adMobBannerAd() {
        myprefs = new MyPref(getApplicationContext());
        myAdd = (LinearLayout) findViewById(R.id.myAdd);
        final AdView adView = new AdView(this);
        adView.setAdUnitId(myprefs.getAddbanner());
        adView.setAdSize(AdSize.BANNER);
        myAdd.addView(adView);
        final AdListener listener = new AdListener() {
            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
                super.onAdLoaded();
            }
        };
        adView.setAdListener(listener);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    // jhbiushcoihsdf

    private void addAdmobAdListner1() {
        interstitialAd1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (interstitialAd1.isLoaded()) {
                    interstitialAd1.show();
                } else {
                }
            }

//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                myAppLayoutl.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
//            }

//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                myAppLayoutl.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
//            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
             handler.removeCallbacks(mShowFullPageAdTask);
     }


    @Override
    protected void onResume() {
        super.onResume();
             handler.postDelayed(mShowFullPageAdTask, 15 * 1000);

    }



}
