package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Logger;

import com.mysql.jdbc.Statement;

import rs.petcom.master.dal.Log;
import rs.petcom.master.dal.User;

public class DocumentCorrection {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private long DocumentCorrectionID;
	private int CorrectionTypeID; //1 - Dodat račun, 2 - ponovljena štama fiskala, 3 - prenos na otvoren dan',
	private String FiscalPrinterID;
	private Date Date;
	private long DoucmentID;
	private long WorkDayID;
	private long UserCreated;
	private String ReciptNumber;
	private Date PrintTime;
	private long UserPrinted;
	private Date MoveTime;
	private long UserMoved;
	private long NewWorkDayID;
	public long getDocumentCorrectionID() {
		return DocumentCorrectionID;
	}
	public void setDocumentCorrectionID(long documentCorrectionID) {
		DocumentCorrectionID = documentCorrectionID;
	}
	public int getCorrectionTypeID() {
		return CorrectionTypeID;
	}
	public void setCorrectionTypeID(int correctionTypeID) {
		CorrectionTypeID = correctionTypeID;
	}
	public String getFiscalPrinterID() {
		return FiscalPrinterID;
	}
	public void setFiscalPrinterID(String fiscalPrinterID) {
		FiscalPrinterID = fiscalPrinterID;
	}
	public Date getDate() {
		return Date;
	}
	public void setDate(Date date) {
		Date = date;
	}
	public long getDoucmentID() {
		return DoucmentID;
	}
	public void setDoucmentID(long doucmentID) {
		DoucmentID = doucmentID;
	}
	public long getWorkDayID() {
		return WorkDayID;
	}
	public void setWorkDayID(long workDayID) {
		WorkDayID = workDayID;
	}
	public long getUserCreated() {
		return UserCreated;
	}
	public void setUserCreated(long userCreated) {
		UserCreated = userCreated;
	}
	public String getReciptNumber() {
		return ReciptNumber;
	}
	public void setReciptNumber(String reciptNumber) {
		ReciptNumber = reciptNumber;
	}
	public Date getPrintTime() {
		return PrintTime;
	}
	public void setPrintTime(Date printTime) {
		PrintTime = printTime;
	}
	public long getUserPrinted() {
		return UserPrinted;
	}
	public void setUserPrinted(long userPrinted) {
		UserPrinted = userPrinted;
	}
	public Date getMoveTime() {
		return MoveTime;
	}
	public void setMoveTime(Date moveTime) {
		MoveTime = moveTime;
	}
	public long getUserMoved() {
		return UserMoved;
	}
	public void setUserMoved(long userMoved) {
		UserMoved = userMoved;
	}
	public long getNewWorkDayID() {
		return NewWorkDayID;
	}
	public void setNewWorkDayID(long newWorkDayID) {
		NewWorkDayID = newWorkDayID;
	}
	public static Logger getLogger() {
		return LOGGER;
	}
	public long insert(User user, Connection connection) throws SQLException{
		String query = "insert into documentcorrection("
					 + "	CorrectionTypeID,"
					 + "	FiscalPrinterID,"
					 + "	Date,"
					 + "	DoucmentID,"
					 + "	WorkDayID,"
					 + "	UserCreated,"
					 + "	ReciptNumber,"
					 + "	PrintTime,"
					 + "	UserPrinted,"
					 + "	MoveTime,"
					 + "	UserMoved,"
					 + "	NewWorkDayID)"
					 + " values (?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1,CorrectionTypeID);
		ps.setString(2,FiscalPrinterID);
		ps.setTimestamp(3, new Timestamp(Date.getTime()));
		ps.setLong(4,DoucmentID);
		ps.setLong(5,WorkDayID);
		ps.setLong(6,UserCreated);
		ps.setString(7,ReciptNumber);
		if (PrintTime != null)
			ps.setTimestamp(8, new Timestamp(PrintTime.getTime()));
		else
			ps.setNull(8, java.sql.Types.TIMESTAMP);
		if (UserPrinted > 0)
			ps.setLong(9,UserPrinted);
		else 
			ps.setNull(9, java.sql.Types.BIGINT);
		if (MoveTime != null)
			ps.setTimestamp(10, new Timestamp(MoveTime.getTime()));
		else
			ps.setNull(10, java.sql.Types.TIMESTAMP);
		if (UserMoved > 0)
			ps.setLong(11,UserMoved);
		else 
			ps.setNull(11, java.sql.Types.BIGINT);
		if (NewWorkDayID > 0)
			ps.setLong(12,NewWorkDayID);
		else 
			ps.setNull(12, java.sql.Types.BIGINT);
		
		ps.executeUpdate();
		
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next())
			DocumentCorrectionID = rs.getLong(1);
		
