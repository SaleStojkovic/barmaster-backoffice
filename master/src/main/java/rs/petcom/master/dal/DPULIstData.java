package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DPULIstData {
	private int DPUListDataID;
	private int DPUListID;
	private int Type;  // 0 - Dnevni izveštaji, 1 - Nalozi za ispravku , 2 - Fiskalni računi
	private String Number;
	private double Sum;
	private double Cash;
	private double Card;
	private double NonFiscal;
	public int getDPUListDataID() {
		return DPUListDataID;
	}
	public void setDPUListDataID(int dPUListDataID) {
		DPUListDataID = dPUListDataID;
	}
	public int getDPUListID() {
		return DPUListID;
	}
	public void setDPUListID(int DPUListID) {
		this.DPUListID = DPUListID;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public String getNumber() {
		return Number;
	}
	public void setNumber(String number) {
		Number = number;
	}
	public double getSum() {
		return Sum;
	}
	public void setSum(double sum) {
		Sum = sum;
	}
	public double getCash() {
		return Cash;
	}
	public void setCash(double cash) {
		Cash = cash;
	}
	public double getCard() {
		return Card;
	}
	public void setCard(double card) {
		Card = card;
	}
	public double getNonFiscal() {
		return NonFiscal;
	}
	public void setNonFiscal(double nonFiscal) {
		NonFiscal = nonFiscal;
	}
	
	public int insert(Connection connection){
		int result = -1;
		String query = "insert into dpulistdata(DPUListID,Number,Type,Cash,Card,NonFiscal) values(?,?,?,?,?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, DPUListID);
			ps.setString(2, Number);
			ps.setInt(3, Type);
			ps.setDouble(4, Cash);
			ps.setDouble(5, Card);
			ps.setDouble(6, NonFiscal);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			DPUListDataID = rs.getInt(1);
			return DPUListDataID;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public void update(Connection connection){
		String query = "update dpulistdata set "
					 + "	DPUListID = ?,"
					 + "	Number = ?,"
					 + "	Type = ?,"
					 + "	Cash = ?,"
					 + "	Card = ?,"
					 + "	NonFiscal = ?"
					 + " where DPUListDataID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, DPUListID);
			ps.setString(2, Number);
			ps.setInt(3, Type);
			ps.setDouble(4, Cash);
			ps.setDouble(5, Card);
			ps.setDouble(6, NonFiscal);
			ps.setInt(7, DPUListDataID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void delete(Connection connection){
		String query = "delete from dpulistdata where DPUListDataID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, DPUListDataID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void delete(int dpulistID, Connection connection){
		String query = "delete from dpulistdata where DPUListID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, dpulistID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ObservableList<DPULIstData> getList(DPUList dpuList, int type, Connection connection){
		ObservableList<DPULIstData> list = FXCollections.observableArrayList();
		String query = "select * "
					 + "  from dpulistdata dd "
					 + " where dd.DPUListID = ?"
					 + "   and dd.`Type` = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, dpuList.getDPUListID());
			ps.setInt(2, type);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				DPULIstData dpuData = new DPULIstData();
				dpuData.setDPUListDataID(rs.getInt("DPUListDataID"));
				dpuData.setDPUListID(rs.getInt("DPUListID"));
				dpuData.setType(rs.getInt("Type"));
				dpuData.setNumber(rs.getString("Number"));
				dpuData.setCash(rs.getDouble("Cash"));
				dpuData.setCard(rs.getDouble("Card"));
				dpuData.setNonFiscal(rs.getDouble("NonFiscal"));
				dpuData.setSum(dpuData.getCash() + dpuData.getCard() + dpuData.getNonFiscal());
				list.add(dpuData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ObservableList<DPULIstData> getNiList(Date date, int objectId, Connection connection){
		ObservableList<DPULIstData> list = FXCollections.observableArrayList();
		String query = "CALL dpu_specifikacija_ni(?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ps.setInt(2, objectId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				DPULIstData dpuData = new DPULIstData();
				dpuData.setDPUListDataID(0);
				dpuData.setDPUListID(0);
				dpuData.setType(2);
				dpuData.setNumber(rs.getString("Number"));
				dpuData.setCash(rs.getDouble("Cash"));
				dpuData.setCard(rs.getDouble("Card"));
				dpuData.setNonFiscal(rs.getDouble("NonFiscal"));
				dpuData.setSum(dpuData.getCash() + dpuData.getCard() + dpuData.getNonFiscal());
				if (!dpuData.getNumber().equals(""))
					list.add(dpuData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ObservableList<DPULIstData> getRfList(Date date, rs.petcom.master.dal.Object object,Connection connection){
		ObservableList<DPULIstData> list = FXCollections.observableArrayList();
		String query = "call dpu_specifikacija_rpr(?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ps.setInt(2, object.getObjectID());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				DPULIstData dpuData = new DPULIstData();
				dpuData.setDPUListDataID(0);
				dpuData.setDPUListID(0);
				dpuData.setType(2);
				dpuData.setNumber(rs.getString("Number"));
				dpuData.setCash(rs.getDouble("Cash"));
				dpuData.setCard(rs.getDouble("Card"));
				dpuData.setNonFiscal(rs.getDouble("NonFiscal"));
				dpuData.setSum(dpuData.getCash() + dpuData.getCard() + dpuData.getNonFiscal());
				if (!dpuData.getNumber().equals(""))
					list.add(dpuData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
