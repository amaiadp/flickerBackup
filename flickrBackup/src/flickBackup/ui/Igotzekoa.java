package flickBackup.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import flickrBackup.model.Argazkia;
import flickrBackup.model.ListaArgazki;
import flickrBackup.model.Nagusia;

public class Igotzekoa extends JPanel {
	
	private JPanel nagusia;
//	private JLabel aukeratu;
	private JLabel argazkia;
	private JLabel izena;
	private JLabel deskr;
	private JLabel etiketak;
	private JLabel album;
	private JLabel pribatu;
	private JLabel badago;
	private File karpeta;
	private ArrayList<JCheckBox> cblist = new ArrayList<JCheckBox>();
	private ListaArgazki aukeratutakoArgazkiak = new ListaArgazki();
	

	
	public void sortu(){
		this.setLayout(new BorderLayout());
		nagusia = new JPanel();
		nagusia.setLayout(new GridLayout(0, 8,0, 2));
		JCheckBox aukeratu = new JCheckBox("Denak Aukeratu");
		aukeratu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (JCheckBox jCheckBox : cblist) {
					jCheckBox.setSelected(aukeratu.isSelected());
				}
				
			}
		});
//		aukeratu = new JLabel("Aukeratu", SwingConstants.CENTER);
		argazkia = new JLabel("Argazkia", SwingConstants.CENTER);
		izena = new JLabel("Izena", SwingConstants.CENTER);
		deskr = new JLabel("Deskribapena", SwingConstants.CENTER);
		etiketak = new JLabel("Etiketak", SwingConstants.CENTER);
		album = new JLabel("Album", SwingConstants.CENTER);
		pribatu = new JLabel("Pribatutasuna", SwingConstants.CENTER);
		badago = new JLabel("Badago?", SwingConstants.CENTER);
		nagusia.add(aukeratu);
		nagusia.add(argazkia);
		nagusia.add(izena);
		nagusia.add(deskr);
		nagusia.add(etiketak);
		nagusia.add(album);
		nagusia.add(pribatu);
		nagusia.add(badago);
		//this.add(nagusia, BorderLayout.CENTER);
		
		JScrollPane jsp = new JScrollPane(nagusia);
		this.add(jsp, BorderLayout.CENTER);
		
		JButton ireki = new JButton("IREKI");
		this.add(ireki, BorderLayout.NORTH);
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
		
		
	}
	
	
	public void argazkiakLortu(){
		Nagusia nL = Nagusia.getInstantzia();
		cblist = new ArrayList<>();
		ArrayList<Argazkia> igotzeko = nL.igotzekoArgazkiakLortu(karpeta);
		for (Argazkia argazkia : igotzeko) {
			JCheckBox cb = new JCheckBox();
			cb.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (cb.isSelected()){
						aukeratutakoArgazkiak.gehituArgazkia(argazkia);
					}
					else{
						aukeratutakoArgazkiak.ezabatuArgazkia(argazkia);
					}
				}
			});
			cblist.add(cb);
			nagusia.add(cb);
			JLabel thumb = new JLabel("", SwingConstants.CENTER);
			thumb.setIcon(argazkia.getThumbnail());
			nagusia.add(thumb);
			nagusia.add(new JLabel(argazkia.getIzena(), SwingConstants.CENTER));
			nagusia.add(new JLabel(argazkia.getDeskribapena(), SwingConstants.CENTER));
			nagusia.add(new JLabel(argazkia.getEtiketak().get(0), SwingConstants.CENTER));
			nagusia.add(new JLabel("", SwingConstants.CENTER));
			nagusia.add(new JLabel("", SwingConstants.CENTER));
			nagusia.add(new JLabel("", SwingConstants.CENTER));
		}
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
