package db.ruralindiaonline.org;

public class AuthorFinder {

	public static void main(String[] args) throws Exception {
		QueryMap photosWithNoPhotographers = new ImagesSansOrigin(
				"select id as affiximage_id, file from core_affiximage WHERE core_affiximage.id NOT IN (select core_affiximage.id from core_affiximage, core_affiximage_photographers where core_affiximage.id=core_affiximage_photographers.affiximage_id)",
				"affiximage_id", "file");

		QueryMap inlineImages = new OriginOfImageTag(
				"select location_location.id as location_id, content from article_article, article_article_locations, location_location where article_article.page_ptr_id = article_article_locations.article_id and article_article_locations.location_id = location_location.id;",
				"location_id", "content");

		QueryMap.fuzeQueryMaps(photosWithNoPhotographers, inlineImages, false);
	}

}
