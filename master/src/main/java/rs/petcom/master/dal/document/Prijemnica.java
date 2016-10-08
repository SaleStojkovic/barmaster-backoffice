package rs.petcom.master.dal.document;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.SQLHelper;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.person.Person;

public class Prijemnica {

	private long DocumentID;
	private int DepartmentID;
	private int Number;
	private String ExternalNumber;
	private Date Date;
	private Date DeliveryDate;
	private Date PaymentDate;
	private Person dobavljac;
	private String Place;
	private DocumentStatus DocumentStatus;
	private String comment;
	
	public long getDocumentID() {
		return DocumentID;
	}
	public void setDocumentID(long documnetID) {
		DocumentID = documnetID;
	}
	public int getDepartmentID() {
		return DepartmentID;
	}
	public void setDepartmentID(int departmnetID) {
		DepartmentID = departmnetID;
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
	public Person getDobavljac() {
		return dobavljac;
	}
	public void setDobavljac(Person dobavljac) {
		this.dobavljac = dobavljac;
	}	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}	
	public String getExternalNumber() {
		return ExternalNumber;
	}
	public void setExternalNumber(String externalNumber) {
		ExternalNumber = externalNumber;
	}
	public Date getDeliveryDate() {
		return DeliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		DeliveryDate = deliveryDate;
	}
	public Date getPaymentDate() {
		return PaymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		PaymentDate = paymentDate;
	}
	public String getPlace() {
		return Place;
	}
	public void setPlace(String place) {
		Place = place;
	}
	public DocumentStatus getDocumentStatus() {
		return DocumentStatus;
	}
	public void setDocumentStatus(DocumentStatus documentStatus) {
		DocumentStatus = documentStatus;
	}
	public void delete(Connection connection) throws SQLException{
		String query = "delete from document where DocumentID = ?";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setLong(1, DocumentID);
		ps.executeUpdate();
	}
	
