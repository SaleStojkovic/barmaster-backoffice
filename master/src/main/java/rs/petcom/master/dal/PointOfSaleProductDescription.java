package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.dal.product.Product;

public class PointOfSaleProductDescription {
	private int PointOfSaleProductDescriptionID;
	private int PointOfSaleID;
	private long ProductID;
	private int DescriptionID;
	private boolean Active;
	private String color;
	private String Name;
	private int PositionBtn;

	public int getPositionBtn() {
		return this.PositionBtn;
	}

	public void setPositionBtn(int positionBtn) {
		this.PositionBtn = positionBtn;
	}

	public String getName() {
		return this.Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public int getPointOfSaleProductDescriptionID() {
		return this.PointOfSaleProductDescriptionID;
	}

	public void setPointOfSaleProductDescriptionID(int pointOfSaleProductDescriptionID) {
		this.PointOfSaleProductDescriptionID = pointOfSaleProductDescriptionID;
	}

	public int getPointOfSaleID() {
		return this.PointOfSaleID;
	}

	public void setPointOfSaleID(int pointOfSaleID) {
		this.PointOfSaleID = pointOfSaleID;
	}

	public long getProductID() {
		return this.ProductID;
	}

	public void setProductID(long productID) {
		this.ProductID = productID;
	}

	public int getDescriptionID() {
		return this.DescriptionID;
	}

	public void setDescriptionID(int descriptionID) {
		this.DescriptionID = descriptionID;
	}

	public boolean isActive() {
		return this.Active;
	}

	public void setActive(boolean active) {
		this.Active = active;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	//Ubacivanje novog DESCRIPTION-a za odredjeni PRODUCT
	public void insert(Connection connection) {

		String query = "INSERT INTO pointofsaleproductdescription (" 
					 + "    PointOfSaleID," 
					 + "    ProductID,"
					 + "	DescriptionID," 
					 + "	Active," 
					 + "	color)" 
					 + " VALUES (?,?,?,?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, PointOfSaleID);
			ps.setLong(2, ProductID);
			ps.setLong(3, DescriptionID);
			ps.setBoolean(4, Active);
			ps.setString(5, color);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Apdejtovanje novog DESCRIPTION-a za odredjeni PRODUCT
	public void update(Connection connection) {
		String query = "UPDATE pointofsaleproductdescription SET " 
					 + " PointOfSaleID = ?,"
					 + " ProductID = ?," 
					 + " DescriptionID = ?," 
					 + " Active = ?," 
					 + " color = ?,"
					 + " PositionBtn = ?"
					 + " WHERE PointOfSaleProductDescriptionID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);

			ps.setInt(1, PointOfSaleID);
			ps.setLong(2, ProductID);
			ps.setLong(3, DescriptionID);
			ps.setBoolean(4, Active);
			ps.setString(5, color);
			ps.setInt(6, PositionBtn);
			ps.setInt(7, PointOfSaleProductDescriptionID);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Brisanje novog DESCRIPTION-a za odredjeni PRODUCT
	public void delete(Connection connection) {
		String query = "DELETE FROM pointofsaleproductdescription WHERE PointOfSaleProductDescriptionID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, PointOfSaleProductDescriptionID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ObservableList<PointOfSaleProductDescription> getListLoadTableOpisni(PointOfSale pos, Product product,
			Connection connection) {
		ObservableList<PointOfSaleProductDescription> list = FXCollections.observableArrayList();

		
		String query = "SELECT DISTINCT psd.*, d.Name, psd.PositionBtn" 
					 + " FROM pointofsaleproductdescription psd" 
					 + " JOIN grouppointofsaleproduct gposp on gposp.ProductID = psd.ProductID"
					 + " JOIN description d on d.DescriptionID = psd.DescriptionID"
					 + " JOIN product p on p.ProductID = gposp.ProductID" 
					 + " JOIN `group` g on g.GroupID = gposp.GroupID" 
					 + " WHERE psd.PointOfSaleID = ? and p.ProductID = ?";

		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, pos.getPointOfSaleID());
			if (product != null)
				ps.setLong(2, product.getProductID());
			else
				ps.setNull(2, java.sql.Types.BIGINT);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PointOfSaleProductDescription pospd = new PointOfSaleProductDescription();
				pospd.PointOfSaleProductDescriptionID = rs.getInt("PointOfSaleProductDescriptionID");
				pospd.PointOfSaleID = rs.getInt("PointOfSaleID");
				pospd.ProductID = rs.getLong("ProductID");
				pospd.DescriptionID = rs.getInt("DescriptionID");
				pospd.Active = rs.getBoolean("Active");
				pospd.color = rs.getString("color");
				pospd.Name = rs.getString("Name");
				pospd.PositionBtn = rs.getInt("PositionBtn");
				list.add(pospd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ObservableList<PointOfSaleProductDescription> getListOpisni(int pos, long product, Connection connection) {
		ObservableList<PointOfSaleProductDescription> list = FXCollections.observableArrayList();

		
		String query = "select psd.*, d.Name"
					 + " from pointofsaleproductdescription psd"
					 + " left join description d on psd.DescriptionID = d.DescriptionID"
					 + " where d.Active and psd.PointOfSaleID = ? and psd.ProductID = ?";

		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, pos);
			ps.setLong(2, product);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PointOfSaleProductDescription pospd = new PointOfSaleProductDescription();
				pospd.PointOfSaleProductDescriptionID = rs.getInt("PointOfSaleProductDescriptionID");
				pospd.PointOfSaleID = rs.getInt("PointOfSaleID");
				pospd.ProductID = rs.getLong("ProductID");
				pospd.DescriptionID = rs.getInt("DescriptionID");
				pospd.Active = rs.getBoolean("Active");
				pospd.color = rs.getString("color");
				pospd.Name = rs.getString("Name");
				pospd.PositionBtn = rs.getInt("PositionBtn");
				list.add(pospd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
