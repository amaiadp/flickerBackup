package flickrBackup.model;

import java.util.Collection;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;

import flickrBackup.kudeatzaileak.AlbumakKud;

public class Albumak {
	
	private ListaAlbum lista =null; 
	private static Albumak albumak = new Albumak();
	
	private Albumak(){
		lista = albumakLortu();
	}
	
	private ListaAlbum albumakLortu(){
		ListaAlbum lal = new ListaAlbum();
		PhotosetsInterface pi = Nagusia.getInstantzia().getPhotosetsInterface();
		try {
			Photosets ps = pi.getList(Nagusia.getInstantzia().getProperty("nsid"));
			Collection<Photoset> cps = ps.getPhotosets();
			for(Photoset photoset: cps){
				AlbumakKud alkud = AlbumakKud.getInstantzia();
				String id = photoset.getId();
				if(!alkud.DBandago(id,Nagusia.getInstantzia().getProperty("username"))){
					Photoset pset = pi.getInfo(id);
					String izen = pset.getTitle();
					String deskr =pset.getDescription();
					alkud.albumaSartu(pset.getId(), izen, deskr, pset.getOwner().getRealName());
					Album album = new Album(izen, deskr,id);
					lal.add(album);
				}
			}
			return lal;
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Albumak getInstantzia(){
		return albumak;
	}
}
