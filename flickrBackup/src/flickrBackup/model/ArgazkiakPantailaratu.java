package flickrBackup.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.tags.Tag;
import com.flickr4java.flickr.util.IOUtilities;

import flickrBackup.kudeatzaileak.ArgazkiakKud;

public class ArgazkiakPantailaratu {

	static String apiKey;

	static String sharedSecret;

	Flickr f;

	REST rest;

	RequestContext requestContext;

	Properties properties = null;
	
	HashMap<String, String> ids = new HashMap<String,String>();

	public ArgazkiakPantailaratu() throws IOException {
		InputStream in = null;
		try {
			in = new FileInputStream("src/setup.properties");
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

	public static void main(String[] args) {
		try {
			ArgazkiakPantailaratu t = new ArgazkiakPantailaratu();
//			t.showPhotos();
			t.preparePhotos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

//	public ArrayList<Argazkia> preparePhotos(){
//		String username = properties.getProperty("username");
//		ArrayList<Argazkia> argkol = new ArrayList<Argazkia>();
//		List<Object[]> argazkiak = ArgazkiakKud.getInstantzia().argazkiakLortu(username);
//		
//	}

	public ArrayList<Argazkia> preparePhotos() {
		ArrayList<Argazkia> argkol = new ArrayList<Argazkia>();
		String userId = properties.getProperty("nsid");
		String secret1 = properties.getProperty("secret");
		PhotosetsInterface photosetsInterface = f.getPhotosetsInterface();
		Photosets photosets;
		PhotosInterface pi = f.getPhotosInterface();
		try {
			Collection<Photo> argazkiak = pi.getNotInSet(100, 1);
			Argazkia a;
			for (Photo argazki: argazkiak){
				if (!badago(argazki.getId())){
					Photo arg = pi.getInfo(argazki.getId(), secret1);
					System.out.println("\t" + "Title: "+arg.getTitle()+" Deskr: "+arg.getDescription()+" Tags: " +arg.getTags().toString());
					ids.put(arg.getId(), null);
					a = argazkiaSortu(arg);
					argkol.add(a);
				}
			}
			photosets = photosetsInterface.getList(userId);
			Collection<Photoset> bildumak = photosets.getPhotosets();

			for (Photoset photoset : bildumak) {
				String id = photoset.getId();
				String title = photoset.getTitle();
				String secret = photoset.getSecret();
				int photoCount = photoset.getPhotoCount();
				System.out.println("Title:" + title +" Deskr: " + photoset.getDescription());

				PhotoList<Photo> col;
				int PHOTOSPERPAGE = 2;
				int HOWMANYPAGES = photoCount/PHOTOSPERPAGE;
				if (photoCount % PHOTOSPERPAGE != 0){
					HOWMANYPAGES++;
				}
				for (int page = 1; page <= HOWMANYPAGES; page++) {
					col = photosetsInterface.getPhotos(id /* photosetId */, PHOTOSPERPAGE, page);
					for (Photo argazkia : col) {
						if (!badago(argazkia.getId())){
							Photo arg = pi.getInfo(argazkia.getId(), secret1);
							ids.put(arg.getId(), null);
							System.out.println("\t" + "Title: "+arg.getTitle()+" Deskr: "+arg.getDescription()+" Tags: " +arg.getTags().toString());
							a = argazkiaSortu(arg);
//							saveImage(argazkia);
							argkol.add(a);
						}
					}
				}
			}
		} catch (FlickrException e) {
			e.printStackTrace();
		}
		return argkol;
	}

	@SuppressWarnings("deprecation")
	private Argazkia argazkiaSortu(Photo p){
		String id =p.getId();
		String deskr = p.getDescription();
		String title = p.getTitle();
		ImageIcon img = null;
		try{
			img = new ImageIcon(p.getThumbnailImage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collection<Tag> tags= p.getTags();
		ArrayList<String> etiketak = new ArrayList<String>();
		String etiketa = null;
		for(Tag tag: tags){
			etiketa = tag.getValue();
			etiketak.add(etiketa);
		}
		Argazkia arg = new Argazkia(title,deskr,etiketak, img, id);
		arg.setPribatutasuna(p.isPublicFlag(), p.isFriendFlag(), p.isFamilyFlag());
		return arg;
	}

	private boolean badago(String id){
		return ids.containsKey(id);
	}
	
	//pics carpeta sortuta egon behar da
	@SuppressWarnings("deprecation")
	public boolean saveImage(Photo p) {
		String path = "pics" + File.separator;
		File largeFile = new File(path + p.getTitle() + p.getId() +'.'+ p.getOriginalFormat());
		
		try {
			System.out.println(p.getTitle()+":"+p.getId());
			Photo nfo = f.getPhotosInterface().getInfo(p.getId(), null);
			p.setOriginalSecret(nfo.getOriginalSecret());
			ImageIO.write(p.getLargeImage(), p.getOriginalFormat(), largeFile);
			System.out.println(p.getTitle() + "\t" + p.getOriginalUrl() + " was written to " + largeFile.getName());
		} catch (FlickrException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
