package db.ruralindiaonline.org;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.ESqlStatementType;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TResultColumn;

@SuppressWarnings("serial")
public abstract class QueryMap extends LinkedHashMap<String, Set<String>> {

	private String query;
	private String[] resultColumnNames;

	public QueryMap(String query) throws Exception {
		this.query = query;
		setResultColumnNames(query);
		execute();
	}

	private void setResultColumnNames(String query) {
		TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvpostgresql);
		sqlparser.sqltext = query;
		sqlparser.parse();
		for (int j = 0; j < sqlparser.sqlstatements.size(); j++) {
			TCustomSqlStatement stmt = sqlparser.sqlstatements.get(j);
			if (stmt.sqlstatementtype == ESqlStatementType.sstselect) {
				this.resultColumnNames = new String[stmt.getResultColumnList().size()];
				for (int i = 0; i < resultColumnNames.length; i++) {
					TResultColumn resultColumn = stmt.getResultColumnList().getResultColumn(i);
					if (resultColumn.getAliasClause() != null) {
						resultColumnNames[i] = resultColumn.getAliasClause().toString();
					} else {
						resultColumnNames[i] = resultColumn.getExpr().toString();
					}
				}
			}
		}
	}

	abstract void onNext(String[] resultColumnValues) throws SQLException;

	private void execute() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pari", "pari",
				"!abcd1234");
		Statement stmt = connection.createStatement();
		ResultSet set = stmt.executeQuery(query);

		while (set.next()) {
			String[] values = new String[resultColumnNames.length];
			for (int i = 0; i < values.length; i++) {
				values[i] = set.getString(resultColumnNames[i]);
			}
			onNext(values);
		}
		set.close();
		stmt.close();
		connection.close();
	}

	public void put(String add, String affiximage_id) {
		if (!containsKey(add)) {
			HashSet<String> value = new HashSet<String>();
			value.add(affiximage_id);
			put(add, value);
		} else {
			get(add).add(affiximage_id);
		}
	}

	static void fuzePrint(QueryMap photosWithNoPhotographers, QueryMap inlineImages, boolean manyToMany) {
		for (String p : photosWithNoPhotographers.keySet()) {
			if (inlineImages.keySet().contains(p)) {
				Set<String> photos = photosWithNoPhotographers.get(p);
				Set<String> author_ids = inlineImages.get(p);
				for (String author_id : author_ids) {
					for (String affiximage_id : photos) {
						System.out.println(affiximage_id + "," + author_id);
					}
					if (!manyToMany)
						break;
				}
			}
		}
	}
}
