package com.db.log4j;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

/**
 * 执行删除文件操作
 * 
 * @author Kenmy
 * @date 创建时间：2012-12-7 下午04:11:03
 */
public class DeleteLogs {

	private static Logger log = Logger.getLogger(DeleteLogs.class);

	/**
	 * 删除
	 * 
	 * @param path
	 *            日志文件路径
	 * @param day
	 *            日志保留天数 -1为不执行删除
	 * @param logName
	 *            保留的日志文件
	 */
	public static void deleteLog(String path, int day, String... logName) {
		if (day != -1) {
			Date date;
			Date dateNow;
			Calendar cal = Calendar.getInstance();
			Calendar calNow = Calendar.getInstance();
			if (path != "" && path != null) {
				File filepath = new File(path);
				File[] files;
				if (filepath.exists()) {
					if (filepath.isDirectory()) {
						files = filepath.listFiles();
						for (int i = 0; i < files.length; i++) {
							date = new Date(files[i].lastModified());
							dateNow = new Date();
							cal.setTime(date);
							calNow.setTime(dateNow);
							int year = cal.get(Calendar.YEAR);
							int yearNow = calNow.get(Calendar.YEAR);
							int dec = 0;
							if (yearNow - year > 0) {
								int days = new GregorianCalendar().isLeapYear(year) ? 366 : 365;
								dec = calNow.get(Calendar.DAY_OF_YEAR) + days - cal.get(Calendar.DAY_OF_YEAR);
							} else {
								dec = calNow.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR);
							}
							// 删除指定日期的文件
							if (dec >= day) {
								// 不允许删除log4j基础日志文件
								if (logName != null) {
									for (String s : logName) {
										if (files[i].getName().equals(s)) {
											continue;
										}
									}
								}
								files[i].delete();
								log.info("############ 删除日志文件 " + files[i].getName() + " ############");
							}
						}
					}
				}
			}
		} else {
			log.info("不执行删除日志");
		}
	}

	public static void main(String[] args) throws ParseException {
		deleteLog("D:\\logs\\DB_Spring", 1, "");

		// Calendar cal = Calendar.getInstance();
		// int year = cal.get(Calendar.YEAR);
		// System.out.println(year);
		//
		// Calendar cal1 = Calendar.getInstance();
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// cal1.setTime(df.parse("2013-12-06 00:00:00"));
		// int year1 = cal1.get(Calendar.YEAR);
		//
		// if (year - year1 > 0) {
		// int days = new GregorianCalendar().isLeapYear(year) ? 366 : 365;
		// int t = cal.get(Calendar.DAY_OF_YEAR) + days -
		// cal1.get(Calendar.DAY_OF_YEAR);
		// System.out.println(t);
		// }
		//
		// System.out.println(year1);
	}
}
