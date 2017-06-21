package com.amtee.friendsfinder;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amtee.friendsfinder.pojo.FriendRequestDetail_Pojo;
import com.amtee.friendsfinder.pojo.FriendRequestStatus_Pojo;
import com.amtee.friendsfinder.pojo.StoreGCMidinDb_Pojo;
import com.amtee.friendsfinder.pojo.UserLocation_Pojo;
import com.amtee.friendsfinder.retofitwork.Rest_Interface;
import com.amtee.friendsfinder.retofitwork.Service_Generator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnGeofencingTransitionListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geofencing.model.GeofenceModel;
import io.nlopez.smartlocation.geofencing.utils.TransitionGeofence;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class Main_Activity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnGeofencingTransitionListener, OnActivityUpdatedListener, OnLocationUpdatedListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    //this for showing my location on map
    private GoogleMap map;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Location fLastLocation;
    private Marker marker;
    private LocationGooglePlayServicesProvider locationGooglePlayServicesProvider;
    TextView tv_location;
    ImageButton search_friend, friend_list, chat_friend;
    String latitude, longitude;

    Call<UserLocation_Pojo> userlocationpojo;
    Rest_Interface ri_verification;

    //this for no of rquest
    TextView tv_friend_request_count;
    RelativeLayout rl_no_of_friend_request;

    //this for setting up Gcm

    private final static int PLAY_SERVICE_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging googleCloudMessaging;
    String resistration_id;
    private My_Prefs my_prefs;
    Call<StoreGCMidinDb_Pojo> store_gcm_id_in_db_pojoCall;
    Call<FriendRequestStatus_Pojo> friend_request_status_pojoCall;
    Rest_Interface ri_storegcmidonserverdb;
    Rest_Interface ri_friendrequeststatus;
    ProgressDialog progressDialog;

    //this is for navigation view
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    double lat;
    double lng;
    double lat1;
    double lng1;
    //    LatLng fromPosition;
//    LatLng toPosition;
//    Document document;
//    GMapV2GetRouteDirection v2GetRouteDirection;
    String myloc;
    String friendloc;
    Geocoder geocoder;
    List<Address> addresses;


