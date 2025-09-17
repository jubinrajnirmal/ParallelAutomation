package org.wikipedia.data;

import org.wikipedia.utilities.ExcelConfig;

public class DataFile {
	public static String homePageURL = "https://wikipedia.org/";
	public static String mainPageURL = "https://en.wikipedia.org/wiki/Main_Page";
	public static String loginPageURL = "https://auth.wikimedia.org/enwiki/wiki/Special:UserLogin";
	public static String userHomePageURL = "https://en.wikipedia.org/w/index.php?title=Special:Homepage";
	public static String username = "SeleniumTestWiki";
	public static String password = "seleniumWikiT3st!";
	public static String email = "seleniumwikitest@hotmail.com";

	static {
		try {
			ExcelConfig.load("src/test/resources/TestData/LoginData.xlsx", "Config");
			homePageURL		= ExcelConfig.get("homePageURL", homePageURL);
			mainPageURL     = ExcelConfig.get("mainPageURL",     mainPageURL);
	        loginPageURL    = ExcelConfig.get("loginPageURL",    loginPageURL);
	        userHomePageURL = ExcelConfig.get("userHomePageURL", userHomePageURL);
	        username        = ExcelConfig.get("username",        username);
	        password        = ExcelConfig.get("password",        password);
	        email           = ExcelConfig.get("email",           email);
		} catch (Exception e) {
			System.err.println("[WARN] Failed to load Excel Config:" + e.getMessage());
		}
	}
	
}
