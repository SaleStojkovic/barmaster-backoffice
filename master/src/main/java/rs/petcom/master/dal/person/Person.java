package rs.petcom.master.dal.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.mysql.jdbc.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.dal.User;

public class Person {
	private long PersonID;
	private int PersonTypeID;
	private int PersonCategoryID;
	private int PersonVatTypeID;  // 1 - PDV obveznik; 2 - Nije PDV obveznik
	private String FirstName;
	private String LastName;
	private String IdentificationNumber;
	private String Name;
	private String PIB;
	private String MB;
	private boolean Active;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	private String Code;
	
	private double Discount;
	
	public long getPersonID() {
		return PersonID;
	}
	public void setPersonID(long personID) {
		PersonID = personID;
	}
	public int getPersonTypeID() {
		return PersonTypeID;
	}
	public void setPersonTypeID(int personTypeID) {
		PersonTypeID = personTypeID;
	}
	public int getPersonCategoryID() {
		return PersonCategoryID;
	}
	public void setPersonCategoryID(int personCategoryID) {
		PersonCategoryID = personCategoryID;
	}
	public int getPersonVatTypeID() {
		return PersonVatTypeID;
	}
	public void setPersonVatTypeID(int personVatTypeID) {
		PersonVatTypeID = personVatTypeID;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getIdentificationNumber() {
		return IdentificationNumber;
	}
	public void setIdentificationNumber(String identificationNumber) {
		IdentificationNumber = identificationNumber;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public String getPIB() {
		return PIB;
	}
	public void setPIB(String pIB) {
		PIB = pIB;
	}
	public String getMB() {
		return MB;
	}
	public void setMB(String mB) {
		MB = mB;
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
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	
	public double getDiscount() {
		return Discount;
	}
	public void setDiscount(double discount) {
		Discount = discount;
	}
	
	public String toString(){
		if (Name == null || Name.equals(""))
			return FirstName + " " + LastName;
		else
			return Name;
	}
	public long insert(User user, Connection connection){
		String query = "insert into person ( "
					 + "	`PersonTypeID`,"
					 + "	`PersonCategoryID`,"
					 + "	`PersonVatTypeID`,"
					 + "	`FirstName`,"
					 + "	`LastName`,"
					 + "	`IdentificationNumber`,"
					 + "	`Name`,"
					 + "	`PIB`,"
					 + "	`MB`,"
					 + "	`Active`,"
					 + "	`sysDTCreated`,"
					 + "	`sysUserAddID`,"
					 + "	`Code`)"
					 + "value (?,?,?,?,?,?,?,?,?,?,now(),?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, PersonTypeID);
			ps.setInt(2, PersonCategoryID);
			ps.setInt(3, PersonVatTypeID);
			ps.setString(4, FirstName);
			ps.setString(5, LastName);
			ps.setString(6, IdentificationNumber);
			ps.setString(7, Name);
			ps.setString(8, PIB);
			ps.setString(9, MB);
			ps.setBoolean(10,Active);
			ps.setLong(11, user.getUserID());
			ps.setString(12, Code);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next(); 
			PersonID = rs.getLong(1);
			return PersonID;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	public void update(User user, Connection connection){
		String query = "update person set "
					 + "	`PersonTypeID` = ?,"
					 + "	`PersonCategoryID` = ?,"
					 + "	`PersonVatTypeID` = ?,"
					 + "	`FirstName` = ?,"
					 + "	`LastName` = ?,"
					 + "	`IdentificationNumber` = ?,"
					 + "	`Name` = ?,"
					 + "	`PIB` = ?,"
					 + "	`MB` = ?,"
					 + "	`Active` = ?,"
					 + "	`sysDTEdit` = now(),"
					 + "	`sysUserEditID` = ?,"
					 + "	`Code` = ?"
					 + " where `PersonID` = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setLong(1, PersonTypeID);
			ps.setInt(2, PersonCategoryID);
			ps.setInt(3, PersonVatTypeID);
			ps.setString(4, FirstName);
			ps.setString(5, LastName);
			ps.setString(6, IdentificationNumber);
			ps.setString(7, Name);
			ps.setString(8, PIB);
			ps.setString(9, MB);
			ps.setBoolean(10,Active);
			ps.setLong(11, user.getUserID());
			ps.setString(12, Code);
			ps.setLong(13, PersonID);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(User user, Connection connection){
		String query = "delete from person where `PersonID` = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, PersonID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static ArrayList<Person> getList(String letter, String filter,Connection connection){
		ArrayList<Person> list = new ArrayList<>();
		
		try {
			PreparedStatement ps = connection.prepareStatement( "select p.*, "
															  + "       coalesce( (select max(l1.discount) "
															  + "                    from loyality l1 "
															  + "					where l1.PersonID = p.PersonID),0) as discount "
															  + "  from loyality l "
															  + "  left join person p on p.PersonID = l.PersonID "
															  + " where l.Active = true "
															  + " and ((? = '') or ((? > '') and (p.LastName like concat(?,'%')))) "
															  + " and ((? = '') or ((? > '') and (concat(p.LastName, ' ', p.FirstName) like concat('%',?,'%')))) "
															  + " order by p.LastName");
	
			ps.setString(1, letter);
			ps.setString(2, letter);
			ps.setString(3, letter);
			ps.setString(4, filter);
			ps.setString(5, filter);
			ps.setString(6, filter);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Person person = new Person();
				person.PersonID = rs.getLong("PersonID");
				person.PersonTypeID = rs.getInt("PersonTypeID");
				person.PersonCategoryID = rs.getInt("PersonCategoryID");
				person.PersonVatTypeID = rs.getInt("PersonVatTypeID");
				person.FirstName = rs.getString("FirstName");
				person.LastName = rs.getString("LastName");
				person.IdentificationNumber = rs.getString("IdentificationNumber");
				person.Name = rs.getString("Name");
				person.PIB = rs.getString("PIB");
				person.MB = rs.getString("MB");
				person.Active = rs.getBoolean("Active");
				person.sysDTCreated = rs.getDate("sysDTCreated");
				person.sysDTEdit = rs.getDate("sysDTEdit");
				person.sysUserAddID = rs.getLong("sysUserAddID");
				person.sysUserEditID = rs.getLong("sysUserEditID");
				person.Code = rs.getString("Code");
				person.Discount = rs.getDouble("discount");
				
				list.add(person);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static ObservableList<Person> getObeservableList(String letter, String filter,Connection connection){
		ObservableList<Person> list = FXCollections.observableArrayList();
		for(Person p : Person.getList(letter, filter,connection)){
			list.add(p);
		}
		return list;
	}
	
	public String getFirstLastName(){
		return LastName + " " + FirstName;
	}
	
	public static Person getByCode(String code, Connection connection){
		Person person = null;
		
		try {
			PreparedStatement ps = connection.prepareStatement( "select p.*"
															  + "  from person p "
															  + " where p.code = ? ");
	
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			rs.next();
			person = new Person();
			person.PersonID = rs.getLong("PersonID");
			person.PersonTypeID = rs.getInt("PersonTypeID");
			person.PersonCategoryID = rs.getInt("PersonCategoryID");
			person.PersonVatTypeID = rs.getInt("PersonVatTypeID");
			person.FirstName = rs.getString("FirstName");
			person.LastName = rs.getString("LastName");
			person.IdentificationNumber = rs.getString("IdentificationNumber");
			person.Name = rs.getString("Name");
			person.PIB = rs.getString("PIB");
			person.MB = rs.getString("MB");
			person.Active = rs.getBoolean("Active");
			person.sysDTCreated = rs.getDate("sysDTCreated");
			person.sysDTEdit = rs.getDate("sysDTEdit");
			person.sysUserAddID = rs.getLong("sysUserAddID");
			person.sysUserEditID = rs.getLong("sysUserEditID");
			person.Code = rs.getString("Code");
				
		} catch (SQLException e) {
			return null;
		}
		return person;
	}
	
	public static ObservableList<Person> getList(boolean activeOnly, Connection connection){
		ObservableList<Person> list = FXCollections.observableArrayList();
		try {
			PreparedStatement ps = connection.prepareStatement( "select p.* "
															  + "  from person p"
															  + " where p.Active = ? ");
	
			ps.setBoolean(1, activeOnly);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Person person = new Person();
				person.PersonID = rs.getLong("PersonID");
				person.PersonTypeID = rs.getInt("PersonTypeID");
				person.PersonCategoryID = rs.getInt("PersonCategoryID");
				person.PersonVatTypeID = rs.getInt("PersonVatTypeID");
				person.FirstName = rs.getString("FirstName");
				person.LastName = rs.getString("LastName");
				person.IdentificationNumber = rs.getString("IdentificationNumber");
				person.Name = rs.getString("Name");
				person.PIB = rs.getString("PIB");
				person.MB = rs.getString("MB");
				person.Active = rs.getBoolean("Active");
				person.sysDTCreated = rs.getDate("sysDTCreated");
				person.sysDTEdit = rs.getDate("sysDTEdit");
				person.sysUserAddID = rs.getLong("sysUserAddID");
				person.sysUserEditID = rs.getLong("sysUserEditID");
				person.Code = rs.getString("Code");
				
				list.add(person);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ObservableList<Person> getList(PersonCategory category, 
												 boolean activeOnly, 
												 String filter,
												 Connection connection){
		ObservableList<Person> list = FXCollections.observableArrayList();
		try {
			String query = "select p.* "
					  	 + "  from person p"
					  	 + " where p.Active = ? ";
			if (category != null)
				query += "   and p.PersonCategoryID = ?";
			query += "   and (p.Name like ? or "
					  	 + "		 p.FirstName like ? or "
					  	 + "		 p.LastName like ?)";
			
			PreparedStatement ps = connection.prepareStatement(query);
	
			ps.setBoolean(1, activeOnly);
			if (category != null){
				ps.setInt(2, category.getPersonCategoryID());
				ps.setString(3, "%" + filter + "%");
				ps.setString(4, "%" + filter + "%");
				ps.setString(5, "%" + filter + "%");
				
			}
			else{
				ps.setString(2, "%" + filter + "%");
				ps.setString(3, "%" + filter + "%");
				ps.setString(4, "%" + filter + "%");
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Person person = new Person();
				person.PersonID = rs.getLong("PersonID");
				person.PersonTypeID = rs.getInt("PersonTypeID");
				person.PersonCategoryID = rs.getInt("PersonCategoryID");
				person.PersonVatTypeID = rs.getInt("PersonVatTypeID");
				person.FirstName = rs.getString("FirstName");
				person.LastName = rs.getString("LastName");
				person.IdentificationNumber = rs.getString("IdentificationNumber");
				person.Name = rs.getString("Name");
				person.PIB = rs.getString("PIB");
				person.MB = rs.getString("MB");
				person.Active = rs.getBoolean("Active");
				person.sysDTCreated = rs.getDate("sysDTCreated");
				person.sysDTEdit = rs.getDate("sysDTEdit");
				person.sysUserAddID = rs.getLong("sysUserAddID");
				person.sysUserEditID = rs.getLong("sysUserEditID");
				person.Code = rs.getString("Code");
				
				list.add(person);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static Person getById(long id, Connection connection){
		Person person = null;
		
		try {
			PreparedStatement ps = connection.prepareStatement( "select p.* "
															  + "  from person p"
															  + " where p.PersonID = ? ");
	
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			person = new Person();
			person.PersonID = rs.getLong("PersonID");
			person.PersonTypeID = rs.getInt("PersonTypeID");
			person.PersonCategoryID = rs.getInt("PersonCategoryID");
			person.PersonVatTypeID = rs.getInt("PersonVatTypeID");
			person.FirstName = rs.getString("FirstName");
			person.LastName = rs.getString("LastName");
			person.IdentificationNumber = rs.getString("IdentificationNumber");
			person.Name = rs.getString("Name");
			person.PIB = rs.getString("PIB");
			person.MB = rs.getString("MB");
			person.Active = rs.getBoolean("Active");
			person.sysDTCreated = rs.getDate("sysDTCreated");
			person.sysDTEdit = rs.getDate("sysDTEdit");
			person.sysUserAddID = rs.getLong("sysUserAddID");
			person.sysUserEditID = rs.getLong("sysUserEditID");
			person.Code = rs.getString("Code");
				
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return person;
	}
}
