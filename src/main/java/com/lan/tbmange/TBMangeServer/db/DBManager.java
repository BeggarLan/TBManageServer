package com.lan.tbmange.TBMangeServer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBManager {

	private static DataSource dataSource = null;
	static {

		try {

			ComboPooledDataSource pool = new ComboPooledDataSource();
			pool.setDriverClass("com.mysql.jdbc.Driver");
			pool.setJdbcUrl("jdbc:mysql:///schedulearrangement?useUnicode=true&characterEncoding=UTF-8");
			pool.setUser("root");
//			 pool.setPassword("!Qq199712272012");
			pool.setPassword("134827");

			// c3p0反空闲设置，防止8小时失效问题28800
			pool.setTestConnectionOnCheckout(false);
			pool.setTestConnectionOnCheckin(true);
			pool.setIdleConnectionTestPeriod(3600);

			// 连接池配置
			pool.setInitialPoolSize(10);
			pool.setMaxPoolSize(100);
			pool.setMinPoolSize(10);
			pool.setMaxIdleTime(3600);

			// 连接超过3秒 就断开
			pool.setLoginTimeout(3000);

			dataSource = pool;
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("数据库连接失败!");

		}
	}

	public static Connection getConnection() throws SQLException {

		return dataSource.getConnection();
	}

	public static void closeAll(ResultSet resultSet, Statement statement, Connection connection) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 执行命令 添加、删除和修改 insert into emp(...) values(?,?,?) delete from emp where id=?
	 */
	public static int executeUpdate(String sql, Object... objects) {

		Connection connection = null;
		PreparedStatement pstat = null;

		try {
			connection = getConnection();
			pstat = connection.prepareStatement(sql);
			for (int i = 0; i < objects.length; i++) {
				pstat.setObject(i + 1, objects[i]);
			}

			return pstat.executeUpdate(); // 返回执行受到影响的行数

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(null, pstat, connection);
		}

		return 0;
	}

}
