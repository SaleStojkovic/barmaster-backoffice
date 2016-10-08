package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.ScreensController;

public class OrderDetaile {
	private long OrderDetailsID;
	private long OrderID;
	private Date CreateTime;
	private Date StartTime;
	private Date FinishTime;
	private long ProductDepartmentID;
	private Date PreparationTime;
	private double Quantity;
	private double Price;
	private double Discount;
	private double PriceWithDiscount;
	private String Name;
	private Date preostaloVreme;
	private long ParentOrderDetaileID;
	private boolean additional = false;
	private boolean descriptional = false;
	
	public long getOrderDetailsID() {
		return OrderDetailsID;
	}
	public void setOrderDetailsID(long orderDetailsID) {
		OrderDetailsID = orderDetailsID;
	}
	public long getOrderID() {
		return OrderID;
	}
	public void setOrderID(long orderID) {
		OrderID = orderID;
	}
	public Date getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}
	public Date getStartTime() {
		return StartTime;
	}
	public void setStartTime(Date startTime) {
		StartTime = startTime;
	}
	public Date getFinishTime() {
		return FinishTime;
	}
	public void setFinishTime(Date finishTime) {
		FinishTime = finishTime;
	}
	public long getProductDepartmentID() {
		return ProductDepartmentID;
	}
	public void setProductDepartmentID(long productDepartmentID) {
		ProductDepartmentID = productDepartmentID;
	}
	public Date getPreparationTime() {
		return PreparationTime;
	}
	public void setPreparationTime(Date preparationTime) {
		PreparationTime = preparationTime;
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
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Date getPreostaloVreme() {
		return preostaloVreme;
	}
	public void setPreostaloVreme(Date preostaloVreme) {
		this.preostaloVreme = preostaloVreme;
	}

	public long getParentOrderDetaileID() {
		return ParentOrderDetaileID;
	}
	public void setParentOrderDetaileID(long parentOrderDetaileID) {
		ParentOrderDetaileID = parentOrderDetaileID;
	}
	public boolean isDescriptional() {
		return descriptional;
	}
	public void setDescriptional(boolean descriptional) {
		this.descriptional = descriptional;
	}
	public boolean isAdditional() {
		return additional;
	}
	public void setAdditional(boolean additional) {
		this.additional = additional;
	}
	public long insert(Connection connection) throws SQLException{
		long result = -1;
		String query = "insert into orderdetails(OrderID,CreateTime,ProductDepartmentID,"
					 + "						 Quantity,Price,Discount,PriceWithDiscount, ParentOrderDetaileID) "
					 + "values(?,now(),?,?,?,?,?,?)";
		PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		ps.setLong(1, OrderID);
		ps.setLong(2, ProductDepartmentID);
		ps.setDouble(3, Quantity);
		ps.setDouble(4, Price);
		ps.setDouble(5, Discount);
		ps.setDouble(6, PriceWithDiscount);
		if (ParentOrderDetaileID > 0)
			ps.setDouble(7, ParentOrderDetaileID);
		else
			ps.setNull(7, java.sql.Types.BIGINT);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		result = rs.getLong(1);
		OrderDetailsID = result;
		return result;
	}
	
	public static ObservableList<OrderDetaile> getList(long orderId, ScreensController controller){
		ObservableList<OrderDetaile> list = FXCollections.observableArrayList();
		
		String query = "select od.*,p.Name, pd.PreparationTime"
					 + "  from orderdetails od"
					 + "  left join productdepartment pd on pd.ProductDepartmentID = od.ProductDepartmentID"
					 + "  left join product p on pd.ProductID = p.ProductID"
					 + " where OrderID = ? and pd.KitchenDisplayID =?";
		try {
			PreparedStatement ps = controller.getSettings().getConnection().prepareStatement(query);
			ps.setLong(1, orderId);
			ps.setInt(2, controller.getSettings().getKitchenMonitorId());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				OrderDetaile orderDetaile = new OrderDetaile();
				orderDetaile.setOrderDetailsID(rs.getLong("OrderDetailsID"));
				orderDetaile.setOrderID(rs.getLong("OrderID"));
				orderDetaile.setCreateTime(rs.getTimestamp("CreateTime"));
				orderDetaile.setStartTime(rs.getTimestamp("StartTime"));
				orderDetaile.setFinishTime(rs.getTimestamp("FinishTime"));
				orderDetaile.setProductDepartmentID(rs.getLong("ProductDepartmentID"));
				orderDetaile.setQuantity(rs.getDouble("Quantity"));
				orderDetaile.setPrice(rs.getDouble("Price"));
				orderDetaile.setDiscount(rs.getDouble("Discount"));
				orderDetaile.setPriceWithDiscount(rs.getDouble("PriceWithDiscount"));
				orderDetaile.setParentOrderDetaileID(rs.getLong("ParentOrderDetaileID"));
				
				orderDetaile.setAdditional(orderDetaile.getParentOrderDetaileID() > 0);
				if (!orderDetaile.isAdditional()) 
					orderDetaile.setName(rs.getString("Name"));
				if (orderDetaile.isAdditional()) 
					orderDetaile.setName(" - " + rs.getString("Name"));
				orderDetaile.setPreparationTime(rs.getTime("PreparationTime"));
				orderDetaile.setPreostaloVreme(rs.getTime("PreparationTime"));
				
				list.add(orderDetaile);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	public static ObservableList<OrderDetaile> getListFP(long orderId, ScreensController controller){
		ObservableList<OrderDetaile> list = FXCollections.observableArrayList();
		
		String query = "select od.*,p.Name, pd.PreparationTime"
					 + "  from orderdetails od"
					 + "  left join productdepartment pd on pd.ProductDepartmentID = od.ProductDepartmentID"
					 + "  left join product p on pd.ProductID = p.ProductID"
					 + " where OrderID = ?";
		try {
			PreparedStatement ps = controller.getSettings().getConnection().prepareStatement(query);
			ps.setLong(1, orderId);
			//ps.setInt(2, controller.getSettings().getKitchenMonitorId());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				OrderDetaile orderDetaile = new OrderDetaile();
				orderDetaile.setOrderDetailsID(rs.getLong("OrderDetailsID"));
				orderDetaile.setOrderID(rs.getLong("OrderID"));
				orderDetaile.setCreateTime(rs.getTimestamp("CreateTime"));
				orderDetaile.setStartTime(rs.getTimestamp("StartTime"));
				orderDetaile.setFinishTime(rs.getTimestamp("FinishTime"));
				orderDetaile.setProductDepartmentID(rs.getLong("ProductDepartmentID"));
				orderDetaile.setQuantity(rs.getDouble("Quantity"));
				orderDetaile.setPrice(rs.getDouble("Price"));
				orderDetaile.setDiscount(rs.getDouble("Discount"));
				orderDetaile.setPriceWithDiscount(rs.getDouble("PriceWithDiscount"));
				orderDetaile.setParentOrderDetaileID(rs.getLong("ParentOrderDetaileID"));
				
				orderDetaile.setAdditional(orderDetaile.getParentOrderDetaileID() > 0);
				if (!orderDetaile.isAdditional()) 
					orderDetaile.setName(rs.getString("Name"));
				if (orderDetaile.isAdditional()) 
					orderDetaile.setName("> " + rs.getString("Name"));
				orderDetaile.setPreparationTime(rs.getTime("PreparationTime"));
				orderDetaile.setPreostaloVreme(rs.getTime("PreparationTime"));
				
				list.add(orderDetaile);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void start(Connection connection) throws SQLException{
		String query = "update orderdetails set StartTime = now() where OrderDetailsID = ?";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setLong(1, OrderDetailsID);
		ps.executeUpdate();
	}
	public void finish(Connection connection) throws SQLException{
		String query = "update orderdetails set FinishTime = now() where OrderDetailsID = ?";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setLong(1, OrderDetailsID);
		ps.executeUpdate();
	}
}
