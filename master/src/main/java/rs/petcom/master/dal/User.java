package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class User {
	private long UserID;
	private String Code;
	private String CardCode;
	private String UserName;
	private String Password;
	private boolean Active;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	private Date SanitaryDateExpire;
	
	private ArrayList<Role> userRole = new ArrayList<Role>();
	
	public long getUserID() {
		return UserID;
	}
	public void setUserID(long userID) {
		UserID = userID;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getCardCode() {
		return CardCode;
	}
	public void setCardCode(String cardCode) {
		CardCode = cardCode;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	public Date getSysDTCreated() {
		return sysDTCreated;
	}
	public void setSysDTCreated(Date sysDTCreated) {
		this.sysDTCreated = sysDTCreated;
	}
	public Date getSysDTEdit() {
		return sysDTEdit;
	}
	public void setSysDTEdit(Date sysDTEdit) {
		this.sysDTEdit = sysDTEdit;
	}
	public long getSysUserAddID() {
		return sysUserAddID;
	}
	public void setSysUserAddID(long sysUserAddID) {
		this.sysUserAddID = sysUserAddID;
	}
	public long getSysUserEditID() {
		return sysUserEditID;
	}
	public void setSysUserEditID(long sysUserEditID) {
		this.sysUserEditID = sysUserEditID;
	}
	public Date getSanitaryDateExpire() {
		return SanitaryDateExpire;
	}
	public void setSanitaryDateExpire(Date sanitaryDateExpire) {
		SanitaryDateExpire = sanitaryDateExpire;
	}

	public ArrayList<Role> getUserRole() {
		return userRole;
	}
	public void setUserRole(ArrayList<Role> userRole) {
		this.userRole = userRole;
	}
	public static User getFromDb(String userCode, String posCode, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select u.* "
															  + " from user u"
															  + " left join userpointofsale up on up.UserID = u.UserID"
															  + " left join pointofsale p on up.PointOfSaleID = p.PointOfSaleID"
															  + " where p.Code = ?"
															  + "	and ((u.Password = ?) or  "
															  + "        (? in (select i.code "
															  + " 						 from idcard i "
															  + "						where i.UserID = u.UserID)))");
			ps.setString(1, posCode);
			ps.setString(2, userCode);
			ps.setString(3, userCode);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			User user = new User();
			user.UserID = rs.getLong("UserID");
			user.Code = rs.getString("Code");
			user.CardCode = rs.getString("CardCode");
			user.UserName = rs.getString("UserName");
			user.Password = rs.getString("Password");
			user.Active = rs.getBoolean("Active");
			user.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
			try{
				user.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
			}catch (NullPointerException e){}	
			user.sysUserAddID = rs.getLong("sysUserAddID");
			user.sysUserEditID = rs.getLong("sysUserEditID");
			try{
				user.SanitaryDateExpire = new Date(rs.getDate("SanitaryDateExpire").getTime());
			}catch (NullPointerException e){}

			user.userRole = Role.getList(user.UserID, connection);
			return user;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static User getFromDb(String userCode, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select u.* "
															  + "  from user u"
															  + " where u.Password = ?"
															  + "    or (? in (select i.code"
															  + "			     from idcard i"
															  + "				where i.UserID = u.UserID))");
			ps.setString(1, userCode);
			ps.setString(2, userCode);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			User user = new User();
			user.UserID = rs.getLong("UserID");
			user.Code = rs.getString("Code");
			user.CardCode = rs.getString("CardCode");
			user.UserName = rs.getString("UserName");
			user.Password = rs.getString("Password");
			user.Active = rs.getBoolean("Active");
			user.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
			try{
				user.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
			}catch (NullPointerException e){}	
			user.sysUserAddID = rs.getLong("sysUserAddID");
			user.sysUserEditID = rs.getLong("sysUserEditID");
			try{
				user.SanitaryDateExpire = new Date(rs.getDate("SanitaryDateExpire").getTime());
			}catch (NullPointerException e){}

			user.userRole = Role.getList(user.UserID, connection);
			return user;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public boolean hasRole(String code){
		boolean result = false;
		for (int i=0;i<userRole.size();i++){
			if (userRole.get(i).getCode().equals(code)){
				result = true;
				break;
			}
		}
		
		return result;
	}
}
