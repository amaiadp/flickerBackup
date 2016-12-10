package flickrBackup.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import flickrBackup.model.Argazkia;
import flickrBackup.model.ArgazkiakPantailaratu;

public class BistaratuJT extends AbstractTableModel {

	
	private Vector<Object[]> data = new Vector<Object[]>();
	private Vector<String> columnNames = new Vector<String>();
	
	public BistaratuJT(){
		this.columnNames.add("Argazkia");
		this.columnNames.add("Izena");
		this.columnNames.add("Deskribapena");
		this.columnNames.add("Etiketak");
		this.columnNames.add("Pribatutasuna");
		ArgazkiakPantailaratu argpant;
		try {
			argpant = new ArgazkiakPantailaratu();
			ArrayList<Argazkia> lista = argpant.preparePhotos();
	        for (Argazkia arg:lista) {
	            Object[] lerro = new Object[5];
	            lerro[0] = (ImageIcon) arg.getThumbnail();
	            lerro[1] = (String) arg.getIzena();
	            lerro[2] = (String) arg.getDeskribapena();
	            lerro[3] = (String) arg.getEtiketak().toString();
	            lerro[4] = (String) arg.getPrib();
	            data.addElement(lerro);
	            
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	@Override
	public int getRowCount() {
		return this.data.size();
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.data.get(rowIndex)[columnIndex];
	}
	
	public String getColumnName(int i){
		return this.columnNames.get(i);
	}
	
	public Class<?> getColumnClass(int col){
		Class<?> emaitza= null;
		switch (col) {
		case 0:
			emaitza = ImageIcon.class;
			break;
		case 1:
		case 2:
		case 3:
		case 4:
			emaitza = String.class;
			break;
		}
		return emaitza;
	}
	
	
	public boolean isCellEditable(int row, int col){
		return false;

	}
	
//	public void setValueAt(Object value, int row, int col){
//			//System.out.println(this.data.get(row).getBalioa(col));
//			this.data.get(row).insertElementAt(value, col);
//			//System.out.println(this.data.get(row).getBalioa(col));
//		
//	}
	
	
}