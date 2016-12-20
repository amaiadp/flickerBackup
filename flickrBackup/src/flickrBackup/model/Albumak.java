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
		albumakLortuFlickr();
		AlbumakKud albkud = AlbumakKud.getInstantzia();
		lal = lal.addAll(albkud.albumakLortuDB(Nagusia.getInstantzia().getProperty("username")));
		System.out.println("DBan:"+lal.luzeera());
		return lal;
	}
	
	private void albumakLortuFlickr(){
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
					if (deskr==null){
						deskr= "";
					}
					String ppID = pset.getPrimaryPhoto().getId();
					alkud.albumaSartu(pset.getId(), izen, deskr, ppID, Nagusia.getInstantzia().getProperty("username"));
				}
			}
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getAlbumInfo(int i){
		return lista.getAlbumInfo(i);
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
	
	
	public Album getAlbum(int index){
		return this.lista.getAlbum(index);
	}
	
	public Album bilatu(String id){
		return lista.bilatu(id);
	}

	public boolean ppDa(String flickrID) {
		return lista.ppDa(flickrID);
	}

	public void gehituAlbum(Album al) {
		lista.add(al);
	}
}
