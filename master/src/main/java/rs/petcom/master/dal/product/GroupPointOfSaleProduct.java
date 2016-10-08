package rs.petcom.master.dal.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.dal.PointOfSale;

public class GroupPointOfSaleProduct {

	private static long GroupProductID;
	private int GroupID;
	private long ProductID;
	private long ProductDepartmentID;
	private int Sort;
	private boolean Active;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	private int position;
	private String color;
	private String ColorDodatni;
	private int PointOfSaleID;
	private String GroupName;
	private String DodatniName;
	private int PositionDodatni;
	private boolean ActiveDodatni;
	
	public boolean isActiveDodatni() {
		return this.ActiveDodatni;
	}

	public void setActiveDodatni(boolean activeDodatni) {
		this.ActiveDodatni = activeDodatni;
	}

	public String getColorDodatni() {
		return this.ColorDodatni;
	}

	public void setColorDodatni(String colorDodatni) {
		this.ColorDodatni = colorDodatni;
	}

	public int getPositionDodatni() {
		return this.PositionDodatni;
	}

	public void setPositionDodatni(int positiondodatni) {
		this.PositionDodatni = positiondodatni;
	}

	public String getDodatniName() {
		return this.DodatniName;
	}

	public void setDodatniName(String dodatniName) {
		this.DodatniName = dodatniName;
	}

	public long getGroupProductID() {
		return GroupProductID;
	}

	public void setGroupProductID(long groupProductID) {
		GroupProductID = groupProductID;
	}

	public int getGroupID() {
		return GroupID;
	}

	public void setGroupID(int groupID) {
		GroupID = groupID;
	}

	public long getProductID() {
		return ProductID;
	}

	public void setProductID(long productID) {
		ProductID = productID;
	}

	public long getProductDepartmentID() {
		return ProductDepartmentID;
	}

	public void setProductDepartmentID(long productDepartmentID) {
		ProductDepartmentID = productDepartmentID;
	}

	public int getSort() {
		return Sort;
	}

