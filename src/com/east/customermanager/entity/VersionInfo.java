package com.east.customermanager.entity;

/**
 * 版本信息,客户端升级时使用
 */
public class VersionInfo {

	public enum UpdateType {
		NO_UPDATE, UPDATE_AND_PROMPT, UPDATE_NO_PROMPT
	}

	private UpdateType updateType;

	private String versionId;
	private String name;
	private String type;
	private String version;
	private String buildno;
	private String url;
	private String wurl;
	private String ssize;
	private String describe;
	private long createTime;

	public UpdateType getUpdateType() {
		return updateType;
	}

	public void setUpdateType(UpdateType updateType) {
		this.updateType = updateType;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuildno() {
		return buildno;
	}

	public void setBuildno(String buildno) {
		this.buildno = buildno;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWurl() {
		return wurl;
	}

	public void setWurl(String wurl) {
		this.wurl = wurl;
	}

	public String getSsize() {
		return ssize;
	}

	public void setSsize(String ssize) {
		this.ssize = ssize;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
