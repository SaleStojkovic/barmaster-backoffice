package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Object {
	private int ObjectID;
	private String Name;
	private String Code;
	private String CompanyName;
	private String PIB;
	private String MB;
	private long RegionID;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	private boolean Active;
	private long AddressID;
	
	public Object(){}
	
	public Object(int id, String name){
		ObjectID = id;
		Name = name;
	}
	
	public int getObjectID() {
		return ObjectID;
	}
	public void setObjectID(int objectID) {
		ObjectID = objectID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public long getRegionID() {
		return RegionID;
	}
	public String getCompanyName() {
		return CompanyName;
	}
	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
	public String getPIB() {
		return PIB;
	}
	public void setPIB(String pIB) {
		PIB = pIB;
	}
	public String getMB() {
		return MB;
	}
	public void setMB(String mB) {
		MB = mB;
	}
	public void setRegionID(long regionID) {
		RegionID = regionID;
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
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	
	public long getAddressID() {
		return AddressID;
	}

	public void setAddressID(long addressID) {
		AddressID = addressID;
	}

	@Override
	public String toString() {
		return Name;
	}
	
	public static Object getById(int id, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select * from object where ObjectID = ? ");
	
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Object object = new Object();
			object.ObjectID = rs.getInt("ObjectID");
			object.Name = rs.getString("Name");
			object.Code = rs.getString("Code");
			object.CompanyName = rs.getString("CompanyName");
			object.PIB = rs.getString("PIB");
			object.MB = rs.getString("MB");
			object.RegionID = rs.getLong("RegionID");
			object.sysDTCreated = rs.getDate("sysDTCreated");
			object.sysDTEdit = rs.getDate("sysDTEdit");
			object.sysUserAddID = rs.getLong("sysUserAddID");
			object.sysUserEditID = rs.getLong("sysUserEditID");;
			object.Active = rs.getBoolean("Active");
			object.AddressID = rs.getLong("AddressID");
			
			return object;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static ObservableList<Object> getList(Connection connection){
		ObservableList<Object> list = FXCollections.observableArrayList();
		try {
			PreparedStatement ps = connection.prepareStatement( "select * from object order by name");
	
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Object object = new Object();
				object.ObjectID = rs.getInt("ObjectID");
				object.Name = rs.getString("Name");
				object.Code = rs.getString("Code");
				object.CompanyName = rs.getString("CompanyName");
				object.PIB = rs.getString("PIB");
				object.MB = rs.getString("MB");
				object.RegionID = rs.getLong("RegionID");
				object.sysDTCreated = rs.getDate("sysDTCreated");
				object.sysDTEdit = rs.getDate("sysDTEdit");
				object.sysUserAddID = rs.getLong("sysUserAddID");
				object.sysUserEditID = rs.getLong("sysUserEditID");;
				object.Active = rs.getBoolean("Active");
				object.AddressID = rs.getLong("AddressID");
				list.add(object);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int getPosCount(Connection connection){
		return SQLHelper.QueryToInt("select count(*) from pointofsale where ObjectID = " + ObjectID, connection, 0);
	}
}
