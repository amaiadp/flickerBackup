package flickrBackup.kudeatzaileak;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlbumakKud {

	private static final AlbumakKud albumakKud = new AlbumakKud();
	
	public static AlbumakKud getInstantzia(){
		return albumakKud;
	}

	private AlbumakKud() {
		// Singleton patroia
	}
	
	public void albumaSartu(String id, String tit, String deskr, String username){
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		dbkud.execSQL(String.format("INSERT INTO Album VALUES (%s,%s,%s,%s)", id,tit,deskr,username));
	}

	public boolean DBandago(String id) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL("Select * From Album");
		try {
			rs.next();
			return (rs.getString("id")==null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
}
