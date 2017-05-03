package com.example.bot.c3p0.database;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class CommonDBSource implements DBSource {
	private Properties props;
	private String url;
	private String user;
	private String passwd;
	private ComboPooledDataSource cpds;
	private static volatile  CommonDBSource datasource;
	private static String propertyPath = "resources/cfg/jdbc.properties";
	public static CommonDBSource getInstance() {
		if (datasource == null) {
			datasource = new CommonDBSource();
			return datasource;
		} else {
			
			return datasource;
		}
	}

	public CommonDBSource() {
		this(CommonDBSource.propertyPath);
	}

	public CommonDBSource(String configFile) {
		cpds = new ComboPooledDataSource();
		props = new Properties();
		try {
			props.load(new FileInputStream(configFile));
			cpds.setDriverClass(props.getProperty("onlyfun.caterpillar.driver"));
		} catch (IOException | PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		url = props.getProperty("onlyfun.caterpillar.url");
		user = props.getProperty("onlyfun.caterpillar.user");
		passwd = props.getProperty("onlyfun.caterpillar.password");

		cpds.setJdbcUrl(url);
		cpds.setUser(user);
		cpds.setPassword(passwd);

		// the settings below are optional -- c3p0 can work with defaults
		// http://www.blogjava.net/Alpha/archive/2015/03/09/262789.html
		String minpoolsize = props.getProperty("onlyfun.caterpillar.minpoolsize");
		String acquireincrement = props.getProperty("onlyfun.caterpillar.acquireincrement");
		String maxpoolsize = props.getProperty("onlyfun.caterpillar.maxpoolsize");
		String maxstatements = props.getProperty("onlyfun.caterpillar.maxstatements");
		cpds.setMinPoolSize(Integer.valueOf(minpoolsize));
		cpds.setAcquireIncrement(Integer.valueOf(acquireincrement));
		cpds.setMaxPoolSize(Integer.valueOf(maxpoolsize));
		cpds.setMaxStatements(Integer.valueOf(maxstatements));
	}

	public Connection getConnection()  {
		try {
			return this.cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void closeConnection(Connection conn)  {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}