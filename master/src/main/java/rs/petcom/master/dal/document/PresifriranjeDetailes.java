package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.product.Product;

public class PresifriranjeDetailes {
	
	long PresifriranjeDetailesID;
	long PresifriranjeID;
	long ProductID;
	String ProductCode;
	String ProductName;
	double Quantity;
	
	public PresifriranjeDetailes(){}
	public PresifriranjeDetailes(long PresifriranjeDetailesID,
								 long PresifriranjeID,
								 long ProductID,
								 String ProductCode,
								 String ProductName,
								 double Quantity){
		this.PresifriranjeDetailesID = PresifriranjeDetailesID;
		this.PresifriranjeID = PresifriranjeID;
		this.ProductID = ProductID;
		this.ProductCode = ProductCode;
		this.ProductName = ProductName;
		this.Quantity = Quantity;
	}
	public long getPresifriranjeDetailesID() {
		return PresifriranjeDetailesID;
	}
	public void setSravnjenjePoPopisuDetailesID(long sravnjenjePoPopisuDetailesID) {
		PresifriranjeDetailesID = sravnjenjePoPopisuDetailesID;
	}
	public long getPresifriranjeID() {
		return PresifriranjeID;
	}
	public void setPresifriranjeID(long PresifriranjeID) {
		this.PresifriranjeID = PresifriranjeID;
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
	public void setQuantity(double Quantity) {
		this.Quantity = Quantity;
	}
	
	public static ObservableList<PresifriranjeDetailes> getList(Presifriranje presifriranje,
																Connection connection){
		ObservableList<PresifriranjeDetailes> list = FXCollections.observableArrayList();
		String query = "select "
					 + "	d.DocumentDetailsID,"
					 + "	d.DocumentID,"
					 + "	d.ProductID,"
					 + "	p.Code,"
					 + "	p.Name,"
					 + "	Quantity"
					 + "  from documentdetails d"
					 + "  left join product p on p.ProductID = d.ProductID"
					 + " where DocumentID = ?"
					 + " order by p.Code";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			if (presifriranje.getDocumentID() > 0){
				ps.setLong(1, presifriranje.getDocumentID());
				ResultSet rs = ps.executeQuery();
				while (rs.next()){
					list.add(new PresifriranjeDetailes(rs.getLong("DocumentDetailsID"), 
													   rs.getLong("DocumentID"),
													   rs.getLong("ProductID"),
													   rs.getString("Code"), 
													   rs.getString("Name"),
													   rs.getDouble("Quantity")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void delete(Connection connection){
		DocumentDetails dd = new DocumentDetails();
		dd.setDocumentDetailsID(PresifriranjeDetailesID);
		dd.delete(connection);
	}
	public void update(ScreensController controller){
		DocumentDetails dd = DocumentDetails.getByID(PresifriranjeDetailesID,controller.getSettings().getConnection());
		dd.setQuantity(Quantity);
		dd.update(controller);
	}
	public long insert(ScreensController controller){
		
		Product p = Product.getByID(ProductID, controller.getSettings().getConnection());
		DocumentDetails dd = new DocumentDetails();
		dd.setDocumentID(PresifriranjeID);
		dd.setProductID(ProductID);
		dd.setUnitID(p.getUnitID());
		dd.setVatID(p.getVatID());
		dd.setQuantity(Quantity);
		dd.setSysUserAddID(controller.getUser().getUserID());
		
		long result = dd.insert(controller.getSettings().getConnection());
		PresifriranjeDetailesID = result;
		return result;
	}
}
