package flickrBackup.model;

import java.util.Collection;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;

import flickrBackup.kudeatzaileak.AlbumakKud;

public class Albumak {
	
	private ListaAlbum lista =null; 
	private static Albumak albumak;
	
	private Albumak(){
		lista = albumakLortu();
		System.out.println(lista.luzeera());
	}
	
	private ListaAlbum albumakLortu(){
		ListaAlbum lal = new ListaAlbum();
		lal = albumakLortuFlickr();
		AlbumakKud albkud = AlbumakKud.getInstantzia();
		lal.addAll(albkud.albumakLortuDB(Nagusia.getInstantzia().getProperty("username")));
		System.out.println("DBan:"+lal.luzeera());
		return lal;
	}
	
	private ListaAlbum albumakLortuFlickr(){
		ListaAlbum lal = new ListaAlbum();
		Flickr flickr = Nagusia.getInstantzia().getFlickr();
		PhotosetsInterface pi = flickr.getPhotosetsInterface();
		try {
			Photosets ps = pi.getList(Nagusia.getInstantzia().getProperty("nsid"));
			Collection<Photoset> cps = ps.getPhotosets();
			for(Photoset photoset: cps){
				AlbumakKud alkud = AlbumakKud.getInstantzia();
				String id = photoset.getId();
				if(!alkud.DBandago(id,Nagusia.getInstantzia().getProperty("username"))){
					System.out.println("EZ DAGO DB");
					Photoset pset = pi.getInfo(id);
					String izen = pset.getTitle();
					String deskr =pset.getDescription();
					alkud.albumaSartu(pset.getId(), izen, deskr, Nagusia.getInstantzia().getProperty("username"));
					Album album = new Album(izen, deskr,id);
					lal.add(album);
				}
			}
			System.out.println("flickren:"+lal.luzeera());
			return lal;
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String getAlbum(int i){
		return lista.get(i);
	}
	
	public int luzeera(){
		return lista.luzeera();
	}
	
	public static Albumak getInstantzia(){
		if (albumak==null){
			albumak = new Albumak();
		}
		return albumak;
	}
}
