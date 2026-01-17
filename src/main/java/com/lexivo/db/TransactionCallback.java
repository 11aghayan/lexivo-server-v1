package com.lexivo.db;

import com.lexivo.exceptions.UnauthorizedAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionCallback {
	void run(Connection connection) throws SQLException, UnauthorizedAccessException;
}
