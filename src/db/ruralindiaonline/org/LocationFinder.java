package db.ruralindiaonline.org;

public class LocationFinder {
	public static void main(String[] args) throws Exception {

		QueryMap photosWithNoPhotographers = new ImagesSansOrigin("locations");

		QueryMap inlineImages = new OriginOfImageTag("location");

		QueryMap.fuzePrint(photosWithNoPhotographers, inlineImages, true);

	}
}
