package flickrBackup.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Collection;

import javax.swing.JOptionPane;
import org.scribe.model.Token;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.uploader.Uploader;
import com.flickr4java.flickr.util.IOUtilities;

import flickrBackup.kudeatzaileak.AlbumakKud;
import flickrBackup.kudeatzaileak.ErabiltzaileKud;
import flickrBackup.model.Argazkia.Pribatutasuna;
import flickrBackup.ui.LoginUI;
import flickrBackup.ui.NagusiaUI;

public class Nagusia {

	private static Nagusia instantzia = getInstantzia();
	private ArrayList<Argazkia> igotzekoa;
	
	static String apiKey;
	static String sharedSecret;
	static Flickr f;
	public static Boolean berridatzi = null;
	REST rest;
	RequestContext requestContext;
	Properties properties = null;


	static final String[] EXTENSIONS = new String[]{
	        "jpeg", "jpg", "gif", "png", "bmp"// and other formats you need
	    };
	
	 public static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

	        @Override
	        public boolean accept(final File dir, final String name) {
	            for (final String ext : EXTENSIONS) {
	                if (name.endsWith("." + ext)) {
	                    return (true);
	                }
	            }
	            return (false);
	        }
	    };
	
	
	
	private Nagusia() throws IOException{
	}

	public static Nagusia getInstantzia(){
		if (instantzia==null){
			try {
				instantzia = new Nagusia();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instantzia;
	}
	
	private void flickrSortu(){
		InputStream in = null;
		try {
			in = getClass().getResourceAsStream("/setup.properties");
			properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			IOUtilities.close(in);
		}
		f = new Flickr(properties.getProperty("apiKey"), properties.getProperty("secret"), new REST());
		requestContext = RequestContext.getRequestContext();
		Auth auth = new Auth();
		auth.setPermission(Permission.READ);
		auth.setToken(properties.getProperty("token"));
		auth.setTokenSecret(properties.getProperty("tokensecret"));
		requestContext.setAuth(auth);
		Flickr.debugRequest = false;
		Flickr.debugStream = false;
		apiKey = f.getApiKey();
		sharedSecret = f.getSharedSecret();
	}
	
	
	public Uploader getUploader(){
		if(f==null){
			flickrSortu();
		}
		return f.getUploader();
	}
	
	public PhotosetsInterface getPhotosetsInterface(){
		if(f==null){
			flickrSortu();
		}
		return f.getPhotosetsInterface();
	}
	
	public PhotosInterface getPhotosInterface(){
		if(f==null){
			flickrSortu();
		}
		return f.getPhotosInterface();
	}
	
	public String getProperty(String key){
		return (properties.getProperty(key));
	}

	public ArrayList<Argazkia> igotzekoArgazkiakLortu(File pF){
		igotzekoa= new ArrayList<Argazkia>();
		for (File f : pF.listFiles(IMAGE_FILTER)){
			igotzekoa.add(new Argazkia(f));
		}
		return igotzekoa;
	}
	
//	private String album;
//	
//	private void setAlbum(String a){
//		album = a;
//	}
	
//	public void argazkiakIgo(){
//		for(Argazkia argazki: igotzekoa){
//			if(argazki.getFlickrID()==null){
//				String idF = argazki.igo(f.getUploader());
// Esto son todo para pruebas, al igual que el if de arriba
//				System.out.println(idF);
//				if (argazki.getIzena().equals("4.png")){
//					String idPS = albumaSortu("PruebasFrikis", null, idF);
//					System.out.println(idPS);
//					setAlbum(idPS);
//				}
//				else{
//					albumeraGehitu(album, idF);
//				}
//			}
//			else{
//				argazki.aldatu(f.getPhotosInterface());
//			}
//		}
//		
//	}
	
	public void albumeraGehitu(String photosetID, String photoID){
		PhotosetsInterface psi = f.getPhotosetsInterface();
		try {
			psi.addPhoto(photosetID, photoID);
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String albumaSortu(String titulua, String deskr, String photoID){
		PhotosetsInterface psi = f.getPhotosetsInterface();
		try {
			Photoset ps = psi.create(titulua, deskr, photoID);
			String psID = ps.getId();
			return psID;
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void albumakLortu(){
		PhotosetsInterface pi = getPhotosetsInterface();
		try {
			Photosets ps = pi.getList(properties.getProperty("nsid"));
			Collection<Photoset> cps = ps.getPhotosets();
			for(Photoset p: cps){
				AlbumakKud alkud = AlbumakKud.getInstantzia();
				String id = p.getId();
				if(!alkud.DBandago(id)){
					Photoset pset = pi.getInfo(p.getId());
					alkud.albumaSartu(pset.getId(), pset.getTitle(), pset.getDescription(), pset.getOwner().getRealName());
				}

			}
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void propertiesFitxategiaBete(List<String[]> datuak){
		Properties properties = new Properties();
		for (String[] strings : datuak) {
			properties.setProperty(strings[0],strings[1]);
		}
		properties.remove("password");
		try {
			FileOutputStream out = new FileOutputStream(new File("src/setup.properties"));
			properties.store(out, null);
			//out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		InputStream in = null;
		try {
			in = new FileInputStream("src/setup.properties");
			Properties properties = new Properties();
			properties.load(in);
			if (properties.getProperty("apiKey")==null || properties.getProperty("apiKey").equals("")){
				new LoginUI();
			}
			else{
				NagusiaUI.getNagusiaUI().hasieratu();
				NagusiaUI.getNagusiaUI().bistaratu();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Errorea setup.properties fitxategi kargatzean.");

		} finally {
			IOUtilities.close(in);
		}
    }

	public boolean erabiltzaileakKonprobatu(String username) {
		//erabiltzailea DB-n badago true itzultzen du
		List<String[]> datuak = ErabiltzaileKud.getInstantzia().getErabiltzailea(username);
		if (datuak.size()>0){
			for (String[] strings : datuak) {
				System.out.println(strings[0]+ "    " + strings[1]);

			}
		}
		
		return (datuak.size()>0);
	}

	public void sortuErabiltzailearenDatuak(Token requestToken, Auth auth, String apiKey, String secret, String passw) {
		List<String[]> lista = new ArrayList<>();
        String[] token = new String[2];
        token[0]= "token";
        token[1]= requestToken.getToken();
        String[] tokensecret = new String[2];
        tokensecret[0] = "tokensecret";
        tokensecret[1]= requestToken.getSecret();
        String[] nsid = new String[2];
        nsid[0]= "nsid";
        nsid[1] = auth.getUser().getId();
        String[] displayname = new String[2];
        displayname[0] = "displayname";
        displayname[1] = auth.getUser().getRealName();
        String[] username = new String[2];
        username[0] = "username";
        username[1] = auth.getUser().getUsername();
        String[] apiKeyLista = new String[2];
        apiKeyLista[0] = "apiKey";
        apiKeyLista[1] = apiKey;
        String[] secretL = new String[2];
        secretL[0] = "secret";
        secretL[1] = secret;
        String[] passL = new String[2];
        passL[0] = "password";
        passL[1] = passw;
        
        lista.add(username);
        lista.add(displayname);
        lista.add(tokensecret);
        lista.add(token);
        lista.add(nsid);
        lista.add(secretL);
        lista.add(apiKeyLista);
        lista.add(passL);
        
        for (String[] string : lista) {
			System.out.println(string[0]+ "    " + string[1]);
		}
        propertiesFitxategiaBete(lista);
        ErabiltzaileKud.getInstantzia().sortuErabiltzailea(lista, username[1]);
	}

	public boolean pasahitzaKonprobatu(String username, String pass) {
		String pasahitza = ErabiltzaileKud.getInstantzia().getPasahitza(username);
		return pasahitza.equals(pass);
	}

	
}
