package com.east.customermanager.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.east.customermanager.log.CMLog;

/**
 * 评论
 * 
 */
public class Comment implements Parcelable {

	public static final int VOTE_UP = 1;
	public static final int VOTE_DOWN = -1;
	public static final int VOTE_NO = 0;
	private static final String TAG = "Comment";

	private int id;
	private String content;
	private int depth;
	private long created_time;
	private float score;
	private boolean assent;
	private int is_vote;
	private int downs;
	private int ups;
	private User user;

	private Comment parent;
	private ArrayList<Comment> children;
	/**
	 * 此节点在父节点中的顺序,重新排序后会有变换
	 */
	private int sort;
	private boolean toggle = true;

	/**
	 * 兄弟节点的数量
	 */
	private int brothersCount;
	/**
	 * 需要化竖线的深度级,会继承父节点的同属性(累加)
	 */
	private ArrayList<Integer> lineDepths;

	public Comment() {
		super();
	}

	Comment(Parcel in) {
		this.id = in.readInt();
		this.content = in.readString();
		this.depth = in.readInt();
		this.created_time = in.readLong();
		this.score = in.readFloat();
		this.assent = in.readInt() == 1 ? true : false;
		this.is_vote = in.readInt();
		this.downs = in.readInt();
		this.ups = in.readInt();
		this.user = in.readParcelable(User.class.getClassLoader());

		Parcelable[] childrenArray = in.readParcelableArray(Comment.class.getClassLoader());
		if (childrenArray != null && childrenArray.length > 0) {
			this.children = new ArrayList<Comment>();
			for (int i = 0; i < childrenArray.length; i++) {
				this.children.add((Comment) childrenArray[i]);
			}
		}

		this.sort = in.readInt();
		this.toggle = in.readInt() == 1 ? true : false;
		this.brothersCount = in.readInt();
		int lineDepthsArrayLength = in.readInt();
		if (lineDepthsArrayLength > 0) {
			int[] lineDepthsArray = new int[lineDepthsArrayLength];
			in.readIntArray(lineDepthsArray);
			if (lineDepthsArray != null && lineDepthsArray.length > 0) {
				this.lineDepths = new ArrayList<Integer>();
				for (int i = 0; i < lineDepthsArray.length; i++) {
					this.lineDepths.add(lineDepthsArray[i]);
				}
			}
		}
	}

