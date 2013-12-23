package com.east.customermanager.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.east.customermanager.log.CMLog;

public class User implements Parcelable {
	private static final String TAG = "User";
	private String jid;
	private String nick;
	private boolean sex;
	private String img_url;
	private int submitted_count;
	private int liked_count;
	private int comments_count;
	private int save_count;
	private boolean isBindPhone;

	public User() {
		super();
	}

	User(Parcel in) {
		this.jid = in.readString();
		this.nick = in.readString();
		this.sex = in.readInt() == 1 ? true : false;
		this.img_url = in.readString();
		this.submitted_count = in.readInt();
		this.liked_count = in.readInt();
		this.comments_count = in.readInt();
		this.save_count=in.readInt();
		this.isBindPhone = in.readInt() == 1 ? true : false;
	}

	public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User[] newArray(int size) {
			return new User[size];
		}

		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(jid);
		dest.writeString(nick);
		dest.writeInt(sex ? 1 : 0);
		dest.writeString(img_url);
		dest.writeInt(submitted_count);
		dest.writeInt(liked_count);
		dest.writeInt(comments_count);
		dest.writeInt(save_count);
		dest.writeInt(isBindPhone ? 1 : 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Comment) {
			if (this.jid != null && this.jid.equalsIgnoreCase(((User) obj).getJid())) {
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
			if (!jsonObject.isNull("jid")) {
				setJid(jsonObject.optString("jid"));
			}
			if (!jsonObject.isNull("nick")) {
				setNick(jsonObject.optString("nick"));
			}
			if (!jsonObject.isNull("sex")) {
				setSex(jsonObject.optBoolean("sex"));
			}
			if (!jsonObject.isNull("img_url")) {
				setImg_url(jsonObject.optString("img_url"));
			}
			if (!jsonObject.isNull("submitted_count")) {
				setSubmitted_count(jsonObject.optInt("submitted_count"));
			}
			if (!jsonObject.isNull("liked_count")) {
				setLiked_count(jsonObject.optInt("liked_count"));
			}
			if (!jsonObject.isNull("comments_count")) {
				setComments_count(jsonObject.optInt("comments_count"));
			}
			if (!jsonObject.isNull("save_count")) {
				setSave_count(jsonObject.optInt("save_count"));
			}
			if (!jsonObject.isNull("isBindPhone")) {
				setBindPhone(jsonObject.optBoolean("isBindPhone"));
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
			jsonObject.put("jid", jid);
			jsonObject.put("nick", nick);
			jsonObject.put("sex", sex);
			jsonObject.put("img_url", img_url);
			jsonObject.put("submitted_count", submitted_count);
			jsonObject.put("liked_count", liked_count);
			jsonObject.put("comments_count", comments_count);
			jsonObject.put("save_count", save_count);
			jsonObject.put("isBindPhone", isBindPhone);
		} catch (JSONException e) {
			CMLog.e(TAG, e);
		}
		return jsonObject;
	}

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public boolean getSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public int getSubmitted_count() {
		return submitted_count;
	}

	public void setSubmitted_count(int submitted_count) {
		this.submitted_count = submitted_count;
	}

	public int getLiked_count() {
		return liked_count;
	}

	public void setLiked_count(int liked_count) {
		this.liked_count = liked_count;
	}

	public int getComments_count() {
		return comments_count;
	}

	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}

	public int getSave_count() {
		return save_count;
	}

	public void setSave_count(int save_count) {
		this.save_count = save_count;
	}

	public boolean isBindPhone() {
		return isBindPhone;
	}

	public void setBindPhone(boolean isBindPhone) {
		this.isBindPhone = isBindPhone;
	}

}