		switch(CorrectionTypeID){
		case 1:
			Log.writeLog(user, "Dodavanje računa u bazu. DocumentCorrectionID=" + DocumentCorrectionID, connection);
			LOGGER.info("Dodavanje računa u bazu. DocumentCorrectionID=" + DocumentCorrectionID + ", Korisnik: " + user.getUserName());
			break;
		case 2:
			Log.writeLog(user, "Ponovljena štampa fiskalnog računa. DocumentCorrectionID=" + DocumentCorrectionID, connection);
			LOGGER.info("Ponovljena štampa fiskalnog računa. DocumentCorrectionID=" + DocumentCorrectionID + ", Korisnik: " + user.getUserName());
			break;
		case 3:
			Log.writeLog(user, "Prenos računa na otvoren dan. DocumentCorrectionID=" + DocumentCorrectionID, connection);
			LOGGER.info("Prenos računa na otvoren dan. DocumentCorrectionID=" + DocumentCorrectionID + ", Korisnik: " + user.getUserName());
			break;
		}
		return DocumentCorrectionID;
	}
	
	public void update(User user, Connection connection) throws SQLException{
		String query = "update documentcorrection set "
					 + "	CorrectionTypeID = ?,"
					 + "	FiscalPrinterID = ?,"
					 + "	Date = ?,"
					 + "	DoucmentID = ?,"
					 + "	WorkDayID = ?,"
					 + "	UserCreated = ?,"
					 + "	ReciptNumber = ?,"
					 + "	PrintTime = ?,"
					 + "	UserPrinted = ?,"
					 + "	MoveTime = ?,"
					 + "	UserMoved = ?,"
					 + "	NewWorkDayID = ?"
					 + " where DocumentCorrectionID = ?";
		PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1,CorrectionTypeID);
		ps.setString(2,FiscalPrinterID);
		ps.setTimestamp(3, new Timestamp(Date.getTime()));
		ps.setLong(4,DoucmentID);
		ps.setLong(5,WorkDayID);
		ps.setLong(6,UserCreated);
		ps.setString(7,ReciptNumber);
		if (PrintTime != null)
			ps.setTimestamp(8, new Timestamp(PrintTime.getTime()));
		else
			ps.setNull(8, java.sql.Types.TIMESTAMP);
		if (UserPrinted > 0)
			ps.setLong(9,UserPrinted);
		else 
			ps.setNull(9, java.sql.Types.BIGINT);
		if (MoveTime != null)
			ps.setTimestamp(10, new Timestamp(MoveTime.getTime()));
		else
			ps.setNull(10, java.sql.Types.TIMESTAMP);
		if (UserMoved > 0)
			ps.setLong(11,UserMoved);
		else 
			ps.setNull(11, java.sql.Types.BIGINT);
		if (NewWorkDayID > 0)
			ps.setLong(12,NewWorkDayID);
		else 
			ps.setNull(12, java.sql.Types.BIGINT);
		
		ps.setLong(13, DocumentCorrectionID);
		ps.executeUpdate();
					
		switch(CorrectionTypeID){
		case 1:
			Log.writeLog(user, "Dodavanje računa u bazu. DocumentCorrectionID=" + DocumentCorrectionID, connection);
			LOGGER.info("Dodavanje računa u bazu. DocumentCorrectionID=" + DocumentCorrectionID + ", Korisnik: " + user.getUserName());
			break;
		case 2:
			Log.writeLog(user, "Ponovljena štampa fiskalnog računa. DocumentCorrectionID=" + DocumentCorrectionID, connection);
			LOGGER.info("Ponovljena štampa fiskalnog računa. DocumentCorrectionID=" + DocumentCorrectionID + ", Korisnik: " + user.getUserName());
			break;
		case 3:
			Log.writeLog(user, "Prenos računa na otvoren dan. DocumentCorrectionID=" + DocumentCorrectionID, connection);
			LOGGER.info("Prenos računa na otvoren dan. DocumentCorrectionID=" + DocumentCorrectionID + ", Korisnik: " + user.getUserName());
			break;
		}
	}
	

	public static DocumentCorrection getDocument(long documentID, Connection connection){
		String query = "select * from documentcorrection dc where dc.DoucmentID = ?";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setLong(1, documentID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				DocumentCorrection dc = new DocumentCorrection();
				dc.DocumentCorrectionID = rs.getLong("DocumentCorrectionID");
				dc.CorrectionTypeID = rs.getInt("CorrectionTypeID");
				dc.FiscalPrinterID = rs.getString("FiscalPrinterID");
				dc.Date = rs.getTimestamp("Date");
				dc.DoucmentID = rs.getLong("DoucmentID");
				dc.WorkDayID = rs.getLong("WorkDayID");
				dc.UserCreated = rs.getLong("UserCreated");
				dc.ReciptNumber = rs.getString("ReciptNumber");
				dc.PrintTime = rs.getTimestamp("PrintTime");
				dc.UserPrinted = rs.getLong("UserPrinted");
				dc.MoveTime = rs.getTimestamp("MoveTime");
				dc.UserMoved = rs.getLong("UserMoved");
				dc.NewWorkDayID = rs.getLong("NewWorkDayID");
				return dc;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;		
	}
}
