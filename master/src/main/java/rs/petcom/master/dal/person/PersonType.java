package rs.petcom.master.dal.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonType {
	private int PersonTypeID;
	private String Name;
	
	public PersonType(int PersonTypeID, String Name){
		this.PersonTypeID = PersonTypeID;
		this.Name = Name;
	}
	
	public int getPersonTypeID() {
		return PersonTypeID;
	}
	public void setPersonTypeID(int PersonTypeID) {
		this.PersonTypeID = PersonTypeID;
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
	
	public static ObservableList<PersonType> getList(Connection connection){
		ObservableList<PersonType> list = FXCollections.observableArrayList();
		try {
			PreparedStatement ps = connection.prepareStatement("select * from persontype");
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				PersonType pt = new PersonType(rs.getInt("PersonTypeID"), rs.getString("Name"));
				list.add(pt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
