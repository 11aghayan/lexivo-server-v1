package com.lexivo.db;

import com.lexivo.exceptions.UnauthorizedAccessException;
import com.lexivo.logger.Logger;
import com.lexivo.schema.appschema.Dict;
import com.lexivo.schema.appschema.Grammar;
import com.lexivo.schema.appschema.Lang;
import com.lexivo.schema.appschema.Word;
import com.lexivo.util.ReceivedDataUtil;
import com.sun.mail.imap.protocol.UIDSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TableDict {
	private static final String COL_ID = "id";
	private static final String COL_LANG = "lang";
	private static final String COL_USER_EMAIL = "user_email";
	private final Logger logger = new Logger();

	public boolean isUserAuthorized(String dictId, String requesterEmail) {
		try (Connection connection = Db.getDbConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM dict WHERE id = ?")) {
			statement.setString(1, dictId);
			try(ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) return true;
				String userEmail = resultSet.getString(COL_USER_EMAIL);
				return requesterEmail.equals(userEmail);
			}
		}
		catch(Exception e) {
			logger.exception(e, new String[]{"Exception in TableDict.isUserAuthorized", e.getMessage()});
			return false;
		}
	}

	public List<Dict> getAllOfUser(String userEmail) {
		String sql = "SELECT * FROM dict WHERE " + COL_USER_EMAIL + "= ?" ;
		try (Connection connection = Db.getDbConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, userEmail);
			try(ResultSet resultSet = statement.executeQuery()) {
				List<Dict> dictList = new ArrayList<>();
				while (resultSet.next()) {
					String joinedId = resultSet.getString(COL_ID);
					String langString = resultSet.getString(COL_LANG);

					String[] idList = ReceivedDataUtil.separateJoinedId(joinedId);
					String id = idList[idList.length - 1];
					List<Word> words = Db.word().getAll(id, userEmail);
					List<Grammar> grammarList = Db.grammar().getAll(id, userEmail);

					Lang lang = Db.lang().get(langString);

					dictList.add(new Dict(joinedId, words, grammarList, lang));
				}

				return dictList;
			}
		}
		catch(Exception e) {
			logger.exception(e, new String[]{"Exception in TableDict.getAllOfUser", e.getMessage()});
			return List.of();
		}
	}

	public Dict getById(String dictId, String userEmail) {
		String sql = "SELECT * FROM dict WHERE " + COL_ID + "= ?" ;
		try (Connection connection = Db.getDbConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, ReceivedDataUtil.createJoinedId(dictId, userEmail));
			try(ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) return null;

				String joinedId = resultSet.getString(COL_ID);
				String langString = resultSet.getString(COL_LANG);

				String[] idList = ReceivedDataUtil.separateJoinedId(joinedId);
				String id = idList[idList.length - 1];
				List<Word> words = Db.word().getAll(id, userEmail);
				List<Grammar> grammarList = Db.grammar().getAll(id, userEmail);

				Lang lang = Db.lang().get(langString);

				return new Dict(dictId, words, grammarList, lang);
			}
		}
		catch(Exception e) {
			logger.exception(e, new String[]{"Exception in TableDict.getById", e.getMessage()});
			return null;
		}
	}

//	TODO: Add (one for add and import, handle through requests), Update

	public void delete(String dictId, String userEmail) throws UnauthorizedAccessException {
		String sql = "DELETE FROM dict WHERE " + COL_ID + " = ?";

		try {
			String joinedId = ReceivedDataUtil.createJoinedId(userEmail, dictId);

			if (!isUserAuthorized(joinedId, userEmail)) throw new UnauthorizedAccessException();

			Db.executeTransaction((connection -> {
				try(PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setString(1, joinedId);
					statement.execute();
				}
			}));
		}
		catch (Exception e) {
			if (e instanceof UnauthorizedAccessException) throw new UnauthorizedAccessException();
			logger.exception(e, userEmail, new String[]{"Exception in TableDict.delete"});
		}
	}
}
