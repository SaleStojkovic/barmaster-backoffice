package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.product.Product;

public class PocetnoStanjeDetailes {
	
	long PocetnoStanjeDetailesID;
	long PocetnoStanjeID;
	long ProductID;
	String ProductCode;
	String ProductName;
	double Quantity;
	
	public PocetnoStanjeDetailes(){}
	public PocetnoStanjeDetailes(long PocetnoStanjeDetailesID,
								 long PocetnoStanjeID,
								 long ProductID,
								 String ProductCode,
								 String ProductName,
								 double Quantity){
		this.PocetnoStanjeDetailesID = PocetnoStanjeDetailesID;
		this.PocetnoStanjeID = PocetnoStanjeID;
		this.ProductID = ProductID;
		this.ProductCode = ProductCode;
		this.ProductName = ProductName;
		this.Quantity = Quantity;
	}
	
	public long getPocetnoStanjeDetailesID() {
		return PocetnoStanjeDetailesID;
	}
	public void setPocetnoStanjeDetailesID(long pocetnoStanjeDetailesID) {
		PocetnoStanjeDetailesID = pocetnoStanjeDetailesID;
	}
	public long getPocetnoStanjeID() {
		return PocetnoStanjeID;
	}
	public void setPocetnoStanjeID(long pocetnoStanjeID) {
		PocetnoStanjeID = pocetnoStanjeID;
	}
	public long getProductID() {
		return ProductID;
	}
	public void setProductID(long productID) {
		ProductID = productID;
	}
	public String getProductCode() {
		return ProductCode;
	}
	public void setProductCode(String productCode) {
		ProductCode = productCode;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public double getQuantity() {
		return Quantity;
	}
	public void setQuantity(double quantity) {
		Quantity = quantity;
	}
	
	public static ObservableList<PocetnoStanjeDetailes> getlist(PocetnoStanje pocetnoStanje, Connection connection){
		ObservableList<PocetnoStanjeDetailes> list = FXCollections.observableArrayList();
		String query = "select "
					 + "	d.DocumentDetailsID,"
					 + "	d.DocumentID,"
					 + "	d.ProductID,"
					 + "	p.Code,"
					 + "	p.Name,"
					 + "	d.Quantity"
					 + "  from documentdetails d"
					 + "  left join product p on p.ProductID = d.ProductID"
					 + " where DocumentID = ?"
					 + " order by p.Code";
		if (pocetnoStanje.getDocumentID() < 1)
			query = "select "
				  + "	-1 as DocumentDetailsID,"
				  + "	-1 as DocumentID,"
				  + "	p.ProductID,"
				  + "	p.Code,"
				  + "	p.Name,"
				  + "	0 as Quantity"
				  + "  from product p "
				  + " where p.ProductTypeID in (1,2,4,5,6,7)"
				  + " order by p.Code";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			if (pocetnoStanje.getDocumentID() > 0)
				ps.setLong(1, pocetnoStanje.getDocumentID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				list.add(new PocetnoStanjeDetailes(rs.getLong("DocumentDetailsID"), 
												   rs.getLong("DocumentID"),
												   rs.getLong("ProductID"),
												   rs.getString("Code"), 
												   rs.getString("Name"),
												   rs.getDouble("Quantity")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void delete(Connection connection){
		DocumentDetails dd = new DocumentDetails();
		dd.setDocumentDetailsID(PocetnoStanjeDetailesID);
		dd.delete(connection);
	}
	public void update(ScreensController controller){
		DocumentDetails dd = DocumentDetails.getByID(PocetnoStanjeDetailesID,controller.getSettings().getConnection());
		dd.setQuantity(Quantity);
		dd.update(controller);
	}
	public long insert(ScreensController controller){
		
		Product p = Product.getByID(ProductID, controller.getSettings().getConnection());
		DocumentDetails dd = new DocumentDetails();
		dd.setDocumentID(PocetnoStanjeID);
		dd.setProductID(ProductID);
		dd.setUnitID(p.getUnitID());
		dd.setVatID(p.getVatID());
		dd.setQuantity(Quantity);
		dd.setSysUserAddID(controller.getUser().getUserID());
		
		long result = dd.insert(controller.getSettings().getConnection());
		PocetnoStanjeDetailesID = result;
		return result;
	}
}
