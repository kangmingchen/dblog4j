package com.db.log4j;

import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 定时器
 * 
 * @author Kenmy
 * @version 创建时间：2011-9-6 上午11:20:18
 */
public class TimerContextListener implements ServletContextListener {

	private Timer timer = null;

	public void contextInitialized(ServletContextEvent event) {
		Calendar cal = Calendar.getInstance();
		int minute = cal.get(Calendar.MINUTE);// 分
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min;
		/**
		 * C_SCHEDULE_HOUR属性设置每天的几点钟开始执行，24小时制
		 */
		final int C_SCHEDULE_HOUR = 23;
		if (hour >= C_SCHEDULE_HOUR) {
			min = (24 - (hour - C_SCHEDULE_HOUR)) * 60 - minute;
			if (min < 0) {
				min = minute;
			}
		} else {
			min = (C_SCHEDULE_HOUR - hour) * 60 - minute;
		}
		timer = new Timer(true);

		System.out.println(Version.VERSION_INFO + "定时器已启动");
		event.getServletContext();
		// schedule表示执行,后面跟的时间是多少秒开始执行，第二个是多长时间一个周期
		timer.schedule(new Log4jTask(event.getServletContext()), min * 60 * 1000, 24 * 60 * 60 * 1000);
		System.out.println(Version.VERSION_INFO + "已经添加任务调度表");
	}

	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		System.out.println(Version.VERSION_INFO + "定时器销毁");
	}

}
