package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Window;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Department;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.SQLHelper;
import rs.petcom.master.dal.person.Address;
import rs.petcom.master.dal.person.Person;
import rs.petcom.print.JRPrintPreviewPane;

public class Document {
	private long DocumentID;
	private int DocumentTypeID;
	private int DocumentStatusID;
	private int PointOfSaleID;
	private int Number;
	private int Year;
	private String FullNumber;
	private String AlternativeFullNumber;
	private Date Date;
	private Date DeliveryDate;
	private Date CurrencyDate;
	private Date DateFrom;
	private Date DateTo;
	private String Note;
	private String Place;
	private long PersonID;
	private int WarehouseID;
	private int PaymentMethodID;
	private long LoyalityID;
	private double Sum;
	private double SumWithDiscount;
	private double Paid;
	private String ExternalNumber;
	private boolean FromCashBox;
	private long WorkDayID;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	private int DepartmentID;
	private double Discount;

	private String PersonName;

	public long getDocumentID() {
		return DocumentID;
	}

	public void setDocumentID(long documentID) {
		DocumentID = documentID;
	}

	public int getDocumentTypeID() {
		return DocumentTypeID;
	}

	public void setDocumentTypeID(int documentTypeID) {
		DocumentTypeID = documentTypeID;
	}

	public int getDocumentStatusID() {
		return DocumentStatusID;
	}

	public void setDocumentStatusID(int documentStatusID) {
		DocumentStatusID = documentStatusID;
	}

	public int getPointOfSaleID() {
		return PointOfSaleID;
	}

	public void setPointOfSaleID(int pointOfSaleID) {
		PointOfSaleID = pointOfSaleID;
	}

	public int getNumber() {
		return Number;
	}

	public void setNumber(int number) {
		Number = number;
	}

	public int getYear() {
		return Year;
	}

	public void setYear(int year) {
		Year = year;
	}

	public String getFullNumber() {
		return FullNumber;
	}

	public void setFullNumber(String fullNumber) {
		FullNumber = fullNumber;
	}

	public String getAlternativeFullNumber() {
		return AlternativeFullNumber;
	}

	public void setAlternativeFullNumber(String alternativeFullNumber) {
		AlternativeFullNumber = alternativeFullNumber;
	}

	public Date getDate() {
		return Date;
	}

	public void setDate(Date date) {
		Date = date;
	}

	public Date getDeliveryDate() {
		return DeliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		DeliveryDate = deliveryDate;
	}

	public Date getCurrencyDate() {
		return CurrencyDate;
	}

	public void setCurrencyDate(Date currencyDate) {
		CurrencyDate = currencyDate;
	}

	public Date getDateFrom() {
		return DateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		DateFrom = dateFrom;
	}

	public Date getDateTo() {
		return DateTo;
	}

