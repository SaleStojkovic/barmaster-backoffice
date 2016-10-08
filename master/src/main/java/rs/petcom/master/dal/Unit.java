package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Unit {
	private int UnitID;
	private String Code;
	private String Name;
	public int getUnitID() {
		return UnitID;
	}
	public void setUnitID(int unitID) {
		UnitID = unitID;
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
	@Override
	public String toString() {
		return Name;
	}
	
	public static ObservableList<Unit> getList(Connection connection){
		ObservableList<Unit> list = FXCollections.observableArrayList();
		
		String query = "select * from unit order by Name";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Unit u = new Unit();
				u.setUnitID(rs.getInt("UnitID"));
				u.setCode(rs.getString("Code"));
				u.setName(rs.getString("Name"));
				list.add(u);
			}
		} catch (SQLException e) {e.printStackTrace();
		}
		return list;
	}
}
