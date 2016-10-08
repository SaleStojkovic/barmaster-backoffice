package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KitchenDisplay {
	private int KitchenDisplayID;
	private int ObjectID;
	private String Name;
	
	public KitchenDisplay(){}
	public KitchenDisplay(int KitchenDisplayID, int ObjectID,String Name ){
		this.KitchenDisplayID = KitchenDisplayID;
		this.ObjectID = ObjectID;
		this.Name = Name;
	}
	
	public int getKitchenDisplayID() {
		return KitchenDisplayID;
	}
	public void setKitchenDisplayID(int kitchenDisplayID) {
		KitchenDisplayID = kitchenDisplayID;
	}
	public int getObjectID() {
		return ObjectID;
	}
	public void setObjectID(int objectID) {
		ObjectID = objectID;
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
	
	public static ObservableList<KitchenDisplay> getList(rs.petcom.master.dal.Object object,
												  Connection connection){
		ObservableList<KitchenDisplay> list = FXCollections.observableArrayList();
		list.add(new KitchenDisplay(-1,-1,""));
		
		try {
			PreparedStatement ps = connection.prepareStatement( "select * from KitchenDisplay "
															  + " where ObjectID = ?"
															  + " order by name");
			ps.setInt(1, object.getObjectID());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				KitchenDisplay kd = new KitchenDisplay();
				kd.setKitchenDisplayID(rs.getInt("KitchenDisplayID"));
				kd.setObjectID(rs.getInt("ObjectID"));
				kd.setName(rs.getString("Name"));
				list.add(kd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;		
	}

}
