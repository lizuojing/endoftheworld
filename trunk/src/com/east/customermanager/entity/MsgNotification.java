package com.east.customermanager.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.east.customermanager.log.CMLog;

/**
 * 评论
 * 
 */
public class MsgNotification implements Parcelable {

	private static final String TAG = "MsgNotify";

	private int id;
	private int commentId;
	private int linksId;
	private int messageId;
	private long createtime;
	private String srcContent;
	private String commentContent;
	private boolean isRead;
	private User destUser;
	private User fromUser;

	public MsgNotification() {
		super();
	}

	MsgNotification(Parcel in) {
		this.id = in.readInt();
		this.commentId = in.readInt();
		this.linksId = in.readInt();
		this.messageId = in.readInt();
		this.createtime = in.readLong();
		this.srcContent = in.readString();
		this.commentContent = in.readString();
		this.isRead = in.readInt() == 1 ? true : false;
		this.destUser = in.readParcelable(User.class.getClassLoader());
		this.fromUser = in.readParcelable(User.class.getClassLoader());
	}

	public static final Parcelable.Creator<MsgNotification> CREATOR = new Creator<MsgNotification>() {
		@Override
		public MsgNotification[] newArray(int size) {
			return new MsgNotification[size];
		}

		@Override
		public MsgNotification createFromParcel(Parcel source) {
			return new MsgNotification(source);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(commentId);
		dest.writeInt(linksId);
		dest.writeInt(messageId);
		dest.writeLong(createtime);
		dest.writeString(srcContent);
		dest.writeString(commentContent);
		dest.writeInt(isRead ? 1 : 0);
		dest.writeParcelable(destUser, flags);
		dest.writeParcelable(fromUser, flags);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof MsgNotification) {
			if (this.id == ((MsgNotification) obj).getId()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将json数据解析为 commnet
	 * 
	 * @param jsonObject
	 */
	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull("id")) {
				this.id = jsonObject.optInt("id");
			}
			if (!jsonObject.isNull("commentId")) {
				this.commentId = jsonObject.optInt("commentId");
			}
			if (!jsonObject.isNull("linksId")) {
				this.linksId = jsonObject.optInt("linksId");
			}
			if (!jsonObject.isNull("messageId")) {
				this.messageId = jsonObject.optInt("messageId");
			}
			if (!jsonObject.isNull("createtime")) {
				this.createtime = jsonObject.optLong("createtime");
			}
			if (!jsonObject.isNull("srcContent")) {
				this.srcContent = jsonObject.optString("srcContent");
			}
			if (!jsonObject.isNull("commentContent")) {
				this.commentContent = jsonObject.optString("commentContent");
			}
			if (!jsonObject.isNull("isRead")) {
				this.isRead=jsonObject.optBoolean("isRead");
			}
			if (!jsonObject.isNull("destUser")) {
				User user = new User();
				user.parseJson(jsonObject.optJSONObject("destUser"));
				this.destUser = user;
			}
			if (!jsonObject.isNull("fromUser")) {
				User user = new User();
				user.parseJson(jsonObject.optJSONObject("fromUser"));
				this.fromUser = user;
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
			jsonObject.put("commentId", commentId);
			jsonObject.put("linksId", linksId);
			jsonObject.put("messageId", messageId);
			jsonObject.put("createtime", createtime);
			jsonObject.put("srcContent", srcContent);
			jsonObject.put("commentContent", commentContent);
			jsonObject.put("isRead", isRead);
			if (destUser != null) {
				jsonObject.put("destUser", destUser.buildJson());
			}
			if (fromUser != null) {
				jsonObject.put("fromUser", fromUser.buildJson());
			}
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

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getLinksId() {
		return linksId;
	}

	public void setLinksId(int linksId) {
		this.linksId = linksId;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public String getSrcContent() {
		return srcContent;
	}

	public void setSrcContent(String srcContent) {
		this.srcContent = srcContent;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public User getDestUser() {
		return destUser;
	}

	public void setDestUser(User destUser) {
		this.destUser = destUser;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

}
