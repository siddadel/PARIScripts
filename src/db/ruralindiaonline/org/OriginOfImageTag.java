package db.ruralindiaonline.org;

import java.io.File;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class OriginOfImageTag extends QueryMap{
	

	public OriginOfImageTag(String query, String resultColumnName1, String resultColumnName2) throws Exception {
		super(query, resultColumnName1, resultColumnName2);
	}

	void onNext(String origin_id, String content) throws SQLException {
        Document doc = Jsoup.parse(content);
        for (Element e : doc.select("img")) {
        	String src = e.attr("src").toString();
        	String srcSet =  e.attr("srcset").toString();
        	
        	if(!src.equals("")){
        		String name = new File(src).getName();
        		String add = name.split("\\.")[0];
        		put(add, origin_id);
        	} else if (!srcSet.equals("")){
        		String add = new File(srcSet.split(",")[1]).getName().split("\\.")[0];
        		put(add, origin_id);
        	}
        }
	}
}
