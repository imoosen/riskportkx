package com.allinpay.bigdata.model;

public class TChanlqyInfoKey {
    private Integer id;

    private String merId;

    private String userId;

    private String intfcId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId == null ? null : merId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getIntfcId() {
        return intfcId;
    }

    public void setIntfcId(String intfcId) {
        this.intfcId = intfcId == null ? null : intfcId.trim();
    }
}