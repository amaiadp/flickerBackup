package flickrBackup.model;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;

public class Album {
	
	String id;
	String izena;
	String deskribapena;
	String primaryPhotoID;
	PhotosetsInterface pi;
	
	
	public Album(String izen, String deskr){
		pi = Nagusia.getPhotosetsInterface();
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
	
	public boolean albumeraGehitu(String photoID){
		try {
			pi.addPhoto(id, photoID);
			return true;
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean primaryPhotoChange(String photoID){
		try {
			pi.setPrimaryPhoto(id, photoID);
			primaryPhotoID = photoID;
			return true;
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean existitzenDa(){
		return(id!=null);
	}
	
	public static void main(String[] args) {
		Album j = new Album("prueba", "algo hay que escribir");
	}
	
	
	
	
	
}
