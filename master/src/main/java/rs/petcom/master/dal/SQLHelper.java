package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class SQLHelper {
	
	public static int QueryToInt(String query, Connection connection, int defaultValue){
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs =  ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public static String QueryToString(String query, Connection connection, String defaultValue){
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs =  ps.executeQuery();
			rs.next();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}
	
	public static Date QueryToDate(String query, Connection connection, Date defaultValue){
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs =  ps.executeQuery();
			rs.next();
			return rs.getTimestamp(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}
}
