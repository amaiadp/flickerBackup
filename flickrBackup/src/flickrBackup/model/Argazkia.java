package flickrBackup.model;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Argazkia {

	private String id;
	private File path;
	private String izena;
	private String deskribapena;
	private List<String> etiketak;
	private ImageIcon thumbnail;
	private Pribatutasuna prib = Pribatutasuna.PRIVACY_LEVEL_NO_FILTER;
	
	public static enum Pribatutasuna{
		PRIVACY_LEVEL_NO_FILTER, PRIVACY_LEVEL_PUBLIC, PRIVACY_LEVEL_FRIENDS, PRIVACY_LEVEL_FAMILY, PRIVACY_LEVEL_FRIENDS_FAMILY, PRIVACY_LEVEL_PRIVATE
	}
	
	
	
	public Argazkia(File f){
		id = md5Lortu(f);
		izena = f.getName();
		path = f;
		etiketak = new ArrayList<String>();
		etiketak.add(id);
		thumbnail = thumbnailLortu(f);
		
		System.out.println(f);
		System.out.println(id);
		System.out.println(izena);
		System.out.println(etiketak.get(0));
		
	}
	


	private String md5Lortu(File f){
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
	        FileInputStream fis = new FileInputStream(f);
	
	        byte[] dataBytes = new byte[1024];
	
	        int nread = 0;
	        while ((nread = fis.read(dataBytes)) != -1) {
	          md.update(dataBytes, 0, nread);
	        };
	        byte[] mdbytes = md.digest();
	
	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < mdbytes.length; i++) {
	          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	        }

			return sb.toString();
		}
		catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	
	private ImageIcon thumbnailLortu(File f){
		Image img;
		try {
			img = ImageIO.read(f).getScaledInstance(60, 60, BufferedImage.SCALE_SMOOTH);
		
		ImageIcon icon = new ImageIcon(img);
		return icon;
		}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	}
	
	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public String getDeskribapena() {
		return deskribapena;
	}

	public void setDeskribapena(String deskribapena) {
		this.deskribapena = deskribapena;
	}

	public String getId() {
		return id;
	}

	public File getPath() {
		return path;
	}

	public List<String> getEtiketak() {
		return etiketak;
	}
	
	public ImageIcon getThumbnail() {
		return thumbnail;
	}
	
	
	public Object getBalioa(int i){
		Object erantzuna = null;
		switch (i) {
		case 0:
			erantzuna = thumbnail;
			break;
		case 1:
			erantzuna = izena;
			break;
		case 2:
			erantzuna = deskribapena;
			break;
		case 3:
			erantzuna = etiketak;
			break;
		case 4:
			erantzuna = null;
			break;
		case 5:
			erantzuna = prib;
			break;
		case 6:
			erantzuna = null;
			break;
		default:
			break;
		}
		
		return erantzuna;
	}
	
	public void insertElementAt(Object value, int i){
		switch (i) {
		case 0:
			thumbnail = (ImageIcon) value;
			break;
		case 1:
			izena = (String) value;
			break;
		case 2:
			deskribapena = (String) value;
			break;
		case 3:
			etiketak = (List<String>) value;
			break;
//		case 4:
//			erantzuna = null;
//			break;
		case 5:
			prib = (Pribatutasuna) value;
			break;
//		case 6:
//			erantzuna = null;
//			break;
		default:
			break;
		}
		
	}
}
