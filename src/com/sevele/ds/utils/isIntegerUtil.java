package com.sevele.ds.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class isIntegerUtil {
	/***
	 * 判断 String 是否是 int
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isInteger(String input) {
		Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
		return mer.find();
	}
}
