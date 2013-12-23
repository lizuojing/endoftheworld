package com.east.customermanager.config;

public class Config {
	/**
	 * 启动页停留时间配置
	 */
	public static final int STARTUP_MIN_REMAIN_TIME = 2000;
	public static final int STARTUP_MAX_REMAIN_TIME = 5000;

	/**
	 * 首页配置
	 */
	public static final int MAIN_LOAD_TIME_INTERVAL_HOT = 20 * 60 * 1000;
	public static final int MAIN_LOAD_TIME_INTERVAL_OTHER = 15 * 60 * 1000;

	/**
	 * 评论页配置
	 */
	// 发表评论,最多可输入150个中文字符,即300个字节;
	public static final int COMMENT_TEXT_COUNT_LIMIT = 300;
	// 分享输入框字数限制
	public static final int SHARE_POPUP_INPUT_LIMIT = 280;
	// 发布段子输入框字数限制
	public static final int PUBLIC_TEXT_INPUT_LIMIT = 300;

	/**
	 * 统计相关配置
	 */

	// GOZAP统计的开关
	public static final boolean ANALYTICS_GOZAP_ENABLE = true;

	/**
	 * 设置参数配置
	 */

	public static final String SHARED_PREFS_NAME_SETTING = "shared_prefs_name_setting";
	public static final String SHARED_PREFS_NAME_PRE_LOAD_TIME = "shared_prefs_name_pre_load_time";
	public static final String SHARED_PREFS_NAME_USER = "shared_prefs_name_user";
	public static final String SHARED_PREFS_NAME_NOTIFACTION = "shared_prefs_name_notifaction";
	public static final String SHARED_PREFS_NAME_PUSH = "shared_prefs_name_push";

}
