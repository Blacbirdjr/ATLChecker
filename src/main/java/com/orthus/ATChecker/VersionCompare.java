package com.orthus.ATChecker;
import com.orthus.ATChecker.ATChecker;


public class VersionCompare {
	
	public static Boolean Test;

	public static Boolean main(String str1, String str2 )
	{
		System.out.println("versionCompare running");
		Test = (str1.equals(str2));
		return Test;
	}
}
