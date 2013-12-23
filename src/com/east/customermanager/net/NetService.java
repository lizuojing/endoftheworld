package com.east.customermanager.net;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.east.customermanager.log.CMLog;
import com.east.customermanager.util.Utils;

/**
 * 与服务端交互,收发协议
 * 
 */
public class NetService {

	private static final String TAG = "NetService";

	/**
	 * 以post方式,向服务端发送请求,并获取json格式的返回结果
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @return HttpResultJson
	 */
	public static HttpResultJson httpPostReturnJson(Context context, String url, List<NameValuePair> params) {
		HttpResultJson result = new HttpResultJson();
		if (!checkNetwork(context)) {
			result.setResultCode(HttpResult.RESULT_FAIL);
			result.setFailCode(HttpFailCode.NONETWORK);
			result.setFailMessage("not network");
			return result;
		}

		HttpClient client = CustomHttpClient.getInstance();
		CustomHttpPost post = null;
		try {
			post = new CustomHttpPost(url);
		} catch (IllegalArgumentException e) {
			createFailHttpResult(e, HttpFailCode.UNDEFINED, result);
			return result;
		}
		post.addHeader("Accept-Encoding", "GZIP");
		HttpEntity entity = null;
		try {
			if (params != null && params.size() > 0) {
				entity = new UrlEncodedFormEntity(params, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			createFailHttpResult(e, HttpFailCode.UNDEFINED, result);
			return result;
		}
		if (entity != null) {
			post.setEntity(entity);
		}
		CMLog.e(TAG, "request url is " + url);
		CMLog.e(TAG, "params is " + params.toString());
		HttpResponse response;
		try {
			response = executeHttpPost(client, post);
		} catch (Exception e) {
			int failCode = HttpFailCode.UNDEFINED;
			if (e instanceof ConnectTimeoutException) {
				failCode = HttpFailCode.CONNECT_TIME_OUT;
			} else if (e instanceof SocketTimeoutException) {
				failCode = HttpFailCode.SOCKET_TIME_OUT;
			} else if (e instanceof UnknownHostException) {
				failCode = HttpFailCode.UN_KNOWN_HOST;
			} else if (e instanceof ConnectionPoolTimeoutException) {

			}
			createFailHttpResult(e, failCode, result);
			return result;
		}
		StatusLine status = response.getStatusLine();
		int statusCode = status.getStatusCode();
		CMLog.e("Result", "status code is " + statusCode);
		if (statusCode != HttpStatus.SC_OK) {
			result.setResultCode(HttpResult.RESULT_FAIL);
			if (statusCode == 404) {
				result.setFailCode(HttpFailCode.HTTP_404);
			} else if (statusCode >= 500) {
				result.setFailCode(HttpFailCode.SERVER_ERROR);
			} else {
				result.setFailCode(statusCode);
			}
			result.setFailMessage(status.getReasonPhrase());
			return result;
		}
		parseResponseToJson(response, result);
		return result;
	}

	public static HttpResultJson updateFile(Context context, String url, List<NameValuePair> params,
			NameValuePair fileNVPair) {
		HttpResultJson result = new HttpResultJson();
		if (!checkNetwork(context)) {
			result.setResultCode(HttpResult.RESULT_FAIL);
			result.setFailCode(HttpFailCode.NONETWORK);
			result.setFailMessage("not network");
			return result;
		}

		HttpClient client = CustomHttpClient.getInstance();
		CustomHttpPost post = null;
		try {
			post = new CustomHttpPost(url);
		} catch (IllegalArgumentException e) {
			createFailHttpResult(e, HttpFailCode.UNDEFINED, result);
			return result;
		}
		post.addHeader("Accept-Encoding", "GZIP");
		MultipartEntity entity = null;
		try {
			entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			if (params != null) {
				for (int i = 0; i < params.size(); i++) {
					NameValuePair pair = params.get(i);
//					StringBody stringBody = new StringBody(URLEncoder.encode(pair.getValue(), "UTF-8"));
//					FormBodyPart formBodyPart = new FormBodyPart(URLEncoder.encode(pair.getName()), stringBody);
					StringBody stringBody = new StringBody(pair.getValue());
					FormBodyPart formBodyPart = new FormBodyPart(pair.getName(), stringBody);
					entity.addPart(formBodyPart);
				}
			}
			if (fileNVPair != null) {
				File file = new File(fileNVPair.getValue());
				FileBody fileBody = new FileBody(file);
//				FormBodyPart formBodyPart = new FormBodyPart(URLEncoder.encode(fileNVPair.getName()), fileBody);
				FormBodyPart formBodyPart = new FormBodyPart(fileNVPair.getName(), fileBody);
				entity.addPart(formBodyPart);
			}
		} catch (UnsupportedEncodingException e) {
			createFailHttpResult(e, HttpFailCode.UNDEFINED, result);
			return result;
		}
		if (entity != null) {
			post.setEntity(entity);
		}
		HttpResponse response;
		try {
			response = executeHttpPost(client, post);
		} catch (Exception e) {
			int failCode = HttpFailCode.UNDEFINED;
			if (e instanceof ConnectTimeoutException) {
				failCode = HttpFailCode.CONNECT_TIME_OUT;
			} else if (e instanceof SocketTimeoutException) {
				failCode = HttpFailCode.SOCKET_TIME_OUT;
			} else if (e instanceof UnknownHostException) {
				failCode = HttpFailCode.UN_KNOWN_HOST;
			} else if (e instanceof ConnectionPoolTimeoutException) {

			}
			createFailHttpResult(e, failCode, result);
			return result;
		}
		StatusLine status = response.getStatusLine();
		int statusCode = status.getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			result.setResultCode(HttpResult.RESULT_FAIL);
			if (statusCode == 404) {
				result.setFailCode(HttpFailCode.HTTP_404);
			} else if (statusCode >= 500) {
				result.setFailCode(HttpFailCode.SERVER_ERROR);
			} else {
				result.setFailCode(statusCode);
			}
			result.setFailMessage(status.getReasonPhrase());
			return result;
		}
		parseResponseToJson(response, result);
		return result;
	}

	/**
	 * 以get方式,向服务端发送请求,并获取json格式的返回结果
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @return HttpResultJson
	 */
	public static HttpResultJson httpGetReturnJson(Context context, String url, List<NameValuePair> params) {

		HttpResultJson result = new HttpResultJson();

		if (!checkNetwork(context)) {
			result.setResultCode(HttpResult.RESULT_FAIL);
			result.setFailCode(HttpFailCode.NONETWORK);
			result.setFailMessage("not network");
			return result;
		}

		HttpClient client = CustomHttpClient.getInstance();
		if (params != null && params.size() > 0) {
			url += "?";
		}
		int i = 0;
		for (NameValuePair param : params) {
			if (i < params.size() - 1) {
				url += param.getName() + "=" + param.getValue() + "&";
			} else {
				url += param.getName() + "=" + param.getValue();
			}
			i++;
		}
		CMLog.e(TAG, "get_url=" + url);

		CustomHttpGet get = null;
		try {
			get = new CustomHttpGet(url);
		} catch (IllegalArgumentException e) {
			createFailHttpResult(e, HttpFailCode.UNDEFINED, result);
			return result;
		}
		get.addHeader("Accept-Encoding", "GZIP");
		HttpResponse response = null;
		try {
			response = executeHttpGet(client, get);
		} catch (Exception e) {
			int failCode = HttpFailCode.UNDEFINED;
			if (e instanceof ConnectTimeoutException) {
				failCode = HttpFailCode.CONNECT_TIME_OUT;
			} else if (e instanceof SocketTimeoutException) {
				failCode = HttpFailCode.SOCKET_TIME_OUT;
			} else if (e instanceof UnknownHostException) {
				failCode = HttpFailCode.UN_KNOWN_HOST;
			}
			createFailHttpResult(e, failCode, result);
			return result;
		}
		StatusLine status = response.getStatusLine();
		int statusCode = status.getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			result.setResultCode(HttpResult.RESULT_FAIL);
			if (statusCode == 404) {
				result.setFailCode(HttpFailCode.HTTP_404);
			} else if (statusCode >= 500) {
				result.setFailCode(HttpFailCode.SERVER_ERROR);
			} else {
				result.setFailCode(statusCode);
			}
			result.setFailMessage(status.getReasonPhrase());
			return result;
		}
		parseResponseToJson(response, result);
		return result;
	}

