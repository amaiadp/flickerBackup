package flickrBackup.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import flickrBackup.model.Nagusia;

public class NagusiaUI extends JFrame {

	private static NagusiaUI nagusiaUI = new NagusiaUI();

	private boolean igoArgazkia = false;
	
	private NagusiaUI(){
	}
	
	public static NagusiaUI getNagusiaUI(){
		return nagusiaUI;
	}
	
	
	public void hasieratu(){
		getContentPane().setLayout(new BorderLayout());
		JTabbedPane tabPanel = new JTabbedPane(2);
		Igotzekoa igo = new Igotzekoa();
		igo.sortu();
		Bistaratu bis = new Bistaratu();
		bis.sortu();
		tabPanel.setTabPlacement(JTabbedPane.TOP);
		tabPanel.addTab("Igotzeko", igo);
		tabPanel.addTab("Bistaratu Flickr-eko Argazkiak", bis);
		getContentPane().add(tabPanel, BorderLayout.CENTER);
	}
	
	
	public void bistaratu() {
		this.pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NagusiaUI n = new NagusiaUI();
		n.hasieratu();
		n.bistaratu();
	}

	public boolean argazkiaDagoIgo() {
		JDialog jd = new JDialog();
		jd.setModal(true);
		JPanel jp = new JPanel(new BorderLayout());
		JPanel erdikoJp = new JPanel(new BorderLayout());
		JPanel behekoJp = new JPanel(new FlowLayout());
		jp.add(behekoJp, BorderLayout.SOUTH);
		jp.add(erdikoJp, BorderLayout.CENTER);
		JButton igo = new JButton("Igo");
		JButton ezIgo = new JButton("Ez igo");
		JLabel mezua = new JLabel("Argazkia dagoeneko Flickr-en dago. Igo nahi duzu?");
		JCheckBox gogoratu = new JCheckBox("Gogoratu aukera");
		behekoJp.add(igo);
		behekoJp.add(ezIgo);
		erdikoJp.add(mezua, BorderLayout.CENTER);
		erdikoJp.add(gogoratu, BorderLayout.SOUTH);
		
		igo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				igoArgazkia = true;
				if (gogoratu.isSelected()){
					Nagusia.berridatzi = new Boolean(true);
				}
				jd.dispose();
			}
		});
		
		ezIgo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gogoratu.isSelected()){
					Nagusia.berridatzi = new Boolean(false);
				}
				jd.dispose();
			}
		});
		
		
		jd.getContentPane().add(jp, BorderLayout.CENTER);
		jd.pack();
		jd.setVisible(true);
	
		
		return igoArgazkia;
	
	}

}
