package flickrBackup.model;

import java.util.ArrayList;

public class ListaAlbum {
	private ArrayList<Album> lista;
	
	
	public ListaAlbum(){
		lista = new ArrayList<Album>();
	}
	
	public void argazkiaSartu(String id) {
		for(Album al:lista){
			al.sartu(id);
		}
		
	}
}
