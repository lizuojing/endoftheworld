package com.east.customermanager.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.east.customermanager.log.CMLog;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonComment implements Parcelable {

	private static final String TAG = "PersonComment";

	private int id;
	private String content;
	private long created_time;
	private int link_id;
	private String link_title;

	public PersonComment() {
		super();
	}

	PersonComment(Parcel in) {
		this.id = in.readInt();
		this.content = in.readString();
		this.created_time = in.readLong();
		this.link_id = in.readInt();
		this.link_title = in.readString();
	}

	public static final Parcelable.Creator<PersonComment> CREATOR = new Creator<PersonComment>() {
		@Override
		public PersonComment[] newArray(int size) {
			return new PersonComment[size];
		}

		@Override
		public PersonComment createFromParcel(Parcel source) {
			return new PersonComment(source);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(content);
		dest.writeLong(created_time);
		dest.writeInt(link_id);
		dest.writeString(link_title);
	}

	/**
	 * 将json数据解析为 commnet
	 * 
	 * @param jsonObject
	 */
	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull("id")) {
				setId(jsonObject.optInt("id"));
			}
			if (!jsonObject.isNull("content")) {
				setContent(jsonObject.optString("content"));
			}
			if (!jsonObject.isNull("created_time")) {
				setCreated_time(jsonObject.optLong("created_time"));
			}
			if (!jsonObject.isNull("link_id")) {
				setLink_id(jsonObject.optInt("link_id"));
			}
			if (!jsonObject.isNull("link_title")) {
				setLink_title(jsonObject.optString("link_title"));
			}
		}
	}

	/**
	 * 将commnet 构建成 JSONObject
	 * 
	 * @return JSONObject
	 */
	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", id);
			jsonObject.put("content", content);
			jsonObject.put("created_time", created_time);
			jsonObject.put("link_id", link_id);
			jsonObject.put("link_title", link_title);
		} catch (JSONException e) {
			CMLog.e(TAG, e);
		}
		return jsonObject;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof PersonComment) {
			if (this.id == ((PersonComment) obj).getId()) {
				return true;
			}
		}
		return false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreated_time() {
		return created_time;
	}

	public void setCreated_time(long created_time) {
		this.created_time = created_time;
	}

	public int getLink_id() {
		return link_id;
	}

	public void setLink_id(int link_id) {
		this.link_id = link_id;
	}

	public String getLink_title() {
		return link_title;
	}

	public void setLink_title(String link_title) {
		this.link_title = link_title;
	}

}
