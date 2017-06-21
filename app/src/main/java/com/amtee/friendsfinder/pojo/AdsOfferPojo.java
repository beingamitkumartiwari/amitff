package com.amtee.friendsfinder.pojo;

/**
 * Created by Amit on 27-08-2016.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AdsOfferPojo {

    @SerializedName("offerList")
    private List<OfferList> offerList = new ArrayList<OfferList>();
    @SerializedName("responseCode")
    private String responseCode;

    /**
     *
     * @return
     * The offerList
     */
    public List<OfferList> getOfferList() {
        return offerList;
    }

    /**
     *
     * @param offerList
     * The offerList
     */
    public void setOfferList(List<OfferList> offerList) {
        this.offerList = offerList;
    }

    /**
     *
     * @return
     * The responseCode
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     *
     * @param responseCode
     * The responseCode
     */
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString(){
        return "responseCode: "+responseCode+"   List Size: "+offerList.size();
    }

}
