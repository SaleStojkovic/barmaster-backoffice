package rs.petcom.master.dal.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.dal.PointOfSale;

public class Description {

	private int DescriptionID;
	private String Name;
	boolean Active;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	private String color;
	private int PositionBtn;

	public int getPositionBtn() {
		return this.PositionBtn;
	}

	public void setPositionBtn(int positionBtn) {
		this.PositionBtn = positionBtn;
	}

	public int getDescriptionID() {
		return DescriptionID;
	}

	public void setDescriptionID(int descriptionID) {
		DescriptionID = descriptionID;
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Description() {
	}

	public Description(String name, Connection connection) {
		try {
			PreparedStatement ps = connection.prepareStatement("select * from description where Name = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DescriptionID = rs.getInt("DescriptionID");
				Name = rs.getString("Name");
				Active = rs.getBoolean("Active");
				sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try {
					sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e) {
				}
				sysUserAddID = rs.getLong("sysUserAddID");
				try {
					sysUserEditID = rs.getLong("sysUserEditID");
				} catch (NullPointerException e) {
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean equals(Description description) {
		return DescriptionID == description.getDescriptionID();
	}

	public Description(int productId, long pointOfSaleId, Connection connection) {
		try {
			String query = "select d.Name " + "from description d"
					+ "join pointofsaleproductdescription pospd on pospd.DescriptionID = d.DescriptionID"
					+ "join pointofsale pos on pos.PointOfSaleID = pospd.PointOfSaleID"
					+ "join product p on p.ProductID = pospd.ProductID"
					+ "where p.ProductID = ? and pos.PointOfSaleID = ?" + "and d.Name like ?";

			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, productId);
			ps.setLong(2, pointOfSaleId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Name = rs.getString("Name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ObservableList<Description> getObeservableList(String filter, Connection connection) {

		ObservableList<Description> list = FXCollections.observableArrayList();

		String query = "select * from description d" + " where d.Name like ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, "%" + filter + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Description description = new Description();
				description.Active = rs.getBoolean("Active");
				description.DescriptionID = rs.getInt("DescriptionId");
				description.Name = rs.getString("Name");
				description.sysDTCreated = rs.getDate("sysDTCreated");
				description.sysDTEdit = rs.getDate("sysDTEdit");
				description.sysUserAddID = rs.getLong("sysUserAddID");
				description.sysUserEditID = rs.getLong("sysUserEditID");
				list.add(description);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ObservableList<Description> getListLoadTableOpisni(PointOfSale pos, 
													  Product product, 
													  Connection connection) {
		
		ObservableList<Description> list = FXCollections.observableArrayList();

		String query = "select d.Name" 
						+ " from description d"
						+ " join pointofsaleproductdescription pospd on pospd.DescriptionID = d.DescriptionID" 
						+ " join pointofsale pos on pos.PointOfSaleID = pospd.PointOfSaleID" 
						+ " join product p on p.ProductID = pospd.ProductID" 
						+ " where pos.PointOfSaleID = ? and p.ProductID = ?";

		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, pos.getPointOfSaleID());
			if (product != null)
				ps.setLong(2, product.getProductID());
			else
				ps.setNull(2, java.sql.Types.BIGINT);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Description des = new Description();
				des.Name = rs.getString("Name");
				list.add(des);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ObservableList<Description> getListName(int DescriptionID, Connection connection) {
		ObservableList<Description> list = FXCollections.observableArrayList();
		String query = "select *" 
					 + " from description d"
					 + " where d.DescriptionID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, DescriptionID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Description des = new Description();
				list.add(des);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
}
