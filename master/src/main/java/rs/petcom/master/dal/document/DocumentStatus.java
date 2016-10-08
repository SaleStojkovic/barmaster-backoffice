package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DocumentStatus {
	
	private int DocumentStatusID;
	private String Name;
		
	public int getDocumentStatusID() {
		return DocumentStatusID;
	}
	public void setDocumentStatusID(int documentStatusID) {
		DocumentStatusID = documentStatusID;
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

	public static ObservableList<DocumentStatus> getList(Connection connection){
		ObservableList<DocumentStatus> list = FXCollections.observableArrayList();
		String query = "select * from documentstatus";
		try{
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				DocumentStatus ds = new DocumentStatus();
				ds.DocumentStatusID = rs.getInt("DocumentStatusID");
				ds.Name = rs.getString("Name");
				list.add(ds);
			}
			return list;
		} catch (SQLException e){
			return null;
		}	
	}
}
