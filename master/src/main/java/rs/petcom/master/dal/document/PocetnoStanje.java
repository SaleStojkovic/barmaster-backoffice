package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;

public class PocetnoStanje {
	long DocumentID;
	int Number;
	Date Date;
	int DepartmentID;
	String Note;
	
	public PocetnoStanje(long DocumentID, int Number, Date Date, int DepartmentID,String Note){
		this.DocumentID = DocumentID;
		this.Number = Number;
		this.Date = Date;
		this.DepartmentID = DepartmentID;
		this.Note = Note;
	}
	
	public long getDocumentID() {
		return DocumentID;
	}
	public void setDocumentID(long documentID) {
		DocumentID = documentID;
	}
	public int getNumber() {
		return Number;
	}
	public void setNumber(int number) {
		Number = number;
	}
	public Date getDate() {
		return Date;
	}
	public void setDate(Date date) {
		Date = date;
	}
	public int getDepartmentID() {
		return DepartmentID;
	}
	public void setDepartmentID(int departmentID) {
		DepartmentID = departmentID;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}

	public static ObservableList<PocetnoStanje> getlist(Department department, Connection connection){
		ObservableList<PocetnoStanje> list = FXCollections.observableArrayList();
		String query = "select d.DocumentID,d.Date,d.Number,d.DepartmentID, d.Note"
					 + "  from document d "
					 + " where d.DocumentTypeID = 11"
					 + "   and d.DepartmentID = ?"
					 + " order by d.Date desc, d.Number desc";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, department.getDepartmentID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				list.add(new PocetnoStanje(rs.getLong("DocumentID"), 
										   rs.getInt("Number"), 
										   new Date(rs.getTimestamp("Date").getTime()), 
										   rs.getInt("DepartmentID"),
										   rs.getString("Note")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public long insert(ScreensController controller) throws SQLException{
		Department department = Department.getById(getDepartmentID(), controller.getSettings().getConnection());
		Document document = new Document();
		document.setDocumentTypeID(11);
		document.setDocumentStatusID(1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		document.setYear(cal.get(Calendar.YEAR));
		document.setDate(Date);
		document.setNumber(Document.getNextNumber(11, department, controller));
		document.setSysUserAddID(controller.getUser().getUserID());
		document.setDepartmentID(DepartmentID);
		document.setNote(Note);
		DocumentID = document.insert(controller.getSettings().getConnection());
		return document.getDocumentID();
	}
	
	public void update(ScreensController controller){
		try{
			Document document = Document.getByID(DocumentID, controller.getSettings().getConnection());
			
			document.setDocumentTypeID(11);
			document.setDocumentStatusID(1);
			document.setDate(Date);
			document.setDepartmentID(DepartmentID);
			document.setNote(Note);
			document.update(controller);
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void delete(Connection connection) throws SQLException{
		Document d = Document.getByID(DocumentID, connection);
		DocumentDetails.deleteDetailes(d, connection);
		Document.delete(DocumentID, connection);
	}
}
