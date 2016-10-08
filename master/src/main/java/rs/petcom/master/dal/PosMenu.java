package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PosMenu {
	int PosMenuID;
	String Code;
	String Title;
	boolean IsCommand;
	boolean isMenu;
	public int getPosMenuID() {
		return PosMenuID;
	}
	public void setPosMenuID(int posMenuID) {
		PosMenuID = posMenuID;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public boolean isIsCommand() {
		return IsCommand;
	}
	public void setIsCommand(boolean isCommand) {
		IsCommand = isCommand;
	}
	public boolean isMenu() {
		return isMenu;
	}
	public void setMenu(boolean isMenu) {
		this.isMenu = isMenu;
	}
	
	public static ArrayList<PosMenu> getList(Long userId, Connection connection){
		ArrayList<PosMenu> list = new ArrayList<>();
		try {
			PreparedStatement ps = connection.prepareStatement( "select m.* "
															  + "  from posmenuuser mu "
															  + " left join posmenu m on m.PosMenuID = mu.PosMenuID "
															  + " where m.ParentPosMenuID is null"
															  + "   and mu.UserID = ? "
															  + " order by sort");
			ps.setLong(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				PosMenu pm = new PosMenu();
				pm.PosMenuID = rs.getInt("PosMenuID");
				pm.Code = rs.getString("Code");
				pm.Title = rs.getString("Title");
				pm.IsCommand = rs.getBoolean("IsCommand");
				pm.isMenu = rs.getBoolean("isMenu");
				
				list.add(pm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static ArrayList<PosMenu> getList(int PosMenuID, Long userId, Connection connection){
		ArrayList<PosMenu> list = new ArrayList<>();
		try {
			PreparedStatement ps = connection.prepareStatement( "select distinct m.* "
															  + "  from posmenuuser mu "
															  + " left join posmenu m on m.PosMenuID = mu.PosMenuID "
															  + " where m.ParentPosMenuID = ? "
															  + "   and  mu.UserID = ? "
															  + " order by sort");
			ps.setInt(1, PosMenuID);
			ps.setLong(2, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				PosMenu pm = new PosMenu();
				pm.PosMenuID = rs.getInt("PosMenuID");
				pm.Code = rs.getString("Code");
				pm.Title = rs.getString("Title");
				pm.IsCommand = rs.getBoolean("IsCommand");
				pm.isMenu = rs.getBoolean("isMenu");
				
				list.add(pm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
