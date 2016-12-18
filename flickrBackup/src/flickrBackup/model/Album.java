package flickrBackup.model;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;

public class Album {
	
	String id;
	String izena;
	String deskribapena;
	String primaryPhotoID;
	PhotosetsInterface pi;
	
	
	public Album(String izen, String deskr){
		pi = Nagusia.getInstantzia().getPhotosetsInterface();
		izena = izen;
		deskribapena = deskr;
		primaryPhotoID = null;
		id = null;
	}
	
	public boolean albumaSortu(String photoID){
		primaryPhotoID = photoID;
		try {
			Photoset p = pi.create(izena, deskribapena, primaryPhotoID);
			id = p.getId();
			return true;
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean albumeraGehitu(String photoID){
		try {
			pi.addPhoto(id, photoID);
			return true;
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
//	public boolean primaryPhotoChange(String photoID){
//		try {
//			pi.setPrimaryPhoto(id, photoID);
//			primaryPhotoID = photoID;
//			return true;
//		} catch (FlickrException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return false;
//	}
	
	public boolean existitzenDa(){
		return(id!=null);
	}

	public void sartu(String idPhoto) {
		if (!badago(idPhoto)){
			albumeraGehitu(idPhoto);
		}
	}

	private boolean badago(String idPhoto) {
		try {
			pi.getContext(idPhoto, id);
			return true;
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
//	public static void main(String[] args) {
//		String pid1 = "31136300990";
//		String pid3 = "30717794854";
//		String pid2 = "31360510542";
//		String aid = "72157675979128901";
//		Album a = new Album("jaja", "ehhhh");
//		a.id = aid;
//		a.sartu(pid1);
//		a.sartu(pid2);
//		a.sartu(pid3);
//	}
	
}
