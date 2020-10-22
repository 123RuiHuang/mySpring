package com.ray.mySpring.TransactionManager;

import java.sql.SQLException;

public interface TransactionManager {
    void beginTransaction() throws Exception;
    void commit() throws Exception;
    void rollback() throws Exception;
}
