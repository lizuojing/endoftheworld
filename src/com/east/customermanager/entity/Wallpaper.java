package com.east.customermanager.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.east.customermanager.log.CMLog;

public class Wallpaper implements Parcelable {
	private static final String TAG = "Wallpaper";
	
	private int id;
	private String day_img_url;
	private String night_img_url;
	private String desc;
	private long start_time;
	private long end_time;

	public Wallpaper() {
		super();
	}

	Wallpaper(Parcel in) {
		this.id = in.readInt();
		this.day_img_url = in.readString();
		this.night_img_url = in.readString();
		this.desc = in.readString();
		this.start_time = in.readLong();
		this.end_time = in.readLong();
	}

	public static final Parcelable.Creator<Wallpaper> CREATOR = new Creator<Wallpaper>() {
		@Override
		public Wallpaper[] newArray(int size) {
			return new Wallpaper[size];
		}

		@Override
		public Wallpaper createFromParcel(Parcel source) {
			return new Wallpaper(source);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(day_img_url);
		dest.writeString(night_img_url);
		dest.writeString(desc);
		dest.writeLong(start_time);
		dest.writeLong(end_time);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Wallpaper) {
			if (this.id == ((Wallpaper) obj).getId()) {
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
			if (!jsonObject.isNull("id")) {
				this.id=jsonObject.optInt("id");
			}
			if (!jsonObject.isNull("day_img_url")) {
				this.day_img_url=jsonObject.optString("day_img_url");
			}
			if (!jsonObject.isNull("night_img_url")) {
				this.night_img_url=jsonObject.optString("night_img_url");
			}
			if (!jsonObject.isNull("desc")) {
				this.desc=jsonObject.optString("desc");
			}
			if (!jsonObject.isNull("start_time")) {
				this.start_time=jsonObject.optLong("start_time");
			}
			if (!jsonObject.isNull("end_time")) {
				this.end_time=jsonObject.optLong("end_time");
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
			jsonObject.put("id", id);
			jsonObject.put("day_img_url", day_img_url);
			jsonObject.put("night_img_url", night_img_url);
			jsonObject.put("desc", desc);
			jsonObject.put("start_time", start_time);
			jsonObject.put("end_time", end_time);
		} catch (JSONException e) {
			CMLog.e(TAG, e);
		}
		return jsonObject;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDay_img_url() {
		return day_img_url;
	}

	public void setDay_img_url(String day_img_url) {
		this.day_img_url = day_img_url;
	}

	public String getNight_img_url() {
		return night_img_url;
	}

	public void setNight_img_url(String night_img_url) {
		this.night_img_url = night_img_url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

}
