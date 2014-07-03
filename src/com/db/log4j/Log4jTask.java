package com.db.log4j;

import java.util.*;

import javax.servlet.*;

import org.apache.log4j.Logger;

/**
 * 定时任务列表
 * 
 * @author Kenmy
 * @version 创建时间：2012-12-17 上午11:30:02
 */
public class Log4jTask extends TimerTask {
	private static boolean isRunning = false;
	private static Logger log = Logger.getLogger(Log4jTask.class);

	public Log4jTask(ServletContext context) {
	}

	@Override
	public void run() {
		if (!isRunning) {
			isRunning = true;
			log.info(Version.VERSION_INFO + " 开始执行指定任务");
			DeleteLogs.deleteLog(Log4jInitListener.FILE_FOLDER, Log4jInitListener.LOG_DAY, Log4jInitListener.LOG_NAME);
			log.info(Version.VERSION_INFO + " 开始执行指定任务 over");
			isRunning = false;
		} else {
			log.info("上一次任务执行还未结束");
		}
	}
}
