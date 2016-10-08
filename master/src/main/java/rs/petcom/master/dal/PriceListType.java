package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PriceListType {
	private int Type;
	private String Name;
	
	public PriceListType(int Type, String Name){
		this.Type = Type;
		this.Name = Name;
	}
	
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	public String toString(){
		return Name;
	}
	
	public static ObservableList<PriceListType> getList(Connection connection){
		ObservableList<PriceListType> list = FXCollections.observableArrayList();

		String query = " select * from pricelisttype order by TypeID";
	
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				PriceListType plt = new PriceListType(rs.getInt("TypeID"), rs.getString("Title"));
				list.add(plt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
