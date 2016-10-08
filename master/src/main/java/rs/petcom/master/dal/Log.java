package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Log {
	
	public static final int ERROR = 0;
	public static final int WARNING = 1;
	public static final int MESSAGE = 2;
	
	public static void logIn(long userId, int pos, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement("insert into loginlog(UserID,LoginDate,PointOfSaleID) values(?,now(),?)");
			ps.setLong(1, userId);
			ps.setInt(2, pos);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void logOut(long userId, int pos, Connection connection){
		try {
			String upit = "select max(LoginLogID) from loginlog where UserId = ? and PointOfSaleID = ?";
			PreparedStatement ps = connection.prepareStatement(upit);
			ps.setLong(1, userId);
			ps.setInt(2, pos);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			long id = rs.getLong(1);
			
			ps = connection.prepareStatement( "update loginlog l set LoutDate = now() "
										    + " where LoginLogID = ?");
			ps.setLong(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void storno(long userId, long authId, int pos, Round round, Settings settings, Connection connection){
		
		String text = "Round storno \n";
		
		for (RoundItem ri : round.getItemList()){
			text += ri.getProductDepartment().getProduct().getName()  + " x " + ri.getQuantity() + "\n";
			for(RoundItem ra : ri.getAdditionalList()){
				text += " - " + ra.getProductDepartment().getProduct().getName() + " x " + ra.getQuantity() + "\n";
			}
			for(RoundItemDescription d : ri.getDescriptionalList()){
				text +=" ->"  + d.getDescription().getName() + " x " + d.getQuantity() + "\n";
			}
		}
		
		try {
			PreparedStatement ps = connection.prepareStatement("insert into stornolog(UserID,AuthUserID,StornoDate,PointOfSaleID,Text) values(?,?,now(),?,?)");
			ps.setLong(1, userId);
			ps.setLong(2, authId);
			ps.setInt(3, pos);
			ps.setString(4, text);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static void storno(long userId, long authId, int pos, RoundItem item, 
							  Settings settings, Connection connection){
		try {
			
			String text = "RoundItem storno \n";
			text += item.getProductDepartment().getProduct().getName()  + " x " + item.getQuantity() + "\n";
			for(RoundItem ra : item.getAdditionalList()){
				text += " - " + ra.getProductDepartment().getProduct().getName() + " x " + ra.getQuantity() + "\n";
			}
			for(RoundItemDescription d : item.getDescriptionalList()){
				text +=" ->"  + d.getDescription().getName() + " x " + d.getQuantity() + "\n";
			}
			PreparedStatement ps = connection.prepareStatement("insert into stornolog(UserID,AuthUserID,StornoDate,PointOfSaleID,Text) values(?,?,now(),?,?)");
			ps.setLong(1, userId);
			ps.setLong(2, authId);
			ps.setInt(3, pos);
			ps.setString(4, text);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeLog(User user, String description, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement("insert into log(UserID,Description) values(?,?)");
			ps.setLong(1, user.getUserID());
			ps.setString(2, description);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
