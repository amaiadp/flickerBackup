package flickrBackup.model;

import java.util.ArrayList;

public class ListaAlbum {
	private ArrayList<Album> lista;
	
	
	public ListaAlbum(){
		lista = new ArrayList<Album>();
	}
	
	public void argazkiaSartu(String md5, String idF) {
		for(Album al:lista){
			al.sartu(md5, idF);
		}
		
	}

	public void add(Album album) {
		add(album);
		
	}
}
