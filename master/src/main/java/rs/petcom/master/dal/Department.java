package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Department {
	private int DepartmentID;
	private String Name;
	private String ShortName;
	private String Code;
	private boolean Active;
	private int PrinterID;
	private int ObjectID;
	private boolean Selected = false;
	
	public Department(){}
	
	public Department(int id, String name){
		this.DepartmentID = id;
		this.Name = name;
	}
	
	public int getDepartmentID() {
		return DepartmentID;
	}
	public void setDepartmentID(int departmentID) {
		DepartmentID = departmentID;
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
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	public int getPrinterID() {
		return PrinterID;
	}
	public void setPrinterID(int printerID) {
		PrinterID = printerID;
	}
	public int getObjectID() {
		return ObjectID;
	}
	public void setObjectID(int objectID) {
		ObjectID = objectID;
	}	
	public boolean isSelected() {
		return Selected;
	}
	public void setSelected(boolean selected) {
		Selected = selected;
	}
	
	@Override
	public String toString(){
		return Name;
	}
	
	public static Department getByCode(String code, Connection connection){
		String query = "select * from department where Code = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Department d = new Department();
			d.DepartmentID = rs.getInt("DepartmentID");
			d.Name = rs.getString("Name");
			d.ShortName = rs.getString("ShortName");
			d.Code = rs.getString("Code");
			d.Active = rs.getBoolean("Active");
			d.PrinterID = rs.getInt("PrinterID");
			d.ObjectID = rs.getInt("ObjectID");
			return d;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	public static Department getById(int id, Connection connection) throws SQLException{
		String query = "select * from department where DepartmentID = ?";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		Department d = new Department();
		d.DepartmentID = rs.getInt("DepartmentID");
		d.Name = rs.getString("Name");
		d.ShortName = rs.getString("ShortName");
		d.Code = rs.getString("Code");
		d.Active = rs.getBoolean("Active");
		d.PrinterID = rs.getInt("PrinterID");
		d.ObjectID = rs.getInt("ObjectID");
		return d;	
	}
	
	public static ObservableList<Department> getList(rs.petcom.master.dal.Object object, String filter, Connection connection){
		ObservableList<Department> list = FXCollections.observableArrayList();
		String query = "select d.* "
					 + "  from department d"
					 + " where d.DepartmentID in (select pd.DepartmentID"
					 + " 							from pointofsaledepartment pd"
					 + "							left join pointofsale p on p.PointOfSaleID = pd.PointOfSaleID"
					 + "						   where p.ObjectID = ?)"
					 + "   and Name like ? "
					 + " order by Name";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, object.getObjectID());
			ps.setString(2, "%" + filter + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Department d = new Department();
				d.DepartmentID = rs.getInt("DepartmentID");
				d.Name = rs.getString("Name");
				d.ShortName = rs.getString("ShortName");
				d.Code = rs.getString("Code");
				d.Active = rs.getBoolean("Active");
				d.PrinterID = rs.getInt("PrinterID");
				list.add(d);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ObservableList<Department> getList(Object object, Connection connection){
		ObservableList<Department> list = FXCollections.observableArrayList();
		String query = "select d.* "
					 + "  from department d"
					 + " where d.ObjectID  = ?"
					 + " order by Name";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, object.getObjectID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Department d = new Department();
				d.DepartmentID = rs.getInt("DepartmentID");
				d.Name = rs.getString("Name");
				d.ShortName = rs.getString("ShortName");
				d.Code = rs.getString("Code");
				d.Active = rs.getBoolean("Active");
				d.PrinterID = rs.getInt("PrinterID");
				d.ObjectID  = rs.getInt("ObjectID");
				list.add(d);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
