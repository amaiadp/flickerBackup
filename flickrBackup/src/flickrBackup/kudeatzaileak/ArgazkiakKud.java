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

	
	public List<String[]> getArgazkia(String id){
		
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL("select xxxxxx from argazkia");
		List<String[]> emaitza = new ArrayList<String[]>();
		try {
			while(rs.next()){
				String[] res = new String[3];
				res[0] = rs.getString("userid");
				res[1] = rs.getString("key");
				res[2] = rs.getString("value");
				emaitza.add(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return emaitza;
	}

	public List<Object[]> argazkiakLortu(String username) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL("SELECT md5,izena,deskribapena,pribatutasuna FROM argazki");
		List<Object[]> emaitza = new ArrayList<Object[]>();
		try {
			while(rs.next()){
				Object[] em = new Object[4];
				em[0] = (String) rs.getString("izena");
				em[1] = (String) rs.getString("deskribapena");
				em[2] = (String) rs.getString("pribatutasuna");
				ResultSet rsetik =dbkud.execSQL(String.format("SELECT etiketa FROM Etiketak WHERE md5=%s AND username=%s", rs.getString("md5"), username));
				ArrayList<String> etiketak = new ArrayList<String>();
				while(rsetik.next()){
					etiketak.add(rsetik.getString("etiketa"));
				}
				em[3] = (ArrayList<String>) etiketak;
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

	public void argazkiaSartu(String md5, String izen, String deskr, String prib, String flickrID, String username) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		dbkud.execSQL(String.format("INSERT INTO argazkia (md5,izena,deskribapena,pribatutasuna,flickrID,username) VALUES (%s,%s,%s,%s,%s,%s)", md5,izen,deskr,prib,flickrID,username));
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