	/**
	 * 以post方式,向服务端发送请求,并获取xml格式的返回结果
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @return HttpResultXml
	 */
	public static HttpResultXml httpPostReturnXml(Context context, String url, List<NameValuePair> params) {
		HttpResultXml result = new HttpResultXml();
		if (!checkNetwork(context)) {
			result.setResultCode(HttpResult.RESULT_FAIL);
			result.setFailCode(HttpFailCode.NONETWORK);
			result.setFailMessage("not network");
			return result;
		}

		HttpClient client = CustomHttpClient.getInstance();
		CustomHttpPost post = null;
		try {
			post = new CustomHttpPost(url);
		} catch (IllegalArgumentException e) {
			createFailHttpResult(e, HttpFailCode.UNDEFINED, result);
			return result;
		}
		post.addHeader("Accept-Encoding", "GZIP");
		HttpEntity entity = null;
		try {
			if (params != null && params.size() > 0) {
				entity = new UrlEncodedFormEntity(params, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			createFailHttpResult(e, HttpFailCode.UNDEFINED, result);
			return result;
		}
		if (entity != null) {
			post.setEntity(entity);
		}
		HttpResponse response;
		try {
			response = executeHttpPost(client, post);
		} catch (Exception e) {
			int failCode = HttpFailCode.UNDEFINED;
			if (e instanceof ConnectTimeoutException) {
				failCode = HttpFailCode.CONNECT_TIME_OUT;
			} else if (e instanceof SocketTimeoutException) {
				failCode = HttpFailCode.SOCKET_TIME_OUT;
			} else if (e instanceof UnknownHostException) {
				failCode = HttpFailCode.UN_KNOWN_HOST;
			}
			createFailHttpResult(e, failCode, result);
			return result;
		}
		StatusLine status = response.getStatusLine();
		int statusCode = status.getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			result.setResultCode(HttpResult.RESULT_FAIL);
			if (statusCode == 404) {
				result.setFailCode(HttpFailCode.HTTP_404);
			} else if (statusCode >= 500) {
				result.setFailCode(HttpFailCode.SERVER_ERROR);
			} else {
				result.setFailCode(statusCode);
			}
			result.setFailMessage(status.getReasonPhrase());
			return result;
		}
		parseResponseToXml(response, result);
		return result;
	}

	public static HttpEntity downloadImg(Context context, String urlStr) throws Exception {
		CMLog.d("Downloader", "request url is " + urlStr);
		HttpClient client = CustomHttpClient.getInstance();
		CustomHttpGet get = new CustomHttpGet(urlStr);
		get.setResendEnabled(false);

		HttpResponse response = null;
		HttpEntity httpEntity = null;
		try {
			response = executeHttpGet(client, get);
			httpEntity = response.getEntity();
		} catch (Exception e) {
			CMLog.e(TAG, e);
			throw new RuntimeException("request url failed");
		}
		StatusLine status = response.getStatusLine();
		int statusCode = status.getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			if (statusCode == 404) {
				throw new ServerNotFileException("ServerNotFile");
			} else {
				throw new RuntimeException("request url failed");
			}
		}
		return httpEntity;
	}

