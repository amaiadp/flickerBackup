package flickrBackup.kudeatzaileak;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import flickrBackup.model.Argazkia;

public class ArgazkiakKud {

	private static final ArgazkiakKud argazkiakKud = new ArgazkiakKud();
	
	public static ArgazkiakKud getInstantzia(){
		return argazkiakKud;
	}

	private ArgazkiakKud() {
		// Singleton patroia
	}

	
	public Object[] getArgazkia(String id, String username){
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL(String.format("select izena,deskribapena,pribatutasuna,flickrID from argazkia WHERE md5='%s' AND username='%s'",id,username));
		ResultSet albrs = dbkud.execSQL(String.format("Select idAlbum From Izan Where md5='%s' AND username='%s'", id,username));
		try {
			rs.next();
			Object[] res = new Object[6];
			res[0] = rs.getString("izena");
			res[1] = rs.getString("deskribapena");
			res[2] = rs.getString("pribatutasuna");
			res[3] = rs.getString("flickrID");
			res[4] = getEtiketak(id,username);
			ArrayList<String> albums = new ArrayList<String>();
			while(albrs.next()){
				albums.add(albrs.getString("idAlbum"));
			}
			if(albums.isEmpty()){
				albums = null;
			}
			res[5] = albums;
			return res;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Object[]> argazkiakLortu(String username) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL(String.format("SELECT md5,izena,deskribapena,pribatutasuna FROM argazki WHERE username='%s'",username));
		List<Object[]> emaitza = new ArrayList<Object[]>();
		try {
			while(rs.next()){
				Object[] em = new Object[4];
				em[0] = rs.getString("izena");
				em[1] = rs.getString("deskribapena");
				em[2] = rs.getString("pribatutasuna");
				em[3] = getEtiketak(rs.getString("md5"),username);
				emaitza.add(em);
			}
			// TODO Auto-generated method stub
			return emaitza;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private List<String> getEtiketak(String md5, String username){
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rsetik =dbkud.execSQL(String.format("SELECT etiketa FROM Etiketak WHERE md5='%s' AND username='%s'", md5, username));
		ArrayList<String> etiketak = new ArrayList<String>();
		try {
			while(rsetik.next()){
				etiketak.add(rsetik.getString("etiketa"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return etiketak;
	}

	public void argazkiaSartu(String md5, String izen, String deskr, String prib, String flickrID, String username) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		dbkud.execSQL(String.format("INSERT INTO argazkia (md5,izena,deskribapena,pribatutasuna,flickrID,username) VALUES ('%s','%s','%s','%s','%s','%s')", md5,izen,deskr,prib,flickrID,username));
	}

	public void etiketakSartu(String md5, List<String> etiketak, String username){
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		for(String etiketa: etiketak){
			if(!badago(md5,etiketa,username)){
				dbkud.execSQL(String.format("INSERT INTO Etiketak (md5,etiketa,username) VALUES('%s','%s','%s')", md5,etiketa,username));
			}
		}
	}

	private boolean badago(String md5, String etiketa, String username){
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL(String.format("SELECT * FROM Etiketak WHERE md5='%s' AND etiketa='%s' AND username='%s'",md5,etiketa,username));
		try {
			rs.next();
			rs.getString("md5");
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public void argazkiaEzabatu(String md5, String username) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		dbkud.execSQL(String.format("DELETE FROM argazkia WHERE md5='%s' AND username='%s'", md5,username));
		dbkud.execSQL(String.format("DELETE FROM Etiketak WHERE md5='%s' AND username='%s'", md5,username));
	}
	
	public boolean badago(String md5, String username){
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL(String.format("SELECT * FROM argazkia WHERE md5='%s' AND username='%s'",md5,username));
		try {
			rs.next();
			rs.getString("md5");
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	
//	public List<String[]> getEzarpenak(){
//		
//		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
//		ResultSet rs = dbkud.execSQL("select userid, `key`, value from properties");
//		List<String[]> emaitza = new ArrayList<String[]>();
//		try {
//			while(rs.next()){
//				String[] res = new String[3];
//				res[0] = rs.getString("userid");
//				res[1] = rs.getString("key");
//				res[2] = rs.getString("value");
//				emaitza.add(res);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return emaitza;
//	}
//
//	public void gorde(String erabid, String gakoa, String balioa) {
//		DBKudeatzaile.getInstantzia().execSQL(String.format("insert into properties set userid='%s',`key`='%s',value='%s'",erabid,gakoa,balioa));
//		
//	}
}
