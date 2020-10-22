package com.lagou.edu.utils;

import com.ray.mySpring.annotation.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Rui Huang
 */
@Component
public class ConnectionUtils {

    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public Connection getCurrentThreadConn() throws SQLException {
        Connection connection = threadLocal.get();
        if(connection == null) {
            // get connection from pool
            connection = DruidUtils.getInstance().getConnection();
            // bind connection to local thread
            threadLocal.set(connection);
        }
        return connection;
    }
}
