package com.east.customermanager.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.east.customermanager.log.CMLog;


public class Subject implements Parcelable{

	private static final String TAG = "Subject";
    private int id;
	private String name;
	private String name_cn;
	private String name_en;
	private String uri;
	
	public Subject() {
		super();
	}

	public Subject(int id, boolean is_prefix, String name, String name_cn, String name_en, String uri) {
		super();
		this.id = id;
		this.name = name;
		this.name_cn = name_cn;
		this.name_en = name_en;
		this.uri = uri;
	}

	Subject(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.name_cn = in.readString();
		this.name_en = in.readString();
		this.uri = in.readString();
	}
	
	public static final Parcelable.Creator<Subject> CREATOR = new Creator<Subject>() {
		@Override
		public Subject[] newArray(int size) {
			return new Subject[size];
		}
		@Override
		public Subject createFromParcel(Parcel source) {
			return new Subject(source);
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(name_cn);
		dest.writeString(name_en);
		dest.writeString(uri);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null&&obj instanceof Subject){
			if(this.id==((Subject)obj).getId()){
				return true;
			}
		}
		return false;
	}

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull("id")) {
				setId(jsonObject.optInt("id"));
			}
			if (!jsonObject.isNull("name")) {
				setName(jsonObject.optString("name"));
			}
			if (!jsonObject.isNull("name_cn")) {
				setName_cn(jsonObject.optString("name_cn"));
			}
			if (!jsonObject.isNull("name_en")) {
				setName_en(jsonObject.optString("name_en"));
			}
			if (!jsonObject.isNull("uri")) {
				setUri(jsonObject.optString("uri"));
			}
		}
	}
	
	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", id);
			jsonObject.put("name", name);
			jsonObject.put("name_cn", name_cn);
			jsonObject.put("name_en", name_en);
			jsonObject.put("uri", uri);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_cn() {
		return name_cn;
	}

	public void setName_cn(String name_cn) {
		this.name_cn = name_cn;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
