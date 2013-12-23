package com.east.customermanager.net;

import org.json.JSONObject;

/**
 * 当返回正确结果时,此类存放返回的 JSONObject
 */
public class HttpResultJson extends HttpResult {
	private JSONObject json;
	public JSONObject getJson() {
		return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
	}
}
