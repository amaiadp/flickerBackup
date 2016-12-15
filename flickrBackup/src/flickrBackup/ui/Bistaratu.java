package flickrBackup.ui;

import java.awt.*;
import javax.swing.*;

public class Bistaratu extends JPanel{
    private JTable taula;
	private BistaratuJT model;
    
    public Bistaratu() {
    	super(new BorderLayout());
    }
    
    public void sortu(){
        argazkiakLortu();
//        createAndShowGUI();
    }

    private static void createAndShowGUI(){
        JFrame frame = new JFrame("Bistaratu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//       JComponent newContentPane = new Bistaratu();
//       newContentPane.setOpaque(true); 
//       frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void argazkiakLortu(){
		model = new BistaratuJT();
		taula = new JTable(model);
		taula.setRowHeight(60);
		taula.setRowMargin(5);
		taula.getTableHeader().setReorderingAllowed(false);
		JScrollPane tableContainer = new JScrollPane(taula);
		this.add(tableContainer, BorderLayout.CENTER);
	}

    public static void main(String[] args) {
    	Bistaratu j = new Bistaratu();
    	j.sortu();
    }
}
