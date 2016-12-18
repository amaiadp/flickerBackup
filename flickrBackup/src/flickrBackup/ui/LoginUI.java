package flickrBackup.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Properties;

import javax.sql.rowset.spi.TransactionalWriter;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.scribe.model.Token;
import org.scribe.model.Verifier;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;

import flickrBackup.kudeatzaileak.ErabiltzaileKud;
import flickrBackup.model.Nagusia;

public class LoginUI extends JFrame{

	
	private JTextField apiKey =  new JTextField(15);
	private JTextField secret = new JTextField(15);
	private JTextField tokenT;
	private Properties properties = null;
	private Flickr flickr;
	private Token token;
	private JTextField eraT;
	private JPasswordField pasT;
	
	public LoginUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.erabiltzaileaEskatu();
		
	}
	
	private void erabiltzaileaEskatu(){
		JLabel eraL = new JLabel("Flickr-eko erabiltzailea:");
		eraT = new JTextField(15);
		JButton sartu = new JButton("Sartu");
		JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEADING));
		//getContentPane().add(jp);

		this.add(jp, BorderLayout.CENTER);
		this.setVisible(true);
		jp.add(eraL);
		jp.add(eraT);
		jp.add(sartu);
		this.pack();
		sartu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean dago =Nagusia.getInstantzia().erabiltzaileakKonprobatu(eraT.getText());
				if (dago){
					pasahitzaEskatu();
				}
				else{
					pasahitzaEskatuLehenAldia();
				}
			}


		});
		
	}
	
	private void pasahitzaEskatu() {
		JLabel pasL = new JLabel("Pasahitza: ");
		JPasswordField pasT = new JPasswordField(15);
		JButton sartu = new JButton("Sartu");
		JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JPanel behekoa = new JPanel(new FlowLayout());
		JPanel nagusi = new JPanel(new BorderLayout());
		JButton atzera = new JButton("Atzera");

		jp.add(pasL);
		jp.add(pasT);
		behekoa.add(sartu);
		behekoa.add(atzera);
		nagusi.add(behekoa, BorderLayout.SOUTH);
		nagusi.add(jp, BorderLayout.CENTER);
		sartu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean zuzena =Nagusia.getInstantzia().pasahitzaKonprobatu(eraT.getText(),((JTextField)pasT).getText());
				if (zuzena){
					List<String[]> list = ErabiltzaileKud.getInstantzia().getErabiltzailea(eraT.getText());
					Nagusia.getInstantzia().propertiesFitxategiaBete(list);
					NagusiaUI.getNagusiaUI().hasieratu();
					NagusiaUI.getNagusiaUI().bistaratu();
					dispose();
				}
				else{
		            JOptionPane.showMessageDialog(null, "Sartutako pasahitza ez da zuzena", "Errorea", JOptionPane.ERROR_MESSAGE);

				}
			}


		});
		
		atzera.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new LoginUI();
				
			}
		});
		this.setContentPane(nagusi);
		this.pack();
		this.setVisible(true);
		
	}
	
	private void erabiltzaileBerria(){
		JPanel nagusi = new JPanel(new BorderLayout());
		JPanel keySec = new JPanel(new FlowLayout());
		JLabel apikeyL = new JLabel("Api key");
		JLabel secretL = new JLabel("Secret");
//		apiKey = new JTextField(15);
//		secret = new JTextField(15);
		
		keySec.add(apikeyL);
		keySec.add(apiKey);
		keySec.add(secretL);
		keySec.add(secret);
		
		nagusi.add(keySec, BorderLayout.CENTER);
		JPanel behekoJP = new JPanel(new FlowLayout());
		JButton sartu = new JButton("Sartu");
		behekoJP.add(sartu);
		JButton atzera = new JButton("Atzera");
		behekoJP.add(atzera);
		JButton apiEZ = new JButton("Ez daukat apiKey");
		behekoJP.add(apiEZ);
		nagusi.add(behekoJP, BorderLayout.SOUTH);
		sartu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String url = getURL(apiKey.getText(),secret.getText());
				//System.out.println(url);
				if (url!=null){
					Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
				    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
				        try {
				            desktop.browse(new URI(url));
				        } catch (Exception e1) {
				        	JTextArea text = new JTextArea("Sartu honako helbidean: "+ url);
				        	text.setEditable(false);
				            JOptionPane.showMessageDialog(null, text, "Login", JOptionPane.INFORMATION_MESSAGE);
				        }
				    }
				    tokenaEskatu();
				}else{
		            JOptionPane.showMessageDialog(null, "Sartutako datuak ez dira zuzenak", "Errorea", JOptionPane.ERROR_MESSAGE);
				}
			    
			}
		});
		
		atzera.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new LoginUI();
				
			}
		});
		
		apiEZ.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String url = "https://www.flickr.com/services/api/misc.api_keys.html";
				//System.out.println(url);
					Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
				    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
				        try {
				            desktop.browse(new URI(url));
				        } catch (Exception e1) {
				        	JTextArea text = new JTextArea("Sartu honako helbidean: "+ url);
				        	text.setEditable(false);
				            JOptionPane.showMessageDialog(null, text, "API key lortu", JOptionPane.INFORMATION_MESSAGE);
				        }
				    }			
			}
		});
		
		this.setContentPane(nagusi);
		this.pack();
		this.setVisible(true);
	}

	private String getURL(String key, String secret) {
		String url=null;
		flickr = new Flickr(key, secret, new REST());
        Flickr.debugStream = false;
        AuthInterface authInterface = flickr.getAuthInterface();

        try{
        	token = authInterface.getRequestToken();

        	url = authInterface.getAuthorizationUrl(token, Permission.DELETE);
        }catch(Exception e){
        	
        }
        
		return url;
	}
	
	
	private void tokenaEskatu(){
		JPanel nagusi = new JPanel(new BorderLayout());
		JLabel tokl = new JLabel("Token");
		tokenT = new JTextField(15);
		JPanel erdikoPanela = new JPanel(new FlowLayout());
		erdikoPanela.add(tokl);
		erdikoPanela.add(tokenT);
		nagusi.add(erdikoPanela);
		JButton bidali = new JButton("Bidali");
		nagusi.add(bidali, BorderLayout.SOUTH);
		
		bidali.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					AuthInterface authInterface = flickr.getAuthInterface();
					Token requestToken = authInterface.getAccessToken(token, new Verifier(tokenT.getText()));
			        Auth auth = authInterface.checkToken(requestToken);
					Nagusia.getInstantzia().sortuErabiltzailearenDatuak(requestToken, auth, apiKey.getText(), secret.getText(),((JTextField)pasT).getText());
					NagusiaUI.getNagusiaUI().hasieratu();
					NagusiaUI.getNagusiaUI().bistaratu();
					dispose();
				}catch(Exception e1){
					e1.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Tokena ez da zuzena.", "Errorea", JOptionPane.ERROR_MESSAGE);
		            erabiltzaileBerria();
				}


				
			}

					});
		
		this.setContentPane(nagusi);
		this.pack();
		this.setVisible(true);
	}
	
	private void pasahitzaEskatuLehenAldia() {
		JPanel nagusi = new JPanel(new BorderLayout());
		JLabel pasl = new JLabel("Pasahitz bat sartu");
		pasT =  new JPasswordField(15);
		JPanel erdikoPanela = new JPanel(new FlowLayout());
		JButton atzera = new JButton("Atzera");
		JPanel behekoa = new JPanel(new FlowLayout());
		
		erdikoPanela.add(pasl);
		erdikoPanela.add(pasT);
		nagusi.add(erdikoPanela);
		JButton bidali = new JButton("Bidali");
		behekoa.add(bidali);
		behekoa.add(atzera);
		
		nagusi.add(behekoa, BorderLayout.SOUTH);
		
		bidali.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				erabiltzaileBerria();
			}

		});
		
		atzera.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new LoginUI();
				
			}
		});
		
		
		this.setContentPane(nagusi);
		this.pack();
		this.setVisible(true);				
	}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new LoginUI();
	}
	
	
	
}
