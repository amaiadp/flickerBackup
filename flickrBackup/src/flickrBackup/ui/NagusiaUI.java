package flickrBackup.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class NagusiaUI extends JFrame {

	
	
	private void hasieratu(){
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

}
