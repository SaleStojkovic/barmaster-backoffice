package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DocumentType {
	
	private int DocumentTypeID;
	private String Name;
	private String Code;
	private int FirstNumber;
	private int Year;
	private int Stock;
	private int Finance;
	public int getDocumentTypeID() {
		return DocumentTypeID;
	}
	public void setDocumentTypeID(int documentTypeID) {
		DocumentTypeID = documentTypeID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public int getFirstNumber() {
		return FirstNumber;
	}
	public void setFirstNumber(int firstNumber) {
		FirstNumber = firstNumber;
	}
	public int getYear() {
		return Year;
	}
	public void setYear(int year) {
		Year = year;
	}
	public int getStock() {
		return Stock;
	}
	public void setStock(int stock) {
		Stock = stock;
	}
	public int getFinance() {
		return Finance;
	}
	public void setFinance(int finance) {
		Finance = finance;
	}
	
	@Override
	public String toString(){
		return Name;
	}

	public static DocumentType getById(int id, Connection connection){
		String query = "select * from documenttype where DocumentTypeID = ?";
		try{
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			DocumentType dt = new DocumentType();
			dt.DocumentTypeID = rs.getInt("DocumentTypeID");
			dt.Name = rs.getString("Name");
			dt.Code = rs.getString("Code");
			dt.FirstNumber = rs.getInt("FirstNumber");
			dt.Year = rs.getInt("Year");
			dt.Stock = rs.getInt("Stock");
			dt.Finance = rs.getInt("Finance");
			return dt;
		} catch (SQLException e){
			return null;
		}	
	}
	
	public static DocumentType getByCode(String code, Connection connection){
		String query = "select * from documenttype where Code = ?";
		try{
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			rs.next();
			DocumentType dt = new DocumentType();
			dt.DocumentTypeID = rs.getInt("DocumentTypeID");
			dt.Name = rs.getString("Name");
			dt.Code = rs.getString("Code");
			dt.FirstNumber = rs.getInt("FirstNumber");
			dt.Year = rs.getInt("Year");
			dt.Stock = rs.getInt("Stock");
			dt.Finance = rs.getInt("Finance");
			return dt;
		} catch (SQLException e){
			return null;
		}	
	}
}
