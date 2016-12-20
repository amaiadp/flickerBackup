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
import javax.swing.ImageIcon;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;

import flickrBackup.kudeatzaileak.ArgazkiakKud;
import flickrBackup.ui.NagusiaUI;

import com.flickr4java.flickr.photos.PhotosInterface;

public class Argazkia {

	private String id;
	private File path;
	private String izena;
	private String deskribapena;
	private List<String> etiketak;
	private ImageIcon thumbnail;
	private String flickrID;
	private Pribatutasuna prib = Pribatutasuna.PRIVACY_LEVEL_PUBLIC;
	private ListaAlbum albumak;
	
	
	public static enum Pribatutasuna{
		PRIVACY_LEVEL_PUBLIC, PRIVACY_LEVEL_FRIENDS, PRIVACY_LEVEL_FAMILY, PRIVACY_LEVEL_FRIENDS_FAMILY, PRIVACY_LEVEL_PRIVATE
	}
	
	public void setPrib(Pribatutasuna pri){
		prib = pri;
	}
	
	public Argazkia(File f){
		id = md5Lortu(f);
		ArgazkiakKud argkud = ArgazkiakKud.getInstantzia();
		thumbnail = thumbnailLortu(f);
		etiketak = new ArrayList<String>();
		path = f;
		albumak = new ListaAlbum();
		if(!argkud.badago(id, Nagusia.getInstantzia().getProperty("username"))){
			izena = f.getName();
			deskribapena = "";
			flickrID = null;
		}
		else{
			Object[] info = argkud.getArgazkia(id,Nagusia.getInstantzia().getProperty("username"));
			izena = (String) info[0];
			deskribapena = (String)info[1];
			setPrib((String)info[2]);
			flickrID = (String)info[3];
			etiketak = (List<String>) info[4];
			etiketak.remove(id);
			for(String id:(List<String>)info[5]){
				albumak.add(Albumak.getInstantzia().bilatu(id));
			}
			
		}
	}
		
	public void setPrib(String s){
		if(s.equals(Pribatutasuna.PRIVACY_LEVEL_FAMILY.toString())){
			prib = Pribatutasuna.PRIVACY_LEVEL_FAMILY;
		}
		else{
			if(s.equals(Pribatutasuna.PRIVACY_LEVEL_FRIENDS.toString())){
				prib = Pribatutasuna.PRIVACY_LEVEL_FRIENDS;
			}
			else{
				if(s.equals(Pribatutasuna.PRIVACY_LEVEL_FRIENDS_FAMILY.toString())){
					prib = Pribatutasuna.PRIVACY_LEVEL_FRIENDS_FAMILY;
				}
				else{
					if(s.equals(Pribatutasuna.PRIVACY_LEVEL_PRIVATE.toString())){
						prib = Pribatutasuna.PRIVACY_LEVEL_PRIVATE;
					}
					else{
						if(s.equals(Pribatutasuna.PRIVACY_LEVEL_PUBLIC.toString())){
							prib = Pribatutasuna.PRIVACY_LEVEL_PUBLIC;
						}
					}
				}
			}
		}
	}
	
	public Argazkia(String izen, String deskr, List<String> etik, ImageIcon thumb, String iD){
		izena = izen;
		deskribapena = deskr; 
		etiketak = etik;
		thumbnail = thumb;
		flickrID = iD;
	}
	

	public ImageIcon getThumb(){
		return thumbnail;
	}
	
