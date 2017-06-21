package com.amtee.friendsfinder.pojo;

import java.util.List;

/**
 * Created by Amit Kumar Tiwar on 28/07/16.
 */
public class FriendSearch_Pojo {

    String responseCode;
    List<FriendSearchDetails_Pojo> searchDetails;


    public String getResponseCode() {return responseCode;}

    public void setResponseCode(String responseCode) {this.responseCode = responseCode;}

    public List<FriendSearchDetails_Pojo> getSearchDetails() {return searchDetails;}

    public void setSearchDetails(List<FriendSearchDetails_Pojo> searchDetails) {this.searchDetails = searchDetails;}
}