	public void setDateTo(Date dateTo) {
		DateTo = dateTo;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getPlace() {
		return Place;
	}

	public void setPlace(String place) {
		Place = place;
	}

	public long getPersonID() {
		return PersonID;
	}

	public void setPersonID(long personID) {
		PersonID = personID;
	}

	public int getWarehouseID() {
		return WarehouseID;
	}

	public void setWarehouseID(int warehouseID) {
		WarehouseID = warehouseID;
	}

	public int getPaymentMethodID() {
		return PaymentMethodID;
	}

	public void setPaymentMethodID(int paymentMethodID) {
		PaymentMethodID = paymentMethodID;
	}

	public long getLoyalityID() {
		return LoyalityID;
	}

	public void setLoyalityID(long loyalityID) {
		LoyalityID = loyalityID;
	}

	public double getDiscount() {
		return Discount;
	}

	public void setDiscount(double discount) {
		Discount = discount;
	}

	public double getSum() {
		return Sum;
	}

	public void setSum(double sum) {
		Sum = sum;
	}

	public double getSumWithDiscount() {
		return SumWithDiscount;
	}

	public void setSumWithDiscount(double sumWithDiscount) {
		SumWithDiscount = sumWithDiscount;
	}

	public double getPaid() {
		return Paid;
	}

	public void setPaid(double paid) {
		Paid = paid;
	}

	public String getExternalNumber() {
		return ExternalNumber;
	}

	public void setExternalNumber(String externalNumber) {
		ExternalNumber = externalNumber;
	}

	public boolean isFromCashBox() {
		return FromCashBox;
	}

	public void setFromCashBox(boolean fromCashBox) {
		FromCashBox = fromCashBox;
	}

	public long getWorkDayID() {
		return WorkDayID;
	}

	public void setWorkDayID(long workDayID) {
		WorkDayID = workDayID;
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

	public int getDepartmentID() {
		return DepartmentID;
	}

	public void setDepartmentID(int departmentID) {
		DepartmentID = departmentID;
	}

	public String getPersonName() {
		return PersonName;
	}

	public void setPersonName(String PersonName) {
		this.PersonName = PersonName;
	}

	public long insert(Connection connection) {
		String query = " INSERT INTO document( " + "		`DocumentID`," + "		`DocumentTypeID`,"
				+ "		`DocumentStatusID`," + "		`PointOfSaleID`," + "		`Number`," + "		`Year`,"
				+ "		`FullNumber`," + "		`AlternativeFullNumber`," + "		`Date`," + "		`DeliveryDate`,"
				+ "		`CurrencyDate`," + "		`DateFrom`," + "		`DateTo`," + "		`Note`,"
				+ "		`Place`," + "		`PersonID`," + "		`WarehouseID`," + "		`PaymentMethodID`,"
				+ "		`LoyalityID`," + "		`Discount`," + "		`Sum`," + "		`SumWithDiscount`,"
				+ "		`Paid`," + "		`ExternalNumber`," + "		`FromCashBox`," + "		`WorkDayID`,"
				+ "		`sysDTCreated`," + "		`sysDTEdit`," + "		`sysUserAddID`,"
				+ "		`sysUserEditID`," + "		`DepartmentID`)"
				+ " VALUES (null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

			ps.setInt(1, DocumentTypeID);
			ps.setInt(2, DocumentStatusID);
			if (PointOfSaleID > 0)
				ps.setInt(3, PointOfSaleID);
			else
				ps.setNull(3, java.sql.Types.INTEGER);
			ps.setInt(4, Number);
			ps.setInt(5, Year);
			ps.setString(6, FullNumber);
			ps.setString(7, AlternativeFullNumber);
			if (Date == null) Date = new Date();
			ps.setTimestamp(8, new Timestamp(Date.getTime()));
			try {
				ps.setTimestamp(9, new Timestamp(DeliveryDate.getTime()));
			} catch (NullPointerException e) {
				ps.setNull(9, java.sql.Types.TIMESTAMP);
			}
			try {
				ps.setTimestamp(10, new Timestamp(CurrencyDate.getTime()));
			} catch (NullPointerException e) {
				ps.setNull(10, java.sql.Types.TIMESTAMP);
			}
			try {
				ps.setTimestamp(11, new Timestamp(DateFrom.getTime()));
			} catch (NullPointerException e) {
				ps.setNull(11, java.sql.Types.TIMESTAMP);
			}
			try {
				ps.setTimestamp(12, new Timestamp(DateTo.getTime()));
			} catch (NullPointerException e) {
				ps.setNull(12, java.sql.Types.TIMESTAMP);
			}
			ps.setString(13, Note);
			ps.setString(14, Place);
			if (PersonID != 0)
				ps.setLong(15, PersonID);
			else
				ps.setNull(15, java.sql.Types.BIGINT);
			if (WarehouseID != 0)
				ps.setLong(16, WarehouseID);
			else
				ps.setNull(16, java.sql.Types.BIGINT);
			if (PaymentMethodID != 0)
				ps.setInt(17, PaymentMethodID);
			else
				ps.setNull(17, java.sql.Types.INTEGER);
			if (LoyalityID != 0)
				ps.setLong(18, LoyalityID);
			else
				ps.setNull(18, java.sql.Types.BIGINT);
			ps.setDouble(19, Discount);
			ps.setDouble(20, Sum);
			ps.setDouble(21, SumWithDiscount);
			ps.setDouble(22, Paid);
			ps.setString(23, ExternalNumber);
			ps.setBoolean(24, FromCashBox);
			if (WorkDayID != 0)
				ps.setLong(25, WorkDayID);
			else
				ps.setNull(25, java.sql.Types.BIGINT);
			try {
				ps.setTimestamp(26, new Timestamp(sysDTCreated.getTime()));
			} catch (NullPointerException e) {
				ps.setTimestamp(26, new Timestamp(new Date().getTime()));
			}
			try {
				ps.setTimestamp(27, new Timestamp(sysDTEdit.getTime()));
			} catch (NullPointerException e) {
				ps.setNull(27, java.sql.Types.TIMESTAMP);
			}
			ps.setLong(28, sysUserAddID);
			if (sysUserEditID != 0)
				ps.setLong(29, sysUserEditID);
			else
				ps.setNull(29, java.sql.Types.BIGINT);
			if (DepartmentID != 0)
				ps.setInt(30, DepartmentID);
			else
				ps.setNull(30, java.sql.Types.INTEGER);

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			DocumentID = rs.getLong(1);
			return DocumentID;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public void update(ScreensController controller) throws SQLException {
		String query = " UPDATE document SET  " + "		`DocumentTypeID` = ?," + "		`DocumentStatusID` = ?,"
				+ "		`PointOfSaleID` = ?," + "		`Number` = ?," + "		`Year` = ?,"
				+ "		`FullNumber` = ?," + "		`AlternativeFullNumber` = ?," + "		`Date` = ?,"
				+ "		`DeliveryDate` = ?," + "		`CurrencyDate` = ?," + "		`DateFrom` = ?,"
				+ "		`DateTo` = ?," + "		`Note` = ?," + "		`Place` = ?," + "		`PersonID` = ?,"
				+ "		`WarehouseID` = ?," + "		`PaymentMethodID` = ?," + "		`LoyalityID` = ?,"
				+ "		`Discount` = ?," + "		`Sum` = ?," + "		`SumWithDiscount` = ?," + "		`Paid` = ?,"
				+ "		`ExternalNumber` = ?," + "		`FromCashBox` = ?," + "		`WorkDayID` = ?,"
				+ "		`sysDTEdit` = ?," + "		`sysUserEditID` = ?," + "		`DepartmentID` = ?"
				+ " WHERE `DocumentID` = ?";

		PreparedStatement ps = controller.getSettings().getConnection().prepareStatement(query);

		ps.setInt(1, DocumentTypeID);
		ps.setInt(2, DocumentStatusID);
		if (PointOfSaleID > 0)
			ps.setInt(3, PointOfSaleID);
		else
			ps.setNull(3, java.sql.Types.INTEGER);
		ps.setInt(4, Number);
		ps.setInt(5, Year);
		ps.setString(6, FullNumber);
		ps.setString(7, AlternativeFullNumber);
		ps.setTimestamp(8, new Timestamp(Date.getTime()));
		if (DeliveryDate != null)
			ps.setTimestamp(9, new Timestamp(DeliveryDate.getTime()));
		else
			ps.setNull(9, java.sql.Types.TIMESTAMP);
		if (CurrencyDate != null)
			ps.setTimestamp(10, new Timestamp(CurrencyDate.getTime()));
		else
			ps.setNull(10, java.sql.Types.TIMESTAMP);
		try {
			ps.setTimestamp(11, new Timestamp(DateFrom.getTime()));
		} catch (NullPointerException e) {
			ps.setNull(11, java.sql.Types.TIMESTAMP);
		}
		try {
			ps.setTimestamp(12, new Timestamp(DateTo.getTime()));
		} catch (NullPointerException e) {
			ps.setNull(12, java.sql.Types.TIMESTAMP);
		}
		ps.setString(13, Note);
		ps.setString(14, Place);
		if (PersonID != 0)
			ps.setLong(15, PersonID);
		else
			ps.setNull(15, java.sql.Types.BIGINT);
		if (WarehouseID != 0)
			ps.setLong(16, WarehouseID);
		else
			ps.setNull(16, java.sql.Types.BIGINT);
		if (PaymentMethodID > 0)
			ps.setInt(17, PaymentMethodID);
		else
			ps.setNull(17, java.sql.Types.INTEGER);
		if (LoyalityID != 0)
			ps.setLong(18, LoyalityID);
		else
			ps.setNull(18, java.sql.Types.BIGINT);
		ps.setDouble(19, Discount);
		ps.setDouble(20, Sum);
		ps.setDouble(21, SumWithDiscount);
		ps.setDouble(22, Paid);
		ps.setString(23, ExternalNumber);
		ps.setBoolean(24, FromCashBox);
		if (WorkDayID > 0)
			ps.setLong(25, WorkDayID);
		else
			ps.setNull(25, java.sql.Types.BIGINT);
		ps.setTimestamp(26, new Timestamp(new Date().getTime()));
		ps.setLong(27, controller.getUser().getUserID());
		if (DepartmentID > 0)
			ps.setInt(28, DepartmentID);
		else
			ps.setNull(28, java.sql.Types.INTEGER);
		ps.setLong(29, DocumentID);

		ps.executeUpdate();
	}

	public static void delete(Long documentId, Connection connection) throws SQLException {
		String query = "delete from documentdetails where DocumentID = ?";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setLong(1, documentId);
		ps.executeUpdate();

		query = "delete from document where DocumentID = ?";
		ps = connection.prepareStatement(query);
		ps.setLong(1, documentId);
		ps.executeUpdate();
	}

	public static Document getByID(long documetnId, Connection connection) {
		String query = "select d.*," + "       p.Name as PersonName" + "  from document d"
				+ "  left join documenttype dt on d.DocumentTypeID = dt.DocumentTypeID"
				+ "  left join person p on p.PersonID = d.PersonID"
				+ "  left join pointofsale ps on d.PointOfSaleID = ps.PointOfSaleID " + " where d.DocumentID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, documetnId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Document d = new Document();
			d.DocumentID = rs.getLong("DocumentID");
			d.DocumentTypeID = rs.getInt("DocumentTypeID");
			d.DocumentStatusID = rs.getInt("DocumentStatusID");
			d.PointOfSaleID = rs.getInt("PointOfSaleID");
			d.Number = rs.getInt("Number");
			d.Year = rs.getInt("Year");
			d.FullNumber = rs.getString("FullNumber");
			d.AlternativeFullNumber = rs.getString("AlternativeFullNumber");
			d.Date = new Date(rs.getTimestamp("Date").getTime());
			try {
				d.DeliveryDate = new Date(rs.getTimestamp("DeliveryDate").getTime());
			} catch (NullPointerException e) {
			}
			try {
				d.CurrencyDate = new Date(rs.getTimestamp("CurrencyDate").getTime());
			} catch (NullPointerException e) {
			}
			try {
				d.DateFrom = new Date(rs.getDate("DateFrom").getTime());
			} catch (NullPointerException e) {
			}
			try {
				d.DateTo = new Date(rs.getDate("DateTo").getTime());
			} catch (NullPointerException e) {
			}
			d.Note = rs.getString("Note");
			d.Place = rs.getString("Place");
			d.PersonID = rs.getLong("PersonID");
			d.WarehouseID = rs.getInt("WarehouseID");
			d.PaymentMethodID = rs.getInt("PaymentMethodID");
			d.LoyalityID = rs.getLong("LoyalityID");
			d.Sum = rs.getDouble("Sum");
			d.SumWithDiscount = rs.getDouble("SumWithDiscount");
			d.Paid = rs.getDouble("Paid");
			d.ExternalNumber = rs.getString("ExternalNumber");
			d.FromCashBox = rs.getBoolean("FromCashBox");
			d.WorkDayID = rs.getLong("WorkDayID");
			d.sysDTCreated = new Date(rs.getTimestamp("sysDTCreated").getTime());
			try {
				d.sysDTEdit = new Date(rs.getTimestamp("sysDTEdit").getTime());
				;
			} catch (NullPointerException e) {
			}
			d.sysUserAddID = rs.getLong("sysUserAddID");
			d.sysUserEditID = rs.getLong("sysUserEditID");
			try {
				d.DepartmentID = rs.getInt("DepartmentID");
			} catch (NullPointerException e) {
			}
			d.Discount = rs.getDouble("Discount");
			d.PersonName = rs.getString("PersonName");

			return d;
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<Document> getList(int type, int objectId, Date startDate, Date endDate,
			Connection connection) {
		ArrayList<Document> list = new ArrayList<>();

		String query = "select d.*," + "       p.Name as PersonName" + "  from document d"
				+ "  inner join workday w on w.WorkDayID = d.WorkDayID and w.Date >= ? and w.Date <= ?"
				+ "  left join documenttype dt on d.DocumentTypeID = dt.DocumentTypeID"
				+ "  left join person p on p.PersonID = d.PersonID"
				+ "  left join pointofsale ps on d.PointOfSaleID = ps.PointOfSaleID " + " where dt.DocumentTypeID = ?"
				+ "   and ps.ObjectID = ?";

		if (DocumentType.getById(type, connection).getCode().equals("R"))
			query += "   and d.DocumentID not in (select dr.DocumentParentID"
					+ "  							  from documentrelationship dr"
					+ "							 where dr.RelationshipTypeID = 2 "
					+ "							   and dr.Active = true)";

		query += " order by d.Date desc";

		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setTimestamp(1, new Timestamp(startDate.getTime()));
			ps.setTimestamp(2, new Timestamp(endDate.getTime()));
			ps.setInt(3, type);
			ps.setInt(4, objectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Document d = new Document();
				d.DocumentID = rs.getLong("DocumentID");
				d.DocumentTypeID = rs.getInt("DocumentTypeID");
				d.DocumentStatusID = rs.getInt("DocumentStatusID");
				d.PointOfSaleID = rs.getInt("PointOfSaleID");
				d.Number = rs.getInt("Number");
				d.Year = rs.getInt("Year");
				d.FullNumber = rs.getString("FullNumber");
				d.AlternativeFullNumber = rs.getString("AlternativeFullNumber");
				d.Date = new Date(rs.getTimestamp("Date").getTime());
				d.DeliveryDate = new Date(rs.getTimestamp("DeliveryDate").getTime());
				d.CurrencyDate = new Date(rs.getTimestamp("CurrencyDate").getTime());
				try {
					d.DateFrom = new Date(rs.getDate("DateFrom").getTime());
				} catch (NullPointerException e) {
				}
				try {
					d.DateTo = new Date(rs.getDate("DateTo").getTime());
				} catch (NullPointerException e) {
				}
				d.Note = rs.getString("Note");
				d.Place = rs.getString("Place");
				d.PersonID = rs.getLong("PersonID");
				d.WarehouseID = rs.getInt("WarehouseID");
				d.PaymentMethodID = rs.getInt("PaymentMethodID");
				d.LoyalityID = rs.getLong("LoyalityID");
				d.Sum = rs.getDouble("Sum");
				d.SumWithDiscount = rs.getDouble("SumWithDiscount");
				d.Paid = rs.getDouble("Paid");
				d.ExternalNumber = rs.getString("ExternalNumber");
				d.FromCashBox = rs.getBoolean("FromCashBox");
				d.WorkDayID = rs.getLong("WorkDayID");
				d.sysDTCreated = new Date(rs.getTimestamp("sysDTCreated").getTime());
				try {
					d.sysDTEdit = new Date(rs.getTimestamp("sysDTEdit").getTime());
					;
				} catch (NullPointerException e) {
				}
				d.sysUserAddID = rs.getLong("sysUserAddID");
				d.sysUserEditID = rs.getLong("sysUserEditID");
				try {
					d.DepartmentID = rs.getInt("DepartmentID");
				} catch (NullPointerException e) {
				}
				d.Discount = rs.getDouble("Discount");
				d.PersonName = rs.getString("PersonName");
				list.add(d);
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ObservableList<Document> getObeservableList(int type, int objectId, Date startDate, Date endDate,
			Connection connection) {
		ObservableList<Document> list = FXCollections.observableArrayList();
		for (Document d : Document.getList(type, objectId, startDate, endDate, connection)) {
			list.add(d);
		}
		return list;
	}

	public static ObservableList<Document> getObeservableListNalogIspravka(int type, rs.petcom.master.dal.Object object,
			PointOfSale pos, Date date, boolean prikaziNI , Connection connection) {
		ObservableList<Document> list = FXCollections.observableArrayList();

		String query = "select d.*," + "       p.Name as PersonName" + "  from document d"
				+ "  left join documenttype dt on d.DocumentTypeID = dt.DocumentTypeID"
				+ "  left join workday w on w.WorkDayID = d.WorkDayID"
				+ "  left join person p on p.PersonID = d.PersonID"
				+ "  left join pointofsale ps on d.PointOfSaleID = ps.PointOfSaleID "
				+ "  left join paymentmethod pm on pm.PaymentMethodID = d.PaymentMethodID"
				+ "  where dt.DocumentTypeID = ?"
				+ "  and ps.ObjectID = ?";
				//+ "  and d.CurrencyDate = ?";
		if (DocumentType.getById(type, connection).getCode().equals("NI")) {
			if (prikaziNI) {
				query += "   and d.DocumentID not in (select dr.DocumentParentID"
						+ "  							  from documentrelationship dr"
						+ "							 where dr.RelationshipTypeID = 2 "
						+ "							   and dr.Active = true)";
			}
			query += "   and w.Date = ?";
		} else {
			query += "   and d.Date >= ? and d.Date < DATE_ADD(?, INTERVAL 1 DAY)  ";
		}
		
		if (pos.getPointOfSaleID() != 0)
			query += " and ps.PointOfSaleID = " + pos.getPointOfSaleID();

		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, type);
			ps.setInt(2, object.getObjectID());
			ps.setDate(3, new java.sql.Date(date.getTime()));
			if (!DocumentType.getById(type, connection).getCode().equals("NI")) {
				ps.setDate(4, new java.sql.Date(date.getTime()));
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Document d = new Document();
				d.DocumentID = rs.getLong("DocumentID");
				d.DocumentTypeID = rs.getInt("DocumentTypeID");
				d.DocumentStatusID = rs.getInt("DocumentStatusID");
				d.PointOfSaleID = rs.getInt("PointOfSaleID");
				d.Number = rs.getInt("Number");
				d.Year = rs.getInt("Year");
				d.FullNumber = rs.getString("FullNumber");
				d.AlternativeFullNumber = rs.getString("AlternativeFullNumber");
				d.Date = new Date(rs.getTimestamp("Date").getTime());
				d.DeliveryDate = new Date(rs.getTimestamp("DeliveryDate").getTime());
				d.CurrencyDate = new Date(rs.getTimestamp("CurrencyDate").getTime());
				try {
					d.DateFrom = new Date(rs.getDate("DateFrom").getTime());
				} catch (NullPointerException e) {
				}
				try {
					d.DateTo = new Date(rs.getDate("DateTo").getTime());
				} catch (NullPointerException e) {
				}
					d.Note = rs.getString("Note");
					d.Place = rs.getString("Place");
					d.PersonID = rs.getLong("PersonID");
					d.WarehouseID = rs.getInt("WarehouseID");
					d.PaymentMethodID = rs.getInt("PaymentMethodID");
					d.LoyalityID = rs.getLong("LoyalityID");
					d.Sum = rs.getDouble("Sum");
					d.SumWithDiscount = rs.getDouble("SumWithDiscount");
					d.Paid = rs.getDouble("Paid");
					d.ExternalNumber = rs.getString("ExternalNumber");
					d.FromCashBox = rs.getBoolean("FromCashBox");
					d.WorkDayID = rs.getLong("WorkDayID");
					d.sysDTCreated = new Date(rs.getTimestamp("sysDTCreated").getTime());
				try {
					d.sysDTEdit = new Date(rs.getTimestamp("sysDTEdit").getTime());
					;
				} catch (NullPointerException e) {
				}
					d.sysUserAddID = rs.getLong("sysUserAddID");
					d.sysUserEditID = rs.getLong("sysUserEditID");
				try {
					d.DepartmentID = rs.getInt("DepartmentID");
				} catch (NullPointerException e) {
				}
				d.Discount = rs.getDouble("Discount");
				d.PersonName = rs.getString("PersonName");
				try {
					d.DepartmentID = rs.getInt("DepartmentID");
				} catch (NullPointerException e) {
				}
				list.add(d);
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ObservableList<Document> getObeservableList(int type, rs.petcom.master.dal.Object object,
			PointOfSale pos, Date date, boolean osnovniNaciniPlacanja, boolean prikaziNI, Connection connection) {
		ObservableList<Document> list = FXCollections.observableArrayList();

		String query = "select d.*," + "       p.Name as PersonName" + "  from document d"
				+ "  left join documenttype dt on d.DocumentTypeID = dt.DocumentTypeID"
				+ "  left join workday w on w.WorkDayID = d.WorkDayID"
				+ "  left join person p on p.PersonID = d.PersonID"
				+ "  left join pointofsale ps on d.PointOfSaleID = ps.PointOfSaleID "
				+ "  left join paymentmethod pm on pm.PaymentMethodID = d.PaymentMethodID"
				+ " where dt.DocumentTypeID = ?" + "   and ps.ObjectID = ?";

		if (DocumentType.getById(type, connection).getCode().equals("R")) {
			if (!prikaziNI) {
				query += "   and d.DocumentID not in (select dr.DocumentParentID"
						+ "  							  from documentrelationship dr"
						+ "							 where dr.RelationshipTypeID = 2 "
						+ "							   and dr.Active = true)";
			}
			query += "   and w.Date = ?";
		} else {
			query += "   and d.Date >= ? and d.Date < DATE_ADD(?, INTERVAL 1 DAY)  ";
		}
		if (pos.getPointOfSaleID() != 0)
			query += " and ps.PointOfSaleID = " + pos.getPointOfSaleID();

		if (osnovniNaciniPlacanja) {
			query += " and pm.ParentPaymentMethodID is null ";
		}

		query += " order by d.Date desc";

		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, type);
			ps.setInt(2, object.getObjectID());
			ps.setDate(3, new java.sql.Date(date.getTime()));
			if (!DocumentType.getById(type, connection).getCode().equals("R")) {
				ps.setDate(4, new java.sql.Date(date.getTime()));
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Document d = new Document();
				d.DocumentID = rs.getLong("DocumentID");
				d.DocumentTypeID = rs.getInt("DocumentTypeID");
				d.DocumentStatusID = rs.getInt("DocumentStatusID");
				d.PointOfSaleID = rs.getInt("PointOfSaleID");
				d.Number = rs.getInt("Number");
				d.Year = rs.getInt("Year");
				d.FullNumber = rs.getString("FullNumber");
				d.AlternativeFullNumber = rs.getString("AlternativeFullNumber");
				d.Date = new Date(rs.getTimestamp("Date").getTime());
				d.DeliveryDate = new Date(rs.getTimestamp("DeliveryDate").getTime());
				d.CurrencyDate = new Date(rs.getTimestamp("CurrencyDate").getTime());
				try {
					d.DateFrom = new Date(rs.getDate("DateFrom").getTime());
				} catch (NullPointerException e) {
				}
				try {
					d.DateTo = new Date(rs.getDate("DateTo").getTime());
				} catch (NullPointerException e) {
				}
				d.Note = rs.getString("Note");
				d.Place = rs.getString("Place");
				d.PersonID = rs.getLong("PersonID");
				d.WarehouseID = rs.getInt("WarehouseID");
				d.PaymentMethodID = rs.getInt("PaymentMethodID");
				d.LoyalityID = rs.getLong("LoyalityID");
				d.Sum = rs.getDouble("Sum");
				d.SumWithDiscount = rs.getDouble("SumWithDiscount");
				d.Paid = rs.getDouble("Paid");
				d.ExternalNumber = rs.getString("ExternalNumber");
				d.FromCashBox = rs.getBoolean("FromCashBox");
				d.WorkDayID = rs.getLong("WorkDayID");
				d.sysDTCreated = new Date(rs.getTimestamp("sysDTCreated").getTime());
				try {
					d.sysDTEdit = new Date(rs.getTimestamp("sysDTEdit").getTime());
					;
				} catch (NullPointerException e) {
				}
				d.sysUserAddID = rs.getLong("sysUserAddID");
				d.sysUserEditID = rs.getLong("sysUserEditID");
				try {
					d.DepartmentID = rs.getInt("DepartmentID");
				} catch (NullPointerException e) {
				}
				d.Discount = rs.getDouble("Discount");
				d.PersonName = rs.getString("PersonName");
				try {
					d.DepartmentID = rs.getInt("DepartmentID");
				} catch (NullPointerException e) {
				}
				list.add(d);
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void insertParent(int RelationshipTypeID, long parentId, Connection connection){
		String query = "insert into documentrelationship(RelationshipTypeID,DocumentParentID,DocumentChildID,Active,sysUserAddID,sysDTCreated) "
					 + " values(?,?,?,?,?,?) ";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, RelationshipTypeID);
			ps.setLong(2, parentId);
			ps.setLong(3, DocumentID);
			ps.setBoolean(4, true);
			ps.setLong(5, getSysUserAddID());
			ps.setTimestamp(6, new Timestamp(new Date().getTime()));
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printInvoice(Person customer, ScreensController controller, Window owner) {

		String reportFileName = "rs/petcom/master/jasper/Invoice.jasper";

		Map<String, Object> hm = new HashMap<>();
		hm.put("memorandum", controller.getSettings().getSetting("company.memorandum", controller.getPointOfSale()));
		hm.put("customer", customer);
		try {
			hm.put("customerAddress", Address.getList(customer, controller.getSettings().getConnection()).get(0));
		} catch (IndexOutOfBoundsException e) {
		}
		hm.put("document", this);
		hm.put("documentId", DocumentID);
		hm.put("gotovinski", getPaymentMethodID() != 5);

		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm,
					controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);

			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("report", printViewPane);
			controller.showModalDialog("Faktura", ScreensController.DIALOG_PRINT_PREVIEW, hm2, owner);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public static int getNextNumber(int typeId, long posId, ScreensController controller) {
		return SQLHelper.QueryToInt(
				"select max(Number) from document where DocumentTypeID = " + typeId + "   and PointOfSaleID = " + posId,
				controller.getSettings().getConnection(), 1) + 1;
	}
	public static int getNextNumber(int typeId, rs.petcom.master.dal.Object object, ScreensController controller) {
		return SQLHelper.QueryToInt( "select coalesce(max(d.Number) ,0) as Number "
								   + "  from document d"
								   + "  inner join pointofsale p on p.PointOfSaleID = d.PointOfSaleID"
								   + " where DocumentTypeID = " + typeId 
								   + "   and p.ObjectID = " + object.getObjectID(),
				controller.getSettings().getConnection(), 1) + 1;
	}

	public static int getNextNumber(int typeId, Department department, ScreensController controller) {
		return SQLHelper.QueryToInt("select max(Number) from document where DocumentTypeID = " + typeId
				+ "   and DepartmentID = " + department.getDepartmentID(), controller.getSettings().getConnection(), 1)
				+ 1;
	}

	public static Date getLastInitialInventory(Department department, Connection connection) {
		String query = "select coalesce(max(d.Date), " + "		        (select min(Date) from document ))"
				+ "  from document d" + " where d.DocumentTypeID = 11" + "   and d.DepartmentID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, department.getDepartmentID());

			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getDate(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public long getParentID(int relationshipId, Connection connection){
		
		String query = "select 	DocumentParentID "
					 + "  from documentrelationship "
					 + " where DocumentChildID = ?"
					 + "   and RelationShipTypeID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, DocumentID);
			ps.setInt(2, relationshipId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getLong("DocumentParentID");
		} catch (SQLException e) {
			return 0;
		}
		
	}
}
