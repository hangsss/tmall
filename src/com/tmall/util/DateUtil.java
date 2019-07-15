package com.tmall.util;

import java.sql.Timestamp;
import java.util.Date;

public class DateUtil{
	//将Date的数据类型转为Timestamp
	public static Timestamp d2t(Date time) {
		if (time == null)
			return null;
		return new Timestamp(time.getTime());
	}
	//将Timestamp的数据类型转为Date
	public static Date t2d(Timestamp time) {
		if (time == null)
			return null;
		return new Date(time.getTime());
	}
}