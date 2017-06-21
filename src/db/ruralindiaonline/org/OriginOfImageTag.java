package db.ruralindiaonline.org;

import java.io.File;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@SuppressWarnings("serial")
public class OriginOfImageTag extends QueryMap {

	public static final int CONTENT = 1;
	public static final int ORIGIN = 0;

	public OriginOfImageTag(String whatToFind) throws Exception {
		super("select " + whatToFind + "_" + whatToFind + ".id as " + whatToFind
				+ "_id, content from article_article, article_article_" + whatToFind + "s, " + whatToFind + "_"
				+ whatToFind + " where article_article.page_ptr_id = article_article_" + whatToFind
				+ "s.article_id and article_article_" + whatToFind + "s." + whatToFind + "_id = " + whatToFind + "_"
				+ whatToFind + ".id;");
	}

	void onNext(String[] values) throws SQLException {
		Document doc = Jsoup.parse(values[CONTENT]);
		for (Element e : doc.select("img")) {
			String src = e.attr("src").toString();
			String srcSet = e.attr("srcset").toString();

			if (!src.equals("")) {
				String name = new File(src).getName();
				String add = name.split("\\.")[0];
				put(add, values[ORIGIN]);
			} else if (!srcSet.equals("")) {
				String add = new File(srcSet.split(",")[1]).getName().split("\\.")[0];
				put(add, values[ORIGIN]);
			}
		}
	}
}
