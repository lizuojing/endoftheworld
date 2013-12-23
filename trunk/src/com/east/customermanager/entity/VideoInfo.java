package com.east.customermanager.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.east.customermanager.log.CMLog;

public class VideoInfo implements Parcelable {
	private static final String TAG = "VideoInfo";
	private long created_time;
	private String imgUrl;
	private String site;
	private String url;
	private String[] videoUrlArray;

	public VideoInfo() {
		super();
	}

	VideoInfo(Parcel in) {
		this.created_time = in.readLong();
		this.imgUrl = in.readString();
		this.site = in.readString();
		this.url = in.readString();
		try {
			this.videoUrlArray = (String[]) in.readArray(String.class.getClassLoader());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final Parcelable.Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
		@Override
		public VideoInfo[] newArray(int size) {
			return new VideoInfo[size];
		}

		@Override
		public VideoInfo createFromParcel(Parcel source) {
			return new VideoInfo(source);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(created_time);
		dest.writeString(imgUrl);
		dest.writeString(site);
		dest.writeString(url);
		dest.writeStringArray(videoUrlArray);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Comment) {
			if (this.url != null && this.url.equalsIgnoreCase(((VideoInfo) obj).getUrl())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将json数据解析为user实体
	 * 
	 * @param jsonObject
	 */
	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull("created_time")) {
				this.created_time = jsonObject.optLong("created_time");
			}
			if (!jsonObject.isNull("imgUrl")) {
				this.imgUrl = jsonObject.optString("imgUrl");
			}
			if (!jsonObject.isNull("site")) {
				this.site = jsonObject.optString("site");
			}
			if (!jsonObject.isNull("url")) {
				this.url = jsonObject.optString("url");
			}
			if (!jsonObject.isNull("videoUrlArray")) {
				JSONArray jsonArray = jsonObject.optJSONArray("videoUrlArray");
				if (jsonArray != null) {
					int length = jsonArray.length();
					if (length > 0) {
						videoUrlArray = new String[length];
						for (int i = 0; i < length; i++) {
							videoUrlArray[i] = jsonArray.optString(i);
						}
					}
				}
			}

		}
	}

	/**
	 * 将user 构建成JSONObject
	 * 
	 * @return JSONObject
	 */
	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("created_time", created_time);
			jsonObject.put("imgUrl", imgUrl);
			jsonObject.put("site", site);
			jsonObject.put("url", url);
			if (videoUrlArray != null) {
				int length = videoUrlArray.length;
				if (length > 0) {
					JSONArray array = new JSONArray();
					for (int i = 0; i < length; i++) {
						array.put(videoUrlArray[i]);
					}
					jsonObject.put("videoUrlArray", videoUrlArray);
				}
			}
		} catch (JSONException e) {
			CMLog.e(TAG, e);
		}
		return jsonObject;
	}

	public long getCreated_time() {
		return created_time;
	}

	public void setCreated_time(long created_time) {
		this.created_time = created_time;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String[] getVideoUrlArray() {
		return videoUrlArray;
	}

	public void setVideoUrlArray(String[] videoUrlArray) {
		this.videoUrlArray = videoUrlArray;
	}

}