	public long insert(ScreensController controller) throws SQLException{
		Document document = new Document();
		document.setDocumentTypeID(1);
		document.setDocumentStatusID(1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		document.setYear(cal.get(Calendar.YEAR));
		document.setDate(Date);
		document.setDeliveryDate(DeliveryDate);
		document.setCurrencyDate(PaymentDate);
		document.setNumber(Number);
		document.setSysUserAddID(controller.getUser().getUserID());
		document.setDepartmentID(DepartmentID);
		document.setNote(comment);
		document.setPersonID(dobavljac.getPersonID());
		document.setExternalNumber(ExternalNumber);
		DocumentID = document.insert(controller.getSettings().getConnection());
		return document.getDocumentID();
	}
	
	public void update(ScreensController controller){
		Document document = Document.getByID(DocumentID, controller.getSettings().getConnection());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		document.setYear(cal.get(Calendar.YEAR));
		document.setDate(Date);
		document.setDeliveryDate(DeliveryDate);
		document.setCurrencyDate(PaymentDate);
		document.setPersonID(dobavljac.getPersonID());
		document.setDocumentStatusID(DocumentStatus.getDocumentStatusID());
		document.setPlace(Place);
		document.setNote(comment);
		document.setSysUserAddID(controller.getUser().getUserID());
		try {
			document.update(controller);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean exists(String externalNumber, Connection connection){
		boolean result = false;
		String query = "select count(DocumentID) "
					 + "  from document "
					 + " where DocumentTypeID = 1 "
					 + "   and ExternalNumber = '" + externalNumber + "'";
		result = SQLHelper.QueryToInt(query, connection, 0) > 0;
		return result;
	}
	
	public static ObservableList<Prijemnica> getList(Date dateFrom, Date dateTo, Department department, Connection connection){
		ObservableList<Prijemnica> list = FXCollections.observableArrayList();
		
		String query = "select * "
					 + "  from document"
					 + " where DocumentTypeID = 1"
					 + "   and Date >= ? "
					 + "   and Date < DATE_ADD(?,INTERVAL 1 DAY) "
					 + "   and DepartmentID = ?"
					 + " order by Date desc";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setTimestamp(1, new Timestamp(dateFrom.getTime()));
			ps.setTimestamp(2, new Timestamp(dateTo.getTime()));
			ps.setInt(3, department.getDepartmentID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Prijemnica p = new Prijemnica();
				p.DocumentID = rs.getLong("DocumentID");
				p.Number = rs.getInt("Number");
				p.Date = rs.getTimestamp("Date");
				p.DeliveryDate = rs.getTimestamp("DeliveryDate");
				p.PaymentDate = rs.getTimestamp("CurrencyDate");
				long personID = rs.getLong("PersonID");
				p.dobavljac = Person.getById(personID, connection);
				p.comment = rs.getString("Note");
				p.Place = rs.getString("Place");
				p.DepartmentID = rs.getInt("DepartmentID");
				p.ExternalNumber = rs.getString("ExternalNumber");
				list.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void exportToXml(String fileName, Connection connection) 
			throws FileNotFoundException, 
				   UnsupportedEncodingException, 
				   SQLException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		NumberFormat nf4 = Settings.getNumberFormat(4, false);
		
		Department department = Department.getById(DepartmentID, connection);
		
		PrintWriter writer;
		writer = new PrintWriter(fileName, "UTF-8");
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		writer.println("<Prijemnica>");
		writer.println("	<PrijemnicaData>");
		writer.println("		<PrijemnicaDataID>" + getDocumentID() + "</PrijemnicaDataID>");
		writer.println("		<DepartmentCode>" + department.getCode() + "</DepartmentCode>");
		writer.println("		<DepartmentName>" + department.getName() + "</DepartmentName>");
		writer.println("		<Number>" + getNumber() + "</Number>");
		writer.println("		<ExternalNumber>" + getExternalNumber() + "</ExternalNumber>");
		writer.println("		<Date>" + sdf.format(getDate()) + "</Date>");
		writer.println("		<DeliveryDate>" + sdf.format(getDeliveryDate()) + "</DeliveryDate>");
		writer.println("		<PaymentDate>" + sdf.format(getPaymentDate()) + "</PaymentDate>");
		writer.println("		<DobavljacCode>" + dobavljac.getCode() + "</DobavljacCode>");
		writer.println("		<DobavljacName>" + dobavljac.getName() + "</DobavljacName>");
		writer.println("		<DobavljacFirstName>" + dobavljac.getFirstName() + "</DobavljacFirstName>");
		writer.println("		<DobavljacLastName>" + dobavljac.getLastName() + "</DobavljacLastName>");
		writer.println("		<Place>" + Place + "</Place>");
		writer.println("		<DocumentStatus>" + DocumentStatus + "</DocumentStatus>");
		writer.println("		<Comment>" + comment + "</Comment>");
		writer.println("	</PrijemnicaData>");
		writer.println("	<PrijemnicaStavke>");
		for (PrijemnicaDetailes pd: PrijemnicaDetailes.getlist(this, connection)){
			writer.println("		<Stavka>");
			writer.println("			<ProductCode>" + pd.getProductCode() + "</ProductCode>");
			writer.println("			<ProductName>" + pd.getProductName() + "</ProductName>");
			writer.println("			<Quantity>" + nf4.format(pd.getQuantity()) + "</Quantity>");
			writer.println("			<Price>" + nf4.format(pd.getPrice()) + "</Price>");
			writer.println("			<Discount>" + nf4.format(pd.getDiscount()) + "</Discount>");
			writer.println("			<PriceWithDiscount>" + nf4.format(pd.getPriceWithDiscount()) + "</PriceWithDiscount>");
			writer.println("		</Stavka>");
		}
		writer.println("	</PrijemnicaStavke>");
		writer.println("</Prijemnica>");
		writer.close();
	}
}
