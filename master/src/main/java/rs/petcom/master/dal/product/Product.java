package rs.petcom.master.dal.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.User;
import rs.petcom.master.dal.WorkDay;
import rs.petcom.master.dal.document.DocumentDetails;

public class Product {
	private long ProductID;
	private int ProductTypeID;
	private int ProductCategoryID;
	private String Code;
	private String Name;
	private String ShortName;
	private int VatID;
	private int UnitID;
	private boolean Active;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	private double NutritionalValue;
	private double EnergyValue;
	private String color;
	private int position;
	private double price;
	private double discount;
	
	private ArrayList<ProductComponent> componentList;
	
	public ArrayList<ProductComponent> getComponentList() {
		return componentList;
	}
	public void setComponentList(ArrayList<ProductComponent> componentList) {
		this.componentList = componentList;
	}

	private long ProductDepartmentID;
	private int KitchenDisplayID;
	private Date PreparationTime;
	
	public long getProductID() {
		return ProductID;
	}
	public void setProductID(long productID) {
		ProductID = productID;
	}
	public int getProductTypeID() {
		return ProductTypeID;
	}
	public void setProductTypeID(int productTypeID) {
		ProductTypeID = productTypeID;
	}
	public int getProductCategoryID() {
		return ProductCategoryID;
	}
	public void setProductCategoryID(int productCategoryID) {
		ProductCategoryID = productCategoryID;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getShortName() {
		return ShortName;
	}
	public void setShortName(String shortName) {
		ShortName = shortName;
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
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
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
	public double getNutritionalValue() {
		return NutritionalValue;
	}
	public void setNutritionalValue(double nutritionalValue) {
		NutritionalValue = nutritionalValue;
	}
	public double getEnergyValue() {
		return EnergyValue;
	}
	public void setEnergyValue(double energyValue) {
		EnergyValue = energyValue;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getDiscount() {
		return discount;
	}
	
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public long getProductDepartmentID() {
		return ProductDepartmentID;
	}
	public void setProductDepartmentID(long productDepartmentID) {
		ProductDepartmentID = productDepartmentID;
	}	
	public int getKitchenDisplayID() {
		return KitchenDisplayID;
	}
	public void setKitchenDisplayID(int kitchenDisplayID) {
		KitchenDisplayID = kitchenDisplayID;
	}
	public Date getPreparationTime() {
		return PreparationTime;
	}
	public void setPreparationTime(Date preparationTime) {
		PreparationTime = preparationTime;
	}
	public static ArrayList<Product> getList(int group, PointOfSale pos, Connection connection){
		ArrayList<Product> list = new ArrayList<Product>();
		try {
			PreparedStatement ps = connection.prepareStatement( "select p.*, "
															  + "		gp.position, "
															  + "		gp.color"
															  + "  from grouppointofsaleproduct gp "
															  + "  left join product p on p.ProductID = gp.ProductID "
															  + " where GroupID = ? "
															  + "   and gp.PointOfSaleID = ?");
			ps.setLong(1, group);
			ps.setLong(2, pos.getPersonID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Product product = new Product();
				product.ProductID = rs.getLong("ProductID");
				product.ProductTypeID = rs.getInt("ProductTypeID");
				product.ProductCategoryID = rs.getInt("ProductCategoryID");
				product.Code = rs.getString("Code");
				product.Name = rs.getString("Name");
				product.ShortName = rs.getString("ShortName");
				product.VatID = rs.getInt("VatID");
				product.UnitID = rs.getInt("UnitID");
				product.Active = rs.getBoolean("Active");
				product.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try{
					product.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e){}
				product.sysUserAddID = rs.getLong("sysUserAddID");
				product.sysUserEditID = rs.getLong("sysUserEditID");
				product.NutritionalValue = rs.getDouble("NutritionalValue");
				product.EnergyValue = rs.getDouble("EnergyValue");
				product.color = rs.getString("color");
				//product.getCurrentPriceAndDiscount(connection);
				try{
					product.position = rs.getInt("position");
				} catch (NullPointerException e){
					product.position = 0;
				}
				list.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static Product getByShortName(String name, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select * "
															  + "  from product "
															  + " where  ShortName = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Product product = new Product();
				product.ProductID = rs.getLong("ProductID");
				product.ProductTypeID = rs.getInt("ProductTypeID");
				product.ProductCategoryID = rs.getInt("ProductCategoryID");
				product.Code = rs.getString("Code");
				product.Name = rs.getString("Name");
				product.ShortName = rs.getString("ShortName");
				product.VatID = rs.getInt("VatID");
				product.UnitID = rs.getInt("UnitID");
				product.Active = rs.getBoolean("Active");
				product.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try{
					product.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch(NullPointerException e){}
				product.sysUserAddID = rs.getLong("sysUserAddID");
				product.sysUserEditID = rs.getLong("sysUserEditID");
				product.NutritionalValue = rs.getDouble("NutritionalValue");
				product.EnergyValue = rs.getDouble("EnergyValue");
				//product.getCurrentPriceAndDiscount(connection);
				return product;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	public static Product getByCode(String name, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select * "
															  + "  from product "
															  + " where  Code = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Product product = new Product();
				product.ProductID = rs.getLong("ProductID");
				product.ProductTypeID = rs.getInt("ProductTypeID");
				product.ProductCategoryID = rs.getInt("ProductCategoryID");
				product.Code = rs.getString("Code");
				product.Name = rs.getString("Name");
				product.ShortName = rs.getString("ShortName");
				product.VatID = rs.getInt("VatID");
				product.UnitID = rs.getInt("UnitID");
				product.Active = rs.getBoolean("Active");
				product.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try{
					product.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch(NullPointerException e){}
				product.sysUserAddID = rs.getLong("sysUserAddID");
				product.sysUserEditID = rs.getLong("sysUserEditID");
				product.NutritionalValue = rs.getDouble("NutritionalValue");
				product.EnergyValue = rs.getDouble("EnergyValue");
				//product.getCurrentPriceAndDiscount(connection);
				return product;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Product getByID(long id, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select * "
															  + "  from product "
															  + " where  ProductID = ?");
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Product product = new Product();
				product.ProductID = rs.getLong("ProductID");
				product.ProductTypeID = rs.getInt("ProductTypeID");
				product.ProductCategoryID = rs.getInt("ProductCategoryID");
				product.Code = rs.getString("Code");
				product.Name = rs.getString("Name");
				product.ShortName = rs.getString("ShortName");
				product.VatID = rs.getInt("VatID");
				product.UnitID = rs.getInt("UnitID");
				product.Active = rs.getBoolean("Active");
				product.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try{
					product.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch(NullPointerException e){}
				product.sysUserAddID = rs.getLong("sysUserAddID");
				product.sysUserEditID = rs.getLong("sysUserEditID");
				product.NutritionalValue = rs.getDouble("NutritionalValue");
				product.EnergyValue = rs.getDouble("EnergyValue");
				//product.getCurrentPriceAndDiscount(connection);
				return product;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void getCurrentPriceAndDiscount(PointOfSale pos, Connection connection){
		price = 0;
		discount = 0;
		try {
			PreparedStatement  cs = connection.prepareStatement("{ CALL current_price_and_discount(?, ?) }");
			cs.setLong(1, ProductID);
			cs.setInt(2, pos.getPointOfSaleID());
			boolean results = cs.execute();
			while (results){
				ResultSet rs = cs.getResultSet();
				while(rs.next()){
					price = rs.getDouble(1);
					discount = rs.getDouble(2);
				}
				results = cs.getMoreResults();
			}
			cs.close();
		} catch (SQLException e) {
			price = 0;
			discount = 0;
			
		}	
	}
	/*
	public ArrayList<Product> getAdditionalList(Connection connection){
		ArrayList<Product> list = new ArrayList<Product>();
		String upit = "select * "
				    + "  from productaditional pa "
				    + "  left join product p on p.ProductID = pa.ChildProductID "
				    + " where pa.ParentProductID = ?";
		
		try {
			PreparedStatement ps = connection.prepareStatement(upit);
			ps.setLong(1, getProductID());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Product product = new Product();
				product.ProductID = rs.getLong("ProductID");
				product.ProductTypeID = rs.getInt("ProductTypeID");
				product.ProductCategoryID = rs.getInt("ProductCategoryID");
				product.Code = rs.getString("Code");
				product.Name = rs.getString("Name");
				product.VatID = rs.getInt("VatID");
				product.UnitID = rs.getInt("UnitID");
				product.Active = rs.getBoolean("Active");
				product.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try{
					product.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e){}
				product.sysUserAddID = rs.getLong("sysUserAddID");
				product.sysUserEditID = rs.getLong("sysUserEditID");
				product.NutritionalValue = rs.getDouble("NutritionalValue");
				product.EnergyValue = rs.getDouble("EnergyValue");
				list.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	*/
	public ArrayList<Description> getDescriptionList(int posId, Connection connection){
		ArrayList<Description> list = new ArrayList<Description>();
		String upit = "select * "
				    + "  from pointofsaleproductdescription psd "
				    + "  left join description d on psd.DescriptionID = d.DescriptionID "
				    + " where d.Active "
				    + "   and psd.PointOfSaleID = ? "
				    + "   and psd.ProductID = ?";
		
		try {
			PreparedStatement ps = connection.prepareStatement(upit);
			ps.setInt(1, posId);
			ps.setLong(2, getProductID());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Description description = new Description();
				description.setDescriptionID(rs.getInt("DescriptionID"));
				description.setName(rs.getString("Name"));
				description.setActive(rs.getBoolean("Active"));
				description.setSysDTCreated(new Date(rs.getDate("sysDTCreated").getTime()));
				try{
					description.setSysDTEdit(new Date(rs.getDate("sysDTEdit").getTime()));
				} catch (NullPointerException e){}
				description.setSysUserAddID(rs.getLong("sysUserAddID"));
				try{
					description.setSysUserEditID(rs.getDate("sysUserEditID").getTime());
				} catch (NullPointerException e){}
				description.setColor(rs.getString("color"));
				list.add(description);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean equals(Product product){
		return (ProductID == product.ProductID) && (price == product.price);
	}
	
	public static ObservableList<Product> getObeservableList(Product excludedProduct,
															 String filter,
															 boolean activeOnly,
															 Department department,
															 Connection connection){
		
		ObservableList<Product> list = FXCollections.observableArrayList();
		
		String query = "select p.*, pd.ProductDepartmentID"
					 + "  from product p"
					 + " left join productdepartment pd on pd.ProductID = p.ProductID";
		if (department != null)
			query += " where pd.DepartmentID = ?"
					+ "  and (p.Code like ? or p.Name like ? or p.ShortName = ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			if (department != null)
				ps.setInt(1, department.getDepartmentID());
			
			ps.setString(2, "%"+filter+"%");
			ps.setString(3, "%"+filter+"%");
			ps.setString(4, "%"+filter+"%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Product product = new Product();
				product.ProductID = rs.getLong("ProductID");
				product.ProductTypeID = rs.getInt("ProductTypeID");
				product.ProductCategoryID = rs.getInt("ProductCategoryID");
				product.Code = rs.getString("Code");
				product.Name = rs.getString("Name");
				product.ShortName = rs.getString("ShortName");
				product.VatID = rs.getInt("VatID");
				product.UnitID = rs.getInt("UnitID");
				product.Active = rs.getBoolean("Active");
				product.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try{
					product.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e){}
				product.sysUserAddID = rs.getLong("sysUserAddID");
				product.sysUserEditID = rs.getLong("sysUserEditID");
				product.NutritionalValue = rs.getDouble("NutritionalValue");
				product.EnergyValue = rs.getDouble("EnergyValue");
				product.ProductDepartmentID = rs.getLong("ProductDepartmentID");
				list.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ObservableList<Product> getObeservableList(PointOfSale pos, Connection connection) {

		ObservableList<Product> list = FXCollections.observableArrayList();

		String query = "select p.*, pd.ProductDepartmentID" + "  from product p"
					 + " left join productdepartment pd on pd.ProductID = p.ProductID"
					 + " where pd.DepartmentID in (select psd.DepartmentID"
					 + "							 from pointofsaledepartment psd"
					 + "						    where psd.PointOfSaleID = ?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, pos.getPointOfSaleID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.ProductID = rs.getLong("ProductID");
				product.ProductTypeID = rs.getInt("ProductTypeID");
				product.ProductCategoryID = rs.getInt("ProductCategoryID");
				product.Code = rs.getString("Code");
				product.Name = rs.getString("Name");
				product.ShortName = rs.getString("ShortName");
				product.VatID = rs.getInt("VatID");
				product.UnitID = rs.getInt("UnitID");
				product.Active = rs.getBoolean("Active");
				product.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try {
					product.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e) {
				}
				product.sysUserAddID = rs.getLong("sysUserAddID");
				product.sysUserEditID = rs.getLong("sysUserEditID");
				product.NutritionalValue = rs.getDouble("NutritionalValue");
				product.EnergyValue = rs.getDouble("EnergyValue");
				product.ProductDepartmentID = rs.getLong("ProductDepartmentID");
				list.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Product getCopy(){
		Product p = new Product();
		p.ProductID = ProductID;
		p.ProductTypeID = ProductTypeID;
		p.ProductCategoryID = ProductCategoryID;
		p.Code = Code;
		p.Name = Name;
		p.ShortName = ShortName;
		p.VatID = VatID;
		p.UnitID = UnitID;
		p.Active = Active;
		p.sysDTCreated = sysDTCreated;
		p.sysDTEdit = sysDTEdit;
		p.sysUserAddID = sysUserAddID;
		p.sysUserEditID = sysUserAddID;
		p.NutritionalValue = NutritionalValue;
		p.EnergyValue = EnergyValue;
		
		return p;
	}
	
	public void update(User user, Connection connection) throws SQLException{
		String query = "update product set "
					 + "	`ProductTypeID` = ?, "
					 + "	`ProductCategoryID` = ?,"
					 + "	`Code` = ?,"
					 + "	`Name` = ?,"
					 + "	`ShortName` = ?,"
					 + "	`VatID` = ?,"
					 + "	`UnitID` = ?,"
					 + "	`Active` = ?,"
					 + "	`sysDTEdit` =  now(),"
					 + "	`sysUserEditID` = ?,"
					 + "	`NutritionalValue` = ?,"
					 + "	`EnergyValue` = ?"
					 + " where `ProductID` = ?";
		//try {
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setInt(1, getProductTypeID());
			ps.setInt(2, getProductCategoryID());
			ps.setString(3, getCode());
			ps.setString(4, getName());
			ps.setString(5, getShortName());
			ps.setInt(6, getVatID());
			ps.setInt(7, getUnitID());
			ps.setBoolean(8, isActive());
			ps.setLong(9, user.getUserID());
			ps.setDouble(10, getNutritionalValue());
			ps.setDouble(11, getEnergyValue());
			ps.setLong(12, getProductID());

			ps.executeUpdate();
	/*
	} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
	}
	
	public long insert(User user, Connection connection){
		String query = "insert into product ( "
					 + "	`ProductTypeID`,"
					 + "	`ProductCategoryID`,	"
					 + "	`Code`,"
					 + "	`Name`,"
					 + "	`ShortName`,"
					 + "	`VatID`,"
					 + "	`UnitID`,"
					 + "	`Active`,"
					 + "	`sysDTCreated`,"
					 + "	`sysUserAddID`,"
					 + "	`NutritionalValue`,"
					 + "	`EnergyValue`)"
					 + " values (?,?,?,?,?,?,?,?,now(),?,?,?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1, getProductTypeID());
			ps.setInt(2, getProductCategoryID());
			ps.setString(3, getCode());
			ps.setString(4, getName());
			ps.setString(5, getShortName());
			ps.setInt(6, getVatID());
			ps.setInt(7, getUnitID());
			ps.setBoolean(8, isActive());
			ps.setLong(9, user.getUserID());
			ps.setDouble(10, getNutritionalValue());
			ps.setDouble(11, getEnergyValue());

			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			setProductID(rs.getLong(1));
			return rs.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static ObservableList<Product> getAddList(Department department, String filter, Connection connection){
		ObservableList<Product> list = FXCollections.observableArrayList();
		
		String query = "select p.* "
					 + "  from product p"
					 + " where (p.Code like ? or p.Name like ? or p.ShortName = ?)";
		if (department != null)
			   query += "   and p.ProductID not in (select pd.ProductID "
					  + "							 from productdepartment pd"
					  + "							where DepartmentID = ?)";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setString(1, "%"+filter+"%");
			ps.setString(2, "%"+filter+"%");
			ps.setString(3, "%"+filter+"%");
			if (department != null)
				ps.setInt(4, department.getDepartmentID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Product product = new Product();
				product.ProductID = rs.getLong("ProductID");
				product.ProductTypeID = rs.getInt("ProductTypeID");
				product.ProductCategoryID = rs.getInt("ProductCategoryID");
				product.Code = rs.getString("Code");
				product.Name = rs.getString("Name");
				product.ShortName = rs.getString("ShortName");
				product.VatID = rs.getInt("VatID");
				product.UnitID = rs.getInt("UnitID");
				product.Active = rs.getBoolean("Active");
				product.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try{
					product.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e){}
				product.sysUserAddID = rs.getLong("sysUserAddID");
				product.sysUserEditID = rs.getLong("sysUserEditID");
				product.NutritionalValue = rs.getDouble("NutritionalValue");
				product.EnergyValue = rs.getDouble("EnergyValue");
				list.add(product);
			}
		} catch (SQLException e) {e.printStackTrace();
		}
		return list;
	}
	
	public void delete(Connection connection) throws SQLException{
		String query = "delete from product where `ProductID` = ?";
		PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
		ps.setLong(1, getProductID());
		ps.executeUpdate();
	}
	
	
	public static ArrayList<Product> getProductListWithComponents(Department department, ScreensController controller){
		ArrayList<ProductDeparmentComponent> pdcList = ProductDeparmentComponent.getList(department.getDepartmentID(), 
																						 controller);
		ObservableList<Product> productlist = Product.getObeservableList(new Product(), "", false, 
																		 department, 
																		 controller.getSettings().getConnection());
		
		ArrayList<Product> list = new ArrayList<>();
		for(Product p : productlist){
			list.add(p);
		}
		
		for (Product p : list){
			p.setComponentList(getComponents(p, pdcList));
		}
		
		return list;
	}
	
	public static ArrayList<Product> getProductListWithComponents(PointOfSale pos, ScreensController controller){
		ArrayList<ProductDeparmentComponent> pdcList = ProductDeparmentComponent.getList(pos,controller);
		ObservableList<Product> productlist = Product.getObeservableList(pos,controller.getSettings().getConnection());
		
		ArrayList<Product> list = new ArrayList<>();
		for(Product p : productlist){
			list.add(p);
		}
		
		for (Product p : list){
			p.setComponentList(getComponents(p, pdcList));
		}
		
		return list;
	}
	
	private static ArrayList<ProductComponent> getComponents(Product product, ArrayList<ProductDeparmentComponent> pdcList){
		ArrayList<ProductComponent> list = new ArrayList<>();
		for(ProductDeparmentComponent pdc : pdcList){
			if (pdc.getArticleProduct().getProductID() == product.getProductID()){
				ProductComponent pc = new ProductComponent();
				pc.setProduct(pdc.getIngredientProduct());
				pc.setNormative(pdc.getNormative());
				if (countChildren(pdc.getIngredientProduct(), pdcList) > 0){
					pc.setComponentList(getChildren(pc.getProduct(), pdcList));
				}
				list.add(pc);
			}
		}
		return list;
	}
	
	private static int countChildren(Product product, ArrayList<ProductDeparmentComponent> pdcList){
		int result = 0;
		for (ProductDeparmentComponent pdc : pdcList){
			if (product.getProductID() == pdc.getArticleProduct().getProductID())
				result++;
		}
		return result;
	}
	
	private static ArrayList<ProductComponent> getChildren(Product product, ArrayList<ProductDeparmentComponent> pdcList){
		ArrayList<ProductComponent> list = new ArrayList<>();
		
		for (ProductDeparmentComponent pdc : pdcList){
			if (product.getProductID() == pdc.getArticleProduct().getProductID()){
				ProductComponent pc = new ProductComponent();
				pc.setProduct(pdc.getIngredientProduct());
				pc.setNormative(pdc.getNormative());
				list.add(pc);
			}
		}		
		return list;
	}
	
	public static ArrayList<Product> getList(Connection connection){
		ArrayList<Product> list = new ArrayList<>();
		
		String query = "select * from product p";
	
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Product product = new Product();
				product.ProductID = rs.getLong("ProductID");
				product.ProductTypeID = rs.getInt("ProductTypeID");
				product.ProductCategoryID = rs.getInt("ProductCategoryID");
				product.Code = rs.getString("Code");
				product.Name = rs.getString("Name");
				product.ShortName = rs.getString("ShortName");
				product.VatID = rs.getInt("VatID");
				product.UnitID = rs.getInt("UnitID");
				product.Active = rs.getBoolean("Active");
				product.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try{
					product.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e){}
				product.sysUserAddID = rs.getLong("sysUserAddID");
				product.sysUserEditID = rs.getLong("sysUserEditID");
				product.NutritionalValue = rs.getDouble("NutritionalValue");
				product.EnergyValue = rs.getDouble("EnergyValue");
				list.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public ArrayList<ProductComponent> getComponents(WorkDay wd, Connection connection){
		ArrayList<ProductComponent> list = new ArrayList<>();
		
		// Prodajni i sirovine nemaju komponente
		if ((ProductTypeID == 1) || (ProductTypeID == 2)){
			return list;
		}
		
		String query = "select p2.*, pc.Normative "
					 + "  from productcomponent pc"
					 + "  inner join productdepartment pd1 on pc.ArticleProductDepartmenID = pd1.ProductDepartmentID "
					 + "								   and pd1.DepartmentID in (select DepartmentID"
					 + "  															  from pointofsaledepartment"
					 + " 															 where PointOfSaleID = ?)"
					 + "  inner join product p1 on pd1.ProductID  = p1.ProductID and pd1.ProductID = ?"
					 + "  left join productdepartment pd2 on pc.IngredientProductDepartmentID = pd2.ProductDepartmentID"
					 + "  left join product p2 on p2.ProductID = pd2.ProductID";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, wd.getPointOfSaleID());
			ps.setLong(2, ProductID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Product product = new Product();
				product.ProductID = rs.getLong("ProductID");
				product.ProductTypeID = rs.getInt("ProductTypeID");
				product.ProductCategoryID = rs.getInt("ProductCategoryID");
				product.Code = rs.getString("Code");
				product.Name = rs.getString("Name");
				product.ShortName = rs.getString("ShortName");
				product.VatID = rs.getInt("VatID");
				product.UnitID = rs.getInt("UnitID");
				product.Active = rs.getBoolean("Active");
				product.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try{
					product.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e){}
				product.sysUserAddID = rs.getLong("sysUserAddID");
				product.sysUserEditID = rs.getLong("sysUserEditID");
				product.NutritionalValue = rs.getDouble("NutritionalValue");
				product.EnergyValue = rs.getDouble("EnergyValue");
				
				// Ako je komponenta prodajni ili sirovina dodaje se u listu sa normativom
				if ((product.getProductTypeID() == 1) || (product.getProductTypeID() == 2)){
					ProductComponent pc = new ProductComponent();
					pc.setProduct(product);
					pc.setNormative(rs.getDouble("Normative"));
					list.add(pc);	
				}
				// Ako je komponenta složena poziva se rekurzivno za komponentu
				if (product.getProductTypeID() == 3){
					ArrayList<ProductComponent> cl = product.getComponents(wd, connection);
					for (ProductComponent pcr : cl){
						pcr.setNormative(pcr.getNormative() * rs.getDouble("Normative"));
						list.add(pcr);
					}
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ObservableList<Product> getObeservableList(rs.petcom.master.dal.Object object, 
															 PointOfSale pos, 
															 Department department, 
															 String filter, 
															 Connection connection) {

		ObservableList<Product> list = FXCollections.observableArrayList();

		String query = "";
		
		if ((department == null) || (department.getDepartmentID() == -1))
			query = "select p.*, "
					+ "		-1 as ProductDepartmentID, "
					+ "		-1 as KitchenDisplayID,"
					+ "     null as PreparationTime "
				  + "  from product p"
				  + " where ((p.Name like ?) or (p.ShortName like ?) or (p.Code like ?) )"
				  + " order by p.Code";
		
		if ((department != null) && (department.getDepartmentID() != -1)){
			query = "select p.*, "
				  + "	(select ProductDepartmentID"
				  + " 	   from productdepartment pd"
				  + "	  where pd.ProductID = p.ProductID"
				  + "       and pd.DepartmentID = ?) as ProductDepartmentID,"

				  + "	(select KitchenDisplayID"
				  + " 	   from productdepartment pd"
				  + "	  where pd.ProductID = p.ProductID"
				  + "       and pd.DepartmentID = ?) as KitchenDisplayID,"

				  + "	(select PreparationTime"
				  + " 	   from productdepartment pd"
				  + "	  where pd.ProductID = p.ProductID"
				  + "       and pd.DepartmentID = ?) as PreparationTime"
			
				  + "  from product p"
				  + " where ((p.Name like ?) or (p.ShortName like ?) or (p.Code like ?) )"
				  + "   and (select ProductDepartmentID"
				  + " 	   	   from productdepartment pd"
				  + "	  	  where pd.ProductID = p.ProductID"
				  + "       	and pd.DepartmentID = ?) is not null"
				  + " order by p.Code";
		}
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			
			if ((department == null) || (department.getDepartmentID() == -1)){
				ps.setString(1, "%" + filter + "%");
				ps.setString(2, "%" + filter + "%");
				ps.setString(3, "%" + filter + "%");
			}
			
			if ((department != null) && (department.getDepartmentID() != -1)){
					ps.setInt(1, department.getDepartmentID());
					ps.setInt(2, department.getDepartmentID());
					ps.setInt(3, department.getDepartmentID());
					ps.setString(4, "%" + filter + "%");
					ps.setString(5, "%" + filter + "%");
					ps.setString(6, "%" + filter + "%");
					ps.setInt(7, department.getDepartmentID());
				}

			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.ProductID = rs.getLong("ProductID");
				product.ProductTypeID = rs.getInt("ProductTypeID");
				product.ProductCategoryID = rs.getInt("ProductCategoryID");
				product.Code = rs.getString("Code");
				product.Name = rs.getString("Name");
				product.ShortName = rs.getString("ShortName");
				product.VatID = rs.getInt("VatID");
				product.UnitID = rs.getInt("UnitID");
				product.Active = rs.getBoolean("Active");
				product.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				try {
					product.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e) {
				}
				product.sysUserAddID = rs.getLong("sysUserAddID");
				product.sysUserEditID = rs.getLong("sysUserEditID");
				product.NutritionalValue = rs.getDouble("NutritionalValue");
				product.EnergyValue = rs.getDouble("EnergyValue");
				product.ProductDepartmentID = rs.getLong("ProductDepartmentID");
				product.KitchenDisplayID = rs.getInt("KitchenDisplayID");
				product.PreparationTime = rs.getTime("PreparationTime");
				list.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static double getCurrentStock(long productId, int departmentId, Connection connection){
		double result = 0;
		
		String query = "select product_initial_stock(?,now(),?) + "
					 + " 	   product_input(?,now(),now(),?) - "
					 + "	   product_output(?,now(),now(),?) as stock";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, productId);
			ps.setInt(2, departmentId);
			ps.setLong(3, productId);
			ps.setInt(4, departmentId);
			ps.setLong(5, productId);
			ps.setInt(6, departmentId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			result = rs.getDouble("stock");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public static ArrayList<DocumentDetails> getNegativeSctockList(PointOfSale pos, Connection connection) throws SQLException{
		ArrayList<DocumentDetails> list = new ArrayList<>();
		
		ArrayList<Department> odeljena = pos.getDepartmentList(connection);
		
		String query = "select "
					 + "	p.Code,p.Name,product_stock(p.ProductID, now(), ?) as stanje"
					 + "  from product p"
					 + " where p.ProductTypeID in (1,2,4)"
					 + "   and product_stock(p.ProductID, now(), ?) < 0";
		
		PreparedStatement ps = connection.prepareStatement(query);
		for (Department d : odeljena){
			ps.setInt(1, d.getDepartmentID());
			ps.setInt(2, d.getDepartmentID());
			ps.executeQuery();
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				DocumentDetails dd = new DocumentDetails();
				dd.setProductCode(rs.getString("Code"));
				dd.setProductName(rs.getString("Name"));
				dd.setQuantity(rs.getDouble("stanje"));
				list.add(dd);
			}
		}
		return list;
	}
	
}
