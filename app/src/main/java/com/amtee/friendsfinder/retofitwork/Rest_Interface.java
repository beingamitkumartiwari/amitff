package com.amtee.friendsfinder.retofitwork;


import com.amtee.friendsfinder.pojo.Accept_Request_Pojo;
import com.amtee.friendsfinder.pojo.AdsOfferPojo;
import com.amtee.friendsfinder.pojo.AlreadyResistered_Pojo;
import com.amtee.friendsfinder.pojo.Delete_Friend_Pojo;
import com.amtee.friendsfinder.pojo.FriendListStatus_Pojo;
import com.amtee.friendsfinder.pojo.FriendLocation_Pojo;
import com.amtee.friendsfinder.pojo.FriendRequestStatus_Pojo;
import com.amtee.friendsfinder.pojo.FriendSearch_Pojo;
import com.amtee.friendsfinder.pojo.GetProfilePojo;
import com.amtee.friendsfinder.pojo.MoreApps_Pojo;
import com.amtee.friendsfinder.pojo.ResponseCode_Pojo;
import com.amtee.friendsfinder.pojo.SendFriendRequest_Pojo;
import com.amtee.friendsfinder.pojo.StoreGCMidinDb_Pojo;
import com.amtee.friendsfinder.pojo.UserLocation_Pojo;
import com.google.gson.JsonObject;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;


/**
 * Created by Amit Kumar Tiwar on 25/07/16.
 */
public interface Rest_Interface {

    String BASE_URL="http://mpaisa.info/FindRegistration.svc/";
    String URL_ENCODED_BITLY = "https://api-ssl.bitly.com/v3/";

    String BASE_URL1="http://mpaisa.info/MtechAppsPromotion.asmx/";

    @POST("signUpfind")
    Call<ResponseCode_Pojo> getSignUpStatus(@Body JsonObject jsonObject);

    @POST("Alreadyregisteredfind")
    Call<AlreadyResistered_Pojo> getAlreadyResister(@Body JsonObject jsonObject);

    @POST("userLocation")
    Call<UserLocation_Pojo> getUserLocation(@Body JsonObject jsonObject);

    @POST("searchFriend")
    Call<FriendSearch_Pojo> getSearchFriend(@Body JsonObject jsonObject);

    @POST("setGcmIdfind")
    Call<StoreGCMidinDb_Pojo> setGCMidOnServer(@Body JsonObject jsonObject);

    @POST("sendRequest")
    Call<SendFriendRequest_Pojo> getSendFriendRequest(@Body JsonObject jsonObject);

    @POST("totRequest")
    Call<FriendRequestStatus_Pojo> getFriendRequestStatus(@Body JsonObject jsonObject);

    @POST("getProfilefind")
    Call<GetProfilePojo> getProfile(@Body JsonObject jsonObject);

    @POST("totalFriend")
    Call<FriendListStatus_Pojo> getFriendListStatus(@Body JsonObject jsonObject);

    @POST("aceptRequest")
    Call<Accept_Request_Pojo> getacceptRequest(@Body JsonObject jsonObject);

    @POST("friendLocation")
    Call<FriendLocation_Pojo> getfriendLocation(@Body JsonObject jsonObject);

    @POST("deleteFriend")
    Call<Delete_Friend_Pojo> getdeleteFriend(@Body JsonObject jsonObject);

    @POST("totalOffer")
    Call<AdsOfferPojo> getMoreApps ();

}
