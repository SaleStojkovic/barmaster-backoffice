package rs.petcom.master.dal.document;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.WorkDay;

public class DocumentDetails {
	private long DocumentDetailsID;
	private long ParentDocumentDetailsID;
	private long DocumentID;
	private long ProductID;
	private long ParentProductID;
	private double Quantity;
	private int VatID;
	private int UnitID;
	private double Price;
	private double Discount;
	private double PriceWithDiscount;
	private double Costs;
	private double NewPrice;
	private long DocumentChangeID;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	private int WarehouseID;
	private int SaleTableID;
	private double PurchacePrice;
	private int OrderNumber;
	private boolean SubTotal;
	private boolean Closed;
	private int DescriptionID;
	private String ProductCode;
	private String ProductName;
	private String ProductShortName;
	private double Value;
		
	public long getDocumentDetailsID() {
		return DocumentDetailsID;
	}
	public void setDocumentDetailsID(long documentDetailsID) {
		DocumentDetailsID = documentDetailsID;
	}
	public long getParentDocumentDetailsID() {
		return ParentDocumentDetailsID;
	}
	public void setParentDocumentDetailsID(long parentDocumentDetailsID) {
		ParentDocumentDetailsID = parentDocumentDetailsID;
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
	public long getParentProductID() {
		return ParentProductID;
	}
	public void setParentProductID(long parentProductID) {
		ParentProductID = parentProductID;
	}
	public double getQuantity() {
		return Quantity;
	}
	public void setQuantity(double quantity) {
		Quantity = quantity;
	}
	public int getVatID() {
		return VatID;
	}
	public void setVatID(int vatID) {
		VatID = vatID;
	}
	public int getUnitID() {
		return UnitID;
	}
	public void setUnitID(int unitID) {
		UnitID = unitID;
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
	public double getCosts() {
		return Costs;
	}
	public void setCosts(double costs) {
		Costs = costs;
	}
	public double getNewPrice() {
		return NewPrice;
	}
	public void setNewPrice(double newPrice) {
		NewPrice = newPrice;
	}
	public long getDocumentChangeID() {
		return DocumentChangeID;
	}
	public void setDocumentChangeID(long documentChangeID) {
		DocumentChangeID = documentChangeID;
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
	public int getWarehouseID() {
		return WarehouseID;
	}
	public void setWarehouseID(int warehouseID) {
		WarehouseID = warehouseID;
	}
	public int getSaleTableID() {
		return SaleTableID;
	}
	public void setSaleTableID(int saleTableID) {
		SaleTableID = saleTableID;
	}
	public double getPurchacePrice() {
		return PurchacePrice;
	}
	public void setPurchacePrice(double purchacePrice) {
		PurchacePrice = purchacePrice;
	}
	public int getOrderNumber() {
		return OrderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		OrderNumber = orderNumber;
	}
	public boolean isSubTotal() {
		return SubTotal;
	}
	public void setSubTotal(boolean subTotal) {
		SubTotal = subTotal;
	}
	public boolean isClosed() {
		return Closed;
	}
	public void setClosed(boolean closed) {
		Closed = closed;
	}
	public int getDescriptionID() {
		return DescriptionID;
	}
	public void setDescriptionID(int descriptionID) {
		DescriptionID = descriptionID;
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
	public String getProductShortName() {
		return ProductShortName;
	}
	public void setProductShortName(String productShortName) {
		ProductShortName = productShortName;
	}
	public void setValue(double value) {
		Value = value;
	}
	public double getValue() {
		return Value;
	}
	
	public long insert(Connection connection){
		String query = "insert into documentdetails( "
				     + "		`DocumentID`,"
				     + "		`ParentDocumentDetailsID`,"
				     + "		`ProductID`,"
				     + "		`ParentProductID`,"
				     + "		`Quantity`,"
				     + "		`VatID`,"
				     + "		`UnitID`,"
				     + "		`Price`,"
				     + "		`Discount`,"
				     + "		`PriceWithDiscount`,"
				     + "		`Costs`,"
				     + "		`NewPrice`,"
				     + "		`DocumentChangeID`,"
				     + "		`sysUserAddID`,"
				     + "		`WarehouseID`,"
				     + "		`SaleTableID`,"
				     + "		`PurchacePrice`,"
				     + "		`OrderNumber`,"
				     + "		`SubTotal`,"
				     + "		`Closed`,"
				     + "		`DescriptionID` )"
				     + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, DocumentID);
			if (ParentDocumentDetailsID > 0)
				ps.setLong(2, ParentDocumentDetailsID);
			else
				ps.setNull(2, java.sql.Types.BIGINT);
			ps.setLong(3, ProductID);
			if (ParentProductID != 0) ps.setLong(4, ParentProductID);
				else ps.setNull(4, java.sql.Types.BIGINT);
		    ps.setDouble(5, Quantity);
		    ps.setInt(6, VatID);
		    ps.setInt(7, UnitID);
		    ps.setDouble(8, Price);
		    ps.setDouble(9, Discount);
		    ps.setDouble(10, PriceWithDiscount);
		    ps.setDouble(11, Costs);
		    ps.setDouble(12, NewPrice);
		    if (DocumentChangeID != 0) ps.setLong(13, DocumentChangeID);
		    	else ps.setNull(13, java.sql.Types.BIGINT);
		    ps.setLong(14, sysUserAddID);
		    if (WarehouseID != 0) ps.setLong(15, WarehouseID);
		    	else ps.setNull(15, java.sql.Types.BIGINT);
		    if (SaleTableID != 0) ps.setInt(16, SaleTableID);
		    	else ps.setNull(16, java.sql.Types.BIGINT);
		    ps.setDouble(17, PurchacePrice);
		    if (OrderNumber != 0) ps.setInt(18, OrderNumber);
		    	else ps.setNull(18, java.sql.Types.INTEGER);
		    ps.setBoolean(19, SubTotal);
		    ps.setBoolean(20, Closed);
		    if (DescriptionID != 0) ps.setLong(21, DescriptionID);
		    	else ps.setNull(21, java.sql.Types.BIGINT);
		    ps.executeUpdate();
		    
		    ResultSet rs = ps.getGeneratedKeys();
		    rs.next();
		    DocumentDetailsID = rs.getLong(1);
		    return rs.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void update(ScreensController controller){
		String query = "update documentdetails set "
					 + "		`ParentDocumentDetailsID` = ?,"
				     + "		`DocumentID` = ?,"
				     + "		`ProductID` = ?,"
				     + "		`ParentProductID` = ?,"
				     + "		`Quantity` = ?,"
				     + "		`VatID` = ?,"
				     + "		`UnitID` = ?,"
				     + "		`Price` = ?,"
				     + "		`Discount` = ?,"
				     + "		`PriceWithDiscount` = ?,"
				     + "		`Costs` = ?,"
				     + "		`NewPrice` = ?,"
				     + "		`DocumentChangeID` = ?,"
				     + "		`sysUserEditID` = ?,"
				     + "		`WarehouseID` = ?,"
				     + "		`SaleTableID` = ?,"
				     + "		`PurchacePrice` = ?,"
				     + "		`OrderNumber` = ?,"
				     + "		`SubTotal` = ?,"
				     + "		`Closed` = ?,"
				     + "		`DescriptionID` = ? "
				     + " where DocumentDetailsID = ?";
		try {
			PreparedStatement ps = controller.getSettings().getConnection().prepareStatement(query);
			if (ParentDocumentDetailsID > 0)
				ps.setLong(1, ParentDocumentDetailsID);
			else
				ps.setNull(1, java.sql.Types.BIGINT);
			ps.setLong(2, DocumentID);
			ps.setLong(3, ProductID);
			if (ParentProductID != 0) ps.setLong(4, ParentProductID);
				else ps.setNull(4, java.sql.Types.BIGINT);
		    ps.setDouble(5, Quantity);
		    ps.setInt(6, VatID);
		    ps.setInt(7, UnitID);
		    ps.setDouble(8, Price);
		    ps.setDouble(9, Discount);
		    ps.setDouble(10, PriceWithDiscount);
		    ps.setDouble(11, Costs);
		    ps.setDouble(12, NewPrice);
		    if (DocumentChangeID != 0) ps.setLong(13, DocumentChangeID);
		    	else ps.setNull(13, java.sql.Types.BIGINT);
		    ps.setLong(14, controller.getUser().getUserID());
		    if (WarehouseID != 0) ps.setLong(15, WarehouseID);
		    	else ps.setNull(15, java.sql.Types.BIGINT);
		    if (SaleTableID != 0) ps.setInt(16, SaleTableID);
		    	else ps.setNull(16, java.sql.Types.BIGINT);
		    ps.setDouble(17, PurchacePrice);
		    if (OrderNumber != 0) ps.setInt(18, OrderNumber);
		    	else ps.setNull(18, java.sql.Types.INTEGER);
		    ps.setBoolean(19, SubTotal);
		    ps.setBoolean(20, Closed);
		    if (DescriptionID != 0) ps.setLong(21, DescriptionID);
		    	else ps.setNull(21, java.sql.Types.BIGINT);
		    ps.setLong(22, DocumentDetailsID);
		    ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static DocumentDetails getByID(long id, Connection connection){
		
		String query = "select dd.*,p.Name,p.ShortName "
					 + "  from documentdetails dd"
					 + "  left join product p on p.ProductID = dd.ProductID "
					 + " where dd.DocumentDetailsID = ?";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			DocumentDetails dd = new DocumentDetails();
			dd.DocumentDetailsID = rs.getLong("DocumentDetailsID");
			dd.ParentDocumentDetailsID = rs.getLong("ParentDocumentDetailsID");
			dd.DocumentID = rs.getLong("DocumentID");
			dd.ProductID = rs.getLong("ProductID");
			dd.ParentProductID = rs.getLong("ParentProductID");
			dd.Quantity = rs.getDouble("Quantity");
			dd.VatID = rs.getInt("VatID");
			dd.UnitID = rs.getInt("UnitID");
			dd.Price = rs.getDouble("Price");
			dd.Discount = rs.getDouble("Discount");
			dd.PriceWithDiscount = rs.getDouble("PriceWithDiscount");
			dd.Costs = rs.getDouble("Costs");
			dd.NewPrice = rs.getDouble("NewPrice");
			dd.DocumentChangeID = rs.getLong("DocumentChangeID");
			dd.sysDTCreated = rs.getTimestamp("sysDTCreated");
			dd.sysDTEdit = rs.getTimestamp("sysDTCreated");
			dd.sysUserAddID = rs.getLong("sysUserAddID");
			dd.sysUserEditID = rs.getLong("sysUserEditID");
			dd.WarehouseID = rs.getInt("WarehouseID");
			dd.SaleTableID = rs.getInt("SaleTableID");
			dd.PurchacePrice = rs.getDouble("PurchacePrice");
			dd.OrderNumber = rs.getInt("OrderNumber");
			dd.SubTotal = rs.getBoolean("SubTotal");
			dd.Closed = rs.getBoolean("Closed");
			dd.DescriptionID = rs.getInt("DescriptionID");
			dd.ProductName = rs.getString("Name");
			dd.ProductShortName = rs.getString("ShortName");
			dd.Value = dd.Quantity * (dd.Price - dd.Discount);
			return dd;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<DocumentDetails> getList(long documentId, Connection connection){
		ArrayList<DocumentDetails> list = new ArrayList<>();
		
		String query = "select dd.*, p.Name, p.ShortName "
					 + "  from documentdetails dd "
					 + "  left join  product p on dd.ProductID = p.ProductID";
		if (documentId != 0)
			   query += " where dd.DocumentID = ?";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			if (documentId != 0)
				ps.setLong(1, documentId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				DocumentDetails dd = new DocumentDetails();
				dd.DocumentDetailsID = rs.getLong("DocumentDetailsID");
				dd.ParentDocumentDetailsID = rs.getLong("ParentDocumentDetailsID");
				dd.DocumentID = rs.getLong("DocumentID");
				dd.ProductID = rs.getLong("ProductID");
				dd.ParentProductID = rs.getLong("ParentProductID");
				dd.Quantity = rs.getDouble("Quantity");
				dd.VatID = rs.getInt("VatID");
				dd.UnitID = rs.getInt("UnitID");
				dd.Price = rs.getDouble("Price");
				dd.Discount = rs.getDouble("Discount");
				dd.PriceWithDiscount = rs.getDouble("PriceWithDiscount");
				dd.Costs = rs.getDouble("Costs");
				dd.NewPrice = rs.getDouble("NewPrice");
				dd.DocumentChangeID = rs.getLong("DocumentChangeID");
				dd.sysDTCreated = rs.getTimestamp("sysDTCreated");
				dd.sysDTEdit = rs.getTimestamp("sysDTCreated");
				dd.sysUserAddID = rs.getLong("sysUserAddID");
				dd.sysUserEditID = rs.getLong("sysUserEditID");
				dd.WarehouseID = rs.getInt("WarehouseID");
				dd.SaleTableID = rs.getInt("SaleTableID");
				dd.PurchacePrice = rs.getDouble("PurchacePrice");
				dd.OrderNumber = rs.getInt("OrderNumber");
				dd.SubTotal = rs.getBoolean("SubTotal");
				dd.Closed = rs.getBoolean("Closed");
				dd.DescriptionID = rs.getInt("DescriptionID");
				dd.ProductName = rs.getString("Name");
				dd.ProductShortName = rs.getString("ShortName");
				dd.Value = dd.Quantity * (dd.Price - dd.Discount);
				list.add(dd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ObservableList<DocumentDetails> getObeservableList(long documentId, Connection connection){
		ObservableList<DocumentDetails> list = FXCollections.observableArrayList();
		for(DocumentDetails dd : DocumentDetails.getList(documentId, connection)){
			list.add(dd);
		}
		return list;
	}
	
	public static ArrayList<DocumentDetails> getSoldItems(WorkDay workday, Connection connection){
		ArrayList<DocumentDetails> list = new ArrayList<>();
		
		String query = "CALL sold_items_workday(?)";
	try {
		CallableStatement cs = connection.prepareCall(query);
		cs.setLong(1, workday.getWorkDayID());
		ResultSet rs = cs.executeQuery();
		while (rs.next()){
			
			DocumentDetails dd = new DocumentDetails();
			dd.DocumentDetailsID = rs.getLong("DocumentDetailsID");
			dd.ParentDocumentDetailsID = rs.getLong("ParentDocumentDetailsID");
			dd.DocumentID = rs.getLong("DocumentID");
			dd.ProductID = rs.getLong("ProductID");
			dd.ParentProductID = rs.getLong("ParentProductID");
			dd.Quantity = rs.getDouble("Quantity");
			dd.VatID = rs.getInt("VatID");
			dd.UnitID = rs.getInt("UnitID");
			dd.Price = rs.getDouble("Price");
			dd.Discount = rs.getDouble("Discount");
			dd.PriceWithDiscount = rs.getDouble("PriceWithDiscount");
			dd.Costs = rs.getDouble("Costs");
			dd.NewPrice = rs.getDouble("NewPrice");
			dd.DocumentChangeID = rs.getLong("DocumentChangeID");
			dd.sysDTCreated = rs.getTimestamp("sysDTCreated");
			dd.sysDTEdit = rs.getTimestamp("sysDTCreated");
			dd.sysUserAddID = rs.getLong("sysUserAddID");
			dd.sysUserEditID = rs.getLong("sysUserEditID");
			dd.WarehouseID = rs.getInt("WarehouseID");
			dd.SaleTableID = rs.getInt("SaleTableID");
			dd.PurchacePrice = rs.getDouble("PurchacePrice");
			dd.OrderNumber = rs.getInt("OrderNumber");
			dd.SubTotal = rs.getBoolean("SubTotal");
			dd.Closed = rs.getBoolean("Closed");
			dd.DescriptionID = rs.getInt("DescriptionID");
			dd.Value = dd.Quantity * (dd.Price - dd.Discount);
			
			boolean dodato = false;
			for ( DocumentDetails dd1 : list){
				if (dd1.getProductID() == dd.ProductID){
					dd1.setQuantity(dd1.getQuantity() + dd.getQuantity());
					dodato = true;
				}
			}
			if (!dodato){
				list.add(dd);
			}
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
		
		return list;
	}
	
	public static ArrayList<DocumentDetails> getSoldItems(Department department, Date dateFrom, Date dateTo, Connection connection){
		ArrayList<DocumentDetails> list = new ArrayList<>();
		
		String query = "select dd.ProductID,"
					 + "	   dd.ParentProductID,"
					 + "	   sum(dd.Quantity),"
					 + "		dd.VatID,"
					 + "		dd.UnitID,"
					 + "		dd.Price,"
					 + "		dd.Discount,"
					 + "		dd.PriceWithDiscount,"
					 + "		dd.Costs,"
					 + "		dd.NewPrice,"
					 + "		dd.DocumentChangeID,"
					 + "		dd.WarehouseID,"
					 + "		dd.SaleTableID,"
					 + "		dd.PurchacePrice,"
					 + "		dd.OrderNumber,"
					 + "		dd.SubTotal,"
					 + "		dd.Closed,"
					 + "		dd.DescriptionID"
				     + "	from documentdetails dd"
				     + "	left join document d on d.DocumentID = dd.DocumentID and d.DocumentTypeID = 3"
				     + "	inner join workday w on w.WorkDayID = d.WorkDayID and w.Date >= ? and w.Date <= ? "
				     + " where d.DocumentID not in (select dr.DocumentParentID from documentrelationship dr where dr.RelationshipTypeID = 2)"
				     + "   and w.PointOfSaleID in (select PointOfSaleID from pointofsaledepartment where DepartmentID = ?)"
				     + " group by dd.ProductID,dd.ParentProductID,dd.VatID,dd.UnitID,dd.Price,dd.Discount,"
				     + "		  dd.PriceWithDiscount,dd.Costs,dd.NewPrice,dd.DocumentChangeID,dd.WarehouseID,"
				     + "		  dd.SaleTableID,dd.PurchacePrice,dd.OrderNumber,dd.SubTotal,dd.Closed,dd.DescriptionID";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(dateFrom.getTime()));
			ps.setDate(2, new java.sql.Date(dateTo.getTime()));
			ps.setInt(3, department.getDepartmentID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				DocumentDetails dd = new DocumentDetails();
				dd.ProductID = rs.getLong("ProductID");
				dd.ParentProductID = rs.getLong("ParentProductID");
				dd.Quantity = rs.getDouble("Quantity");
				dd.VatID = rs.getInt("VatID");
				dd.UnitID = rs.getInt("UnitID");
				dd.Price = rs.getDouble("Price");
				dd.Discount = rs.getDouble("Discount");
				dd.PriceWithDiscount = rs.getDouble("PriceWithDiscount");
				dd.Costs = rs.getDouble("Costs");
				dd.NewPrice = rs.getDouble("NewPrice");
				dd.DocumentChangeID = rs.getLong("DocumentChangeID");
				dd.WarehouseID = rs.getInt("WarehouseID");
				dd.SaleTableID = rs.getInt("SaleTableID");
				dd.PurchacePrice = rs.getDouble("PurchacePrice");
				dd.OrderNumber = rs.getInt("OrderNumber");
				dd.SubTotal = rs.getBoolean("SubTotal");
				dd.Closed = rs.getBoolean("Closed");
				dd.DescriptionID = rs.getInt("DescriptionID");
				dd.Value = dd.Quantity * (dd.Price - dd.Discount);
				
				boolean dodato = false;
				for ( DocumentDetails dd1 : list){
					if (dd1.getProductID() == dd.ProductID){
						dd1.setQuantity(dd1.getQuantity() + dd.getQuantity());
						dodato = true;
					}
				}
				if (!dodato){
					list.add(dd);
				}
				
				//list.add(dd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void delete(Connection connection){
		String query = "delete from documentdetails where DocumentDetailsID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, DocumentDetailsID);
		    ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void deleteDetailes(Document document, Connection connection){
		String query = "delete from documentdetails where DocumentID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, document.getDocumentID());
		    ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
