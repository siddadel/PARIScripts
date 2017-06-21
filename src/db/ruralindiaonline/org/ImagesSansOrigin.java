package db.ruralindiaonline.org;

import java.io.File;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class ImagesSansOrigin extends QueryMap {
	public static final int AFFIXIMAGE_ID = 0;
	public static final int FILE_PATH = 1;

	public ImagesSansOrigin(String whatToFind) throws Exception {
		super("select id as affiximage_id, file from core_affiximage WHERE core_affiximage.id NOT IN (select core_affiximage.id from core_affiximage, core_affiximage_"
				+ whatToFind + " where core_affiximage.id=core_affiximage_" + whatToFind + ".affiximage_id)");
	}

	void onNext(String[] values) throws SQLException {
		String add = new File(values[FILE_PATH]).getName().split("\\.")[0];
		put(add, values[AFFIXIMAGE_ID]);
	}

}