	public String getPrib(){
		return prib.toString();
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
	
	public String getFlickrID(){
		return flickrID;
	}
	
	public void setFlickrID(String fID){
		flickrID = fID;
	}
	
	public ListaAlbum getAlbumak(){
		return albumak;
	}
	
	public void setAlbumak(ListaAlbum al){
		albumak = al;
	}
	
	public void setPribatutasuna(Boolean publi, Boolean fri, Boolean fam){
		if (publi){prib = Pribatutasuna.PRIVACY_LEVEL_PUBLIC;
		}
		else{
			if (fri && fam){prib = Pribatutasuna.PRIVACY_LEVEL_FRIENDS_FAMILY;}
			else{ 
				if(fri){prib = Pribatutasuna.PRIVACY_LEVEL_FRIENDS;}
				else{
					if(fam){prib = Pribatutasuna.PRIVACY_LEVEL_FAMILY;}
					else{prib = Pribatutasuna.PRIVACY_LEVEL_PRIVATE;}
				}
			}
		}
	}
	
	private UploadMetaData pribatutasunaEsleitu(UploadMetaData md){
		if(prib.equals(Pribatutasuna.PRIVACY_LEVEL_PUBLIC)){
			md.setPublicFlag(true);
		}
		else{
			//dando por supuesto que siendo Pribatutasuna.PRIVACY_LEVEL_NO_FILTER lo vamos a dar como pribatu
			md.setPublicFlag(false);
			if(prib.equals(Pribatutasuna.PRIVACY_LEVEL_FRIENDS_FAMILY)){
				md.setFriendFlag(true);
				md.setFamilyFlag(true);
			}
			if(prib.equals(Pribatutasuna.PRIVACY_LEVEL_FRIENDS)){
				md.setFriendFlag(true);
				md.setFamilyFlag(false);
			}
			if(prib.equals(Pribatutasuna.PRIVACY_LEVEL_FAMILY)){
				md.setFamilyFlag(true);
				md.setFriendFlag(false);
			}
		}
		return md;
	}
	
	private String[] lortuTags(){
		String[] emaitza = new String[etiketak.size()];
		int i =0;
		for(String e: etiketak){
			emaitza[i] = e; 
		}
		return emaitza;
	}
	
	
	
	private void ezabatu(){
		PhotosInterface pi = Nagusia.getInstantzia().getFlickr().getPhotosInterface();
		try {
			ArgazkiakKud argkud = ArgazkiakKud.getInstantzia();
			argkud.argazkiaEzabatu(id,Nagusia.getInstantzia().getProperty("username"));
			pi.delete(flickrID);
			
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flickrID = null;
	}
	
	private void igo1(){
		Uploader up = Nagusia.getInstantzia().getFlickr().getUploader();
		UploadMetaData md = new UploadMetaData();
		etiketak.add(id);
		md.setTags(etiketak);
		md.setDescription(deskribapena);
		md.setTitle(izena);
		md = pribatutasunaEsleitu(md);
		String idF =null;
		try {
			idF = up.upload(path, md);
			if (idF!=null){
				System.out.println(getIzena()+":Lo ha subido");
				setFlickrID(idF);
				ArgazkiakKud argkud = ArgazkiakKud.getInstantzia();
				argkud.argazkiaSartu(id,izena,deskribapena,getPrib(),flickrID,Nagusia.getInstantzia().getProperty("username"));
				argkud.etiketakSartu(id,etiketak,Nagusia.getInstantzia().getProperty("username"));
				if (albumak!=null){
					albumak.argazkiaSartu(id,idF);
				}
			}
			else{
				System.out.println(getIzena()+":EZ TEU IGON");
			}
		} catch (FlickrException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void igo(){
		if(flickrID==null){
			igo1();
		}
		else{
			if(!Albumak.getInstantzia().ppDa(flickrID)){
				if(Nagusia.berridatzi==null){
					boolean igo = NagusiaUI.getNagusiaUI().argazkiaDagoIgo();
					if (igo){
						ezabatu();
						igo1();
					}else{
						this.argazkiaMantenduAldatuDatu();
					}
				}
				else{
					if(Nagusia.berridatzi){
						ezabatu();
						igo1();
					}else{
						this.argazkiaMantenduAldatuDatu();
					}
				}
			}
			else{
				NagusiaUI.getNagusiaUI().mezuaErakutsi(izena+" argazkia album baten primary photo-a da. Ezin da berriro igo.");
				this.argazkiaMantenduAldatuDatu();
			}
		}
	}
	
	private void argazkiaMantenduAldatuDatu(){
		ArgazkiakKud argkud = ArgazkiakKud.getInstantzia();
		Object[] info = argkud.getArgazkia(id,Nagusia.getInstantzia().getProperty("username"));
		izena = (String) info[0];
		deskribapena = (String)info[1];
		setPrib((String)info[2]);
		flickrID = (String)info[3];
		etiketak = (List<String>) info[4];
		etiketak.remove(id);
		albumak = new ListaAlbum();
		for(String id:(List<String>)info[5]){
			albumak.add(Albumak.getInstantzia().bilatu(id));
		}
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
			etiketak.remove(id);
			erantzuna = etiketak;
			break;
		case 4:
			if (albumak!=null){
				erantzuna = albumak.inprimatu();
			}
			else{
				erantzuna = null;
			}
			break;
		case 5:
			erantzuna = prib;
			break;
		case 6:
			erantzuna = (flickrID!=null);
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
			List<String> lista = (List<String>) value;
			etiketak = new ArrayList<String>();
			for (String string : lista) {
				if (!etiketak.contains(string)){
					etiketak.add(string);
				}
			}
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

	public void albumakEsleitu(ListaAlbum aukeratutakoAlbumak) {
		this.albumak = new ListaAlbum();
		for (int i = 0; i < aukeratutakoAlbumak.luzeera(); i++) {
			albumak.add(aukeratutakoAlbumak.getAlbum(i));
		}
	}

	public void gehituAlbum(Album al) {
		this.albumak.add(al);
	}
	
//	public static void main(String[] args) {
//		PhotosInterface pi = Nagusia.getInstantzia().getPhotosInterface();
//		String pid = "31136300990";
//		String izena = "dadoak";
//		String deskr = "frogak ein";
//		String[] etik = {"no me lo puedo creer", "func", "iona"};
//		try {
//			Photo ph = pi.getPhoto(pid);
//			pi.setMeta(pid, izena, deskr);
//			pi.setTags(pid,etik );
//			System.out.println("Pu: "+ ph.isPublicFlag()+ "  Fam: "+ph.isFamilyFlag()+ "  Fri: "+ph.isFriendFlag());
//		} catch (FlickrException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

}
