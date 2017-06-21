package com.amtee.friendsfinder.pojo;

import java.util.List;

/**
 * Created by Amit Kumar Tiwar on 12/08/16.
 */
public class FriendListStatus_Pojo {

    String responseCode;
    List<FriendListDetail_Pojo> friendList;

    public String getResponseCode() {return responseCode;}

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public List<FriendListDetail_Pojo> getFriendList() {return friendList;}

    public void setFriendList(List<FriendListDetail_Pojo> friendList) {
        this.friendList = friendList;
    }
}
