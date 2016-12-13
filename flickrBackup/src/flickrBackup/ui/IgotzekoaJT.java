package flickrBackup.ui;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import flickrBackup.model.Argazkia;
import flickrBackup.model.Argazkia.Pribatutasuna;


public class IgotzekoaJT extends AbstractTableModel {

	
	private Vector<Argazkia> data = new Vector<Argazkia>();
	private Vector<String> columnNames = new Vector<String>();
	
	public IgotzekoaJT(ArrayList<Argazkia> argaz){
		this.columnNames.add("Argazkia");
		this.columnNames.add("Izena");
		this.columnNames.add("Deskribapena");
		this.columnNames.add("Etiketak");
		this.columnNames.add("Album");
		this.columnNames.add("Pribatutasuna");
		this.columnNames.add("Badago?");
		for (Argazkia argazkia : argaz) {
			data.add(argazkia);
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
		return this.data.get(rowIndex).getBalioa(columnIndex);
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
			emaitza = String.class;
			break;
		case 3:
			emaitza = Integer.class;	
			break;
		case 4:
			emaitza = String.class;
			break;
		case 5:
			emaitza = Pribatutasuna.class;
			break;
		case 6:
			emaitza = Boolean.class;
			break;

		default:
			break;
		}
		return emaitza;
	}
	
	
	public boolean isCellEditable(int row, int col){
		return(col>=1 && col<3 && row >=0 && row<this.getRowCount());

	}
	
	public void setValueAt(Object value, int row, int col){

			//System.out.println(this.data.get(row).getBalioa(col));
			this.data.get(row).insertElementAt(value, col);
			//System.out.println(this.data.get(row).getBalioa(col));
		
	}
	
	public Argazkia getArgazkia(int i){
		return this.data.elementAt(i);
	}
	
}
