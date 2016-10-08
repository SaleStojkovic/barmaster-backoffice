package rs.petcom.master.dal.document;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.Settings;

public class Presifriranje {
	long DocumentID;
	int Number;
	Date Date;
	int DepartmentID;
	String Note;
	
	public Presifriranje(long DocumentID, int Number, Date Date, int DepartmentID,String Note){
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

	public static ObservableList<Presifriranje> getlist(Department department, Connection connection){
		ObservableList<Presifriranje> list = FXCollections.observableArrayList();
		String query = "select d.DocumentID,d.Date,d.Number,d.DepartmentID, d.Note"
					 + "  from document d "
					 + " where d.DocumentTypeID = 19"
					 + "   and d.DepartmentID = ?"
					 + " order by d.Date desc, d.Number desc";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, department.getDepartmentID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				list.add(new Presifriranje(rs.getLong("DocumentID"), 
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
		document.setDocumentTypeID(19);
		document.setDocumentStatusID(1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		document.setYear(cal.get(Calendar.YEAR));
		document.setDate(Date);
		document.setNumber(Document.getNextNumber(19, department, controller));
		document.setSysUserAddID(controller.getUser().getUserID());
		document.setDepartmentID(DepartmentID);
		document.setNote(Note);
		DocumentID = document.insert(controller.getSettings().getConnection());
		return document.getDocumentID();
	}
	
	public void update(ScreensController controller){
		Document document = Document.getByID(DocumentID, controller.getSettings().getConnection());
		
		document.setDocumentTypeID(19);
		document.setDocumentStatusID(1);
		document.setDate(Date);
		document.setDepartmentID(DepartmentID);
		document.setNote(Note);
		try {
			document.update(controller);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(Connection connection) throws SQLException{
		Document d = Document.getByID(DocumentID, connection);
		DocumentDetails.deleteDetailes(d, connection);
		Document.delete(DocumentID, connection);
	}
	
	public void exportToXml(String fileName, Connection connection) 
			throws FileNotFoundException, 
				   UnsupportedEncodingException, 
				   SQLException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		//NumberFormat nf2 = Settings.getNumberFormat(2, false);
		NumberFormat nf4 = Settings.getNumberFormat(4, false);
		
		Department department = Department.getById(DepartmentID, connection);
		
		PrintWriter writer;
		writer = new PrintWriter(fileName, "UTF-8");
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		writer.println("<Presifravanje>");
		writer.println("	<PresifravanjeData>");
		writer.println("		<PresifravanjeDataID>" + getDocumentID() + "</PresifravanjeDataID>");
		writer.println("		<DepartmentCode>" + department.getCode() + "</DepartmentCode>");
		writer.println("		<DepartmentName>" + department.getName() + "</DepartmentName>");
		writer.println("		<Number>" + getNumber() + "</Number>");
		writer.println("		<Date>" + sdf.format(getDate()) + "</Date>");
		writer.println("		<Comment>" + Note + "</Comment>");
		writer.println("	</PresifravanjeData>");
		writer.println("	<PresifravanjeStavke>");
		for (PresifriranjeDetailes pd: PresifriranjeDetailes.getList(this, connection)){
			writer.println("		<Stavka>");
			writer.println("			<ProductCode>" + pd.getProductCode() + "</ProductCode>");
			writer.println("			<ProductName>" + pd.getProductName() + "</ProductName>");
			writer.println("			<Quantity>" + nf4.format(pd.getQuantity()) + "</Quantity>");
			writer.println("		</Stavka>");
		}
		writer.println("	</PresifravanjeStavke>");
		writer.println("</Presifravanje>");
		writer.close();
	}
}
