package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PriceList {
	private Long PriceListID;
	private int ObjectID;
	private int PointOfSaleID;
	private String Title;
	private String Comment;
	private Date DateFrom;
	private Date DateTo;
	private byte DomFrom;
	private byte DomTo;
	private boolean Monday;
	private boolean Tuesday;
	private boolean Wednesday;
	private boolean Thursday;
	private boolean Friday;
	private boolean Saturday;
	private boolean Sunday;
	private Date TimeFrom;
	private Date TimeTo;
	private byte Priority;
	private boolean Active;
	private int PriceListTypeID;
	
	public Long getPriceListID() {
		return PriceListID;
	}
	public void setPriceListID(Long priceListID) {
		PriceListID = priceListID;
	}
	public int getObjectID() {
		return ObjectID;
	}
	public void setObjectID(int objectID) {
		ObjectID = objectID;
	}
	public int getPointOfSaleID() {
		return PointOfSaleID;
	}
	public void setPointOfSaleID(int pointOfSaleID) {
		PointOfSaleID = pointOfSaleID;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getComment() {
		return Comment;
	}
	public void setComment(String comment) {
		Comment = comment;
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
	public byte getDomFrom() {
		return DomFrom;
	}
	public void setDomFrom(byte domFrom) {
		DomFrom = domFrom;
	}
	public byte getDomTo() {
		return DomTo;
	}
	public void setDomTo(byte domTo) {
		DomTo = domTo;
	}
	public boolean isMonday() {
		return Monday;
	}
	public void setMonday(boolean monday) {
		Monday = monday;
	}
	public boolean isTuesday() {
		return Tuesday;
	}
	public void setTuesday(boolean tuesday) {
		Tuesday = tuesday;
	}
	public boolean isWednesday() {
		return Wednesday;
	}
	public void setWednesday(boolean wednesday) {
		Wednesday = wednesday;
	}
	public boolean isThursday() {
		return Thursday;
	}
	public void setThursday(boolean thursday) {
		Thursday = thursday;
	}
	public boolean isFriday() {
		return Friday;
	}
	public void setFriday(boolean friday) {
		Friday = friday;
	}
	public boolean isSaturday() {
		return Saturday;
	}
	public void setSaturday(boolean saturday) {
		Saturday = saturday;
	}
	public boolean isSunday() {
		return Sunday;
	}
	public void setSunday(boolean sunday) {
		Sunday = sunday;
	}
	public Date getTimeFrom() {
		return TimeFrom;
	}
	public void setTimeFrom(Date timeFrom) {
		TimeFrom = timeFrom;
	}
	public Date getTimeTo() {
		return TimeTo;
	}
	public void setTimeTo(Date timeTo) {
		TimeTo = timeTo;
	}
	public byte getPriority() {
		return Priority;
	}
	public void setPriority(byte priority) {
		Priority = priority;
	}
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	public int getPriceListTypeID() {
		return PriceListTypeID;
	}
	public void setPriceListTypeID(int priceListTypeID) {
		PriceListTypeID = priceListTypeID;
	}
	
	public long insert(Connection connection){
		long result = -1;
		String query = "insert into pricelist (	ObjectID,PointOfSaleID,Title,Comment,"
					 + "						DateFrom,DateTo,DomFrom,DomTo,Monday,Tuesday,"
					 + "						Wednesday,Thursday,Friday,Saturday,Sunday,"
					 + "						TimeFrom,TimeTo,Priority,Active,PriceListTypeID)"
					 + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			if (ObjectID > 0) ps.setInt(1,ObjectID);
			else ps.setNull(1, java.sql.Types.INTEGER);
			
			if (PointOfSaleID > 0) ps.setInt(2,PointOfSaleID);
			else ps.setNull(2, java.sql.Types.INTEGER);
			
			ps.setString(3,Title);
			ps.setString(4,Comment);
			ps.setTimestamp(5, new java.sql.Timestamp(DateFrom.getTime()));
			try{
				ps.setTimestamp(6, new java.sql.Timestamp(DateTo.getTime()));
			} catch (NullPointerException e){
				ps.setNull(6, java.sql.Types.DATE);
			}
			ps.setByte(7,DomFrom);
			ps.setByte(8,DomTo);
			ps.setBoolean(9,Monday);
			ps.setBoolean(10,Tuesday);
			ps.setBoolean(11,Wednesday);
			ps.setBoolean(12,Thursday);
			ps.setBoolean(13,Friday);
			ps.setBoolean(14,Saturday);
			ps.setBoolean(15,Sunday);
			
			if (TimeFrom != null) ps.setTime(16,new java.sql.Time(TimeFrom.getTime()));
			else ps.setNull(16, java.sql.Types.TIME);
			
			if (TimeFrom != null) ps.setTime(17,new java.sql.Time(TimeTo.getTime()));
			else ps.setNull(17, java.sql.Types.TIME);
			
			ps.setByte(18,Priority);
			ps.setBoolean(19,Active);
			ps.setInt(20,PriceListTypeID);
			
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			result = rs.getLong(1);
			PriceListID = result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void update(Connection connection){
		String query = "update pricelist set "
					 + "	ObjectID = ?,"
					 + "	PointOfSaleID = ?,"
					 + "	Title = ?,"
					 + "	Comment = ?,"
					 + "	DateFrom = ?,"
					 + "	DateTo = ?,"
					 + "	DomFrom = ?,"
					 + "	DomTo = ?,"
					 + "	Monday = ?,"
					 + "	Tuesday = ?,"
					 + "	Wednesday = ?,"
					 + "	Thursday = ?,"
					 + "	Friday = ?,"
					 + "	Saturday = ?,"
					 + "	Sunday = ?,"
					 + "	TimeFrom = ?,"
					 + "	TimeTo = ?,"
					 + "	Priority = ?,"
					 + "	Active = ?,"
					 + "	PriceListTypeID = ? "
					 + "where PriceListID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			
			if (ObjectID > 0) ps.setInt(1,ObjectID);
			else ps.setNull(1, java.sql.Types.INTEGER);
			if (PointOfSaleID > 0) ps.setInt(2,PointOfSaleID);
			else ps.setNull(2, java.sql.Types.INTEGER);
			ps.setString(3,Title);
			ps.setString(4,Comment);
			ps.setTimestamp(5, new java.sql.Timestamp(DateFrom.getTime()));
			if (DateTo != null) ps.setTimestamp(6, new java.sql.Timestamp(DateTo.getTime()));
				else ps.setNull(6, java.sql.Types.TIMESTAMP);
			if (DomFrom > 0) ps.setByte(7,DomFrom);
				else ps.setNull(7, java.sql.Types.DATE);
			if (DomTo > 0) ps.setByte(8,DomTo);
				else ps.setNull(8, java.sql.Types.DATE);
			ps.setBoolean(9,Monday);
			ps.setBoolean(10,Tuesday);
			ps.setBoolean(11,Wednesday);
			ps.setBoolean(12,Thursday);
			ps.setBoolean(13,Friday);
			ps.setBoolean(14,Saturday);
			ps.setBoolean(15,Sunday);
			if (TimeFrom != null)
				ps.setTime(16,new java.sql.Time(TimeFrom.getTime()));
			else 
				ps.setNull(16, java.sql.Types.TIME);
			if (TimeTo != null)
				ps.setTime(17,new java.sql.Time(TimeTo.getTime()));
			else 
				ps.setNull(17, java.sql.Types.TIME);
			ps.setByte(18,Priority);
			ps.setBoolean(19,Active);
			ps.setInt(20,PriceListTypeID);
			ps.setLong(21, PriceListID);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(Connection connection) throws SQLException{
		String query = "delete from pricelist where PriceListID = ?";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setLong(1, PriceListID);
		ps.executeUpdate();
	}
		
	public static ArrayList<PriceList> getList(rs.petcom.master.dal.Object object,
											   Connection connection){
		ArrayList<PriceList> list = new ArrayList<PriceList>();
		String upit = "select * "
					+ "  from pricelist"
					+ " where  ObjectID is null"
					+ "    or	((ObjectID is not null) and (ObjectID = ?)) "
					+ " order by priority desc ";
		
		
		try {
			PreparedStatement ps = connection.prepareStatement(upit);
			ps.setInt(1, object.getObjectID());
			/*
			ps.setInt(2, object.getObjectID());
			if (pos != null){
				ps.setInt(3, pos.getPointOfSaleID());
				ps.setInt(4, pos.getPointOfSaleID());
				ps.setInt(5, pos.getPointOfSaleID());
			}
			else{
				ps.setInt(3, -1);
				ps.setInt(4, -1);
				ps.setInt(5, -1);
			}
			*/
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				PriceList pl = new PriceList();
				
				pl.PriceListID = rs.getLong("PriceListID");
				pl.ObjectID = rs.getInt("ObjectID");
				pl.PointOfSaleID = rs.getInt("PointOfSaleID");
				pl.Title = rs.getString("Title");
				pl.Comment = rs.getString("Comment");
				pl.DateFrom = new Date(rs.getTimestamp("DateFrom").getTime());
				try{
					pl.DateTo = new Date(rs.getTimestamp("DateTo").getTime());
				} catch (NullPointerException e){}
				pl.DomFrom = rs.getByte("DomFrom");
				pl.DomTo = rs.getByte("DomTo");
				pl.Monday = rs.getBoolean("Monday");
				pl.Tuesday = rs.getBoolean("Tuesday");
				pl.Wednesday = rs.getBoolean("Wednesday");
				pl.Thursday = rs.getBoolean("Thursday");
				pl.Friday = rs.getBoolean("Friday");
				pl.Saturday = rs.getBoolean("Saturday");
				pl.Sunday = rs.getBoolean("Saturday");
				pl.TimeFrom = rs.getTime("TimeFrom");
				pl.TimeTo = rs.getTime("TimeTo");
				pl.Priority = rs.getByte("Priority");
				pl.Active = rs.getBoolean("Active");
				pl.PriceListTypeID = rs.getInt("PriceListTypeID");
				
				list.add(pl);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ObservableList<PriceList> getObeservableList(rs.petcom.master.dal.Object object, 
															   Connection connection){
		ObservableList<PriceList> list = FXCollections.observableArrayList();
		for(PriceList pl : PriceList.getList(object, connection)){
			list.add(pl);
		}
		return list;
	}
}
