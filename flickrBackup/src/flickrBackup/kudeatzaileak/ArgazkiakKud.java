package flickrBackup.kudeatzaileak;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
