package com.east.customermanager.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	/**
	 * 将byte数组转换为字符串,
	 * 
	 * @param array
	 *            byte数组
	 * @param length
	 *            截取的长度,从0开始,因中文字符等原因,实际截取的长度可能小于length
	 * @param charsetName
	 *            生成的字符串的字符编码
	 * @return
	 */
	public static String getStringFromByteArray(byte[] array, int length, String charsetName) {
		String str = "";
		int sig = 1;
		if (length <= array.length) {
			for (int i = 0; i < length; i++) {
				sig = array[i] * sig >= 0 ? 1 : -1;
			}
			if (sig < 0) {
				length -= 1;
			}
			try {
				str = new String(array, 0, length, charsetName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	public static boolean isNullOrEmpty(String str) {
		boolean flag = true;
		if (str != null && !"".equals(str.trim())) {
			flag = false;
		}
		return flag;
	}

	public static boolean isNotNullOrEmpty(String str) {
		return !isNullOrEmpty(str);
	}

	public static boolean isNickname(final String str) {
		try {
			byte[] bytes=str.getBytes("gbk");
			if(bytes.length>20){
				return false;
			}
		} catch (UnsupportedEncodingException e) {
		}
		final String regex = "[\\u4e00-\\u9fa5\\w]{1,}";
		return match(regex, str);
	}
	
	public static boolean isEmail(final String str) {
		final String regex = "^[a-zA-Z0-9]{1,}[a-zA-Z0-9\\_\\.\\-]{0,}@(([a-zA-Z0-9]){1,}\\.){1,3}[a-zA-Z0-9]{0,}[a-zA-Z]{1,}$";
		return match(regex, str);
	}

	public static boolean isRegUserName(final String str) {
		final String regex = "[0-9a-zA-Z\\_]{5,20}";
		return match(regex, str);
	}
	
	public static boolean isLoginUserName(final String str) {
		final String regex = "[0-9a-zA-Z\\_]{3,20}";
		return match(regex, str);
	}

	public static boolean isPassword(final String str) {
		final String regex = "[\\S]{6,15}";
		return match(regex, str);
	}

	private static boolean match(final String regex, final String str) {
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}

	/**
	 * 修改过长的文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String modifyLongFileName(String filePath) {
		if (filePath != null && filePath.length() > 220) {
			filePath = filePath.substring(0, 100) + filePath.substring(filePath.length() - 130, filePath.length());
		}
		return filePath;
	}
}
