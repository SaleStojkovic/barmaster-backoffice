package rs.petcom.master.dal.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import rs.petcom.master.ScreensController;

public class Address {
	
	private long AddressID;
	private int AddressTypeID;
	private long PersonID;
	private String Address;
	private String City;
	private String State;
	private String Zip;
	private String Municipality;
	private boolean IsPrimary;
	private boolean Active;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	public long getAddressID() {
		return AddressID;
	}
	public void setAddressID(long addressID) {
		AddressID = addressID;
	}
	public int getAddressTypeID() {
		return AddressTypeID;
	}
	public void setAddressTypeID(int addressTypeID) {
		AddressTypeID = addressTypeID;
	}
	public long getPersonID() {
		return PersonID;
	}
	public void setPersonID(long personID) {
		PersonID = personID;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getZip() {
		return Zip;
	}
	public void setZip(String zip) {
		Zip = zip;
	}
	public String getMunicipality() {
		return Municipality;
	}
	public void setMunicipality(String municipality) {
		Municipality = municipality;
	}
	public boolean isIsPrimary() {
		return IsPrimary;
	}
	public void setIsPrimary(boolean isPrimary) {
		IsPrimary = isPrimary;
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
	
	public long insert(ScreensController controller){
		String query = "insert into address (AddressTypeID,PersonID,Address,City,State,Zip,IsPrimary,Active,sysUserAddID,Municipality)"
				     + " values (?,?,?,?,?,?,?,?,?,?)";
		try{
			PreparedStatement ps = controller.getSettings().getConnection().prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1,AddressTypeID);
			ps.setLong(2,PersonID);
			ps.setString(3,Address);
			ps.setString(4,City);
			ps.setString(5,State);
			ps.setString(6,Zip);
			ps.setBoolean(7,IsPrimary);
			ps.setBoolean(8,Active);
			ps.setLong(9,controller.getUser().getUserID());
			ps.setString(6,Municipality);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			AddressID = rs.getLong(1);
			
			return rs.getLong(1);
		} catch (SQLException e){
			e.printStackTrace();
			return 0;
		}
	}

	
	public static ArrayList<Address> getList(Person person, Connection connection){
		ArrayList<Address> list = new ArrayList<>();
		String query = "select * from address "
					 + " where PersonID = ? "
					 + "   and Active = true"
					 + " order by IsPrimary desc";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			if(person != null)
				ps.setLong(1, person.getPersonID());
			else
				ps.setNull(1, java.sql.Types.BIGINT);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Address a = new Address();
				a.AddressID = rs.getLong("AddressID");
				a.AddressTypeID = rs.getInt("AddressTypeID");
				a.PersonID = rs.getLong("PersonID");
				a.Address = rs.getString("Address");
				a.City = rs.getString("City");
				a.State = rs.getString("State");
				a.Zip = rs.getString("Zip");
				a.IsPrimary = rs.getBoolean("IsPrimary");
				a.Active = rs.getBoolean("Active");
				a.sysDTCreated = rs.getTimestamp("sysDTCreated");
				a.sysDTEdit = rs.getTimestamp("sysDTEdit");
				a.sysUserAddID = rs.getLong("sysUserAddID");
				a.sysUserEditID = rs.getLong("sysUserEditID");
				try{
					a.Municipality = rs.getString("Municipality");
				}catch (NullPointerException e){}
				list.add(a);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static Address getByID(long id, Connection connection){
		String query = "select * from address where AddressID = ? ";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Address a = new Address();
			a.AddressID = rs.getLong("AddressID");
			a.AddressTypeID = rs.getInt("AddressTypeID");
			a.PersonID = rs.getLong("PersonID");
			a.Address = rs.getString("Address");
			a.City = rs.getString("City");
			a.State = rs.getString("State");
			a.Zip = rs.getString("Zip");
			a.IsPrimary = rs.getBoolean("IsPrimary");
			a.Active = rs.getBoolean("Active");
			a.sysDTCreated = rs.getTimestamp("sysDTCreated");
			a.sysDTEdit = rs.getTimestamp("sysDTEdit");
			a.sysUserAddID = rs.getLong("sysUserAddID");
			a.sysUserEditID = rs.getLong("sysUserEditID");
			try{
				a.Municipality = rs.getString("Municipality");
			}catch (NullPointerException e){}
			return a;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
