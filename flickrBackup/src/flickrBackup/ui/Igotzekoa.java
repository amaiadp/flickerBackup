package flickrBackup.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import flickrBackup.model.Argazkia;
import flickrBackup.model.Argazkia.Pribatutasuna;
import flickrBackup.model.Nagusia;

public class Igotzekoa extends JPanel {
	

	private File karpeta;
	private JTable taula =null;
	private IgotzekoaJT model=null;
	private JScrollPane argazkiSP;
	private JSplitPane splitPane;
	private JScrollPane aldaketaSP;
	
	
	public void sortu(){
		this.setLayout(new BorderLayout());
		
		JPanel goikoJP = new JPanel(new FlowLayout());
		JPanel goikoEzkerJP = new JPanel(new FlowLayout());
		JPanel goikoEskuinJP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		goikoJP.add(goikoEzkerJP);
		goikoJP.add(goikoEskuinJP);
		JButton ireki = new JButton("IREKI");
		this.add(goikoJP, BorderLayout.NORTH);
		goikoEzkerJP.add(ireki);
		ireki.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int aukera = chooser.showOpenDialog(Igotzekoa.this);
				 if (aukera == JFileChooser.APPROVE_OPTION) {
						karpeta = chooser.getSelectedFile();
//						System.out.println(karpeta);
//						 Igotzekoa.this.pantailaratuArgazkiak();
						 Igotzekoa.this.argazkiakLortu();
						 ((JFrame) SwingUtilities.getWindowAncestor(Igotzekoa.this)).pack();
			      } 

			}
		});
		
		JButton igo = new JButton("Igo Argazkiak");
		goikoEzkerJP.add(igo);
		
		igo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (taula != null){
					int[] indizeak = taula.getSelectedRows();
					int zenbat = indizeak.length;
					for (int index : indizeak){
						 Argazkia a = model.getArgazkia(index);
						 a.igo();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Ez dago argazkirik hautatuta");
				}
				
			}
		});
		
		JButton logout = new JButton("Logout");
		goikoEskuinJP.add(logout);
		logout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PrintWriter writer;
				try {
					writer = new PrintWriter(new File("src/setup.properties"));
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				((JFrame) SwingUtilities.getWindowAncestor(Igotzekoa.this)).dispose();
				new LoginUI();
				
			}
		});
		
		
		JPanel behekoJP = new JPanel(new BorderLayout());
		
		
		JPanel aldaketaJP = new JPanel(new FlowLayout());
		aldaketaSP = new JScrollPane(aldaketaJP);
		
		JLabel deskL = new JLabel("Deskribapena");
		JTextArea deskT = new JTextArea(3,30);
		//JScrollPane deskSP = new JScrollPane(deskT);
		

		JLabel etikL = new JLabel("Etiketak");
		JTextArea etikT = new JTextArea(3,30);
		//JScrollPane etikSP = new JScrollPane(etikT);
		
		JLabel pribL = new JLabel("Pribatutasuna");
		JComboBox<Pribatutasuna> pribCB = new JComboBox<Pribatutasuna>();
		for (Pribatutasuna elem : Pribatutasuna.values()) {
			pribCB.addItem(elem);
		}
		pribCB.setSelectedItem(null);

		aldaketaJP.add(deskL);
		aldaketaJP.add(deskT);
		aldaketaJP.add(etikL);
		aldaketaJP.add(etikT);
		aldaketaJP.add(pribL);
		aldaketaJP.add(pribCB);
		
		this.add(behekoJP, BorderLayout.SOUTH);
		
		JButton gorde = new JButton("Gorde");
		behekoJP.add(gorde, BorderLayout.SOUTH);
		gorde.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (taula != null && taula.getSelectedRows().length > 0){
					for (int index : taula.getSelectedRows()) {
						if (!deskT.getText().equals("")){
							taula.setValueAt(deskT.getText(), index, 2);
						}
						if (!etikT.getText().equals("")){
							ArrayList<String> list = new ArrayList<String>();
							for (String iterable_element : etikT.getText().split("[\\r\\n]+")) {
								list.add(iterable_element);
							};
							taula.setValueAt(list, index, 3);
						}
						if(pribCB.getSelectedItem()!=null){
							taula.setValueAt(pribCB.getSelectedItem(), index, 5);
						}
					}
					model.fireTableStructureChanged();
					pribCB.setSelectedItem(null);
					etikT.setText(null);
					deskT.setText(null);
				}
			}
		});
		
		
	}
	
	
	public void argazkiakLortu(){
		Nagusia nL = Nagusia.getInstantzia();
		ArrayList<Argazkia> igotzeko = nL.igotzekoArgazkiakLortu(karpeta);
		if (model==null){
			model = new IgotzekoaJT(igotzeko);
			taula = new JTable(model);
			taula.getTableHeader().setReorderingAllowed(false);
			taula.setRowHeight(60);
			taula.setRowMargin(5);
			argazkiSP = new JScrollPane(taula);
		
	
			splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			
			splitPane.setTopComponent(argazkiSP);
			
			splitPane.setBottomComponent(aldaketaSP);
			splitPane.setOneTouchExpandable(true);
			//splitPane.setDividerLocation(150);
			splitPane.setEnabled(true);
			splitPane.repaint();
			Dimension minimumSize = new Dimension(100, 50);
			aldaketaSP.setMinimumSize(minimumSize);
			argazkiSP.setMinimumSize(minimumSize);
			
			this.add(splitPane, BorderLayout.CENTER);
		}
		else{
			model.dataAldatu(igotzeko);
			model.fireTableDataChanged();
		}
		
		//this.add(tableContainer, BorderLayout.CENTER);

	}
	
//	public void pantailaratuArgazkiak(){
//		for (File f : karpeta.listFiles(Nagusia.IMAGE_FILTER)){
//			System.out.println(f);
//			try {
//				Image img = ImageIO.read(f).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
//				ImageIcon icon = new ImageIcon(img);
//				JLabel label = new JLabel();
//				label.setIcon(icon);
//				nagusia.add(label);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//           
//         }
//	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Igotzekoa n = new Igotzekoa();
		n.sortu();
		JFrame f = new JFrame();
		f.setContentPane(n);
		f.setVisible(true);
		f.pack();
	}
	
}
