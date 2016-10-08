package rs.petcom.master.dal.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class GroupPointOfSale {
	
	private int GroupPointOfSaleID;
	private int GroupID;
	private int PointOfSaleID;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	private int Sort;
	private int ParentGroupID;
	public int getGroupPointOfSaleID() {
		return GroupPointOfSaleID;
	}
	public void setGroupPointOfSaleID(int groupPointOfSaleID) {
		GroupPointOfSaleID = groupPointOfSaleID;
	}
	public int getGroupID() {
		return GroupID;
	}
	public void setGroupID(int groupID) {
		GroupID = groupID;
	}
	public int getPointOfSaleID() {
		return PointOfSaleID;
	}
	public void setPointOfSaleID(int pointOfSaleID) {
		PointOfSaleID = pointOfSaleID;
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
	public int getSort() {
		return Sort;
	}
	public void setSort(int sort) {
		Sort = sort;
	}
	public int getParentGroupID() {
		return ParentGroupID;
	}
	public void setParentGroupID(int parentGroupID) {
		ParentGroupID = parentGroupID;
	}
	
	public static GroupPointOfSale getFromDb(int pos, int group, Connection connection) throws SQLException{
		String query = "select * from grouppointofsale where PointOfSaleID = ? and GroupID = ?";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setInt(1, pos);
		ps.setInt(2,group);
		ResultSet rs = ps.executeQuery();
		rs.next();
		GroupPointOfSale gps = new GroupPointOfSale();
		gps.setGroupPointOfSaleID(rs.getInt("GroupPointOfSaleID"));
		gps.setGroupID(rs.getInt("GroupID"));
		gps.setPointOfSaleID(rs.getInt("PointOfSaleID"));
		gps.setSysDTCreated(rs.getTimestamp("sysDTCreated"));
		try{
			gps.setSysDTEdit(rs.getTimestamp("sysDTEdit"));
		} catch (NullPointerException e){}
		gps.setSysUserAddID(rs.getLong("sysUserAddID"));
		gps.setSysUserEditID(rs.getLong("sysUserEditID"));
		gps.setSort(rs.getInt("Sort"));
		gps.setParentGroupID(rs.getInt("ParentGroupID"));
		
		return gps;
	}
}
