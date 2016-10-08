package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.product.Product;

public class PrijemnicaDetailes {
	private long DocumentDetailsID;
	private long DocumentID;
	private long ProductID;
	private String ProductCode;
	private String ProductName;
	private double Quantity;
	private double Price;
	private double Discount;
	private double PriceWithDiscount;
	
	public PrijemnicaDetailes(){}
	
	public long getDocumentDetailsID() {
		return DocumentDetailsID;
	}
	public void setDocumentDetailsID(long documentDetailsID) {
		DocumentDetailsID = documentDetailsID;
	}
	public long getDocumentID() {
		return DocumentID;
	}
	public void setDocumentID(long documentID) {
		DocumentID = documentID;
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
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public double getDiscount() {
		return Discount;
	}
	public void setDiscount(double discount) {
		Discount = discount;
	}
	public double getPriceWithDiscount() {
		return PriceWithDiscount;
	}
	public void setPriceWithDiscount(double priceWithDiscount) {
		PriceWithDiscount = priceWithDiscount;
	}
	
	public static ObservableList<PrijemnicaDetailes> getlist(Prijemnica prijemnica, Connection connection){
		ObservableList<PrijemnicaDetailes> list = FXCollections.observableArrayList();
		String query = "select "
					 + "	d.DocumentDetailsID,"
					 + "	d.DocumentID,"
					 + "	d.ProductID,"
					 + "	p.Code,"
					 + "	p.Name,"
					 + "	d.Quantity,"
					 + "    d.Price,"
					 + "    d.Discount,"
					 + "    d.PriceWithDiscount"
					 + "  from documentdetails d"
					 + "  left join product p on p.ProductID = d.ProductID"
					 + " where DocumentID = ?"
					 + " order by p.Code";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1,  prijemnica.getDocumentID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				PrijemnicaDetailes pd = new PrijemnicaDetailes();
				pd.DocumentDetailsID = rs.getLong("DocumentDetailsID");
				pd.DocumentID = rs.getLong("DocumentID");
				pd.ProductID = rs.getLong("ProductID");
				pd.ProductCode = rs.getString("Code");
				pd.ProductName = rs.getString("Name");
				pd.Quantity = rs.getDouble("Quantity");
				pd.Price = rs.getDouble("Price");
				pd.Discount = rs.getDouble("Discount");
				pd.PriceWithDiscount = rs.getDouble("PriceWithDiscount");
				
				list.add(pd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void delete(Connection connection){
		DocumentDetails dd = new DocumentDetails();
		dd.setDocumentDetailsID(this.DocumentDetailsID);
		dd.delete(connection);
	}
	public void update(ScreensController controller){
		DocumentDetails dd = DocumentDetails.getByID(this.DocumentDetailsID,controller.getSettings().getConnection());
		dd.setPrice(Price);
		dd.setQuantity(Quantity);
		dd.setPriceWithDiscount(PriceWithDiscount);
		dd.update(controller);
	}
	public long insert(ScreensController controller){
		
		Product p = Product.getByID(ProductID, controller.getSettings().getConnection());
		DocumentDetails dd = new DocumentDetails();
		dd.setDocumentID(DocumentID);
		dd.setProductID(ProductID);
		dd.setUnitID(p.getUnitID());
		dd.setVatID(p.getVatID());
		dd.setPrice(Price);
		dd.setQuantity(Quantity);
		dd.setPriceWithDiscount(PriceWithDiscount);
		dd.setSysUserAddID(controller.getUser().getUserID());
		
		long result = dd.insert(controller.getSettings().getConnection());
		DocumentDetailsID = result;
		return result;
	}
}
