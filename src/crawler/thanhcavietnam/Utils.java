package crawler.thanhcavietnam;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Utils {
	public static final String SPECIAL_CHARS = "[^A-Za-z0-9 _]";
	
	static {
		String appPath = System.getProperty("user.dir");
		DOMConfigurator.configure(appPath + File.separator + "log4j.xml");
	}

	public static String getStandardFileName(String name) {
		return StringUtils.stripAccents(name).replaceAll("Đ", "D").replaceAll(SPECIAL_CHARS, "");
	}

	public static Logger getLogger() {
		Logger logger = Logger.getLogger("thanhca.log");
		return logger;
	}
}
