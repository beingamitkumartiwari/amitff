package com.amtee.friendsfinder.pojo;

/**
 * Created by Amit Kumar Tiwar on 28/07/16.
 */
public class FriendSearchDetails_Pojo {

    String  phoneNo;
    String responseCode;
    String searchDeviceId;
    String sex;
    String userName;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getSearchDeviceId() {
        return searchDeviceId;
    }

    public void setSearchDeviceId(String searchDeviceId) {
        this.searchDeviceId = searchDeviceId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
