package com.east.customermanager.net;

/**
 * 此类封装了,与服务端,通讯返回的结果,(包括本地发生的异常)
 *	这是一个基类,如果从服务端返回正确的数据,会有两种格式的数据
 *	json和xml, 参考 HttpResultJson 和HttpResultXml
 */
public abstract class HttpResult {
	/**
	 * 请求结果 ok 代表返回了正确的结果
	 * fail代表发生了错误,包括一些异常,或服务端自定义的错误
	 */
	public static final int RESULT_OK=1;
	public static final int RESULT_FAIL=2;
	
	/**
	 * 请求结果,成功或失败
	 */
	protected int resultCode;
	/**
	 * 错误码
	 */
	protected int failCode=0;
	/**
	 * 错误信息
	 */
	protected String failMessage="";
	
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public int getFailCode() {
		return failCode;
	}
	public void setFailCode(int failCode) {
		this.failCode = failCode;
	}
	public String getFailMessage() {
		return failMessage;
	}
	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
	}
}