//    //  ads content
//
    MyPref myprefs;
    Dialog myDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myprefs=new MyPref(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        my_prefs = new My_Prefs(this);

        if (!myprefs.isPolicyRead()) {//&& !myPrefs.isFirstinstall()) {
            policyAlert();
        }

        tv_location = (TextView) findViewById(R.id.tv_location);
        search_friend = (ImageButton) findViewById(R.id.search_friend);
        friend_list = (ImageButton) findViewById(R.id.friend_list);
        chat_friend = (ImageButton) findViewById(R.id.chat_friend);
        ri_verification = Service_Generator.createService(Rest_Interface.class, Rest_Interface.BASE_URL);
        ri_storegcmidonserverdb = Service_Generator.createService(Rest_Interface.class, Rest_Interface.BASE_URL);
        ri_friendrequeststatus = Service_Generator.createService(Rest_Interface.class, Rest_Interface.BASE_URL);

        startLocation();


        initializeGCMRequest();

        checkFriendRequestStatus();

        search_friend.setOnClickListener(this);
        friend_list.setOnClickListener(this);
        chat_friend.setOnClickListener(this);

        rl_no_of_friend_request = (RelativeLayout) findViewById(R.id.rl_no_of_friend_request);
        rl_no_of_friend_request.setOnClickListener(this);

        tv_friend_request_count = (TextView) findViewById(R.id.tv_friend_request_count);
        tv_friend_request_count.setText(my_prefs.getREQUEST_STATUS());

        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);

        if (getIntent().getStringExtra("lat") != null) {
            Intent intent = getIntent();
            lat = Double.parseDouble(intent.getStringExtra("lat"));
            lng = Double.parseDouble(intent.getStringExtra("lng"));
            System.out.println("lc " + lat + " " + lng);
        }



        //GetRouteTask getRoute = new GetRouteTask();
        //getRoute.execute();

    }

    @Override

    protected void onResume() {
        super.onResume();


        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {

            buildGoogleApiClient();
            mGoogleApiClient.connect();

        }

        if (map == null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);
        }


    }

    @Override
    public void onMapReady(GoogleMap retMap) {

        map = retMap;

        setUpMap();

    }

    public void setUpMap() {

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient != null) {
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //mLocationRequest.setSmallestDisplacement(0.1F);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(final Location location) {
        mLastLocation = location;

        //remove previous current location Marker
        if (marker != null) {
            marker.remove();
        }

        double dLatitude = mLastLocation.getLatitude();
        double dLongitude = mLastLocation.getLongitude();
        marker = map.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude)).title(myloc)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 8));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));

        /*
        //
         this is used for friendlocation
         //
          */

        if (getIntent().getStringExtra("lat") != null) {

            final LatLng FRIENDS = new LatLng(lat, lng);

            Marker friends = map.addMarker(new MarkerOptions().position(FRIENDS).title(friendloc)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));
            getAddress();

        }


        SmartLocation.with(this.getApplicationContext()).geocoding().reverse(location, new OnReverseGeocodingListener() {
            @Override
            public void onAddressResolved(Location original, List<Address> results) {
                if (results.size() > 0) {

                    Address result = results.get(0);
                   // StringBuilder builder = new StringBuilder().append("Address: ");
                    StringBuilder builder = new StringBuilder().append("");
                    // builder.append("\nAddress: ");

                    List<String> addressElements = new ArrayList<>();
                    for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                        addressElements.add(result.getAddressLine(i));
                    }
                    builder.append(TextUtils.join(", ", addressElements));
                    myloc = String.valueOf(builder);
                    tv_location.setText(builder.toString());
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                    sendLocation(latitude, longitude);
                    lat1 = location.getLatitude();
                    lng1 = location.getLongitude();
                    //System.out.println("frnloc " + lat1);
//                    if (getIntent().getStringExtra("lat") != null) {
//
//                        LatLng prev = new LatLng(lat, lng);
//                        LatLng my = new LatLng(lat1, lng1);
//
//                        System.out.println("poly " + lat + " " + lng + " " + lat1 + " " + lng1);
//
//                        Polyline line = map.addPolyline(new PolylineOptions().add(prev, my)
//                                .width(3).color(Color.BLUE));
//                    }
                }

            }

        });


    }

    private String getAddress() {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName());
                System.out.println("res " + result);
                friendloc = String.valueOf(result);
            }
        } catch (IOException e) {
            Log.e("tagss", e.getMessage());
        }

        return result.toString();
    }

    public void sendLocation(String lat, String lon) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("deviceId", App_Constant.get_IMEI_Number(this));
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lon);
        userlocationpojo = ri_verification.getUserLocation(jsonObject);
        userlocationpojo.enqueue(new Callback<UserLocation_Pojo>() {
            @Override
            public void onResponse(Response<UserLocation_Pojo> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    System.out.println("userlocation " + response.body().getResponseCode());
                    if (response.body().getResponseCode().equals("1")) {

                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("ashu On failure ");
            }
        });

    }


    private void startLocation() {
        locationGooglePlayServicesProvider = new LocationGooglePlayServicesProvider();
        locationGooglePlayServicesProvider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(locationGooglePlayServicesProvider).start(this);
        smartLocation.activity().start(this);

        // Create some geofences
        GeofenceModel mestalla = new GeofenceModel.Builder("1").setTransition(Geofence.GEOFENCE_TRANSITION_ENTER).setLatitude(39.47453120000001).setLongitude(-0.358065799999963).setRadius(500).build();
        smartLocation.geofencing().add(mestalla).start(this);
    }


    @Override
    public void onGeofenceTransition(TransitionGeofence transitionGeofence) {

    }

    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {

    }

    @Override
    public void onLocationUpdated(Location location) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.search_friend:
                Intent i1 = new Intent(Main_Activity.this, FriendSearch_Activity.class);
                startActivity(i1);
                //finish();
                break;

            case R.id.rl_no_of_friend_request:
                Intent i2 = new Intent(Main_Activity.this, FriendRequestList_Activity.class);
                startActivity(i2);
                finish();
                break;

            case R.id.friend_list:
                Intent i3 = new Intent(Main_Activity.this, FriendList_Activity.class);
                startActivity(i3);
                finish();
                break;

            case R.id.chat_friend:
                Intent i4 = new Intent(Main_Activity.this, NotificationList_Activity.class);
                startActivity(i4);
                //finish();
                break;

        }
    }


    ///// check play services

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(this, "This device doesn't support Play services, Application will not work normally", Toast.LENGTH_LONG).show();
            }
            return false;
        } else {
        }
        return true;
    }


    //////
    ////////  gcm work ///////////
    //////

    private void initializeGCMRequest() {
        if (!TextUtils.isEmpty(my_prefs.getUSER_REGISTERED_GCM_ID())) {
            if (my_prefs.isREGISERED_GCM_ID_SAVED_IN_SERVER()) {

            } else {
                if (progressDialog != null) {
                    progressDialog.show();
                    requestCustomGCMResistration();
                }

            }
        } else {
            if (checkPlayServices()) {
                resisterInBackground();
                System.out.println("Amit GCM1 " + my_prefs.getUSER_REGISTERED_GCM_ID() + "status " + my_prefs.isREGISERED_GCM_ID_SAVED_IN_SERVER());
            }
        }
    }


    public void resisterInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String message = "";
                try {
                    if (googleCloudMessaging == null) {
                        googleCloudMessaging = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    resistration_id = googleCloudMessaging.register(App_Constant.GOOGLE_PROJECT_ID);
                    message = "Registration ID :" + resistration_id;
                } catch (IOException ex) {
                    message = "Error :" + ex.getMessage();
                }
                return message;
            }

            @Override
            protected void onPostExecute(String message) {
                if (!TextUtils.isEmpty(resistration_id)) {
                    my_prefs.setUSER_REGISTERED_GCM_ID(resistration_id);
                    if (progressDialog != null)
                        progressDialog.show();
                    System.out.println("AmitGCM 2 " + resistration_id);
                    requestCustomGCMResistration();
                } else {
                }
            }

        }.execute(null, null, null);
    }

    private void requestCustomGCMResistration() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("deviceId", App_Constant.get_IMEI_Number(this));
        jsonObject.addProperty("registrationId", my_prefs.getUSER_REGISTERED_GCM_ID());
        store_gcm_id_in_db_pojoCall = ri_storegcmidonserverdb.setGCMidOnServer(jsonObject);
        store_gcm_id_in_db_pojoCall.enqueue(new Callback<StoreGCMidinDb_Pojo>() {
            @Override
            public void onResponse(Response<StoreGCMidinDb_Pojo> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    StoreGCMidinDb_Pojo store_gcm_id_in_db_pojo = response.body();
                    if (store_gcm_id_in_db_pojo.getResponseCode().equals("1")) {
                        my_prefs.setREGISERED_GCM_ID_SAVED_IN_SERVER(true);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Toast.makeText(getApplicationContext(),
                        "Unexpected Error occcured! [Most common Error: Device might "
                                + "not be connected to Internet or remote server is not up and running], check for other errors as well",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkFriendRequestStatus() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("deviceId", App_Constant.get_IMEI_Number(this));
        friend_request_status_pojoCall = ri_friendrequeststatus.getFriendRequestStatus(jsonObject);
        friend_request_status_pojoCall.enqueue(new Callback<FriendRequestStatus_Pojo>() {
            @Override
            public void onResponse(Response<FriendRequestStatus_Pojo> response, Retrofit retrofit) {

                if (response.isSuccess()) {
                    if (response.body().getRequestDetails().size() > 0) {
                        FriendRequestStatus_Pojo friend_request_status_pojo = response.body();
                        FriendRequestDetail_Pojo friend_request_detail_pojo = new FriendRequestDetail_Pojo();
                        my_prefs.setREQUEST_STATUS(String.valueOf(friend_request_status_pojo.getRequestDetails().size()));
                        for (int i = 0; i < friend_request_status_pojo.getRequestDetails().size(); i++) {
                            friend_request_detail_pojo.setResponseCode(friend_request_status_pojo.getRequestDetails().get(i).getResponseCode());
                            friend_request_detail_pojo.setSenderphoneNo(friend_request_status_pojo.getRequestDetails().get(i).getSenderphoneNo());
                            friend_request_detail_pojo.setSenderDeviceid(friend_request_status_pojo.getRequestDetails().get(i).getSenderDeviceid());
                            friend_request_detail_pojo.setSendersex(friend_request_status_pojo.getRequestDetails().get(i).getSendersex());
                            friend_request_detail_pojo.setSenderstatus(friend_request_status_pojo.getRequestDetails().get(i).getSenderstatus());
                            friend_request_detail_pojo.setSenderuserName(friend_request_status_pojo.getRequestDetails().get(i).getSenderuserName());
                        }
                    }
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

//            case R.id.action_settings:
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_profile:
                Intent it1 = new Intent(Main_Activity.this, Profile_Activity.class);
                startActivity(it1);
                //finish();
                return true;
            case R.id.nav_home:
                Intent it2 = new Intent(Main_Activity.this, Main_Activity.class);
                startActivity(it2);
                finish();
                return true;
            case R.id.nav_friendlist:
                Intent it3 = new Intent(Main_Activity.this, FriendList_Activity.class);
                startActivity(it3);
                return true;
            case R.id.nav_sendrequest:
                Intent it4 = new Intent(Main_Activity.this, FriendSearch_Activity.class);
                startActivity(it4);
                finish();
                return true;
            case R.id.nav_more:
                Intent it5 = new Intent(Main_Activity.this, More_Activity.class);
                startActivity(it5);
                //finish();
                return true;
            case R.id.nav_policy:
                Intent it6 = new Intent(Main_Activity.this, PrivacyPolicy_Activity.class);
                startActivity(it6);
                //finish();
                return true;
            case R.id.nav_friendrequestlist:
                Intent it7 = new Intent(Main_Activity.this, FriendRequestList_Activity.class);
                startActivity(it7);
                finish();
                return true;
            default:
                return true;
        }

    }






    private void policyAlert() {
        myDialog = new Dialog(Main_Activity.this);
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
        String text = "When you click on advertisements delivered by Amtee Apps, you will be directed to a third party’s webpage and we may pass certain of your information to the third parties operating or hosting these pages, including your email address, phone number and a list of the apps on your device. This policy will tell you what data we collect and how we use it for our games and apps. For more information on how we collect, use and share your data please read Privacy policy. If you do not wish to receive these ads delivered by Amtee Apps you can delete the apps any time.";
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



}



