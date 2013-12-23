package com.east.customermanager.net;

import java.net.URI;

import org.apache.http.client.methods.HttpGet;

public class CustomHttpGet extends HttpGet {
	private int resendTimes=0;
	private boolean resendEnabled=true;
	
	public CustomHttpGet() {
		super();
	}

	public CustomHttpGet(String uri) {
		super(uri);
	}

	public CustomHttpGet(URI uri) {
		super(uri);
	}
	
	public boolean isResendEnabled() {
		return resendEnabled;
	}

	public void setResendEnabled(boolean resendEnabled) {
		this.resendEnabled = resendEnabled;
	}

	public boolean isNeedResend() {
		if(!resendEnabled){
			return false;
		}
		if(resendTimes>2){
			return false;
		}else{
			resendTimes++;
			return true;
		}
	}
}
