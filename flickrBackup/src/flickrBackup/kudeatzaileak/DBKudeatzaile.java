package flickrBackup.kudeatzaileak;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Statement;



public class DBKudeatzaile {

	Connection conn = null;

	private void conOpen() {
		try {
//			String userName = "teletxe";
//			String password = "softing2010";
			String url = "jdbc:sqlite:isadflickr.db";
		//	Class.forName("com.mysql.jdbc.Driver").newInstance();
			Class.forName("org.sqlite.JDBC").newInstance();
			
			conn = (Connection) DriverManager.getConnection(url);
			//, userName,	password);
			System.out.println("Database connection established");
		} catch (Exception e) {
			System.err.println("Cannot connect to database server");
		}
	}



	private void conClose() {

		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		System.out.println("Database connection terminated");

	}

	private ResultSet query(Statement s, String query) {

		ResultSet rs = null;

		try {
			rs = s.executeQuery(query);
			// rs = s.getResultSet();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}

	// singleton patroia
	private static DBKudeatzaile instantzia = new DBKudeatzaile();

	private DBKudeatzaile() {
		this.conOpen();

	}

	public static DBKudeatzaile getInstantzia() {
		return instantzia;
	}

	//
	public ResultSet execSQL(String query) {
		int count = 0;
		Statement s = null;
		ResultSet rs = null;
		
		try {
			s = (Statement) conn.createStatement();
			if (query.toLowerCase().indexOf("select") == 0) {
				// select agindu bat
				rs = this.query(s, query);
				
			} else {
				// update, delete, create agindu bat
				count = s.executeUpdate(query);
				System.out.println(count + " rows affected");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		finally {
//			if (s != null)
//				try {
//					s.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//		}

		return rs;
	}
}
