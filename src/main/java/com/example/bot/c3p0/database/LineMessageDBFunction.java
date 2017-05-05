package com.example.bot.c3p0.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.bot.line.Object.LineMessageInfo;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;

public class LineMessageDBFunction extends DBFunction {

	public static void main(String[] args) {
		// long time = 1492670161000L;
		// System.out.println(new SimpleDateFormat("YYYY-MM-dd
		// HH:mm:ss").format(new Date(time).getTime()));
		// System.out.println(new Date(System.currentTimeMillis()).getTime());
		// System.out.println(System.currentTimeMillis());
//		LineMessageDBFunction lineMessageDBFunction = new LineMessageDBFunction();
//		FollowEvent event=new FollowEvent(null, null, null);
		
//		lineMessageDBFunction.insertLineUserid("U98ad7b0c88037cec38564c8135489beb","1492782872000L");
	}

	private static volatile LineMessageDBFunction lineMessageDBFunction;

	public static LineMessageDBFunction getInstance() {
		if (lineMessageDBFunction == null) {
			synchronized (LineMessageDBFunction.class) {
				lineMessageDBFunction = new LineMessageDBFunction();
			}
		}
		return lineMessageDBFunction;

	}

	public void createLinemessageUserinfo() {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String selectSQL = "SELECT 1 as tf FROM information_schema.tables where table_name = 'linemessage_userinfo'";
		String createSQL = "CREATE TABLE `linemessage_userid` ( "
				+" `senderid` varchar(128) NOT NULL, "
				+" `epochsecond` varchar(32) DEFAULT NULL, "
				+" PRIMARY KEY (`senderid`) "
				+" ) ENGINE=InnoDB DEFAULT CHARSET=utf8; ";

		try {
			ResultSet r = conn.prepareStatement(selectSQL).executeQuery();
			if (r.next()) {
				return;
			} else {
				pstmt = conn.prepareStatement(createSQL);
				pstmt.executeUpdate();
				pstmt.clearParameters();

			}
			conn.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void insertLineMessageInfo(LineMessageInfo lineMessageInfo) {
		Connection conn = getConnection();
		String insertSQL = "insert into linemessage_userinfo values (?,?,?,?,?,?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setString(1, lineMessageInfo.getChanneltoken());
			pstmt.setString(2, lineMessageInfo.getSenderid());
			pstmt.setString(3, lineMessageInfo.getEpochsecond() + "000");
			pstmt.setString(4, lineMessageInfo.getReplytoken());
			pstmt.setString(5, lineMessageInfo.getText());
			pstmt.setString(6, lineMessageInfo.getMessagetype());
			pstmt.executeUpdate();
			pstmt.clearParameters();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertLineUserid(Event event) {
//	public void insertLineUserid(String userid , String tim) {
		String userid = event.getSource().getSenderId();
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String selectSQL = "SELECT 1 as tf FROM linemessage_userid where senderid = '" + userid + "'";
		String insertSQL = "insert into linemessage_userid values (?,?)";
		try {
			ResultSet r = conn.prepareStatement(selectSQL).executeQuery();
			if (r.next()) {
				// System.out.println(r.getInt("tf"));
				return;
			} else {
				pstmt = conn.prepareStatement(insertSQL);
				pstmt.setString(1, userid);
				pstmt.setString(2, event.getTimestamp().toString());
//				pstmt.setString(2, tim);
				pstmt.executeUpdate();
				pstmt.clearParameters();

			}
			conn.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
