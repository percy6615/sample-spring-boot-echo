package com.example.bot.c3p0.database;

import java.sql.Connection;

public interface DBSource {
    public Connection getConnection();
    public void closeConnection(Connection conn) ;
}