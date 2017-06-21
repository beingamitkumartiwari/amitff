package com.amtee.friendsfinder.pojo;

/**
 * Created by Amit Kumar Tiwar on 02/08/16.
 */
public class NotificationItem {

    private String message;
    private String timing;
    private String msgReadStatus;

    public NotificationItem(){}

    public NotificationItem(String message, String timing, String msgReadStatus){
        this.message=message;
        this.timing=timing;
        this.msgReadStatus=msgReadStatus;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getMsgReadStatus() {
        return msgReadStatus;
    }

    public void setMsgReadStatus(String msgReadStatus) {
        this.msgReadStatus = msgReadStatus;
    }

}
