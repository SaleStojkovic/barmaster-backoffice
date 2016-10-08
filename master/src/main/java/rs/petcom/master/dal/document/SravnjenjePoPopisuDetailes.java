package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.product.Product;

public class SravnjenjePoPopisuDetailes {
	
	long SravnjenjePoPopisuDetailesID;
	long SravnjenjePoPopisuID;
	long ProductID;
	String ProductCode;
	String ProductName;
	double PocetnoStanje;
	double Popisano;
	double Razlika;
	
	public SravnjenjePoPopisuDetailes(){}
	public SravnjenjePoPopisuDetailes(long SravnjenjePoPopisuDetailesID,
								 long SravnjenjePoPopisuID,
								 long ProductID,
								 String ProductCode,
								 String ProductName,
								 double PocetnoStanje,
								 double Popisano,
								 double Razlika){
		this.SravnjenjePoPopisuDetailesID = SravnjenjePoPopisuDetailesID;
		this.SravnjenjePoPopisuID = SravnjenjePoPopisuID;
		this.ProductID = ProductID;
		this.ProductCode = ProductCode;
		this.ProductName = ProductName;
		this.PocetnoStanje = PocetnoStanje;
		this.Popisano = Popisano;
		this.Razlika = Razlika;
	}
	public long getSravnjenjePoPopisuDetailesID() {
		return SravnjenjePoPopisuDetailesID;
	}
	public void setSravnjenjePoPopisuDetailesID(long sravnjenjePoPopisuDetailesID) {
		SravnjenjePoPopisuDetailesID = sravnjenjePoPopisuDetailesID;
	}
	public long getSravnjenjePoPopisuID() {
		return SravnjenjePoPopisuID;
	}
	public void setSravnjenjePoPopisuID(long sravnjenjePoPopisuID) {
		SravnjenjePoPopisuID = sravnjenjePoPopisuID;
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
	public double getPocetnoStanje() {
		return PocetnoStanje;
	}
	public void setPocetnoStanje(double pocetnoStanje) {
		PocetnoStanje = pocetnoStanje;
	}
	public double getPopisano() {
		return Popisano;
	}
	public void setPopisano(double popisano) {
		Popisano = popisano;
	}
	public double getRazlika() {
		return Razlika;
	}
	public void setRazlika(double razlika) {
		Razlika = razlika;
	}
	public static ObservableList<SravnjenjePoPopisuDetailes> getlist(SravnjenjePoPopisu sravnjenjePoPopisu, Connection connection){
		ObservableList<SravnjenjePoPopisuDetailes> list = FXCollections.observableArrayList();
		String query = "select "
					 + "	d.DocumentDetailsID,"
					 + "	d.DocumentID,"
					 + "	d.ProductID,"
					 + "	p.Code,"
					 + "	p.Name,"
					 + "	0 as PocetnoStanje,"
					 + "	0 as Popisano,"
					 + "	d.Quantity as Razlika"
					 + "  from documentdetails d"
					 + "  left join product p on p.ProductID = d.ProductID"
					 + " where DocumentID = ?"
					 + " order by p.Code";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			if (sravnjenjePoPopisu.getDocumentID() > 0){
				ps.setLong(1, sravnjenjePoPopisu.getDocumentID());
				ResultSet rs = ps.executeQuery();
				while (rs.next()){
					list.add(new SravnjenjePoPopisuDetailes(rs.getLong("DocumentDetailsID"), 
													   rs.getLong("DocumentID"),
													   rs.getLong("ProductID"),
													   rs.getString("Code"), 
													   rs.getString("Name"),
													   rs.getDouble("PocetnoStanje"),
													   rs.getDouble("Popisano"),
													   rs.getDouble("Razlika")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void delete(Connection connection){
		DocumentDetails dd = new DocumentDetails();
		dd.setDocumentDetailsID(SravnjenjePoPopisuDetailesID);
		dd.delete(connection);
	}
	public void update(ScreensController controller){
		DocumentDetails dd = DocumentDetails.getByID(SravnjenjePoPopisuDetailesID,controller.getSettings().getConnection());
		dd.setQuantity(Razlika);
		dd.update(controller);
	}
	public long insert(ScreensController controller){
		
		Product p = Product.getByID(ProductID, controller.getSettings().getConnection());
		DocumentDetails dd = new DocumentDetails();
		dd.setDocumentID(SravnjenjePoPopisuID);
		dd.setProductID(ProductID);
		dd.setUnitID(p.getUnitID());
		dd.setVatID(p.getVatID());
		dd.setQuantity(Razlika);
		dd.setSysUserAddID(controller.getUser().getUserID());
		
		long result = dd.insert(controller.getSettings().getConnection());
		SravnjenjePoPopisuDetailesID = result;
		return result;
	}
}
