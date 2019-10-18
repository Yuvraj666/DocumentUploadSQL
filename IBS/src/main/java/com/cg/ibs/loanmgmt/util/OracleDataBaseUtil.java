package com.cg.ibs.loanmgmt.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OracleDataBaseUtil {
	private static Connection connection;

	public static Connection getConnection() {
		if (null == connection) {
			ResourceBundle resourceBundle = ResourceBundle.getBundle("db");
			String url = resourceBundle.getString("url");
			String username = resourceBundle.getString("username");
			String password = resourceBundle.getString("password");
			try {
				connection = DriverManager.getConnection(url, username, password);

			} catch (SQLException e) {
				System.out.println("erer");
			}
		}
		return connection;
	}
}
