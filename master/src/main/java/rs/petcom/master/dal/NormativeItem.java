package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.dal.product.Product;

public class NormativeItem {
	
	private long ProductComponentID;
	private long ArticleProductDepartmenID;
	private Product product;
	private double normative;
	private int order;
	public long getProductComponentID() {
		return ProductComponentID;
	}
	public void setProductComponentID(long productComponentID) {
		ProductComponentID = productComponentID;
	}
	public long getArticleProductDepartmenID() {
		return ArticleProductDepartmenID;
	}
	public void setArticleProductDepartmenID(long articleProductDepartmenID) {
		ArticleProductDepartmenID = articleProductDepartmenID;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public double getNormative() {
		return normative;
	}
	public void setNormative(double normative) {
		this.normative = normative;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getCode(){
		return product.getCode();
	}
	public String getName(){
		return product.getName();
	}
	public String getShortName(){
		return product.getShortName();
	}
	
	public void update (Connection connection){
		String query = "update productcomponent set "
					 + " Normative = ?, `Order` = ? "
					 + " where ProductComponentID = ?";
		try{
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setDouble(1, normative);
			ps.setInt(2, order);
			ps.setLong(3, ProductComponentID);
			ps.executeUpdate();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void insert (Connection connection){
		String query = "insert into productcomponent "
					 + "	(`ArticleProductDepartmenID`,`IngredientProductDepartmentID`,"
					 + " 	 `Normative`,`Order`) values( ? , ? , ? , ?)";
		try{
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, ArticleProductDepartmenID);
			ps.setLong(2, product.getProductDepartmentID());
			ps.setDouble(3, normative);
			ps.setInt(4, order);
			ps.executeUpdate();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	public void delete (Connection connection){
		String query = "delete from productcomponent "
					 + " where	ProductComponentID = ?";
		try{
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, ProductComponentID);
			ps.executeUpdate();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public static ObservableList<NormativeItem> getObeservableList(long productId, 
																   Department department,
																   Connection connection){
		ObservableList<NormativeItem> list = FXCollections.observableArrayList();
		String query = "select p.ProductID, "
					 + "	   p.Code,"
					 + "  	   p.Name,"
					 + "	   p.ShortName,"
					 + "	   pc.ProductComponentID,"
					 + "	   pc.Normative,"
					 + "	   pc.Order,"
					 + "       pd.ProductDepartmentID"
					 + "  from productcomponent pc"
					 + "  left join productdepartment pd on pd.ProductDepartmentID = pc.IngredientProductDepartmentID"
					 + "  left join product p on pd.ProductID = p.ProductID"
					 + " where pc.ArticleProductDepartmenID = ?"
					 + "   and pd.DepartmentID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, productId);
			ps.setInt(2, department.getDepartmentID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Product p = new Product();
				p.setProductID(rs.getLong("ProductID"));
				p.setCode(rs.getString("Code"));
				p.setName(rs.getString("Name"));
				p.setShortName(rs.getString("ShortName"));
				p.setProductDepartmentID(rs.getLong("ProductDepartmentID"));
				
				NormativeItem ni = new NormativeItem();
				ni.setProductComponentID(rs.getLong("ProductComponentID"));
				ni.setProduct(p);
				ni.setNormative(rs.getDouble("Normative"));
				ni.setOrder(rs.getInt("Order"));
				list.add(ni);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;		
	}
}
