package com.so.payload.response;

import java.util.Date;
import java.util.Map;

public class BODY {

    private int status;
    public Date timestamp;
    public Object data;
    public String message;
    public Map<String, Object> messages;

    public BODY() {
        this.timestamp = new Date();
    }

    public BODY(Object data) {
        this.timestamp = new Date();
        this.data = data;
    }

    public BODY(Object data, String message) {
        this.timestamp = new Date();
        this.data = data;
        this.message = message;
    }

    public BODY(Object data, Map<String, Object> messages) {
        this.timestamp = new Date();
        this.data = data;
        this.messages = messages;
        if(messages!=null && messages.size() > 0) {
        	this.message = (String) messages.values().toArray()[0];;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}