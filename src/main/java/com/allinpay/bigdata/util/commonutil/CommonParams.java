package com.allinpay.bigdata.util.commonutil;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@ConfigurationProperties(prefix="commonparams") //接收application.yml中的commonparams下面的属性 
public class CommonParams {

	private String kexinbrokenUrl;
	private String kexinAKey;
	private String kexinSKey;
	private String userIP;
	public String getKexinbrokenUrl() {
		return kexinbrokenUrl;
	}
	public void setKexinbrokenUrl(String kexinbrokenUrl) {
		this.kexinbrokenUrl = kexinbrokenUrl;
	}
	public String getKexinAKey() {
		return kexinAKey;
	}
	public void setKexinAKey(String kexinAKey) {
		this.kexinAKey = kexinAKey;
	}
	public String getKexinSKey() {
		return kexinSKey;
	}
	public void setKexinSKey(String kexinSKey) {
		this.kexinSKey = kexinSKey;
	}
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	
}
