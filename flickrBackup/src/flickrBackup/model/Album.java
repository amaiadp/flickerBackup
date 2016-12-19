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
		pi = Nagusia.getInstantzia().getPhotosetsInterface();
		izena = izen;
		deskribapena = deskr;
		id = idF;
	}
	
	
	public boolean albumaSortu(String photoID){
		try {
			Photoset p = pi.create(izena, deskribapena, photoID);
			id = p.getId();
			return true;
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean albumeraGehitu(String md5,String photoID){
		try {
			pi.addPhoto(id, photoID);
			AlbumakKud albkud = AlbumakKud.getInstantzia();
			albkud.albumeraGehitu(md5,id, Nagusia.getInstantzia().getProperty("username"));
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
