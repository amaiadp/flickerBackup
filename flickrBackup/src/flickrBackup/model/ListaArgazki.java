package flickrBackup.model;

import java.util.ArrayList;

public class ListaArgazki {

	private ArrayList<Argazkia> lista;
	
	public ListaArgazki(){
		lista= new ArrayList<Argazkia>();
	}
	
	public boolean gehituArgazkia(Argazkia a){
		return this.lista.add(a);
	}
	
	public boolean ezabatuArgazkia(Argazkia a){
		return this.lista.remove(a);
	}
	

	
}
