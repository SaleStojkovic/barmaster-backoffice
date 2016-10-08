package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Round;
import rs.petcom.master.dal.RoundItem;

public class Order {
	private long OrderID;
	private int SaleTableID;
	private long DocumentID;
	long sysUserAddID;
	
	public Order(){}
	public Order(long sysUserAddID){
		this.sysUserAddID = sysUserAddID;
	}
	
	public long getOrderID() {
		return OrderID;
	}
	public void setOrderID(long orderID) {
		OrderID = orderID;
	}
	public int getSaleTableID() {
		return SaleTableID;
	}
	public void setSaleTableID(int saleTableID) {
		SaleTableID = saleTableID;
	}
	public long getDocumentID() {
		return DocumentID;
	}
	public void setDocumentID(long documentID) {
		DocumentID = documentID;
	}
	public long getSysUserAddID() {
		return sysUserAddID;
	}
	public void setSysUserAddID(long sysUserAddID) {
		this.sysUserAddID = sysUserAddID;
	}
	
	public long insert(Connection connection) throws SQLException{
		long result = -1;
		String query = "insert into `order`(SaleTableID,DocumentID,sysUserAddID) values (?,?,?)";
		PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		if (SaleTableID > 1)
			ps.setInt(1, SaleTableID);
		else ps.setNull(1, java.sql.Types.INTEGER);
		if (DocumentID > 1)
			ps.setLong(2, DocumentID);
		else
			ps.setNull(2, java.sql.Types.BIGINT);
		ps.setLong(3, sysUserAddID);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		result = rs.getLong(1);
		OrderID = result;
		return result;
	}
	
	public static long insertOrder(Round round, 
								   long documentID,
								   ScreensController controller) throws SQLException{
		Order order = new Order(controller.getUser().getUserID());
		order.setDocumentID(documentID);
		long orderId = order.insert(controller.getSettings().getConnection());
		for (RoundItem ri : round.getItemList()){
			if (ri.getProductDepartment().getKitchenDisplayID() > 0){
				OrderDetaile od = new OrderDetaile();
				od.setOrderID(orderId);
				od.setProductDepartmentID(ri.getProductDepartment().getProductDepartmentID());
				od.setQuantity(ri.getQuantity());
				od.setPrice(ri.getProductDepartment().getProduct().getPrice());
				od.setDiscount(ri.getProductDepartment().getProduct().getDiscount());
				od.setPriceWithDiscount(ri.getProductDepartment().getProduct().getPrice() + 
										ri.getProductDepartment().getProduct().getDiscount());
				od.insert(controller.getSettings().getConnection());
				
				for (RoundItem radd : ri.getAdditionalList()){
					OrderDetaile odA = new OrderDetaile();
					odA.setAdditional(true);
					odA.setOrderID(orderId);
					odA.setProductDepartmentID(radd.getProductDepartment().getProductDepartmentID());
					odA.setQuantity(radd.getQuantity());
					odA.setPrice(radd.getProductDepartment().getProduct().getPrice());
					odA.setDiscount(radd.getProductDepartment().getProduct().getDiscount());
					odA.setPriceWithDiscount(radd.getProductDepartment().getProduct().getPrice() + 
											 radd.getProductDepartment().getProduct().getDiscount());
					odA.setParentOrderDetaileID(od.getOrderDetailsID());
					odA.insert(controller.getSettings().getConnection());
				}
			}
		}
		return orderId;
	}
	
	public static ArrayList<Order> getList(ScreensController controller){
		ArrayList<Order> list = new ArrayList<>();
		
		String query = "select * "
					 + "  from `order`"
					 + " where OrderID in (select od.OrderID"
					 + "                     from orderdetails od"
					 + "                     inner join productdepartment pd on pd.ProductDepartmentID = od.ProductDepartmentID"
					 + "                    										    and pd.KitchenDisplayID = ?"
					 + "					 where od.FinishTime is null )";
		try {
			PreparedStatement ps = controller.getSettings().getConnection().prepareStatement(query);
			ps.setInt(1, controller.getSettings().getKitchenMonitorId());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Order order = new Order();
				order.setOrderID(rs.getLong("OrderID"));
				order.setSaleTableID(rs.getInt("SaleTableID"));
				order.setDocumentID(rs.getLong("DocumentID"));
				order.setSysUserAddID(rs.getLong("sysUserAddID"));
				list.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
