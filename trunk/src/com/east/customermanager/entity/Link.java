package com.east.customermanager.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.east.customermanager.log.CMLog;
import com.east.customermanager.util.StringUtils;

/**
 * 封装资讯等数据(因资讯,段子,图片,谣言的数据结构一样,所以只用这一个实体封装)
 * 
 */
public class Link implements Parcelable {

	private static final String TAG = "Link";
	protected int id;
	protected String title = "";
	protected String summary = "";
	protected String url;
	protected String img_url;
	protected String original_img_url;
	protected int ups;
	protected boolean has_uped;
	protected int comments_count;
	// action_time 收藏,发布,推荐,评论的时间
	protected long action_time;
	protected long created_time;
	protected User submitted_user;
	protected ArrayList<Comment> comments;
	protected double score;
	protected int action;

	protected boolean has_saved;
	protected boolean has_read;
	private int subject_id;
	private boolean is_break;
	private long time_into_pool;
	private int fetchType;
	private String phone_content;

	public Link() {
		super();
	}

	Link(Parcel in) {
		this.id = in.readInt();
		this.title = in.readString();
		this.summary = in.readString();
		this.url = in.readString();
		this.img_url = in.readString();
		this.original_img_url = in.readString();
		this.ups = in.readInt();
		this.has_uped = in.readInt() == 1 ? true : false;
		this.comments_count = in.readInt();
		this.action_time = in.readLong();
		this.created_time = in.readLong();
		this.submitted_user = in.readParcelable(User.class.getClassLoader());

		Parcelable[] commentArray = in.readParcelableArray(Comment.class.getClassLoader());
		if (commentArray != null && commentArray.length > 0) {
			this.comments = new ArrayList<Comment>();
			for (int i = 0; i < commentArray.length; i++) {
				this.comments.add((Comment) commentArray[i]);
			}
		}

		this.score = in.readDouble();
		this.action = in.readInt();
		this.has_saved = in.readInt() == 1 ? true : false;
		this.has_read = in.readInt() == 1 ? true : false;
		this.subject_id = in.readInt();
		this.is_break = in.readInt() == 1 ? true : false;
		this.time_into_pool = in.readLong();
		this.fetchType = in.readInt();
		this.phone_content = in.readString();
	}

	public static final Parcelable.Creator<Link> CREATOR = new Creator<Link>() {
		@Override
		public Link[] newArray(int size) {
			return new Link[size];
		}

		@Override
		public Link createFromParcel(Parcel source) {
			return new Link(source);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(summary);
		dest.writeString(url);
		dest.writeString(img_url);
		dest.writeString(original_img_url);
		dest.writeInt(ups);
		dest.writeInt(has_uped ? 1 : 0);
		dest.writeInt(comments_count);
		dest.writeLong(action_time);
		dest.writeLong(created_time);
		dest.writeParcelable(submitted_user, flags);

		Comment[] commentArray = new Comment[comments == null ? 0 : comments.size()];
		for (int i = 0; i < commentArray.length; i++) {
			commentArray[i] = comments.get(i);
		}
		dest.writeParcelableArray(commentArray, flags);

		dest.writeDouble(score);
		dest.writeInt(action);
		dest.writeInt(has_saved ? 1 : 0);
		dest.writeInt(has_read ? 1 : 0);
		dest.writeInt(subject_id);
		dest.writeInt(is_break ? 1 : 0);
		dest.writeLong(time_into_pool);
		dest.writeInt(fetchType);
		dest.writeString(phone_content);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Link) {
			if (this.id == ((Link) obj).getId()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将json数据,解析为link实体
	 * 
	 * @param jsonObject
	 */
	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull("id")) {
				setId(jsonObject.optInt("id"));
			}
			if (!jsonObject.isNull("title")) {
				setTitle(jsonObject.optString("title"));
			}
			if (!jsonObject.isNull("summary")) {
				setSummary(jsonObject.optString("summary"));
			}
			if (!jsonObject.isNull("originalUrl")) {
				setUrl(jsonObject.optString("originalUrl"));
			}
			if (!jsonObject.isNull("url") && StringUtils.isNullOrEmpty(url)) {
				setUrl(jsonObject.optString("url"));
			}
			if (!jsonObject.isNull("img_url")) {
				setImg_url(jsonObject.optString("img_url"));
			}
			if (!jsonObject.isNull("original_img_url")) {
				this.original_img_url = jsonObject.optString("original_img_url");
			}
			if (!jsonObject.isNull("ups")) {
				setUps(jsonObject.optInt("ups"));
			}
			if (!jsonObject.isNull("has_uped")) {
				setHas_uped(jsonObject.optBoolean("has_uped"));
			}
			if (!jsonObject.isNull("comments_count")) {
				setComments_count(jsonObject.optInt("comments_count"));
			}
			if (!jsonObject.isNull("action_time")) {
				setAction_time(jsonObject.optLong("action_time"));
			}
			if (!jsonObject.isNull("created_time")) {
				setCreated_time(jsonObject.optLong("created_time"));
			}
			if (!jsonObject.isNull("has_saved")) {
				setHas_saved(jsonObject.optBoolean("has_saved"));
			}
			if (!jsonObject.isNull("has_read")) {
				setHas_read(jsonObject.optBoolean("has_read"));
			}
			if (!jsonObject.isNull("score")) {
				setScore(jsonObject.optDouble("score"));
			}
			if (!jsonObject.isNull("submitted_user")) {
				User user = new User();
				user.parseJson(jsonObject.optJSONObject("submitted_user"));
				setSubmitted_user(user);
			}
			if (!jsonObject.isNull("comments")) {
				JSONArray array = jsonObject.optJSONArray("comments");
				if (array != null && array.length() > 0) {
					ArrayList<Comment> comments = new ArrayList<Comment>();
					for (int i = 0; i < array.length(); i++) {
						Comment comment = new Comment();
						comment.setBrothersCount(array.length());
						comment.setSort(i);
						comment.parseJson((JSONObject) array.opt(i));
						comments.add(comment);
					}
					this.setComments(comments);
				}
			}
			if (!jsonObject.isNull("subject_id")) {
				this.setSubject_id(jsonObject.optInt("subject_id"));
			}
			if (!jsonObject.isNull("is_break")) {
				this.setIs_break(jsonObject.optBoolean("is_break"));
			}
			if (!jsonObject.isNull("time_into_pool")) {
				this.setTime_into_pool(jsonObject.optLong("time_into_pool"));
			}
			if (!jsonObject.isNull("action")) {
				this.setAction(jsonObject.optInt("action"));
			}
			if (!jsonObject.isNull("fetchType")) {
				this.fetchType = jsonObject.optInt("fetchType");
			}
			if (!jsonObject.isNull("phone_content")) {
				this.phone_content = jsonObject.optString("phone_content");
			}
		}
	}

