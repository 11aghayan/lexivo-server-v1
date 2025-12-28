package com.lexivo.db;

import com.lexivo.exceptions.InvalidLogCategoryException;
import com.lexivo.logger.Logger;
import com.lexivo.schema.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableLogs {
	private static final String COL_CATEGORY = "category";
	private static final String COL_USER_EMAIL = "user_email";
	private static final String COL_CREATED_AT = "created_at";
	private static final String COL_STACK_TRACE = "stack_trace";
	private static final String COL_MESSAGES = "messages";
	private final Logger logger = new Logger();


	public List<Log> getLogs(Log.Category[] categories, String userEmail, long dateFrom, long dateTo) {
		String categoryFilter = categories == null || categories.length == 0 ? "" : "category = ANY(?) AND ";
		String userEmailFilter = userEmail == null ? "" : "user_email = ? AND ";
		String createdAtFilter = "created_at >= ? AND created_at < ?";
		String sql = "SELECT * FROM logs WHERE " + categoryFilter + " " + userEmailFilter + " " + createdAtFilter;

		try (Connection connection = Db.getDbConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			int nextParamIndex = 1;
			if (!categoryFilter.isEmpty()) {
				Array sqlArray = connection.createArrayOf("SHORT_TEXT", Arrays.stream(categories).map(Enum::toString).toArray(String[]::new));
				statement.setArray(nextParamIndex++, sqlArray);
			}
			if (!userEmailFilter.isEmpty()) {
				statement.setString(nextParamIndex++, userEmail);
			}
			statement.setLong(nextParamIndex++, dateFrom);
			statement.setLong(nextParamIndex, dateTo);

			try (ResultSet resultSet = statement.executeQuery()) {
				List<Log> logs = new ArrayList<>();

				while (resultSet.next()) {
					String email = resultSet.getString(COL_USER_EMAIL);
					Log.Category category = null;
					long createdAt = resultSet.getLong(COL_CREATED_AT);
					String[] messages = (String[]) resultSet.getArray(COL_MESSAGES).getArray();
//					TODO: handle null
					Array stackTraceRaw = resultSet.getArray(COL_STACK_TRACE);
					String[] stackTrace = null;
					if (stackTraceRaw != null) {
						stackTrace = (String[]) stackTraceRaw.getArray();
					}

					try {
						category = Log.Category.fromString(resultSet.getString(COL_CATEGORY));
					}
					catch (InvalidLogCategoryException e) {
						logger.exception(e,new String[]{"SQL Exception in TableLogs.getLogs", e.getMessage()});
					}
					logs.add(new Log(createdAt, email, category, stackTrace, messages));

				}

				return logs;
			}
		}
		catch (Exception e) {
			logger.exception(e, userEmail, new String[]{"Exception in TableLogs.getLogs", e.getMessage()});
			return List.of();
		}
	}

	public void addLog(Log log) {
		try {
			Db.executeTransaction((connection -> {
				try(PreparedStatement statement = connection.prepareStatement("INSERT INTO logs (category, user_email, created_at, stack_trace, messages) VALUES(?,?,?,?,?)")) {
					statement.setString(1, log.category().toString());
					statement.setString(2, log.userEmail());
					statement.setLong(3, log.createdAt());
					statement.setArray(4, connection.createArrayOf("TEXT", log.stackTrace()));
					statement.setArray(5, connection.createArrayOf("TEXT", log.messages()));
					statement.execute();
				}
			}));
		}
		catch (Exception e) {
			logger.exception(e, log.userEmail(), new String[]{"Exception in TableLogs.addLog"});
		}
	}
}
