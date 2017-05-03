package com.example.bot.c3p0.database;

import java.sql.Connection;

public class DBFunction {

	public Connection getConnection() {
		DBSource CDBS = CommonDBSource.getInstance();
//		DBSource CDBS = new CommonDBSource();
		Connection conn = CDBS.getConnection();
		return conn;
	}
}
