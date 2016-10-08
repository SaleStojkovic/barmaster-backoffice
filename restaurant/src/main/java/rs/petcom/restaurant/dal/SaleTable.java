package rs.petcom.restaurant.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import rs.petcom.master.dal.User;

public class SaleTable {
	private int SaleTableID;
	private String Name;
	private int Number;
	private int SalePlaceID;
	private int Places;
	private boolean Active;
	private double OffsetX;
	private double OffsetY;
	private double Width;
	private double Height;
	private int Shape;
	private User user;
	private double TableSum;
	public int getSaleTableID() {
		return SaleTableID;
	}
	public void setSaleTableID(int saleTableID) {
		SaleTableID = saleTableID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getNumber() {
		return Number;
	}
	public void setNumber(int number) {
		Number = number;
	}
	public int getSalePlaceID() {
		return SalePlaceID;
	}
	public void setSalePlaceID(int salePlaceID) {
		SalePlaceID = salePlaceID;
	}
	public int getPlaces() {
		return Places;
	}
	public void setPlaces(int places) {
		Places = places;
	}
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	public double getOffsetX() {
		return OffsetX;
	}
	public void setOffsetX(double offsetX) {
		OffsetX = offsetX;
	}
	public double getOffsetY() {
		return OffsetY;
	}
	public void setOffsetY(double offsetY) {
		OffsetY = offsetY;
	}
	public double getWidth() {
		return Width;
	}
	public void setWidth(double width) {
		Width = width;
	}
	public double getHeight() {
		return Height;
	}
	public void setHeight(double height) {
		Height = height;
	}
	public int getShape() {
		return Shape;
	}
	public void setShape(int shape) {
		Shape = shape;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public double getTableSum() {
		return TableSum;
	}
	public void setTableSum(double tableSum) {
		TableSum = tableSum;
	}
	public static ArrayList<SaleTable> getList(SalePlace sp, Connection connection) throws SQLException{
		ArrayList<SaleTable> list = new ArrayList<>();
		
		String query = "select * from saletable st where st.SalePlaceID = ?";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setInt(1, sp.getSalePlaceID());
		ResultSet rs = ps.executeQuery();
		while (rs.next()){
			SaleTable st = new SaleTable();
			st.SaleTableID = rs.getInt("SaleTableID");
			st.Name = rs.getString("Name");
			st.Number = rs.getInt("Number");
			st.SalePlaceID = rs.getInt("SalePlaceID");
			st.Places = rs.getInt("Places");
			st.Active = rs.getBoolean("Active");
			st.OffsetX = rs.getDouble("OffsetX");
			st.OffsetY = rs.getDouble("OffsetY");
			st.Width = rs.getDouble("Width");
			st.Height = rs.getDouble("Height");
			st.Shape = rs.getInt("Shape");
			
			st.user = User.getFromDb("123123", connection);
			st.TableSum = 132456.89;
			
			list.add(st);
		}
		return list;
	}
 }
