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
		lista.add(album);
		
	}

	public String getAlbumInfo(int i) {
		return lista.get(i).inprimatu();
	}

	public int luzeera() {
		return lista.size();
	}

	public void addAll(ArrayList<Album> listaAlbum) {
		lista.addAll(listaAlbum);
		
	}

	public String inprimatu() {
		String emaitza = "";
		Album al1 = null;
		if (!lista.isEmpty()){
			al1 =lista.get(0);
		}
		for(Album al:lista){
			if (!al.equals(al1)){
				emaitza = emaitza +", "+ al.inprimatu();
			}
		}
		return emaitza;
	}
	
	public Album getAlbum(int index){
		return this.lista.get(index);
	}
	
	public void remove(Album al){
		this.lista.remove(al);
	}

	public Album bilatu(String id) {
		for(Album al:lista){
			if(al.hauDa(id)){
				return al;
			}
		}
		return null;
	}

	public boolean ppDa(String flickrID) {
		for(Album al:lista){
			if(al.primaryPhoto(flickrID)){
				return true;
			}
		}
		return false;
	}

}
