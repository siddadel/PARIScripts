package db.ruralindiaonline.org;

public class AuthorFinder {

	public static void main(String[] args) throws Exception {
		QueryMap photosWithNoPhotographers = new ImagesSansOrigin("photographers");

		QueryMap inlineImages = new OriginOfImageTag("author");

		QueryMap.fuzePrint(photosWithNoPhotographers, inlineImages, false);

	}

}
