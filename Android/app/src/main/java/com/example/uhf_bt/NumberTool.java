package com.example.uhf_bt;

import java.math.BigDecimal;

/**
 * 数字工具类
 * @author WuShengjun
 * @date 2017年2月22日
 */
public class NumberTool {

	/**
	 * 保留指定位数小数的double值
	 * @param point 保留几位小数
	 * @param val 需转换值
	 * @return
	 */
	public static double getPointDouble(int point, double val) {
		BigDecimal bd = new BigDecimal(val);
		return bd.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 保留指定位数小数的double值
	 * @param point 保留几位小数
	 * @param val 需转换值
	 * @return
	 */
	public static double getPointDouble(int point, int val) {
		BigDecimal bd = new BigDecimal(val);
		return bd.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 保留指定位数小数的double值
	 * @param point 保留几位小数
	 * @param val 需转换值
	 * @return
	 */
	public static double getPointDouble(int point, long val) {
		BigDecimal bd = new BigDecimal(val);
		return bd.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 保留指定位数小数的double值
	 * @param point 保留几位小数
	 * @param val 需转换值
	 * @return
	 */
	public static double getPointDouble(int point, String val) {
		BigDecimal bd = new BigDecimal(val);
		return bd.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
