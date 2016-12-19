package flickrBackup.kudeatzaileak;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import flickrBackup.model.Album;

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
			rs.getString("id");
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean badago(String md5,String idAlbum, String username) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL(String.format("SELECT * FROM Izan WHERE md5='%s' AND idAlbum='%s' AND username='%s'",md5,idAlbum,username));
		try {
			rs.next();
			System.out.println(rs.getString("md5"));
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public void albumeraGehitu(String md5, String id,String username) {
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		dbkud.execSQL(String.format("INSERT INTO Izan VALUES('%s','%s','%s'",id,md5,username));
	}

	public ArrayList<Album> albumakLortuDB(String username) {
		ArrayList<Album> alalbum = new ArrayList<Album>();
		DBKudeatzaile dbkud = DBKudeatzaile.getInstantzia();
		ResultSet rs = dbkud.execSQL(String.format("SELECT * FROM Album WHERE username='%s'",username));
		try {
			while (rs.next()){
				Album al = new Album(rs.getString("titulua"),rs.getString("deskribapena"),rs.getString("id"));
				alalbum.add(al);
			}
			return alalbum;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
