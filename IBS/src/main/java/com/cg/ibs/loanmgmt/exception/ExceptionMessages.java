package com.cg.ibs.loanmgmt.exception;

public interface ExceptionMessages {

	public static String MESSAGEFORWRONGINPUT = "Sorry! Invalid Input.";
	public static String MESSAGEFORINPUTMISMATCH = "Sorry! You are allowed to enter a single-digit numeric.";
	public static String MESSAGEFORWRONGFILENAMEINPUT = "File name should only include alphabets, begin with an upper case and contain a maximum of  8 characters.";
	public static String MESSAGEFORFILENOTFOUND = "Sorry!  File with the specified pathname does not exist or is inaccessible";
	public static String MESSAGEFORIOEXCEPTION = "Sorry! Failed or interrupted input/output operation.";
	public static String MESSAGEFOREXCEPTION = "Sorry! An error occured.";
	public static String MESSAGEFORINVALIDLOANNUMBER = "Loan number can't begin with 0 and must contain 3 or 4 numerics.";
	public static String MESSAGEFORINPUTMISMATCHATEMI = "Sorry! Please enter an appropriate numeric value.";
	public static String MESSAGEFORSQLEXCEPTION = "Sorry! An error occured in parsing data.";
}
