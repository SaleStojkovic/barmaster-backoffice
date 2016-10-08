package rs.petcom.master.dal.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.PointOfSale;

public class ProductDepartment {
	private long ProductDepartmentID;
	private long ProductID;
	private Product product;
	private int Sort;
	private boolean Active;
	private boolean Favorite;
	private long Min;
	private long Max;
	private int WarehouseID;
	private int PrinterID;
	private int DepartmentID;
	private int Position;
	private String Color;
	private int PointOfSaleID;
	private int KitchenDisplayID;
	private boolean enabled;
	
	public long getProductDepartmentID() {
		return ProductDepartmentID;
	}
	public void setProductDepartmentID(long productDepartmentID) {
		ProductDepartmentID = productDepartmentID;
	}
	public long getProductID() {
		return ProductID;
	}
	public void setProductID(long productID) {
		ProductID = productID;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
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
	public boolean isFavorite() {
		return Favorite;
	}
	public void setFavorite(boolean favorite) {
		Favorite = favorite;
	}
	public long getMin() {
		return Min;
	}
	public void setMin(long min) {
		Min = min;
	}
	public long getMax() {
		return Max;
	}
	public void setMax(long max) {
		Max = max;
	}
	public int getWarehouseID() {
		return WarehouseID;
	}
	public void setWarehouseID(int warehouseID) {
		WarehouseID = warehouseID;
	}
	public int getPrinterID() {
		return PrinterID;
	}
	public void setPrinterID(int printerID) {
		PrinterID = printerID;
	}
	public int getDepartmentID() {
		return DepartmentID;
	}
	public void setDepartmentID(int departmentID) {
		DepartmentID = departmentID;
	}	
	public int getPosition() {
		return Position;
	}
	public void setPosition(int position) {
		Position = position;
	}
	public String getColor() {
		return Color;
	}
	public void setColor(String color) {
		Color = color;
	}
	public int getPointOfSaleID() {
		return PointOfSaleID;
	}
	public void setPointOfSaleID(int pointOfSaleID) {
		PointOfSaleID = pointOfSaleID;
	}	
	public int getKitchenDisplayID() {
		return KitchenDisplayID;
	}
	public void setKitchenDisplayID(int kitchenDisplayID) {
		KitchenDisplayID = kitchenDisplayID;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public long insert(Connection connection) throws SQLException{
		String query = "insert into productdepartment ( "
					 + "	ProductID, "
					 + "	Sort, "
					 + "	Active,"
					 + " 	Favorite,"
					 + "	Min,"
					 + "	Max,"
					 + "	WarehouseID,"
					 + "	PrinterID,"
					 + "	DepartmentID,"
					 + "    KitchenDisplayID)"
					 + " values (?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			ps.setLong(1,ProductID);
			ps.setInt(2, Sort);
			ps.setBoolean(3,Active);
			ps.setBoolean(4,Favorite);
			ps.setLong(5, Min);
			ps.setLong(6,Max);
			if (WarehouseID != 0)
				ps.setInt(7,WarehouseID);
			else 
				ps.setNull(7, java.sql.Types.INTEGER);
			if (PrinterID != 0)
				ps.setInt(8,PrinterID);
			else
				ps.setNull(8, java.sql.Types.INTEGER);
			ps.setInt(9,DepartmentID);
			if (KitchenDisplayID > 0) 
				ps.setInt(10, KitchenDisplayID);
			else{
				ps.setNull(10, java.sql.Types.INTEGER);
			}
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			ProductDepartmentID = rs.getLong(1);
			return rs.getLong(1);
	}
	
	public void delete(Connection connection) throws SQLException{
		String query = "delete from productdepartment where ProductDepartmentID = ? ";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setLong(1,ProductDepartmentID);
		ps.executeUpdate();
	}
	
	public static ObservableList<ProductDepartment> getList(PointOfSale pos, Connection connection){
		ObservableList<ProductDepartment> list = FXCollections.observableArrayList();
		
		String query = "select * from productdepartment where PointOfSaleID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, pos.getPointOfSaleID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ProductDepartment pd = new ProductDepartment();
				pd.ProductDepartmentID = rs.getLong("ProductDepartmentID");
				pd.ProductID = rs.getLong("ProductID");
				pd.Sort = rs.getInt("Sort");
				pd.Active = rs.getBoolean("Active");
				pd.Favorite = rs.getBoolean("Favorite");
				pd.Min = rs.getLong("Min");
				pd.Max = rs.getLong("Max");
				pd.WarehouseID = rs.getInt("WarehouseID");
				pd.PrinterID = rs.getInt("PrinterID");
				pd.DepartmentID = rs.getInt("DepartmentID");
				pd.KitchenDisplayID = rs.getInt("KitchenDisplayID");
				pd.product = Product.getByID(pd.ProductID,connection);
				list.add(pd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ArrayList<ProductDepartment> getList(PointOfSale pos, int groupId, Connection connection){
		ArrayList<ProductDepartment> list = new ArrayList<>();
		
		String query = "select pd.*,gpp.Position,gpp.Color,gpp.PointOfSaleID "
					 + "  from grouppointofsaleproduct gpp"
					 + "  left join productdepartment pd on pd.ProductDepartmentID = gpp.ProductDepartmentID"
					 + " where gpp.PointOfSaleID = ? and GroupID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, pos.getPointOfSaleID());
			ps.setInt(2, groupId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ProductDepartment pd = new ProductDepartment();
				pd.ProductDepartmentID = rs.getLong("ProductDepartmentID");
				pd.ProductID = rs.getLong("ProductID");
				pd.Sort = rs.getInt("Sort");
				pd.Active = rs.getBoolean("Active");
				pd.Favorite = rs.getBoolean("Favorite");
				pd.Min = rs.getLong("Min");
				pd.Max = rs.getLong("Max");
				pd.WarehouseID = rs.getInt("WarehouseID");
				pd.PrinterID = rs.getInt("PrinterID");
				pd.DepartmentID = rs.getInt("DepartmentID");
				pd.Position = rs.getInt("Position");
				pd.Color = rs.getString("Color");
				pd.PointOfSaleID = rs.getInt("PointOfSaleID");
				pd.KitchenDisplayID = rs.getInt("KitchenDisplayID");
				pd.product = Product.getByID(pd.ProductID,connection);
				list.add(pd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ArrayList<ProductDepartment> getAdditionalList(PointOfSale pos, Group group, Connection connection){
		ArrayList<ProductDepartment> list = new ArrayList<>();
		
		String query = "select pd1.*, gpp.color "
					 + "  from productdepartment pd"
					 + "  inner join productaditional pa on pa.ParentProductID = pd.ProductID"
					 + "  inner join productdepartment pd1 on pd1.ProductID = pa.ChildProductID and pd1.DepartmentID = ?"
					 + "  left join grouppointofsaleproduct gpp on gpp.ProductDepartmentID = pd.ProductDepartmentID "
					 + "										and gpp.GroupID = ? "
					 + "										and gpp.PointOfSaleID = ?"
					 + " where pd.DepartmentID = ?"
					 + "   and pd.ProductID = ?"
					 + " order by Sort";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, DepartmentID);
			ps.setInt(2, group.getGroupID());
			ps.setInt(3, pos.getPointOfSaleID());
			ps.setInt(4, DepartmentID);
			ps.setLong(5, ProductID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ProductDepartment pd = new ProductDepartment();
				pd.ProductDepartmentID = rs.getLong("ProductDepartmentID");
				pd.ProductID = rs.getLong("ProductID");
				pd.Sort = rs.getInt("Sort");
				pd.Active = rs.getBoolean("Active");
				pd.Favorite = rs.getBoolean("Favorite");
				pd.Min = rs.getLong("Min");
				pd.Max = rs.getLong("Max");
				pd.WarehouseID = rs.getInt("WarehouseID");
				pd.PrinterID = rs.getInt("PrinterID");
				pd.DepartmentID = rs.getInt("DepartmentID");
				//pd.Position = rs.getInt("Position");
				//pd.Color = rs.getString("Color");
				//pd.PointOfSaleID = rs.getInt("PointOfSaleID");
				pd.KitchenDisplayID = rs.getInt("KitchenDisplayID");
				pd.product = Product.getByID(pd.ProductID,connection);
				list.add(pd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public static ObservableList<ProductDepartment> getList(Department department, Connection connection){
		ObservableList<ProductDepartment> list = FXCollections.observableArrayList();
		
		String query = "select * from productdepartment where DepartmentID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, department.getDepartmentID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ProductDepartment pd = new ProductDepartment();
				pd.ProductDepartmentID = rs.getLong("ProductDepartmentID");
				pd.ProductID = rs.getLong("ProductID");
				pd.Sort = rs.getInt("Sort");
				pd.Active = rs.getBoolean("Active");
				pd.Favorite = rs.getBoolean("Favorite");
				pd.Min = rs.getLong("Min");
				pd.Max = rs.getLong("Max");
				pd.WarehouseID = rs.getInt("WarehouseID");
				pd.PrinterID = rs.getInt("PrinterID");
				pd.DepartmentID = rs.getInt("DepartmentID");
				pd.Color = rs.getString("Color");
				pd.PointOfSaleID = rs.getInt("PointOfSaleID");
				pd.KitchenDisplayID = rs.getInt("KitchenDisplayID");
				pd.product = Product.getByID(pd.ProductID,connection);
				list.add(pd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ProductDepartment getFromDB(long productId, long departmentId, Connection connection) throws SQLException{
		String query = "select * from productdepartment where ProductID = ?  and DepartmentID = ?";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setLong(1, productId);
		ps.setLong(2, departmentId);
		ResultSet rs = ps.executeQuery();
		rs.next();
		ProductDepartment pd = new ProductDepartment();
		pd.ProductDepartmentID = rs.getLong("ProductDepartmentID");
		pd.ProductID = rs.getLong("ProductID");
		pd.Sort = rs.getInt("Sort");
		pd.Active = rs.getBoolean("Active");
		pd.Favorite = rs.getBoolean("Favorite");
		pd.Min = rs.getLong("Min");
		pd.Max = rs.getLong("Max");
		pd.WarehouseID = rs.getInt("WarehouseID");
		pd.PrinterID = rs.getInt("PrinterID");
		pd.DepartmentID = rs.getInt("DepartmentID");
		pd.KitchenDisplayID = rs.getInt("KitchenDisplayID");
		pd.product = Product.getByID(pd.ProductID,connection);
		return pd;
	}
}
