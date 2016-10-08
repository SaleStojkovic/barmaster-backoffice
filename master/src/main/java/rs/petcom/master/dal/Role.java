package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Role {
	private int RoleID;
	private String Code;
	private String Name;
	private boolean Active;
	public int getRoleID() {
		return RoleID;
	}
	public void setRoleID(int roleID) {
		RoleID = roleID;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	
	public static ArrayList<Role> getList(long userId, Connection connection ){
		ArrayList<Role> list = new ArrayList<>();
		
		try {
			PreparedStatement ps = connection.prepareStatement( "select * "
															  + "  from userrole ur "
															  + "  left join role r on ur.RoleID = r.RoleID "
															  + " where (ur.UserID =  ?) and (ur.Active = true) and (r.Active = true)");
			ps.setLong(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Role role = new Role();
				role.RoleID = rs.getInt("RoleID");
				role.Code = rs.getString("Code");
				role.Name = rs.getString("Name");
				role.Active = rs.getBoolean("Active");
				list.add(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
