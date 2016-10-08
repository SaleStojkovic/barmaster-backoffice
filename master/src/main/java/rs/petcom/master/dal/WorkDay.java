package rs.petcom.master.dal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.document.Document;
import rs.petcom.master.dal.document.DocumentDetails;
import rs.petcom.master.dal.product.Product;
import rs.petcom.master.dal.product.ProductComponent;
import rs.petcom.master.fical.FiscalPrinter;

public class WorkDay {
	private long WorkDayID;
	private int PointOfSaleID;
	private Date date;
	private Date DateFrom;
	private Date DateTo;
	private long sysUserAddID;
	private long sysUserEditID;
	
	public long getWorkDayID() {
		return WorkDayID;
	}
	public void setWorkDayID(long workDayID) {
		WorkDayID = workDayID;
	}
	public int getPointOfSaleID() {
		return PointOfSaleID;
	}
	public void setPointOfSaleID(int pointOfSaleID) {
		PointOfSaleID = pointOfSaleID;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDateFrom() {
		return DateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		DateFrom = dateFrom;
	}
	public Date getDateTo() {
		return DateTo;
	}
	public void setDateTo(Date dateTo) {
		DateTo = dateTo;
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
	
	public static boolean isActive(long workDayID, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( " select wd.DateTo "
															  + " from workday wd "
															  + " where wd.WorkDayID=?");
			ps.setLong(1, workDayID);
			ResultSet rs = ps.executeQuery();
			rs.next();
			WorkDay wd = new WorkDay();
			
			wd.DateTo = rs.getDate("DateTo");
			
			if(wd.DateTo==null){
				return true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static WorkDay getFromDb(PointOfSale pos, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select * "
															  + "  from workday "
															  + "  where PointOfSaleID = ?"
															  + " order by DateFrom desc "
															  + " limit 1");
			ps.setInt(1, pos.getPointOfSaleID());
			ResultSet rs = ps.executeQuery();
			rs.next();
			WorkDay wd = new WorkDay();
			
			wd.WorkDayID = rs.getLong("WorkDayID");
			wd.PointOfSaleID = rs.getInt("PointOfSaleID");
			wd.date = new Date(rs.getDate("Date").getTime());
			wd.DateFrom = new Date(rs.getTimestamp("DateFrom").getTime());
			try{
				wd.DateTo = new Date(rs.getTimestamp("DateTo").getTime());
			}catch(NullPointerException e){
				wd.DateTo = null;
			}
			wd.sysUserAddID = rs.getLong("sysUserAddID");
			try{
				wd.sysUserEditID = rs.getLong("sysUserEditID");
			}catch (NullPointerException e){}
			
			return wd;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static WorkDay getFromDb(long workdayId, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select * "
															  + "  from workday "
															  + "  where WorkDayID = ?");
			ps.setLong(1, workdayId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			WorkDay wd = new WorkDay();
			
			wd.WorkDayID = rs.getLong("WorkDayID");
			wd.PointOfSaleID = rs.getInt("PointOfSaleID");
			wd.date = new Date(rs.getDate("Date").getTime());
			wd.DateFrom = new Date(rs.getTimestamp("DateFrom").getTime());
			try{
				wd.DateTo = new Date(rs.getTimestamp("DateTo").getTime());
			}catch(NullPointerException e){
				wd.DateTo = null;
			}
			wd.sysUserAddID = rs.getLong("sysUserAddID");
			try{
				wd.sysUserEditID = rs.getLong("sysUserEditID");
			}catch (NullPointerException e){}
			
			return wd;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public WorkDay getPrevious(Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select * "
															  + "  from workday "
															  + "  where PointOfSaleID = ?"
															  + "    and Date < ?"
															  + " order by DateFrom desc "
															  + " limit 1");
			ps.setInt(1, this.PointOfSaleID);
			ps.setDate(2, new java.sql.Date(this.date.getTime()));
			ResultSet rs = ps.executeQuery();
			rs.next();
			WorkDay wd = new WorkDay();
			
			wd.WorkDayID = rs.getLong("WorkDayID");
			wd.PointOfSaleID = rs.getInt("PointOfSaleID");
			wd.date = new Date(rs.getDate("Date").getTime());
			wd.DateFrom = new Date(rs.getTimestamp("DateFrom").getTime());
			try{
				wd.DateTo = new Date(rs.getTimestamp("DateTo").getTime());
			}catch(NullPointerException e){
				wd.DateTo = null;
			}
			wd.sysUserAddID = rs.getLong("sysUserAddID");
			try{
				wd.sysUserEditID = rs.getLong("sysUserEditID");
			}catch (NullPointerException e){}
			
			return wd;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static void openWorkDay(int posId, long userId, Connection connection){
		
		PointOfSale pos = new PointOfSale();
		pos.setPointOfSaleID(posId);
		WorkDay wd = WorkDay.getFromDb(pos, connection);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if (wd != null && sdf.format(wd.getDate()).equals(sdf.format(new Date()))){
			try {
				wd.reopen(userId, posId, connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				String query = "update workday "
							 + "   set DateTo = now() "
							 + " where DateTo is null "
							 + "   and PointOfSaleId = ?";
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setInt(1, posId);
				ps.executeUpdate();
				
				ps = connection.prepareStatement( "insert into workday(PointOfSaleId,Date,DateFrom,sysUserAddID) "
																  + " values(?,now(),now(),?)");
				ps.setInt(1, posId);
				ps.setLong(2, userId);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeWorkDay(User user, PointOfSale pos, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "update workday set DateTo = now(), sysUserEditID = ?  "
															  + " where PointOfSaleId = ? and DateTo is null ");
			ps.setLong(1, user.getUserID());
			ps.setInt(2, PointOfSaleID);		
			ps.executeUpdate();
			Log.writeLog(user, "PointOfSaleID: " + pos.getPointOfSaleID() + " - Zatvaranje dana", connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void reopen(long userId, int posId, Connection connection) throws SQLException{
		PreparedStatement ps = connection.prepareStatement( "update workday set DateTo = null, sysUserEditID = ?  "
														  + " where PointOfSaleId = ? "
														  + "   and WorkDayID = ?");
		ps.setLong(1, userId);
		ps.setInt(2, posId);		
		ps.setLong(3, WorkDayID);
		ps.executeUpdate();
	}
	
	public static Timestamp start(LocalDate date, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select min(DateFrom) from workday where Date = ?");
			ps.setDate(1, java.sql.Date.valueOf(date));
			ResultSet rs = ps.executeQuery();
			int rowCount = 0;
			while (rs.next()){
				if(rs.getTimestamp(1) != null){
					rowCount++;
					return rs.getTimestamp(1);
				}
			}
			if (rowCount == 0){
				ps = connection.prepareStatement("select min(DateFrom) from workday where Date > ?");
				ps.setDate(1, java.sql.Date.valueOf(date));
				rs = ps.executeQuery();
				rs.next();
				return rs.getTimestamp(1);
			}
			return null;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static Timestamp end(LocalDate date, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select max(coalesce(DateTo,now())) from workday where Date = ?");
			ps.setDate(1, java.sql.Date.valueOf(date));
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getTimestamp(1);
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static Date getFirstStart(Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select min(DateFrom) from workday");
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getTimestamp(1);
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static LocalDateTime getFirstStart(int departmentId, LocalDate date, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select workday_start(?, ?)");
			ps.setInt(1, departmentId);
			ps.setDate(2, new java.sql.Date(Settings.fromLocalDate(date).getTime()));
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getTimestamp(1).toLocalDateTime();
		} catch (SQLException e) {
			return null;
		}
	}
	
	
	public ArrayList<Long> getWdRecapDocumentID(Connection connection){
		ArrayList<Long> list = new ArrayList<>();
		String query = "select d.DocumentID from document d "
					 + " inner join workday w on w.WorkDayID = d.WorkDayID"
					 + "					and w.Date = ? and w.PointOfSaleID = ?"
					 + " where d.DocumentTypeID = 17";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ps.setInt(2, PointOfSaleID);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				list.add(rs.getLong("DocumentID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void saveRecap(ScreensController controller) throws SQLException{
		for (Long docId : getWdRecapDocumentID(controller.getSettings().getConnection())){
			try {
				Document.delete(docId, controller.getSettings().getConnection());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
				
		ArrayList<ProductComponent> mainComponentList = new ArrayList<>();
		
		ArrayList<DocumentDetails> list = DocumentDetails.getSoldItems(this, controller.getSettings().getConnection());
		for( DocumentDetails dd : list){
			Product p = Product.getByID(dd.getProductID(), controller.getSettings().getConnection());
			if (p.getProductTypeID() == 1){
				
				ProductComponent mpc = new ProductComponent();
				mpc.setProduct(p);
				mpc.setNormative(dd.getQuantity());
				
				boolean dodato = false;
				for (ProductComponent mpcl : mainComponentList){
					if (mpcl.getProduct().getProductID() == mpc.getProduct().getProductID()){
						mpcl.setNormative(mpcl.getNormative() + mpc.getNormative());
						dodato = true;
					}
				}
				if (!dodato){
					mainComponentList.add(mpc);
				}
				
			}
			ArrayList<ProductComponent> pclist = p.getComponents(this,controller.getSettings().getConnection());
			for (ProductComponent pc : pclist){
				//if (pc.getProduct().getProductID() == 319){
					//System.out.println(dd.getDocumentID() + " " + dd.getProductID() + " " +
					//				   dd.getProductName() + " - "+ dd.getQuantity());
				//}
				ProductComponent mpc = new ProductComponent();
				mpc.setProduct(pc.getProduct());
				mpc.setNormative(dd.getQuantity() * pc.getNormative());
				
				boolean dodato = false;
				for (ProductComponent mpcl : mainComponentList){
					if (mpcl.getProduct().getProductID() == mpc.getProduct().getProductID()){
						mpcl.setNormative(mpcl.getNormative() + mpc.getNormative());
						dodato = true;
					}
				}
				if (!dodato){
					mainComponentList.add(mpc);
				}
			}
			
		}
		
		Document d = new Document();
		d.setDate(new Date());
		d.setWorkDayID(WorkDayID);
		d.setDocumentTypeID(17);
		d.setYear(Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())));
		d.setNumber(Document.getNextNumber(17, getPointOfSaleID(), controller));
		d.setSysUserAddID(controller.getUser().getUserID());
		d.setDocumentStatusID(1);
		d.setPointOfSaleID(getPointOfSaleID());
		
		long documentId = d.insert(controller.getSettings().getConnection());

		for (ProductComponent mpcl : mainComponentList){
			DocumentDetails dd = new DocumentDetails();
			dd.setDocumentID(documentId);
			dd.setProductID(mpcl.getProduct().getProductID());
			dd.setQuantity( mpcl.getNormative());
			dd.setSysUserAddID(controller.getUser().getUserID());
			dd.setUnitID(mpcl.getProduct().getUnitID());
			dd.setVatID(mpcl.getProduct().getVatID());
			dd.insert(controller.getSettings().getConnection());
		}
	}
	
	public static ArrayList<WorkDay> getList(LocalDate dateFrom, LocalDate dateTo, int objectId, Connection connection){
		ArrayList<WorkDay> list = new ArrayList<>();
		String query = "select * "
					 + "  from workday w"
					 + "  left join pointofsale p on p.PointOfSaleID = w.PointOfSaleID"
					 + " where w.Date >= ? and w.Date <= ? and p.ObjectID = ? ";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setDate(1, new java.sql.Date( Settings.fromLocalDate(dateFrom).getTime()));
			ps.setDate(2, new java.sql.Date( Settings.fromLocalDate(dateTo).getTime()));
			ps.setInt(3, objectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				WorkDay wd = new WorkDay();
				wd.WorkDayID = rs.getLong("WorkDayID");
				wd.PointOfSaleID = rs.getInt("PointOfSaleID");
				wd.date = new Date(rs.getDate("Date").getTime());
				wd.DateFrom = new Date(rs.getTimestamp("DateFrom").getTime());
				try{
					wd.DateTo = new Date(rs.getTimestamp("DateTo").getTime());
				}catch(NullPointerException e){
					wd.DateTo = null;
				}
				wd.sysUserAddID = rs.getLong("sysUserAddID");
				try{
					wd.sysUserEditID = rs.getLong("sysUserEditID");
				}catch (NullPointerException e){}
				list.add(wd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static HashMap<String, java.lang.Object> allowClose(ScreensController controller) throws IOException{
		
		WorkDay w = WorkDay.getFromDb(controller.getPointOfSale(), controller.getSettings().getConnection());
		
		String data = FiscalPrinter.getSummaryData(controller.getSettings().getFiscalFolder()).trim();
		System.out.println(data);
		String[] datas = data.split("\n");
		datas = datas[0].split(";");
		
		double cash = ((double)Integer.parseInt(datas[0]))/100;
		double check = ((double)Integer.parseInt(datas[1]))/100;
		double card = ((double)Integer.parseInt(datas[2].trim()))/100;
		
		
		double dbSum = 0;
		for (Document d : Document.getObeservableList(3, 
				   									  controller.getObject(),
				   									  controller.getPointOfSale(),
				   									  w.getDate(),
				   									  true,
				   									  true,
				   									  controller.getSettings().getConnection())){
			dbSum += d.getSumWithDiscount();
		}
		
		HashMap<String, java.lang.Object> hm = new HashMap<String, java.lang.Object>();
		String text = "Gotovina:\t " + cash + "\n"
					+ "Kartica:\t " + card + "\n"
					+ "Ček:\t\t" + check + "\n"
					+ "Ukupno: \t" + (cash+card+check); 
				
		hm.put("text", text);
		if (dbSum == (cash + card + check))
			hm.put("allowed", true);
		else
			hm.put("allowed", false);
		return hm;
	}

}
