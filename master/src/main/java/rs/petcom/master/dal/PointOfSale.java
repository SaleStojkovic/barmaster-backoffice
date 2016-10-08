package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.person.Address;
import rs.petcom.master.dal.person.Person;

public class PointOfSale {
	int PointOfSaleID;
	String Code;
	String Name;
	boolean Active;
	Date sysDTCreated;
	Date sysDTEdit;
	long sysUserAddID;
	long sysUserEditID;
	int ObjectID;
	long PersonID;
	boolean Selected;
	
	// PointOfSale settings
	private boolean posDescriptionalMandatory;
	
	public int getPointOfSaleID() {
		return PointOfSaleID;
	}
	public void setPointOfSaleID(int pointOfSaleID) {
		PointOfSaleID = pointOfSaleID;
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
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	public Date getSysDTCreated() {
		return sysDTCreated;
	}
	public void setSysDTCreated(Date sysDTCreated) {
		this.sysDTCreated = sysDTCreated;
	}
	public Date getSysDTEdit() {
		return sysDTEdit;
	}
	public void setSysDTEdit(Date sysDTEdit) {
		this.sysDTEdit = sysDTEdit;
	}
	public long getSysUserAddID() {
		return sysUserAddID;
	}
	public void setSysUserAddID(long sysUserAddID) {
		this.sysUserAddID = sysUserAddID;
	}
	public long getSysUserEditID() {
		return sysUserEditID;
	}
	public void setSysUserEditID(long sysUserEditID) {
		this.sysUserEditID = sysUserEditID;
	}
	public int getObjectID() {
		return ObjectID;
	}
	public void setObjectID(int objectID) {
		ObjectID = objectID;
	}	
	public long getPersonID() {
		return PersonID;
	}
	public void setPersonID(long personID) {
		PersonID = personID;
	}
	
	public boolean isPosDescriptionalMandatory() {
		return posDescriptionalMandatory;
	}
	public void setPosDescriptionalMandatory(boolean posDescriptionalMandatory) {
		this.posDescriptionalMandatory = posDescriptionalMandatory;
	}
	public boolean isSelected() {
		return Selected;
	}
	public void setSelected(boolean selected) {
		Selected = selected;
	}
	public PointOfSale(){
	}
	public PointOfSale(int id, String Name){
		PointOfSaleID = id;
		this.Name = Name;
	}
	public PointOfSale(String code, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select * from pointofsale where code = ? ");
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			rs.next();
			this.PointOfSaleID = rs.getInt("PointOfSaleID");
			this.Code = rs.getString("Code"); 
			this.Name = rs.getString("Name");
			this.Active = rs.getBoolean("Active");
			this.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
			try{
				this.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
			}catch (NullPointerException e){}	
			this.sysUserAddID = rs.getLong("sysUserAddID");
			try{
				this.sysUserEditID = rs.getLong("sysUserEditID");
			}catch (NullPointerException e){}
			this.ObjectID = rs.getInt("ObjectID");
			this.PersonID = rs.getLong("PersonID");
			
			String query = "select * from settings";
			ps = connection.prepareCall(query);
			rs = ps.executeQuery();
			while (rs.next()){
				switch (rs.getString("name")) {
				case "descriptional.mandatory":
					posDescriptionalMandatory = rs.getString("actual").equals("true");
					break;

				default:
					break;
				}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public PointOfSale (int posId, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select * from pointofsale where PointOfSaleID = ? ");
			ps.setInt(1, posId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			this.PointOfSaleID = rs.getInt("PointOfSaleID");
			this.Code = rs.getString("Code"); 
			this.Name = rs.getString("Name");
			this.Active = rs.getBoolean("Active");
			this.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
			try{
				this.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
			}catch (NullPointerException e){}	
			this.sysUserAddID = rs.getLong("sysUserAddID");
			try{
				this.sysUserEditID = rs.getLong("sysUserEditID");
			}catch (NullPointerException e){}
			this.ObjectID = rs.getInt("ObjectID");
			this.PersonID = rs.getLong("PersonID");
			
			String query = "select * from settings";
			ps = connection.prepareCall(query);
			rs = ps.executeQuery();
			while (rs.next()){
				switch (rs.getString("name")) {
				case "descriptional.mandatory":
					posDescriptionalMandatory = rs.getString("actual").equals("true");
					break;

				default:
					break;
				}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public Address getAddress(ScreensController controller){
		Person.getById(PersonID, controller.getSettings().getConnection());
		return Address.getList(Person.getById(PersonID, controller.getSettings().getConnection()), 
							   controller.getSettings().getConnection()).get(0);
	}
	
	public static ArrayList<PointOfSale> getPosList(rs.petcom.master.dal.Object object, Connection connection){
		ArrayList<PointOfSale> list = new ArrayList<>();
		String query = "select * "
					 + "  from pointofsale ps"
					 + " where ps.Active = true"
					 + "   and ps.ObjectID = ?"
					 + " order by ps.Name";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, object.getObjectID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				PointOfSale pos = new PointOfSale();
				pos.setPointOfSaleID(rs.getInt("PointOfSaleID"));
				pos.setCode(rs.getString("Code"));
				pos.setName(rs.getString("Name"));
				pos.setActive(rs.getBoolean("Active"));
				list.add(pos);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;		
	}
	
	public String toString(){
		return Name;
	}
	
	public ArrayList<Department> getDepartmentList(Connection connection){
		ArrayList<Department> list = new ArrayList<>();
		String query = "select "
					 + " d.*"
					 + "  from pointofsaledepartment pd"
					 + "  left join department d on d.DepartmentID = pd.DepartmentID"
					 + " where PointOfSaleID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, PointOfSaleID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Department d = new Department();
				d.setDepartmentID(rs.getInt("DepartmentID"));
				d.setName(rs.getString("Name"));
				d.setShortName(rs.getString("ShortName"));
				d.setCode(rs.getString("Code"));
				d.setActive(rs.getBoolean("Active"));
				d.setPrinterID(rs.getInt("PrinterID"));
				d.setObjectID(rs.getInt("ObjectID"));
				list.add(d);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;		
	}
}
