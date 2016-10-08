package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.dal.product.ProductComponent;
public class ControllData {
	private int ControllDataID;
	private int ControllID;
	private int Group;
	private String Name;
	private Date ControllTime;
	private double CalculatedValue;
	private double EnteredValue;
	private double Difference;
	private String Comment;
	private long ProductID;
	private int UnitID;
	private int VatID;
	
	public ControllData(){
		this.CalculatedValue = 0;
		this.EnteredValue = 0;
		this.Difference = 0;
	}
	
	public int getControllDataID() {
		return ControllDataID;
	}
	public void setControllDataID(int controllDataID) {
		ControllDataID = controllDataID;
	}
	public int getControllID() {
		return ControllID;
	}
	public void setControllID(int controllID) {
		ControllID = controllID;
	}
	public Date getControllTime() {
		return ControllTime;
	}
	public void setControllTime(Date controllTime) {
		ControllTime = controllTime;
	}
	public double getCalculatedValue() {
		return CalculatedValue;
	}
	public void setCalculatedValue(double calculatedValue) {
		CalculatedValue = calculatedValue;
	}
	public double getEnteredValue() {
		return EnteredValue;
	}
	public void setEnteredValue(double enteredValue) {
		EnteredValue = enteredValue;
		Difference = CalculatedValue - enteredValue;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getGroup() {
		return Group;
	}
	public void setGroup(int group) {
		Group = group;
	}
	public double getDifference() {
		return Difference;
	}
	public void setDifference(double difference) {
		Difference = difference;
	}
	public String getComment() {
		return Comment;
	}
	public void setComment(String comment) {
		Comment = comment;
	}
	public long getProductID() {
		return ProductID;
	}
	public void setProductID(long productID) {
		ProductID = productID;
	}
	public int getUnitID() {
		return UnitID;
	}

	public void setUnitID(int unitID) {
		UnitID = unitID;
	}

	public int getVatID() {
		return VatID;
	}

	public void setVatID(int vatID) {
		VatID = vatID;
	}
	
	public static ObservableList<ControllData> getControllList(LocalDate dateFrom, 
															   LocalDate dateTo, 
															   int objectId, 
															   Connection connection) throws SQLException{
		ObservableList<ControllData> list = FXCollections.observableArrayList();
		String query = "select distinct cd.ControllTime from controlldata cd  "
					 + " where cd.ControllTime >= ? and cd.ControllTime <= ?"
					 + "   and cd.ObjectID = ? "
					 + " order by cd.ControllTime desc";
		
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setDate(1, java.sql.Date.valueOf(dateFrom));
		ps.setDate(2, java.sql.Date.valueOf(dateTo.plusDays(1)));
		ps.setInt(3, objectId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()){
			ControllData cd = new ControllData();
			cd.setControllTime(rs.getTimestamp("ControllTime"));
			list.add(cd);
		}
		
		return list;
	}

	public static Timestamp getLastControllTime(Connection connection){
		String query = "select coalesce(max(cd.ControllTime),"
					 + "			    (select min(Date)"
					 + "		   		   from document))"
					 + "  from controlldata cd";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getTimestamp(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static ObservableList<ControllData> createNewControll(Timestamp time, 
																 Department department, 
																 Object object,
																 WorkDay workDay,
																 ScreensController controller) throws SQLException{
		// Uradi rekalkulaciju utrošaka za trenutni dan
		
		LocalDate currentDate = Settings.fromDate(workDay.getDate());
		for (WorkDay wd : WorkDay.getList(currentDate,currentDate, 
										  object.getObjectID(),
										  controller.getSettings().getConnection())){
			wd.saveRecap(controller);
		}
		
		// preuzimanje liste proizvoda koji se kontrolišu
		String query = "select "
					 + "	c.ControllID, "
					 + "	c.`Group`,"
					 + "	p.ProductID, "
					 + "	p.Name,"
					 + "	c.ProductID,"
					 + "	product_initial_stock(p.ProductID,?,?) - "
					 + "    product_output(p.ProductID,?,?,?) + "
					 + "    product_input(p.ProductID,?,?,?) as stanje "
					 + "  from controll c "
					 + "  left join product p on p.ProductID = c.ProductID"
					 + " where c.Active = true"
					 + " and c.ObjectID = ?"
					 + " order by sort";
		ObservableList<ControllData> controllList = FXCollections.observableArrayList();
		try{
			PreparedStatement ps = controller.getSettings().getConnection().prepareStatement(query);
			ps.setDate(1, new java.sql.Date(Settings.fromLocalDate(currentDate).getTime()));
			ps.setInt(2, department.getDepartmentID());
			ps.setDate(3, new java.sql.Date(Settings.fromLocalDate(currentDate).getTime()));
			ps.setDate(4, new java.sql.Date(Settings.fromLocalDate(currentDate).getTime()));
			ps.setInt(5, department.getDepartmentID());
			ps.setDate(6, new java.sql.Date(Settings.fromLocalDate(currentDate).getTime()));
			ps.setDate(7, new java.sql.Date(Settings.fromLocalDate(currentDate).getTime()));
			ps.setInt(8, department.getDepartmentID());
			ps.setInt(9, object.getObjectID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ControllData cd = new ControllData();
				cd.setControllID(rs.getInt("ControllID"));
				cd.setGroup(rs.getInt("Group"));
				cd.setProductID(rs.getLong("ProductID"));
				cd.setName(rs.getString("Name"));
				cd.setCalculatedValue(rs.getDouble("stanje"));
				cd.setEnteredValue(0);
				cd.setDifference(0);
				controllList.add(cd);
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		/*
		// prolaz kroz proizvode koji se kontrolišu
		for (ControllData cd :controllList){
			// prolaz kroz listu sastojaka proizvoda
			for (ProductDeparmentComponent pdc : ingrediantList){
				// popunjavanje količine ukoliko je sastojak na listi = kontrolisanom proizvodu
				if (cd.getProductID() == pdc.getArticleProduct().getProductID()){
					cd.setCalculatedValue(cd.getCalculatedValue() + pdc.getNormative());
				}
			}
		}
		*/
		return controllList;
	}
	
	public void insert(Timestamp time, Connection connection, Object object){
		String query = "insert into controlldata (ControllID,ControllTime,CalculatedValue,EnteredValue,Comment,ObjectId) "
					 + " values(?,?,round(?,2),?,?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, getControllID());
			ps.setTimestamp(2, time);
			ps.setDouble(3, getCalculatedValue());
			ps.setDouble(4, getEnteredValue());
			ps.setString(5, getComment());
			ps.setObject(6, object.getObjectID());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static ObservableList<ControllData> getList(Timestamp time, Connection connection){
		ObservableList<ControllData> list = FXCollections.observableArrayList();
		String query = "select cd.ControllDataID,"
					 + " 	   cd.ControllID,"
					 + "	   c.Group,"
					 + "	   p.Name,"
					 + "       cd.ControllTime,"
					 + "       cd.CalculatedValue,"
					 + "       cd.EnteredValue,"
					 + "       cd.CalculatedValue - COALESCE(cd.EnteredValue,0) as Difference,"
					 + "       cd.Comment"
					 + "  from controlldata cd"
					 + "  left join controll c on c.ControllID = cd.ControllID"
					 + "  left join product p on p.ProductID = c.ProductID"
					 + " where cd.ControllTime = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setTimestamp(1, time);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ControllData cd = new ControllData();
				cd.setControllDataID(rs.getInt("ControllDataID"));
				cd.setControllID(rs.getInt("ControllID"));;
				cd.setGroup(rs.getInt("Group"));
				cd.setName(rs.getString("Name"));
				cd.setControllTime(rs.getTimestamp("ControllTime"));
				cd.setCalculatedValue(rs.getDouble("CalculatedValue"));
				cd.setEnteredValue(rs.getDouble("EnteredValue"));
				cd.setDifference(rs.getDouble("Difference"));
				cd.setComment(rs.getString("Comment"));
				list.add(cd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<ProductComponent> prepareComponentList(Department department, Connection connection){
		ArrayList<ProductComponent> mainList = new ArrayList<>();
	
		String query = "select "
					 + "	dd.Quantity * dt.Stock as quantity,"
					 + "	p.ProductID,"
					 + "	p.Name"
					 + " from documentdetails dd"
					 + " left join document d on d.DocumentID = dd.DocumentID"
					 + " left join documenttype dt on dt.DocumentTypeID = d.DocumentTypeID"
					 + " left join productdepartment pd on pd.ProductID = dd.ProductID and pd.DepartmentID = ?"
					 + " left join product p on p.ProductID = pd.ProductID "
					 + "  where d.DocumentID not in (select DocumentParentID from documentrelationship dr where RelationshipTypeID = 2)";
		try{

			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, department.getDepartmentID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ProductComponent pc = new ProductComponent();
				Product p = new Product();
				p.setProductID(rs.getLong("ProductID"));
				p.setName(rs.getString("Name"));
				pc.setProduct(p);
				pc.setNormative(rs.getDouble("quantity"));
				
				mainList.add(pc);
			}
		} catch (SQLException e){}
		return mainList;
	}
	
	public static ArrayList<ProductComponent> prepareComponentList(PointOfSale pos, 
																   Date date, 
																   Connection connection){
		ArrayList<ProductComponent> mainList = new ArrayList<>();
	
		String query = "select "
					 + "	dd.Quantity * dt.Stock as quantity,"
					 + "	p.ProductID,"
					 + "    p.ProductTypeID,"
					 + "    p.Code, "
					 + "	p.Name,"
					 + "    p.UnitID,"
					 + "    p.VatID "
					 + "  from documentdetails dd"
					 + "  left join document d on d.DocumentID = dd.DocumentID"
					 + "  inner join WorkDay w on w.WorkDayID = d.WorkDayID and w.Date = ?"
					 + "  inner join productdepartment pd on pd.ProductID = dd.ProductID and pd.DepartmentID in (select psd.DepartmentID"
					 + "																						   from pointofsaledepartment psd"
					 + "																						  where psd.PointOfSaleID = ?)"
					 + "  left join documenttype dt on dt.DocumentTypeID = d.DocumentTypeID"
					 + "  left join product p on p.ProductID = pd.ProductID"
					 + " where d.DocumentID not in (select dr.DocumentParentID from documentrelationship dr where dr.RelationshipTypeID = 2) ";
		try{

			PreparedStatement ps = connection.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ps.setInt(2, pos.getPointOfSaleID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ProductComponent pc = new ProductComponent();
				Product p = new Product();
				p.setProductID(rs.getLong("ProductID"));
				p.setName(rs.getString("Name"));
				p.setCode(rs.getString("Code"));
				p.setProductTypeID(rs.getInt("ProductTypeID"));
				p.setUnitID(rs.getInt("UnitID"));
				p.setVatID(rs.getInt("VatID"));
				pc.setProduct(p);
				pc.setNormative(rs.getDouble("quantity"));
				
				mainList.add(pc);
			}
		} catch (SQLException e){}
		return mainList;
	}
}
