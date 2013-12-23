package com.east.customermanager.net;

import org.xmlpull.v1.XmlPullParser;

/**
 * 当返回正确结果时,此类存放返回的 XmlPullParser
 */
public class HttpResultXml extends HttpResult {
	private XmlPullParser parser;

	public XmlPullParser getParser() {
		return parser;
	}

	public void setParser(XmlPullParser parser) {
		this.parser = parser;
	}
	
}
