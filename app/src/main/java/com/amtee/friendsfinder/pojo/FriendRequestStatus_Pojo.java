package com.amtee.friendsfinder.pojo;

import java.util.List;

/**
 * Created by Amit Kumar Tiwar on 29/07/16.
 */
public class FriendRequestStatus_Pojo {

    String responseCode;
    List<FriendRequestDetail_Pojo> requestDetails;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public List<FriendRequestDetail_Pojo> getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(List<FriendRequestDetail_Pojo> requestDetails) {
        this.requestDetails = requestDetails;
    }
}
