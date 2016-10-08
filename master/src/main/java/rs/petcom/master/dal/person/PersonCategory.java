package rs.petcom.master.dal.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonCategory {
	private int PersonCategoryID;
	private String Name;
	
	public PersonCategory(int PersonCategoryID, String Name){
		this.PersonCategoryID = PersonCategoryID;
		this.Name = Name;
	}
	
	public int getPersonCategoryID() {
		return PersonCategoryID;
	}
	public void setPersonCategoryID(int PersonCategoryID) {
		this.PersonCategoryID = PersonCategoryID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	@Override
	public String toString(){
		return Name;
	}
	
	public static ObservableList<PersonCategory> getList(Connection connection){
		ObservableList<PersonCategory> list = FXCollections.observableArrayList();
		try {
			PreparedStatement ps = connection.prepareStatement("select * from personcategory");
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				list.add(new PersonCategory(rs.getInt("PersonCategoryID"),rs.getString("Name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static PersonCategory getByID(int id, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement("select * from personcategory where PersonCategoryID = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return new PersonCategory(rs.getInt("PersonCategoryID"), rs.getString("Name"));
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
