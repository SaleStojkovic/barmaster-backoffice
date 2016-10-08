package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Controll {
	private int ControllID;
	private int ObjectID;
	private String Group;
	private boolean Active;
	private int Sort;
	private long ProductID;
	private String Code;
	private String Name;
	public int getControllID() {
		return ControllID;
	}
	public void setControllID(int controllID) {
		ControllID = controllID;
	}
	public int getObjectID() {
		return ObjectID;
	}
	public void setObjectID(int objectID) {
		ObjectID = objectID;
	}
	public String getGroup() {
		return Group;
	}
	public void setGroup(String group) {
		Group = group;
	}
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	public int getSort() {
		return Sort;
	}
	public void setSort(int sort) {
		Sort = sort;
	}
	public long getProductID() {
		return ProductID;
	}
	public void setProductID(long productID) {
		ProductID = productID;
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
	
	public static ObservableList<Controll> getList(Object object, Connection connection){
		ObservableList<Controll> list = FXCollections.observableArrayList();
		String query = "select "
					 + "	c.*,"
					 + "	p.Code,"
					 + "	p.Name"
					 + "  from controll c "
					 + "  left join product p on p.ProductID = c.ProductID"
					 + " where c.ObjectID = ?"
					 + "   and c.Active = true "
					 + " order by Sort";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, object.getObjectID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Controll c = new Controll();
				c.setControllID(rs.getInt("ControllID"));
				c.setObjectID(rs.getInt("ObjectID"));
				c.setGroup(rs.getString("Group"));
				c.setActive(rs.getBoolean("Active"));
				c.setSort(rs.getInt("Sort"));
				c.setProductID(rs.getInt("ProductID"));
				c.setCode(rs.getString("Code"));
				c.setName(rs.getString("Name"));
				list.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
