package flickrBackup.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Bistaratu extends JPanel{
    private JTable taula;
	private BistaratuJT model;
	private JPanel panela;
    
    
    public void sortu(){
    	this.setLayout(new BorderLayout());
    	JButton bistaratu = new JButton("Bistaratu");
    	bistaratu.addActionListener(new ActionListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				JScrollPane al = argazkiakLortu();
				JPanel bis = Bistaratu.this;
				bis.remove(bistaratu);
				bis.add(al);
				bis.revalidate();
				((JFrame) SwingUtilities.getWindowAncestor(bis)).pack();
				((JFrame) SwingUtilities.getWindowAncestor(bis)).setLocationRelativeTo(null);
				bis.setVisible(true);
			}
		});
    	this.add(bistaratu, BorderLayout.NORTH);
    	
    	
    }

    
    public JScrollPane argazkiakLortu(){
		model = new BistaratuJT();
		taula = new JTable(model);
		taula.setRowHeight(60);
		taula.setRowMargin(5);
		taula.getTableHeader().setReorderingAllowed(false);
		JScrollPane tableContainer = new JScrollPane(taula);
		return tableContainer;
	}

    public static void main(String[] args) {
    	Bistaratu n = new Bistaratu();
		n.sortu();
		JFrame f = new JFrame();
		f.setContentPane(n);
		f.setVisible(true);
		f.pack();
    }
}
