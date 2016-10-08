package rs.petcom.print;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rs.petcom.master.dal.Round;
import rs.petcom.master.dal.RoundItem;
import rs.petcom.master.dal.RoundItemDescription;

public class PosPrinter {
	
	private int PrinterID;
	private String Name;
	private String ServerName;
	private int Paperwidth;
	private boolean Wraptext;
	private String SystemPrinterName;
	private boolean Active;
	private int Copies;
	private int Codepage;
	private String Path;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	
	public int getPrinterID() {
		return PrinterID;
	}
	public void setPrinterID(int printerID) {
		PrinterID = printerID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getServerName() {
		return ServerName;
	}
	public void setServerName(String serverName) {
		ServerName = serverName;
	}
	public int getPaperwidth() {
		return Paperwidth;
	}
	public void setPaperwidth(int paperwidth) {
		Paperwidth = paperwidth;
	}
	public boolean isWraptext() {
		return Wraptext;
	}
	public void setWraptext(boolean wraptext) {
		Wraptext = wraptext;
	}
	public String getSystemPrinterName() {
		return SystemPrinterName;
	}
	public void setSystemPrinterName(String systemPrinterName) {
		SystemPrinterName = systemPrinterName;
	}
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	public int getCopies() {
		return Copies;
	}
	public void setCopies(int copies) {
		Copies = copies;
	}
	public int getCodepage() {
		return Codepage;
	}
	public void setCodepage(int codepage) {
		Codepage = codepage;
	}
	public String getPath() {
		return Path;
	}
	public void setPath(String path) {
		Path = path;
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
	
	public static ArrayList<PosPrinter> getAllPrinters(Connection connection){
		ArrayList<PosPrinter> list = new ArrayList<>();
		String query = "select * from printer where Active = true";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				PosPrinter p = new PosPrinter();
				p.setPrinterID(rs.getInt("PrinterID"));
				p.setName(rs.getString("Name"));
				p.setServerName(rs.getString("ServerName"));
				p.setPaperwidth(rs.getInt("Paperwidth"));
				p.setWraptext(rs.getBoolean("Wraptext"));
				p.setSystemPrinterName(rs.getString("SystemPrinterName"));
				p.setActive(rs.getBoolean("Active"));
				p.setCopies(rs.getInt("Copies"));
				p.setCodepage(rs.getInt("Codepage"));
				p.setPath(rs.getString("Path"));
				p.setSysDTCreated(new Date(rs.getTimestamp("sysDTCreated").getTime()));
				try{
					p.setSysDTEdit(new Date(rs.getTimestamp("sysDTEdit").getTime()));
				}catch(NullPointerException e){}
				p.setSysUserAddID(rs.getLong("sysUserAddID"));
				try{
					p.setSysUserEditID(rs.getLong("sysUserEditID"));
				}catch(NullPointerException e){}
				list.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public static boolean printRound(String posFolder,
									String posPrinter,
									Round round,
									String object,
									String pointOfSale,
									String table,
									String department,
									String user,
									Connection connection){
		ArrayList<PosPrinter> allPrinters = PosPrinter.getAllPrinters(connection);
		ArrayList<PosPrinter> printeriZaStampu = new ArrayList<>();
		
		// provera na kojim sve PosPrinterima treba stampati porudžbinu
		for (RoundItem ri : round.getItemList()){ // prolazak kroz sve stavke
			if (ri.getProductDepartment().getPrinterID() > 0){
				// ako je upisan printer prolazak kroz listu printera da se doda u listu za štampu
				for (PosPrinter pp : allPrinters){
					if (pp.PrinterID == ri.getProductDepartment().getPrinterID()){
						boolean postoji = false;
						for (PosPrinter pzs : printeriZaStampu){
							if (pzs.getPrinterID() == pp.PrinterID){
								postoji = true;
								break;
							}
						}
						if (!postoji) printeriZaStampu.add(pp);
					}
				}
			}
		}
		
		boolean result = true;
		for (PosPrinter pp : printeriZaStampu){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
			SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
			PrintWriter writer;
			try {
				Date date = new Date();
				writer = new PrintWriter( pp.Path + pp.getSystemPrinterName() + "_" + 
										  sdf.format(date) + ".xml", "UTF-8");
				writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
				writer.println("<Porudzbina>");
				
				//TODO dovršiti štampu računa
				writer.println("    <Datum>" + sdfDate.format(date) + ".</Datum>");
				writer.println("    <Vreme>" + sdfTime.format(date) + "</Vreme>");
				writer.println("    <ProdajnoMesto>" + object  + "</ProdajnoMesto>");
				writer.println("    <Kasa>" + pointOfSale  + "</Kasa>");
				writer.println("    <Sto>" + table  + "</Sto>");
				writer.println("    <Odeljenje>" + department  + "</Odeljenje>");
				for (RoundItem ri : round.getItemList()){
					writer.println("    <Stavka>");
					writer.println("        <Naziv>" + ri.getProductDepartment().getProduct().getName()  + "</Naziv> ");
					writer.println("        <Kolicina>" + ri.getQuantity()  + "</Kolicina> ");
					writer.println("        <Tip /> ");
					writer.println("    </Stavka>");   
					for(RoundItem ra : ri.getAdditionalList()){
						writer.println("    <Stavka>");
						writer.println("        <Naziv>" + ra.getQuantity() + " x " + ra.getProductDepartment().getProduct().getName() + "</Naziv> ");
						writer.println("        <Kolicina>" + ra.getQuantity() +  "</Kolicina> ");
						writer.println("        <Tip>DODA</Tip>");
						writer.println("    </Stavka>");  
					}
					for(RoundItemDescription d : ri.getDescriptionalList()){
						writer.println("    <Stavka>");
						writer.println("        <Naziv>" + d.getQuantity() + " x " + d.getDescription().getName() + "</Naziv> ");
						writer.println("        <Kolicina>" + d.getQuantity() +  "</Kolicina> ");
						writer.println("        <Tip>DODA</Tip>");
						writer.println("    </Stavka>");  
					}
				}
				writer.println("    <Konobar>" + user + "</Konobar>");   
				writer.println("</Porudzbina>");
				if (writer.checkError()){
					result = false;
				}
					
				writer.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
				result = false;
			}
			
		}
		return result;
	}
}
