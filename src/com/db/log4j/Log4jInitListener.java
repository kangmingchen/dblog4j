package com.db.log4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.PropertyConfigurator;

public class Log4jInitListener implements ServletContextListener, HttpSessionListener {

	private static final String CONF_FILE = "db_log4j";

	public Log4jInitListener() {

	}

	public void contextDestroyed(ServletContextEvent event) {
	}

	/**
	 * 日志文件根目录
	 */
	public static String FILE_FOLDER = "";
	
	/**
	 * 项目名称
	 */
	public static String PROJECT_NAME="";
	
	/**
	 * 日志保留天数，默认10天
	 */
	public static int LOG_DAY = 10;

	/**
	 * 保留日志的名称，用分号分隔
	 */
	public static String[] LOG_NAME = null;

	/**
	 * 输出文件名
	 */
	public static String[] LOG_APPENDER_FILE = null;

	/**
	 * 站点根目录
	 */
	public static String APP_FOLDER = "";

	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		String configFile = context.getInitParameter(CONF_FILE);
		Properties prop = new Properties();
		String prefix1 = context.getRealPath("/");

		APP_FOLDER = prefix1;
		if (!APP_FOLDER.endsWith("/")) {
			APP_FOLDER = APP_FOLDER + "/";
		}
		try {
			FileInputStream fs = new FileInputStream(prefix1 + configFile);
			prop.load(fs);
			fs.close();

			String path = prefix1 + "/WEB-INF" + File.separator + "classes" + File.separator + "log4j.properties";
			String file = prop.getProperty("file");
			if (file != null) {
				path = prefix1 + File.separator + file;
			}

			String log_name = prop.getProperty("log_name");
			if (log_name != null) {
				LOG_NAME = log_name.split(";");
			}

			String log_appender_file = prop.getProperty("log_appender_file");
			if (log_appender_file != null) {
				LOG_APPENDER_FILE = log_appender_file.split(";");
			}

			String log_path = prop.getProperty("log_path");
			String projectName = context.getContextPath().replace("\\", "").replace("/", "");
			System.out.println("*** My Project = ".concat(projectName).concat(" ***"));

			if (log_path != null) {
				FILE_FOLDER = log_path.concat(projectName).concat(File.separator);
			} else {
				FILE_FOLDER = prefix1.concat(File.separator).concat("logs").concat(File.separator);
			}

			String log_day = prop.getProperty("log_day");
			try {
				LOG_DAY = Integer.valueOf(log_day);
			} catch (Exception e) {
				LOG_DAY = 10;
			}

			System.out.println("*** 日志保留天数 = ".concat(String.valueOf(LOG_DAY)).concat(" ***"));
			System.out.println("*** PATH = ".concat(path).concat(" ***"));
			System.out.println("*** FILE_FOLDER = ".concat(FILE_FOLDER).concat(" ***"));
			fs = new FileInputStream(path);
			Properties ps = new Properties();
			ps.load(fs);
			fs.close();
			PROJECT_NAME = projectName;
			ps.setProperty("log4j.MyProjectName", PROJECT_NAME);

			String appenderFile = "log4j.appender.%1$s.File";

			if (LOG_APPENDER_FILE != null) {
				String fileName = null;
				for (int i = 0, size = LOG_APPENDER_FILE.length; i < size; i++) {
					fileName = String.format(appenderFile, LOG_APPENDER_FILE[i]);
					ps.setProperty(fileName, FILE_FOLDER.concat(ps.getProperty(fileName).replace("/", File.separator)));
					System.out.println("*** ".concat(LOG_APPENDER_FILE[i]).concat(" = ").concat(ps.getProperty(fileName)).concat(" ***"));
				}
			}
			PropertyConfigurator.configure(ps);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sessionCreated(HttpSessionEvent event) {
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		session.invalidate();
	}

	public static void main(String[] args) {
		String s = "log4j.appender.%1$s.File";
		System.out.println(String.format(s, "jwsInfo"));

		String file = "d/dd";
		System.out.println(file.replace("/", File.separator));

	}
}
