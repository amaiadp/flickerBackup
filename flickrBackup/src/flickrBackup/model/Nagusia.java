package flickrBackup.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.util.IOUtilities;

import flickrBackup.model.Argazkia.Pribatutasuna;


public class Nagusia {

	private static Nagusia instantzia = getInstantzia();
	private ArrayList<Argazkia> igotzekoa;
	
	static String apiKey;
	static String sharedSecret;
	Flickr f;
	REST rest;
	RequestContext requestContext;
	Properties properties = null;
	
	
	static final String[] EXTENSIONS = new String[]{
	        "jpg", "gif", "png", "bmp"// and other formats you need
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
		InputStream in = null;
		try {
			in = getClass().getResourceAsStream("/setup.properties");
			properties = new Properties();
			properties.load(in);
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
	
	public void argazkiakIgo(){
		for(Argazkia argazki: igotzekoa){
			if(argazki.getFlickrID()==null){
				String idF = argazki.igo(f.getUploader());
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
			}
			else{
				argazki.aldatu(f.getPhotosInterface());
			}
		}
		
	}
	
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

	public ListaArgazki lortuFlicrkekoArgazkiak() {
		PhotosInterface pi = f.getPhotosInterface();
		
		
		
		return null;
	}
	
	
//	public static void main(String[] args) {
//        try {
//            Nagusia t = new Nagusia();
//            t.igotzekoa = new ArrayList<Argazkia>();
//            Argazkia arg = new Argazkia(new File("/home/zihara/Imágenes/4.png"));
//            Argazkia arg1 = new Argazkia(new File("/home/zihara/Imágenes/5.png"));
//            Argazkia arg2 = new Argazkia(new File("/home/zihara/Imágenes/6.png"));
//            arg.setPrib(Pribatutasuna.PRIVACY_LEVEL_PRIVATE);
//            arg1.setPrib(Pribatutasuna.PRIVACY_LEVEL_FRIENDS);
//            arg2.setPrib(Pribatutasuna.PRIVACY_LEVEL_FAMILY);
//            t.igotzekoa.add(arg);
//            t.igotzekoa.add(arg1);
//            t.igotzekoa.add(arg2);
//            t.argazkiakIgo();
//        
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.exit(0);
//    }
}
