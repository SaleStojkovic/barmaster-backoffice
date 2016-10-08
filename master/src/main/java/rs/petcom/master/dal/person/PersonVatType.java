package rs.petcom.master.dal.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonVatType {
	private int PersonVatTypeID;
	private String Name;
	
	public PersonVatType(int PersonVatTypeID, String Name){
		this.PersonVatTypeID = PersonVatTypeID;
		this.Name = Name;
	}
	
	public int getPersonVatTypeID() {
		return PersonVatTypeID;
	}
	public void setPersonVatTypeID(int PersonVatTypeID) {
		this.PersonVatTypeID = PersonVatTypeID;
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
	
	public static ObservableList<PersonVatType> getList(Connection connection){
		ObservableList<PersonVatType> list = FXCollections.observableArrayList();
		try {
			PreparedStatement ps = connection.prepareStatement("select * from personvattype");
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				list.add(new PersonVatType(rs.getInt("PersonVatTypeID"),rs.getString("Name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
