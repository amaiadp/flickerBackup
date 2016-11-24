package flickrBackup.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.util.IOUtilities;

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
	
	private boolean badago(Argazkia argazki){
		return false;
		//TODO DB
	}
	
	public void argazkiakIgo(){
		for(Argazkia argazki: igotzekoa){
			argazki.igo(apiKey, sharedSecret);
		}
		
	}
}
