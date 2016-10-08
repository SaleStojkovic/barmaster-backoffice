package rs.petcom.fastfood;

import java.sql.SQLException;

import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.User;
import rs.petcom.master.dal.WorkDay;

public class Test {

	public static void main(String[] args) {
		
		ScreensController controller = new ScreensController(ScreensController.FASTFOOD);
		
		WorkDay wd = WorkDay.getFromDb(controller.getPointOfSale(), controller.getSettings().getConnection());
		//wd.closeWorkDay(controller.getUser().getUserID(), controller.getSettings().getConnection());
		WorkDay wdp = wd.getPrevious(controller.getSettings().getConnection());
		WorkDay wdp1 = wdp.getPrevious(controller.getSettings().getConnection());
		User user = new User();
		user.setUserID(1);
		controller.setUser(user);
		try {
			wdp1.saveRecap(controller);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		Department d = new Department();
		d.setDepartmentID(1);
		ArrayList<Product> componentList = Product.getProductListWithComponents(d, screensController.getSettings().getConnection());
	
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
							pdc.setNormative(pc.getNormative() * pc1.getNormative());
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
		
		System.out.println("Broj u finalnoj listi: " + finalComponentList.size());
		for(ProductDeparmentComponent pdc : finalComponentList){
			if (pdc.getArticleProduct().getProductID() == 7)
			System.out.println(pdc.getArticleProduct().getProductID() + " >>> " + 
							   pdc.getIngredientProduct().getProductID() + " (" + pdc.getNormative()+")");
		}
		*/
	}

}
