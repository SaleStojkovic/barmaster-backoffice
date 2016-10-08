package rs.petcom.master.dal;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DPUList {
	private int DPUListID;
	private Date date;
	private int ObjectID;
	
	public int getDPUListID() {
		return DPUListID;
	}
	public void setDPUListID(int dPUListID) {
		DPUListID = dPUListID;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getObjectID() {
		return ObjectID;
	}
	public void setObjectID(int objectID) {
		ObjectID = objectID;
	}
	@Override
	public String toString() {
		return Settings.getDateFromatter().format(date);
	}
	
	public int insert(Connection connection){
		int result = -1;
		String query = "insert into dpulist(`Date`,ObjectID) values(?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ps.setInt(2, ObjectID);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			DPUListID = rs.getInt(1);
			return DPUListID;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public void update(Connection connection){
		String query = "update dpulist set "
					 + "	`Date` = ?, ObjectID = ?"
					 + " where DPUListID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ps.setInt(2, ObjectID);
			ps.setInt(3, DPUListID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void delete(Connection connection){
		String query = "delete from dpulist where DPUListID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, DPUListID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ObservableList<DPUList> getList(int objectId, Date dateFrom, Date dateTo, Connection connection){
		ObservableList<DPUList> list = FXCollections.observableArrayList();
		String query = "select * from dpulist "
					 + " where ObjectID = ? and Date >= ? and Date <= ?"
					 + " order by `Date` desc";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, objectId);
			ps.setDate(2, new java.sql.Date(dateFrom.getTime()));
			ps.setDate(3, new java.sql.Date(dateTo.getTime()));
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				DPUList dl = new DPUList();
				dl.setDPUListID(rs.getInt("DPUListID"));
				dl.setObjectID(rs.getInt("ObjectID"));
				dl.setDate(new Date(rs.getDate("Date").getTime()));
				list.add(dl);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void exportToXml(String fileName, Connection connection) throws SQLException, FileNotFoundException, UnsupportedEncodingException{
		System.out.println("Export DPU liste");
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		NumberFormat nf2 = Settings.getNumberFormat(2, false);
		NumberFormat nf4 = Settings.getNumberFormat(4, false);
		
		rs.petcom.master.dal.Object o = rs.petcom.master.dal.Object.getById(ObjectID,connection);
		
		
		PreparedStatement ps = connection.prepareStatement("CALL dpu_lista_v2(?,?)");
		ps.setInt(1, ObjectID);
		ps.setDate(2, new java.sql.Date(date.getTime()));
		ResultSet rs = ps.executeQuery();
		
		PrintWriter writer;
		writer = new PrintWriter(fileName, "UTF-8");
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		writer.println("<DpuListExport>");
		writer.println("	<DpuListData>");
		writer.println("		<ObjectCode>" + o.getCode() + "</ObjectCode>");
		writer.println("		<DpuListDate>" + sdf.format(getDate()) + "</DpuListDate>");
		writer.println("	</DpuListData>");
		writer.println("	<DpuListItems>");
		while (rs.next()){
			writer.println("		<DpuListItem>");
			writer.println("			<ProductCode>" + rs.getString("Code") + "</ProductCode>");
			writer.println("			<ProductName>" + rs.getString("Name") + "</ProductName>");
			writer.println("			<Vat>" + nf2.format(rs.getDouble("Vat")) + "</Vat>");
			writer.println("			<JM>" + rs.getString("JM") + "</JM>");
			writer.println("			<PocetnoStanje>" + nf4.format(rs.getDouble("PocetnoStanje")) + "</PocetnoStanje>");
			writer.println("			<Ulaz>" + nf4.format(rs.getDouble("Ulaz")) + "</Ulaz>");

			writer.println("			<Utroseno>" + nf4.format(rs.getDouble("Utroseno")) + "</Utroseno>");
			writer.println("			<Utroseno_davanja>" + nf4.format(rs.getDouble("Utroseno_davanja")) + "</Utroseno_davanja>");
			writer.println("			<Utroseno_rashod>" + nf4.format(rs.getDouble("Utroseno_rashod")) + "</Utroseno_rashod>");
			writer.println("			<KrajnjeStanje>" + nf4.format(rs.getDouble("KrajnjeStanje")) + "</KrajnjeStanje>");
			writer.println("			<Cena>" + nf4.format(rs.getDouble("Cena")) + "</Cena>");
			writer.println("			<Pice>" + nf4.format(rs.getDouble("Pice")) + "</Pice>");
			writer.println("			<Jelo>" + nf4.format(rs.getDouble("Jelo")) + "</Jelo>");
			
			writer.println("		</DpuListItem>");
		}

		writer.println("	</DpuListItems>");
		
		writer.println("	<DnevniIzvestaji>");
		ps = connection.prepareStatement("CALL dpu_specifikacija_di(?)");
		ps.setInt(1, DPUListID);
		rs = ps.executeQuery();
		while(rs.next()){
			writer.println("		<DnevniIzvestaj>");
			writer.println("			<Broj>" + rs.getString("Number") + "</Broj>");
			writer.println("			<Gotovina>" + nf2.format(rs.getDouble("Cash")) + "</Gotovina>");
			writer.println("			<Kartica>" + nf2.format(rs.getDouble("Card")) + "</Kartica>");
			writer.println("			<Cek>" + nf2.format(rs.getDouble("NonFiscal")) + "</Cek>");
			writer.println("		</DnevniIzvestaj>");
		}
		writer.println("	</DnevniIzvestaji>");
		
		writer.println("	<NaloziZaIspravku>");
		ps = connection.prepareStatement("CALL dpu_specifikacija_ni(?,?)");
		ps.setDate(1, new java.sql.Date(date.getTime()));
		ps.setInt(2, ObjectID);
		rs = ps.executeQuery();
		while(rs.next()){
			if (!rs.getString("Number").equals("")){
				writer.println("		<NalogZaIspravku>");
				writer.println("			<Broj>" + rs.getString("Number") + "</Broj>");
				writer.println("			<Gotovina>" + nf2.format(rs.getDouble("Cash")) + "</Gotovina>");
				writer.println("			<Kartica>" + nf2.format(rs.getDouble("Card")) + "</Kartica>");
				writer.println("			<Cek>" + nf2.format(rs.getDouble("NonFiscal")) + "</Cek>");
				writer.println("		</NalogZaIspravku>");
			}
		}
		writer.println("	</NaloziZaIspravku>");
		
		writer.println("	<RucnoPisaniRacuni>");
		ps = connection.prepareStatement("CALL dpu_specifikacija_rpr(?,?)");
		ps.setDate(1, new java.sql.Date(date.getTime()));
		ps.setInt(2, ObjectID);
		rs = ps.executeQuery();
		while(rs.next()){
			if (!rs.getString("Number").equals("")){
				writer.println("		<RucnoPisaniRacun>");
				writer.println("			<Broj>" + rs.getString("Number") + "</Broj>");
				writer.println("			<Gotovina>" + nf2.format(rs.getDouble("Cash")) + "</Gotovina>");
				writer.println("			<Kartica>" + nf2.format(rs.getDouble("Card")) + "</Kartica>");
				writer.println("			<Cek>" + nf2.format(rs.getDouble("NonFiscal")) + "</Cek>");
				writer.println("		</RucnoPisaniRacun>");
			}
		}
		writer.println("	</RucnoPisaniRacuni>");
		
		writer.println("</DpuListExport>");
		writer.close();
	}
}