	/**
	 * 将link 构建成JSONObject
	 * 
	 * @return JSONObject
	 */
	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", id);
			jsonObject.put("title", title);
			jsonObject.put("summary", summary);
			jsonObject.put("originalUrl", url);
			jsonObject.put("img_url", img_url);
			jsonObject.put("original_img_url", original_img_url);
			jsonObject.put("ups", ups);
			jsonObject.put("has_uped", has_uped);
			jsonObject.put("comments_count", comments_count);
			jsonObject.put("action_time", action_time);
			jsonObject.put("created_time", created_time);
			jsonObject.put("has_saved", has_saved);
			jsonObject.put("has_read", has_read);
			jsonObject.put("score", score);
			if (submitted_user != null) {
				jsonObject.put("submitted_user", submitted_user.buildJson());
			}
			if (comments != null && comments.size() > 0) {
				JSONArray array = new JSONArray();
				for (int i = 0; i < comments.size(); i++) {
					array.put(comments.get(i).buildJson());
				}
				jsonObject.put("comments", array);
			}
			jsonObject.put("subject_id", subject_id);
			jsonObject.put("is_break", is_break);
			jsonObject.put("time_into_pool", time_into_pool);
			jsonObject.put("action", action);
			jsonObject.put("fetchType", fetchType);
			jsonObject.put("phone_content", phone_content);
		} catch (JSONException e) {
			CMLog.e(TAG, e);
		}
		return jsonObject;
	}

	public String getPhone_content() {
		return phone_content;
	}

	public void setPhone_content(String phone_content) {
		this.phone_content = phone_content;
	}

	public int getFetchType() {
		return fetchType;
	}

	public void setFetchType(int fetchType) {
		this.fetchType = fetchType;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getOriginal_img_url() {
		return original_img_url;
	}

	public void setOriginal_img_url(String original_img_url) {
		this.original_img_url = original_img_url;
	}

	public int getUps() {
		return ups;
	}

	public void setUps(int ups) {
		this.ups = ups;
	}

	public boolean isHas_uped() {
		return has_uped;
	}

	public void setHas_uped(boolean has_uped) {
		this.has_uped = has_uped;
	}

	public int getComments_count() {
		return comments_count;
	}

	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}

	public User getSubmitted_user() {
		return submitted_user;
	}

	public void setSubmitted_user(User submitted_user) {
		this.submitted_user = submitted_user;
	}

	public long getAction_time() {
		return action_time;
	}

	public void setAction_time(long action_time) {
		this.action_time = action_time;
	}

	public long getCreated_time() {
		return created_time;
	}

	public void setCreated_time(long created_time) {
		this.created_time = created_time;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	public boolean isHas_saved() {
		return has_saved;
	}

	public void setHas_saved(boolean has_saved) {
		this.has_saved = has_saved;
	}

	public boolean isHas_read() {
		return has_read;
	}

	public void setHas_read(boolean has_read) {
		this.has_read = has_read;
	}

	public int getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}

	public boolean isIs_break() {
		return is_break;
	}

	public void setIs_break(boolean is_break) {
		this.is_break = is_break;
	}

	public long getTime_into_pool() {
		return time_into_pool;
	}

	public void setTime_into_pool(long time_into_pool) {
		this.time_into_pool = time_into_pool;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

}