	public static final Parcelable.Creator<Comment> CREATOR = new Creator<Comment>() {
		@Override
		public Comment[] newArray(int size) {
			return new Comment[size];
		}

		@Override
		public Comment createFromParcel(Parcel source) {
			return new Comment(source);
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
		dest.writeInt(depth);
		dest.writeLong(created_time);
		dest.writeFloat(score);
		dest.writeInt(assent ? 1 : 0);
		dest.writeInt(is_vote);
		dest.writeInt(downs);
		dest.writeInt(ups);
		dest.writeParcelable(user, flags);

		Comment[] childrenArray = new Comment[children == null ? 0 : children.size()];
		for (int i = 0; i < childrenArray.length; i++) {
			childrenArray[i] = children.get(i);
		}
		dest.writeParcelableArray(childrenArray, flags);

		dest.writeInt(sort);
		dest.writeInt(toggle ? 1 : 0);
		dest.writeInt(brothersCount);
		int lineDepthsArrayLength = lineDepths == null ? 0 : lineDepths.size();
		dest.writeInt(lineDepthsArrayLength);
		if (lineDepthsArrayLength > 0) {
			int[] lineDepthsArray = new int[lineDepthsArrayLength];
			for (int i = 0; i < lineDepthsArrayLength; i++) {
				lineDepthsArray[i] = lineDepths.get(i);
			}
			dest.writeIntArray(lineDepthsArray);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Comment) {
			if (this.id == ((Comment) obj).getId()) {
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
				setId(jsonObject.optInt("id"));
			}
			if (!jsonObject.isNull("content")) {
				setContent(jsonObject.optString("content"));
			}
			if (!jsonObject.isNull("depth")) {
				setDepth(jsonObject.optInt("depth"));
			}
			if (!jsonObject.isNull("created_time")) {
				setCreated_time(jsonObject.optLong("created_time"));
			}
			if (!jsonObject.isNull("score")) {
				setScore((float) jsonObject.optDouble("score"));
			}
			if (!jsonObject.isNull("assent")) {
				setAssent(jsonObject.optBoolean("assent"));
			}
			if (!jsonObject.isNull("is_vote")) {
				setIs_vote(jsonObject.optInt("is_vote"));
			}
			if (!jsonObject.isNull("downs")) {
				setDowns(jsonObject.optInt("downs"));
			}
			if (!jsonObject.isNull("ups")) {
				setUps(jsonObject.optInt("ups"));
			}
			computeLineDepths();
			if (!jsonObject.isNull("user")) {
				User user = new User();
				user.parseJson(jsonObject.optJSONObject("user"));
				setUser(user);
			}
			if (!jsonObject.isNull("children")) {
				JSONArray array = jsonObject.optJSONArray("children");
				if (array != null && array.length() > 0) {
					ArrayList<Comment> comments = new ArrayList<Comment>();
					for (int i = 0; i < array.length(); i++) {
						Comment comment = new Comment();
						comment.setBrothersCount(array.length());
						comment.setSort(i);
						comment.setParent(this);
						comment.parseJson((JSONObject) array.opt(i));
						comments.add(comment);
					}
					setChildren(comments);
				}
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
			jsonObject.put("depth", depth);
			jsonObject.put("created_time", created_time);
			jsonObject.put("score", score);
			jsonObject.put("assent", assent);
			jsonObject.put("is_vote", is_vote);
			jsonObject.put("downs", downs);
			jsonObject.put("ups", ups);
			if (user != null) {
				jsonObject.put("user", user.buildJson());
			}
			if (children != null && children.size() > 0) {
				JSONArray array = new JSONArray();
				for (int i = 0; i < children.size(); i++) {
					array.put(children.get(i).buildJson());
				}
				jsonObject.put("children", array);
			}
		} catch (JSONException e) {
			CMLog.e(TAG, e);
		}
		return jsonObject;
	}

	/**
	 * 计算画线的位置
	 */
	public void computeLineDepths() {
		if (depth > 0) {
			if (parent != null) {
				lineDepths = new ArrayList<Integer>();
				if (parent.lineDepths != null) {
					lineDepths.addAll(parent.lineDepths);
				}
			}
			if (sort < brothersCount - 1) {
				if (depth - 1 >= 0) {
					lineDepths.add(depth - 1);
				}
			}
		}
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

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public long getCreated_time() {
		return created_time;
	}

	public void setCreated_time(long created_time) {
		this.created_time = created_time;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public boolean isAssent() {
		return assent;
	}

	public void setAssent(boolean assent) {
		this.assent = assent;
	}

	public int getIs_vote() {
		return is_vote;
	}

	public void setIs_vote(int is_vote) {
		this.is_vote = is_vote;
	}

	public int getDowns() {
		return downs;
	}

	public void setDowns(int downs) {
		this.downs = downs;
	}

	public int getUps() {
		return ups;
	}

	public void setUps(int ups) {
		this.ups = ups;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Comment getParent() {
		return parent;
	}

	public void setParent(Comment parent) {
		this.parent = parent;
	}

	public ArrayList<Comment> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Comment> children) {
		this.children = children;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getBrothersCount() {
		return brothersCount;
	}

	public void setBrothersCount(int brothersCount) {
		this.brothersCount = brothersCount;
	}

	public ArrayList<Integer> getLineDepths() {
		return lineDepths;
	}

	public void setLineDepths(ArrayList<Integer> lineDepths) {
		this.lineDepths = lineDepths;
	}

	public boolean isToggle() {
		return toggle;
	}

	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}

}
