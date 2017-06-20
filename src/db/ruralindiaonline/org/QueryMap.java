package db.ruralindiaonline.org;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public abstract class QueryMap extends LinkedHashMap<String, Set<String>>{

	private String query;
	private String resultColumnName1;
	private String resultColumnName2;

	public QueryMap(String query, String resultColumnName1, String resultColumnName2) throws Exception {
		this.query = query;
		this.resultColumnName1 = resultColumnName1;
		this.resultColumnName2 = resultColumnName2;
		execute();
	}

	
	abstract void onNext(String resultColumnValue1, String resultColumnValue2) throws SQLException;

	private void execute() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pari", "pari", "!abcd1234");
		Statement stmt = connection.createStatement();
		ResultSet set = stmt.executeQuery(query);

		while (set.next()) {
			onNext(set.getString(resultColumnName1), set.getString(this.resultColumnName2));
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
	
	static void fuzeQueryMaps(QueryMap photosWithNoPhotographers, QueryMap inlineImages, boolean manyToMany) {
		for(String p: photosWithNoPhotographers.keySet()){
			if(inlineImages.keySet().contains(p)){
				Set<String> photos = photosWithNoPhotographers.get(p);
				Set<String> author_ids = inlineImages.get(p);
				for(String author_id: author_ids){
					for(String affiximage_id: photos){
						System.out.println(affiximage_id+ ","+author_id);
					}
					if(!manyToMany) break;
				}
			}
		}
	}
}
