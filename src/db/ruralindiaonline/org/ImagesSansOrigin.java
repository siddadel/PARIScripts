package db.ruralindiaonline.org;

import java.io.File;
import java.sql.SQLException;

public class ImagesSansOrigin extends QueryMap {

	public ImagesSansOrigin(String query, String resultColumnName1, String resultColumnName2) throws Exception {
		super(query, resultColumnName1, resultColumnName2);
	}

	void onNext(String affiximage_id, String filePath) throws SQLException {
		String add = new File(filePath).getName().split("\\.")[0];
		put(add, affiximage_id);
	}

}