	private static HttpResponse executeHttpGet(HttpClient client, CustomHttpGet get) throws IOException,
			ClientProtocolException {
		HttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(get);
		} catch (ClientProtocolException e) {
			throw e;
		} catch (IOException e) {
			if ((e instanceof ConnectTimeoutException || e instanceof SocketTimeoutException
					|| e instanceof UnknownHostException || e instanceof ConnectionPoolTimeoutException)
					&& get.isNeedResend()) {
				return executeHttpGet(client, get);
			} else {
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpResponse;
	}

	private static HttpResponse executeHttpPost(HttpClient client, CustomHttpPost post) throws IOException,
			ClientProtocolException {
		HttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(post);
		} catch (ClientProtocolException e) {
			throw e;
		} catch (IOException e) {
			if ((e instanceof ConnectTimeoutException || e instanceof SocketTimeoutException
					|| e instanceof UnknownHostException || e instanceof ConnectionPoolTimeoutException)
					&& post.isNeedResend()) {
				return executeHttpPost(client, post);
			} else {
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpResponse;
	}

	/**
	 * 从服务端返回的response中解析出JSONObject
	 * 
	 * @param response
	 * @param result
	 * @return HttpResultJson
	 */
	private static HttpResultJson parseResponseToJson(HttpResponse response, HttpResultJson result) {
		HttpEntity entity = response.getEntity();
		InputStream inputStream = null;
		try {
			inputStream = entity.getContent();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			bis.mark(2);
			byte[] header = new byte[2];
			int head;
			try {
				head = bis.read(header);
				bis.reset();
				/* gzip的数据是以0x1F8B开头，也就是GZIP的magic number */
				if (head != -1 && Utils.byteToShort(header) == 0x1f8b) {
					inputStream = new GZIPInputStream(bis);
				} else {
					inputStream = bis;
				}
			} catch (IOException e) {
				createFailHttpResult(e, HttpFailCode.UNDEFINED, result);
				return result;
			}
			// Read response into a buffered stream
			ByteArrayOutputStream content = new ByteArrayOutputStream();
			int readBytes = 0;
			byte[] sBuffer = new byte[512];
			while ((readBytes = inputStream.read(sBuffer)) != -1) {
				content.write(sBuffer, 0, readBytes);
			}
			inputStream.close();
			content.close();
			// Return result from buffered stream
			String jsonStr = new String(content.toByteArray());
			CMLog.e("Result", "result is " + jsonStr);
			JSONObject jsonObject;
			int jsonType = Utils.getJSONType(jsonStr);
			switch (jsonType) {
			case 1:
				jsonObject = new JSONObject(jsonStr);
				if ((jsonObject.has("code")&&200==jsonObject.getInt("code"))) {
					result.setResultCode(HttpResult.RESULT_OK);
					result.setJson(jsonObject);
				} else {
					result.setResultCode(HttpResult.RESULT_FAIL);
					result.setFailCode(jsonObject.getInt("code"));
					CMLog.e(TAG, "net-failCode=" + jsonObject.getInt("code"));
					if (!jsonObject.isNull("message")) {
						result.setFailMessage(jsonObject.getString("message"));
						CMLog.e(TAG, "net-failInfo=" + jsonObject.getString("message"));
					}
				}
				break;
			case 2:
				JSONArray jsonArray;
				jsonArray = new JSONArray(jsonStr);
				jsonObject = (JSONObject) jsonArray.opt(0);
				if ((jsonObject.has("code")&&200==jsonObject.getInt("code"))||jsonObject.has("access_token")
						||jsonObject.has("id")||jsonObject.has("uid")||(jsonObject.has("ret")&&0==jsonObject.getInt("ret"))) {
					result.setResultCode(HttpResult.RESULT_OK);
					result.setJson(jsonObject);
				} else {
					result.setResultCode(HttpResult.RESULT_FAIL);
					result.setFailCode(jsonObject.getInt("code"));
					CMLog.e(TAG, "net-failCode=" + jsonObject.getInt("code"));
					if (!jsonObject.isNull("message")) {
						result.setFailMessage(jsonObject.getString("message"));
						CMLog.e(TAG, "net-failInfo=" + jsonObject.getString("message"));
					}
				}
				break;

			default:
				break;
			}
			
			return result;
		} catch(JSONException e) {
			e.printStackTrace();
			return result;
		} catch (Exception e) {
			createFailHttpResult(e, HttpFailCode.UNDEFINED, result);
			return result;
		} finally {
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 从服务端返回的response中解析出XmlPullParser
	 * 
	 * @param response
	 * @param result
	 * @return
	 */
	private static HttpResultXml parseResponseToXml(HttpResponse response, HttpResultXml result) {
		HttpEntity entity = response.getEntity();
		InputStream inputStream = null;
		try {
			inputStream = entity.getContent();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			bis.mark(2);
			byte[] header = new byte[2];
			int head;
			head = bis.read(header);
			bis.reset();
			/* gzip的数据是以0x1F8B开头，也就是GZIP的magic number */
			if (head != -1 && Utils.byteToShort(header) == 0x1f8b) {
				inputStream = new GZIPInputStream(bis);
			} else {
				inputStream = bis;
			}
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(inputStream, "utf-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:
					if ("code".equalsIgnoreCase(parser.getName())) {
						String codeStr = parser.nextText();
						int code = Integer.parseInt(codeStr);
						if (code == 200) {
							result.setResultCode(HttpResult.RESULT_OK);
							result.setParser(parser);
							return result;
						} else {
							result.setResultCode(HttpResult.RESULT_FAIL);
							result.setFailCode(code);
							CMLog.e(TAG, "net-failCode=" + code);
						}
					} else if ("message".equalsIgnoreCase(parser.getName())) {
						String message = parser.nextText();
						result.setFailMessage(message);
						CMLog.e(TAG, "net-failInfo=" + message);
						return result;
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				}
				eventType = parser.next();
			}
			return result;
		} catch (Exception e) {
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (Exception e1) {
				}
			}
			createFailHttpResult(e, HttpFailCode.UNDEFINED, result);
			return result;
		}
	}

	/**
	 * 当发生异常时,创建含有failCode的HttpResult
	 * 
	 * @param e
	 * @param failCode
	 */
	private static void createFailHttpResult(Exception e, int failCode, HttpResult result) {
		CMLog.e(TAG, e);
		result.setResultCode(HttpResult.RESULT_FAIL);
		result.setFailCode(failCode);
		result.setFailMessage(e.toString());
	}

	public static boolean checkNetwork(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = conn.getActiveNetworkInfo();
		if (net != null && net.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}

class ServerNotFileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServerNotFileException(String string) {
		super(string);
	}
}
