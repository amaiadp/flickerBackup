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
		dbkud.execSQL(String.format("INSERT INTO Album VALUES ('%s','%s','%s','%s')", id,tit,deskr,username));
	}

	public boolean DBandago(String id,String username) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL(String.format("SELECT * FROM Album WHERE id='%s' AND username='%s'",id,username));
		try {
			rs.next();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean badago(String md5,String idAlbum, String username) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL(String.format("SELECT * FROM Izan WHERE md5='%s' AND idAlbum='%s' AND username='%s'",md5,idAlbum,username));
		try {
			rs.next();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public void albumeraGehitu(String md5, String id,String username) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		dbkud.execSQL(String.format("INSERT INTO Izan VALUES('%s','%s','%s'",id,md5,username));
	}
}
