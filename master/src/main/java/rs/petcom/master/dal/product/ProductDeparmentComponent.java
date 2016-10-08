package rs.petcom.master.dal.product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.PointOfSale;

public class ProductDeparmentComponent {
	long ProductComponentID;
	Product ArticleProduct;
	Product IngredientProduct;
	double Normative;
	int Order;
	
	public ProductDeparmentComponent(){
		this.ProductComponentID = 0;
		this.ArticleProduct= null;
		this.IngredientProduct = null;
		this.Normative= 0;
		this.Order = 0;
	}
	public long getProductComponentID() {
		return ProductComponentID;
	}
	public void setProductComponentID(long productComponentID) {
		ProductComponentID = productComponentID;
	}
	public Product getArticleProduct() {
		return ArticleProduct;
	}
	public void setArticleProduct(Product articleProduct) {
		ArticleProduct = articleProduct;
	}
	public Product getIngredientProduct() {
		return IngredientProduct;
	}
	public void setIngredientProduct(Product ingredientProduct) {
		IngredientProduct = ingredientProduct;
	}
	public double getNormative() {
		return Normative;
	}
	public void setNormative(double normative) {
		Normative = normative;
	}
	public int getOrder() {
		return Order;
	}
	public void setOrder(int order) {
		Order = order;
	}
	
	public static ArrayList<ProductDeparmentComponent> getList(int departmentId, ScreensController controller){
		ArrayList<ProductDeparmentComponent> list = new ArrayList<>();
		String query = "select "
					 + "	pc.ArticleProductDepartmenID,"
					 + "	p.ProductID as articleID,"
					 + "	p.Name as aName,"
					 + "	pc.IngredientProductDepartmentID,"
					 + "	pd2.ProductID as ingredientID,"
					 + "	p2.Name as iName,"
					 + "	pc.Normative, "
					 + "	pc.`Order`"
					 + "  from productcomponent pc"
					 + "  left join productdepartment pd1 on pd1.ProductDepartmentID = pc.ArticleProductDepartmenID"
					 + "  left join product p on pd1.ProductID = p.ProductID"
					 + "  left join productdepartment pd2 on pd2.ProductDepartmentID = pc.IngredientProductDepartmentID"
					 + "  left join product p2 on p2.ProductID = pd2.ProductID"
					 + " where pd1.DepartmentID = ?";
		try {
			PreparedStatement ps = controller.getSettings().getConnection().prepareStatement(query);
			ps.setInt(1, departmentId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ProductDeparmentComponent pdc = new ProductDeparmentComponent();
				Product pA = new Product();
				pA.setProductID(rs.getLong("articleID"));
				pA.setName(rs.getString("aName"));
				Product pI = new Product();
				pI.setProductID(rs.getLong("ingredientID"));
				
				pdc.setArticleProduct(pA);
				pdc.setIngredientProduct(pI);
				
				pdc.getArticleProduct().setName(rs.getString("aName"));
				pdc.getIngredientProduct().setName(rs.getString("iName"));
				
				pdc.setNormative(rs.getDouble("Normative"));
				pdc.setOrder(rs.getInt("Order"));
				list.add(pdc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<ProductDeparmentComponent> getList(PointOfSale pos, ScreensController controller){
		ArrayList<ProductDeparmentComponent> list = new ArrayList<>();
		String query = "select "
					 + "	pc.ArticleProductDepartmenID,"
					 + "	p.ProductID as articleID,"
					 + "	p.Name as aName,"
					 + "    p.ProductTypeID as aProductTypeID,"
					 + "	pc.IngredientProductDepartmentID,"
					 + "	pd2.ProductID as ingredientID,"
					 + "	p2.Name as iName,"
					 + "	pc.Normative, "
					 + "    p2.ProductTypeID as iProductTypeID,"
					 + "	pc.`Order`"
					 + "  from productcomponent pc"
					 + "  left join productdepartment pd1 on pd1.ProductDepartmentID = pc.ArticleProductDepartmenID"
					 + "  left join product p on pd1.ProductID = p.ProductID"
					 + "  left join productdepartment pd2 on pd2.ProductDepartmentID = pc.IngredientProductDepartmentID"
					 + "  left join product p2 on p2.ProductID = pd2.ProductID"
					 + " where pd1.DepartmentID in (select psd.DepartmentID"
					 + "							  from pointofsaledepartment psd"
					 + "							 where psd.PointOfSaleID = ?)";
		try {
			PreparedStatement ps = controller.getSettings().getConnection().prepareStatement(query);
			ps.setInt(1, pos.getPointOfSaleID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				ProductDeparmentComponent pdc = new ProductDeparmentComponent();
				Product pA = new Product();
				pA.setProductID(rs.getLong("articleID"));
				pA.setName(rs.getString("aName"));
				pA.setProductTypeID(rs.getInt("aProductTypeID"));
				Product pI = new Product();
				pI.setProductID(rs.getLong("ingredientID"));
				
				pdc.setArticleProduct(pA);
				pdc.setIngredientProduct(pI);
				
				pdc.getArticleProduct().setName(rs.getString("aName"));
				pdc.getIngredientProduct().setName(rs.getString("iName"));
				pdc.getIngredientProduct().setProductTypeID(rs.getInt("iProductTypeID"));
				
				pdc.setNormative(rs.getDouble("Normative"));
				pdc.setOrder(rs.getInt("Order"));
				list.add(pdc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<ProductDeparmentComponent> getFlatList(ArrayList<Product> componentList){
		ArrayList<ProductDeparmentComponent> finalComponentList = new ArrayList<>();
		
		for (Product p : componentList){
			for (ProductComponent pc : p.getComponentList()){
				boolean dodat = false;
				
				try{
					for (ProductComponent pc1 : pc.getComponentList()){
						try{
							for (ProductComponent pc2 : pc1.getComponentList()){
								ProductDeparmentComponent pdc = new ProductDeparmentComponent();
								pdc.setArticleProduct(p);
								pdc.setIngredientProduct(pc2.getProduct());
								pdc.setNormative(pc1.getNormative() * pc2.getNormative());
								finalComponentList.add(pdc);
								dodat = true;
							}
						} catch (NullPointerException e){
							ProductDeparmentComponent pdc = new ProductDeparmentComponent();
							pdc.setArticleProduct(p);
							pdc.setIngredientProduct(pc1.getProduct());
							pdc.setNormative(pc1.getNormative() * pc.getNormative());
							finalComponentList.add(pdc);
							dodat = true;
						}
					}
				} catch (NullPointerException e){}
				
				
				if (!dodat){
					ProductDeparmentComponent pdc = new ProductDeparmentComponent();
					pdc.setArticleProduct(p);
					pdc.setIngredientProduct(pc.getProduct());
					
					pdc.setNormative(pc.getNormative());
					finalComponentList.add(pdc);
				}
				
			}
		}
		return finalComponentList;
	}
}
