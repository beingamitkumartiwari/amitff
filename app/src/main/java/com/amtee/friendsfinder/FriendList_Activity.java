package com.amtee.friendsfinder;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amtee.friendsfinder.adapter.FriendList_Adapter;
import com.amtee.friendsfinder.pojo.FriendListDetail_Pojo;
import com.amtee.friendsfinder.pojo.FriendListStatus_Pojo;
import com.amtee.friendsfinder.pojo.FriendLocation_Pojo;
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
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FriendList_Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Call<FriendListStatus_Pojo> listoffriend;
    Rest_Interface ri_listoffriend;

    ListView lv_friendlist;
    FriendList_Adapter friendList_adapter;
    ArrayList<FriendListDetail_Pojo> friendlistarray;

    Call<FriendLocation_Pojo> frindlocation;
    LinearLayout emptylist;

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
    Interstitial interstitial_Ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
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
        emptylist = (LinearLayout) findViewById(R.id.emptylist);
        myprefs=new MyPref(this);
        friendlistarray = new ArrayList<>();
        ri_listoffriend = Service_Generator.createService(Rest_Interface.class, Rest_Interface.BASE_URL);
        lv_friendlist = (ListView) findViewById(R.id.lv_friendlist);
        listofFriend();
//        connectionCheck();
//        adMobFullPageAd();
//        adMobBannerAd();
        lv_friendlist.setOnItemClickListener(this);
    }

    public void listofFriend() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("deviceId", App_Constant.get_IMEI_Number(this));
        listoffriend = ri_listoffriend.getFriendListStatus(jsonObject);
        listoffriend.enqueue(new Callback<FriendListStatus_Pojo>() {
            @Override
            public void onResponse(Response<FriendListStatus_Pojo> response, Retrofit retrofit) {

                if (response.isSuccess()) {
                    if (response.body().getFriendList().size() > 0) {
                        FriendListStatus_Pojo friendListStatus_pojo = response.body();
                        if (friendListStatus_pojo.getResponseCode().equals("1")) {
                            friendlistarray.clear();
                            List<FriendListDetail_Pojo> friendList = friendListStatus_pojo.getFriendList();
                            for (int i = 0; i < friendList.size(); i++) {
                                FriendListDetail_Pojo fd = new FriendListDetail_Pojo();
                                fd.setResponseCode(friendList.get(i).getResponseCode());
                                fd.setSenderphoneNo(friendList.get(i).getSenderphoneNo());
                                fd.setSenderDeviceid(friendList.get(i).getSenderDeviceid());
                                fd.setSendersex(friendList.get(i).getSendersex());
                                fd.setSenderstatus(friendList.get(i).getSenderstatus());
                                fd.setSenderuserName(friendList.get(i).getSenderuserName());
                                friendlistarray.add(fd);
                            }
                        }
                    }
                    else
                    {
                        emptylist.setVisibility(View.VISIBLE);
                    }
                    friendList_adapter = new FriendList_Adapter(getApplicationContext(), friendlistarray);
                    System.out.println("abcd " + friendlistarray.size());
                    lv_friendlist.setAdapter(friendList_adapter);

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String devicid = friendlistarray.get(i).getSenderDeviceid();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("deviceId", devicid);
        frindlocation = ri_listoffriend.getfriendLocation(jsonObject);
        frindlocation.enqueue(new Callback<FriendLocation_Pojo>() {
            @Override
            public void onResponse(Response<FriendLocation_Pojo> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    String lattitude;
                    String longitude;
                    FriendLocation_Pojo friendLocation_pojo = response.body();
                    if (friendLocation_pojo.getResponseCode().equals("1")) {
                        System.out.println("location " + friendLocation_pojo.getLat() + " " + friendLocation_pojo.getLng());
                        lattitude = friendLocation_pojo.getLat();
                        longitude = friendLocation_pojo.getLng();

                        Intent intent = new Intent(getApplicationContext(), Main_Activity.class);

                        intent.putExtra("lat", lattitude);
                        intent.putExtra("lng", longitude);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    @Override
    public void onBackPressed() {
         Intent i2 = new Intent(this, Main_Activity.class);
        startActivity(i2);
        finish();
    }



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
        myDialog = new Dialog(FriendList_Activity.this);
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
