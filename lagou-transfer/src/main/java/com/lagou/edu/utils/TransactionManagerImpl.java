package com.lagou.edu.utils;

import com.ray.mySpring.TransactionManager.TransactionManager;
import com.ray.mySpring.annotation.Autowired;
import com.ray.mySpring.annotation.Component;

import java.sql.SQLException;

/**
 * @author Rui Huang
 */
@Component
public class TransactionManagerImpl implements TransactionManager {

    @Autowired
    private ConnectionUtils connectionUtils;

    @Override
    public void beginTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }

    @Override
    public void commit() throws SQLException {
        connectionUtils.getCurrentThreadConn().commit();
    }

    @Override
    public void rollback() throws SQLException {
        connectionUtils.getCurrentThreadConn().rollback();
    }
}

