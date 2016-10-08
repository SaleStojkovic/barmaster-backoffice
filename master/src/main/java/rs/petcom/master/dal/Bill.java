package rs.petcom.master.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;

import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.document.Document;
import rs.petcom.master.dal.document.DocumentDetails;
import rs.petcom.master.dal.person.Person;
import rs.petcom.master.fical.FiscalPrinter;

public class Bill {
	
	private User user;
	private Person person;
	private int PaymetnMethodID;
	private long LoyalityID;
	private String napomena;
	
	private PaymentMethod paymentMethod;
	
	private ArrayList<Round> roundList = new ArrayList<Round>();
	
	public Bill(User user){
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public int getPaymetnMethodID() {
		return PaymetnMethodID;
	}

	public void setPaymetnMethodID(int paymetnMethodID) {
		PaymetnMethodID = paymetnMethodID;
	}

	public long getLoyalityID() {
		return LoyalityID;
	}

	public void setLoyalityID(long loyalityID) {
		LoyalityID = loyalityID;
	}

	public ArrayList<Round> getRoundList() {
		return roundList;
	}
	
	public void setRoundList(ArrayList<Round> roundList) {
		this.roundList = roundList;
	}
	
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	
	public String getNapomena() {
		return napomena;
	}

	public void setNapomena(String napomena) {
		this.napomena = napomena;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
		this.PaymetnMethodID = paymentMethod.getPaymentMethodID();
	}

	public double getSumWithDiscout(){
		double sum = 0;
		for (Round r : roundList){
			sum += r.getSumWithDiscount();
		}
		return sum;
	}
	
	public double getDiscount(){
		double sum = 0;
		for (Round r : roundList){
			sum += r.getDiscount();
		}
		return sum;
	}
	
	public static long writeBill(Bill bill, 
								 PointOfSale pos, 
								 boolean rucnoPisani,
								 String bi,
								 ScreensController controller) throws SQLException{
			String query =  "insert into document( "
						  + "	`DocumentTypeID`,"
						  + "	`DocumentStatusID`,"
						  + "	`PointOfSaleID`,"
						  + "	`Number`, "
						  + "	`FullNumber`, "
						  + "	`AlternativeFullNumber`, "
						  + "	`Year`, "
						  + "	`Date`, "
						  + "	`DeliveryDate`, "
						  + "	`CurrencyDate`, "
						  + "	`PersonID`, "
						  + "	`PaymentMethodID`, "
						  + "	`LoyalityID`, "
						  + "	`Discount`, "
						  + "	`Sum`, "
						  + "	`SumWithDiscount`, "
						  + "	`Paid`, "
						  + "	`FromCashBox`, "
						  + "	`WorkDayID`, "
						  + "	`sysUserAddID`,"
						  + "   `Place`,"
						  + "   Note) "
						  + "values(?,?,?,?,?,?,?,"
						  + " 	    now(),now(),now(), "
						  + "       ?,?,?,?,?,?,?,?,"
						  + "		(select max(WorkDayID) from workday "
						  + "         where PointOfSaleID = ? "
						  + "           and DateTo is null),"
						  + "		?,?,?)";
															  
			PreparedStatement ps = controller.getSettings().getConnection().prepareStatement( query, PreparedStatement.RETURN_GENERATED_KEYS);
			if (!rucnoPisani)
				ps.setInt(1, 3);
			else
				ps.setInt(1, 18);
			ps.setInt(2, 1);
			ps.setLong(3, pos.getPointOfSaleID());
			//ps.setInt(4, SQLHelper.QueryToInt("select max(Number)+1 from document where PointOfSaleID = " + pos.getPointOfSaleID(), 
			// controller.getSettings().getConnection(), 0));
			if (!rucnoPisani)
				ps.setInt(4, Document.getNextNumber(3, pos.getPointOfSaleID(), controller));
			else
				ps.setInt(4, Document.getNextNumber(18, pos.getPointOfSaleID(), controller));
			ps.setString(5, bi);
			
			
			ps.setString(6, FiscalPrinter.getIBFM(controller.getSettings().getFiscalFolder()));
			ps.setInt(7, Year.now().getValue());
			try{
				ps.setLong(8, bill.getPerson().getPersonID());
			} catch (NullPointerException e){
				ps.setNull(8, java.sql.Types.BIGINT);
			}
			ps.setInt(9, bill.getPaymetnMethodID());
			if (bill.LoyalityID != 0)
				ps.setLong(10, bill.LoyalityID);
			else
				ps.setNull(10, java.sql.Types.BIGINT);
			
			ps.setDouble(11, bill.getDiscount());
			ps.setDouble(12, bill.getSumWithDiscout() + bill.getDiscount());
			ps.setDouble(13, bill.getSumWithDiscout());
			ps.setDouble(14, bill.getSumWithDiscout());
			ps.setBoolean(15, false);
			ps.setLong(16, pos.getPointOfSaleID());
			ps.setLong(17, controller.getUser().getUserID());
			ps.setString(18, pos.getAddress(controller).getCity());
			ps.setString(19, bill.getNapomena());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			Long documnetId = rs.getLong(1);	
			
			String query2 =   "insert into documentdetails( "
							+ "	`DocumentID`, "
							+ "	`ProductID`, "
							+ "	`ParentProductID`, "
							+ "	`Quantity`, "
							+ "	`VatID`, "
							+ "	`UnitID`, "
							+ "	`Price`,"
							+ "	`Discount`,"
							+ "	`PriceWithDiscount`,"
							+ "	`sysUserAddID`,"
							+ "	`SaleTableID`,"
							+ "	`OrderNumber`,"
							+ "	`SubTotal`,"
							+ "	`Closed`"
							+ ")"
							+ "	values(?,?,?,?,?,?,?,?,?,?,null,1,false,true)";
			ps = controller.getSettings().getConnection().prepareStatement(query2, PreparedStatement.RETURN_GENERATED_KEYS);
			// Snimanje stavki računa
			for (int i=0;i<bill.getRoundList().size();i++){
				Round round = bill.getRoundList().get(i);
				for (RoundItem roundItem : round.getItemList()){
					ps.setLong(1, documnetId);
					ps.setLong(2, roundItem.getProductDepartment().getProduct().getProductID());
					ps.setNull(3, java.sql.Types.BIGINT);
					ps.setDouble(4, roundItem.getQuantity());
					ps.setInt(5, roundItem.getProductDepartment().getProduct().getVatID());
					ps.setInt(6, roundItem.getProductDepartment().getProduct().getUnitID());
					ps.setDouble(7, roundItem.getProductDepartment().getProduct().getPrice() + 
									roundItem.getProductDepartment().getProduct().getDiscount());
					ps.setDouble(8, roundItem.getProductDepartment().getProduct().getDiscount());
					ps.setDouble(9, roundItem.getProductDepartment().getProduct().getPrice());
					ps.setLong(10, controller.getUser().getUserID());
					ps.executeUpdate();
					
					rs = ps.getGeneratedKeys();
					rs.next();
					Long itemIndex = rs.getLong(1);
					
					// Snimanje dodatnih artikala
					//rs = ps.getGeneratedKeys();
					//rs.next();
					//Long parentId = rs.getLong(1);
					for (RoundItem rio : roundItem.getAdditionalList()){
						DocumentDetails da = new DocumentDetails();
						da.setDocumentID(documnetId);
						da.setProductID(rio.getProductDepartment().getProduct().getProductID());
						da.setParentDocumentDetailsID(itemIndex);
						da.setQuantity(rio.getQuantity());
						da.setVatID(rio.getProductDepartment().getProduct().getVatID());
						da.setUnitID(rio.getProductDepartment().getProduct().getUnitID());
						da.setPriceWithDiscount(rio.getProductDepartment().getProduct().getPrice() * (1-rio.getProductDepartment().getProduct().getDiscount()/100));
						da.setDiscount(rio.getProductDepartment().getProduct().getDiscount());
						da.setPrice(rio.getProductDepartment().getProduct().getPrice());
						da.setSysUserAddID(controller.getUser().getUserID());
						da.insert(controller.getSettings().getConnection());
						/*
						ps.setLong(1, documnetId);
						ps.setLong(2, rio.getProductDepartment().getProduct().getProductID());
						ps.setLong(3, parentId);
						ps.setDouble(4, rio.getQuantity());
						ps.setInt(5, rio.getProductDepartment().getProduct().getVatID());
						ps.setInt(6, rio.getProductDepartment().getProduct().getUnitID());
						ps.setDouble(7, rio.getProductDepartment().getProduct().getPrice() + rio.getProductDepartment().getProduct().getDiscount());
						ps.setDouble(8, rio.getProductDepartment().getProduct().getDiscount());
						ps.setDouble(9, rio.getProductDepartment().getProduct().getPrice());
						ps.setLong(10, bill.user.getUserID());
						ps.executeUpdate();
						
						rs = ps.getGeneratedKeys();
						rs.next();
						itemIndex = rs.getLong(1);
						
						// Snimanje opisa dodatne stavke
						for(RoundItemDescription rid : rio.getDescriptionalList()){
							psd.setLong(1, itemIndex);
							psd.setLong(2, rid.getDescription().getDescriptionID());
							psd.executeUpdate();
						}
						*/
					}
					
					// Snimanje opisa stavke
					String upitD = "insert into documentdetailsdescription (DocumentDetailsID,DescritptionID) values(?,?)";
					PreparedStatement psd = controller.getSettings().getConnection().prepareStatement(upitD);
					for(RoundItemDescription rid : roundItem.getDescriptionalList()){
						psd.setLong(1, itemIndex);
						psd.setLong(2, rid.getDescription().getDescriptionID());
						psd.executeUpdate();
					}
				}
			}
			ps.setLong(1, documnetId);
			
			return documnetId;
	}
	
	public Round getLastRound(){
		return roundList.get(roundList.size()-1);
	}
}
