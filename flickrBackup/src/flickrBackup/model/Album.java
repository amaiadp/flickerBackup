package flickrBackup.model;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.PhotoContext;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;

import flickrBackup.kudeatzaileak.AlbumakKud;

public class Album {
	
	String id;
	String izena;
	String deskribapena;
	PhotosetsInterface pi;
	
	
	public Album(String izen, String deskr,String idF){
		pi = Nagusia.getInstantzia().getFlickr().getPhotosetsInterface();
		izena = izen;
		deskribapena = deskr;
		id = idF;
	}
	
	
	public void albumaSortu(String md5, String photoID){
		try {
			Photoset p = pi.create(izena, deskribapena, photoID);
			id = p.getId();
			AlbumakKud albkud = AlbumakKud.getInstantzia();
			String username = Nagusia.getInstantzia().getProperty("username");
			albkud.albumaSartu(id, izena, deskribapena, username);
			albkud.albumeraGehitu(md5, id, username);
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void albumeraGehitu(String md5,String photoID){
		try {
			pi.addPhoto(id, photoID);
			AlbumakKud albkud = AlbumakKud.getInstantzia();
			albkud.albumeraGehitu(md5,id, Nagusia.getInstantzia().getProperty("username"));
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ezabatu(){
		try {
			pi.delete(id);
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void sartu(String md5,String idPhoto) {
		if (!badago(md5)){
			albumeraGehitu(md5,idPhoto);
		}
	}

	private boolean badago(String md5) {
		AlbumakKud albkud = AlbumakKud.getInstantzia();
		return albkud.badago(md5,id,Nagusia.getInstantzia().getProperty("username"));
	}


	public String inprimatu() {
		// TODO Auto-generated method stub
		return "ID: "+id+"  Izena: "+izena;
	}
	
}
