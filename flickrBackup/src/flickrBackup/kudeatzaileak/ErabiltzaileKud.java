package flickrBackup.kudeatzaileak;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ErabiltzaileKud {

private static final ErabiltzaileKud erabiltzaileKud = new ErabiltzaileKud();
	
	public static ErabiltzaileKud getInstantzia(){
		return erabiltzaileKud;
	}

	private ErabiltzaileKud() {
		// Singleton patroia
	}

	
	public List<String[]> getErabiltzailea(String username){
		
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL("select * from erabiltzaileak where username='" + username + "'");
		List<String[]> emaitza = new ArrayList<String[]>();
		try {
			while(rs.next()){
				String[] res = new String[2];
				res[0] = rs.getString("key");
				res[1] = rs.getString("value");
				emaitza.add(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return emaitza;
	}

	public void sortuErabiltzailea(String apiKey, String secret, String token, String tokensecret, String nsid,
			String displayname, String username) {
		DBKudeatzaile dbk = DBKudeatzaile.getInstantzia();
		dbk.execSQL(String.format("Insert into erabiltzaileak (username,key,value) values ('%s','%s','%s')",username,"apikey", apiKey));
		dbk.execSQL(String.format("Insert into erabiltzaileak (username,key,value) values ('%s','%s','%s')",username,"secret", secret));
		dbk.execSQL(String.format("Insert into erabiltzaileak (username,key,value) values ('%s','%s','%s')",username,"token", token));
		dbk.execSQL(String.format("Insert into erabiltzaileak (username,key,value) values ('%s','%s','%s')",username,"tokensecret", tokensecret));
		dbk.execSQL(String.format("Insert into erabiltzaileak (username,key,value) values ('%s','%s','%s')",username,"nsid", nsid));
		dbk.execSQL(String.format("Insert into erabiltzaileak (username,key,value) values ('%s','%s','%s')",username,"displayname", displayname));
		dbk.execSQL(String.format("Insert into erabiltzaileak (username,key,value) values ('%s','%s','%s')",username,"username", username));
		
	}

	public void sortuErabiltzailea(List<String[]> lista, String username) {
		DBKudeatzaile dbk = DBKudeatzaile.getInstantzia();

		for (String[] strings : lista) {
			dbk.execSQL(String.format("Insert into erabiltzaileak (username,key,value) values ('%s','%s','%s')",username, strings[0],strings[1]));

		}
		
	}

	public String getPasahitza(String username) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL(String.format("select * from erabiltzaileak where username='%s' and key='password'", username));
		String emaitza = null;
		try {
			rs.next();
				emaitza = rs.getString("value");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return emaitza;
	}

	public void pasahitzaGehitu(String username, String passw) {
		DBKudeatzaile dbk = DBKudeatzaile.getInstantzia();
		try {
			dbk.execSQL(String.format("Insert into erabiltzaileak (username,key,value) values ('%s','%s','%s')",username,"password", passw));

		} catch (Exception e) {
			// TODO: handle exception
		}
				
	}
	
	
}