	public void setSort(int sort) {
		Sort = sort;
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getPointOfSaleID() {
		return PointOfSaleID;
	}

	public void setPointOfSaleID(int pointOfSaleID) {
		PointOfSaleID = pointOfSaleID;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public static ObservableList<GroupPointOfSaleProduct> getAdditionalList(PointOfSale pos, Long p1, Connection connection) {
		ObservableList<GroupPointOfSaleProduct> list = FXCollections.observableArrayList();

		String query = "SELECT DISTINCT gposp.*, p.Name, gpp.position as PositionDodatni, gpp.Active as ActiveDodatni, gpp.color as ColorDodatni" 
				     + " FROM grouppointofsaleproduct gposp"
				     + " JOIN productaditional pa ON pa.ParentProductID = gposp.ProductID"
				     + " JOIN product p ON p.ProductID = pa.ChildProductID"
				     + " JOIN product p1 ON p1.ProductID = gposp.ProductID"
				     + " JOIN grouppointofsaleproduct gpp ON gpp.ProductID = pa.ChildProductID"
				     + " WHERE gposp.PointOfSaleID = ? AND p1.ProductID = ? ";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, pos.getPointOfSaleID());
			ps.setLong(2, p1);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				GroupPointOfSaleProduct gpp = new GroupPointOfSaleProduct();
				GroupPointOfSaleProduct.GroupProductID = rs.getLong("GroupProductID");
				gpp.GroupID = rs.getInt("GroupID");
				gpp.ProductID = rs.getLong("ProductID");
				gpp.ProductDepartmentID = rs.getLong("ProductDepartmentID");
				gpp.Sort = rs.getInt("Sort");
				gpp.Active = rs.getBoolean("Active");
				try {
					gpp.sysDTCreated = new Date(rs.getTimestamp("sysDTCreated").getTime());
				} catch (NullPointerException e1) {
				}
				;
				try {
					gpp.sysDTEdit = new Date(rs.getTimestamp("sysDTEdit").getTime());
				} catch (NullPointerException e1) {
				}
				;
				gpp.sysUserAddID = rs.getLong("sysUserAddID");
				try {
					gpp.sysUserEditID = rs.getLong("sysUserEditID");
				} catch (NullPointerException e1) {
				}
				;
				gpp.position = rs.getInt("position");
				gpp.color = rs.getString("color");
				gpp.PointOfSaleID = rs.getInt("PointOfSaleID");
				gpp.DodatniName = rs.getString("Name");
				gpp.PositionDodatni = rs.getInt("PositionDodatni");
				gpp.ActiveDodatni = rs.getBoolean("ActiveDodatni");
				gpp.ColorDodatni = rs.getString("ColorDodatni");
				list.add(gpp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ObservableList<GroupPointOfSaleProduct> getList(PointOfSale pos, Product product, String filter,
			Connection connection) {
		ObservableList<GroupPointOfSaleProduct> list = FXCollections.observableArrayList();

		String query = "select gpp.*, g.Name as GroupName " + "  from grouppointofsaleproduct gpp"
				+ "  left join `group` g on g.GroupID = gpp.GroupID"
				+ " where gpp.PointOfSaleID = ? and gpp.ProductID = ? " + "   and g.Name like ?";

		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, pos.getPointOfSaleID());
			if (product != null)
				ps.setLong(2, product.getProductID());
			else
				ps.setNull(2, java.sql.Types.BIGINT);
			ps.setString(3, "%" + filter + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				GroupPointOfSaleProduct gpp = new GroupPointOfSaleProduct();
				GroupPointOfSaleProduct.GroupProductID = rs.getLong("GroupProductID");
				gpp.GroupID = rs.getInt("GroupID");
				gpp.ProductID = rs.getLong("ProductID");
				gpp.ProductDepartmentID = rs.getLong("ProductDepartmentID");
				gpp.Sort = rs.getInt("Sort");
				gpp.Active = rs.getBoolean("Active");
				try {
					gpp.sysDTCreated = new Date(rs.getTimestamp("sysDTCreated").getTime());
				} catch (NullPointerException e1) {
				}
				;
				try {
					gpp.sysDTEdit = new Date(rs.getTimestamp("sysDTEdit").getTime());
				} catch (NullPointerException e1) {
				}
				;
				gpp.sysUserAddID = rs.getLong("sysUserAddID");
				try {
					gpp.sysUserEditID = rs.getLong("sysUserEditID");
				} catch (NullPointerException e1) {
				}
				;
				gpp.position = rs.getInt("position");
				gpp.color = rs.getString("color");
				gpp.PointOfSaleID = rs.getInt("PointOfSaleID");
				gpp.GroupName = rs.getString("GroupName");
				list.add(gpp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void insert(Connection connection) {
		String query = "INSERT INTO grouppointofsaleproduct (" + "	`GroupID`," + "	`ProductID`,"
				+ "	 ProductDepartmentID," + "	`Sort`," + "	`Active`," + "	`sysDTCreated`," + "	`sysUserAddID`,"
				+ "	`position`," + "	`color`," + "	`PointOfSaleID`)" + " VALUES (?,?,?,?,?,now(),?,?,?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);

			ps.setInt(1, GroupID);
			ps.setLong(2, ProductID);
			ps.setLong(3, ProductDepartmentID);
			ps.setInt(4, Sort);
			ps.setBoolean(5, Active);
			ps.setLong(6, sysUserAddID);
			ps.setInt(7, position);
			ps.setString(8, color);
			ps.setInt(9, PointOfSaleID);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Connection connection) {
		String query = "UPDATE grouppointofsaleproduct SET " + "	`GroupID` = ?," + "	`ProductID` = ?,"
				+ "	`ProductDepartmentID` = ?," + "	`Sort` = ?," + "	`Active` = ?," + "	`sysDTEdit` = now(),"
				+ "	`sysUserEditID` = ?," + "	`position` = ?," + "	`color` = ?," + "	`PointOfSaleID` = ?"
				+ " WHERE `GroupProductID` = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);

			ps.setInt(1, GroupID);
			ps.setLong(2, ProductID);
			ps.setLong(3, ProductDepartmentID);
			ps.setInt(4, Sort);
			ps.setBoolean(5, Active);
			ps.setLong(6, sysUserEditID);
			ps.setInt(7, position);
			ps.setString(8, color);
			ps.setInt(9, PointOfSaleID);
			ps.setLong(10, GroupProductID);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(Connection connection) {
		String query = "DELETE FROM grouppointofsaleproduct W GroupProductID = ? ";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, GroupProductID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
