package com.allinpay.bigdata.model;

import java.util.Date;

public class TChanlqyInfo extends TChanlqyInfoKey {
    private String qyBatchId;

    private String intfcName;

    private String queryStatus;

    private String rediskey;

    private Date insertime;
    
    private String errorreason;

    public String getQyBatchId() {
        return qyBatchId;
    }

    public void setQyBatchId(String qyBatchId) {
        this.qyBatchId = qyBatchId == null ? null : qyBatchId.trim();
    }

    public String getIntfcName() {
        return intfcName;
    }

    public void setIntfcName(String intfcName) {
        this.intfcName = intfcName == null ? null : intfcName.trim();
    }

    public String getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(String queryStatus) {
        this.queryStatus = queryStatus == null ? null : queryStatus.trim();
    }

    public String getRediskey() {
        return rediskey;
    }

    public void setRediskey(String rediskey) {
        this.rediskey = rediskey == null ? null : rediskey.trim();
    }

    public Date getInsertime() {
        return insertime;
    }

    public void setInsertime(Date insertime) {
        this.insertime = insertime;
    }

	public String getErrorreason() {
		return errorreason;
	}

	public void setErrorreason(String errorreason) {
		this.errorreason = errorreason;
	}
}