package rs.petcom.master.fical;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Bill;
import rs.petcom.master.dal.Log;
import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.Round;
import rs.petcom.master.dal.RoundItem;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.User;
import rs.petcom.master.dal.Vat;
import rs.petcom.master.dal.WorkDay;
import rs.petcom.master.dal.document.Document;

public class FiscalPrinter {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static String printBill(String fiscalFolder, 
									Bill bill, 
									double gotovina, 
									double kartica, 
									double cek, 
									double virman, 
									String user,
									Connection connection,
									PointOfSale pos) throws IOException{
		
		String result = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
		
		StringBuilder sb = new StringBuilder();
		sb.append("PointOfSale: " + pos.getName() + "\n");
		WorkDay wd = WorkDay.getFromDb(pos, connection);
		sb.append("WorkDay: (" + wd.getWorkDayID() + ") " + Settings.getDateFromatter().format(wd.getDate()) + "\n");
		String filename = sdf.format(new Date());
		
		StringBuilder fiscalSb = new StringBuilder(); 
		
			
		fiscalSb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		fiscalSb.append("<FiscalRecipet>\n");
		
		// popunjavanje liste svih stavki 
		ArrayList<RoundItem> itemList = new ArrayList<>();
		
		for(int r=0;r<bill.getRoundList().size();r++){
			Round round = bill.getRoundList().get(r);
			for (RoundItem roundItem : round.getItemList()){
				addItem(itemList, roundItem);
			}
		}
		Vat v = new Vat();
		
		double sumaStavki = 0;
		for (int i=0;i<itemList.size();i++){
			fiscalSb.append("    <FiscalItem>\n");
			fiscalSb.append("        <Naziv>" + itemList.get(i).getProductDepartment().getProduct().getShortName() + "</Naziv>\n");
			fiscalSb.append("        <Cena>" + itemList.get(i).getProductDepartment().getProduct().getPrice()  + "</Cena>\n");
			fiscalSb.append("        <Kolicina>" + itemList.get(i).getQuantity() + "</Kolicina>\n");
			fiscalSb.append("        <PoreskaGrupa>" + v.getById(itemList.get(i).getProductDepartment().getProduct().getVatID(), connection).getCode() + "</PoreskaGrupa>\n");
			fiscalSb.append("    </FiscalItem>\n");
		
			sb.append("    - " + itemList.get(i).getProductDepartment().getProduct().getShortName() +" > " + 
					  itemList.get(i).getQuantity() + " x " + itemList.get(i).getProductDepartment().getProduct().getPrice() + 
					  " = "  + (itemList.get(i).getQuantity() * itemList.get(i).getProductDepartment().getProduct().getPrice()) + "\n" );
			sumaStavki += (itemList.get(i).getQuantity() * itemList.get(i).getProductDepartment().getProduct().getPrice());
		}
		fiscalSb.append("    <Placanje>\n");
		fiscalSb.append("        <Gotovina>" + gotovina + "</Gotovina>\n");
		fiscalSb.append("        <Cek>" + cek + "</Cek>\n");
		fiscalSb.append("        <Kartica>" + kartica + "</Kartica>\n");
		fiscalSb.append("        <Virman>" + virman + "</Virman>\n");
		fiscalSb.append("    </Placanje>\n");
		fiscalSb.append("    <Konobar>" + user + "</Konobar>\n");
		fiscalSb.append("</FiscalRecipet>\n");
		
		sb.append("Iznos racuna : " + sumaStavki);
		
		try {
			File f = File.createTempFile(filename, ".xml");//new File(pathpriv, pathkom);

			FileWriter  writer  = new FileWriter(f.getAbsoluteFile());
			writer.write(fiscalSb.toString());
			writer.flush();
			writer.close();
			
			f.renameTo(new File(fiscalFolder + filename + ".xml"));
			LOGGER.info("fiskalni račun: "  + "Iznos: " + sumaStavki + " " + fiscalFolder + filename + ".xml");
			if (sumaStavki > 0.0){
				Thread.sleep(400);
				File f2 = new File(fiscalFolder + "fromfp\\" + filename + ".txt");
				int vremeCekanja = 0;
				while(!(f2.exists() && !f2.isDirectory())) { 
					Thread.sleep(200);
					vremeCekanja += 200;
					if (vremeCekanja == 5000){
						LOGGER.warning("Vreme čekanja na štampu računa veće od 5s");
						break;
					}
				}
				result = new String(Files.readAllBytes(Paths.get(f2.getAbsolutePath()))).trim();
			}
		} catch (Exception ee) {
			LOGGER.severe("Problem pri štampi fiskalnog računa;" + ee.getMessage());
			
			File f = new File(fiscalFolder + "\\Err\\" + filename + ".xml");

			FileWriter  writer  = new FileWriter(f.getAbsoluteFile());
			writer.write(fiscalSb.toString());
			writer.flush();
			writer.close();

			User u = new User();
			u.setUserID(1);
			Log.writeLog(u, ee.getMessage(), connection);
		}
		
		
		User u = new User();
		u.setUserID(1);
		Log.writeLog(u, sb.toString(), connection);
		return result;
	}
	
	public static void openCassBox(ScreensController controller){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
		PrintWriter writer;
		try {
			writer = new PrintWriter(controller.getSettings().getFiscalFolder() + sdf.format(new Date())+".xml", "UTF-8");
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			writer.println("<Fioka>-</Fioka>");
			writer.close();
			String log = "PointOfSale: " + controller.getPointOfSale().getName() + "Otvaranje fioke";
			Log.writeLog(controller.getUser(), log , controller.getSettings().getConnection());
			LOGGER.info("Otvaranje fioke");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			LOGGER.severe(e.getMessage());
		}
	}
	
	private static void addItem(ArrayList<RoundItem> list, RoundItem ri){
		// Dodavanje glavnog
		boolean inserted = false;
		/*
		for (int i=0;i<list.size();i++){
			if (list.get(i).getProduct().getProductID() == ri.getProduct().getProductID()){
				list.get(i).setQuantity(list.get(i).getQuantity() + ri.getQuantity());
				inserted = true;
				break;
			}
		}
		if (!inserted)
		*/
		list.add(ri);
		
		// Dodavanje dodatnih
		for (int a=0;a<ri.getAdditionalList().size();a++){
			inserted = false;
			for (int i=0;i<list.size();i++){
				if (list.get(i).getProductDepartment().getProduct().getProductID() == ri.getAdditionalList().get(a).getProductDepartment().getProduct().getProductID()){
					list.get(i).setQuantity(list.get(i).getQuantity() + ri.getAdditionalList().get(a).getQuantity());
					inserted = true;
					break;
				}
			}
			if (!inserted) list.add(ri.getAdditionalList().get(a));
		}
	}

	public static boolean printReport(ScreensController controller, String report){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
		PrintWriter writer;
		try {
			writer = new PrintWriter(controller.getSettings().getFiscalFolder() + sdf.format(new Date())+".xml", "UTF-8");
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			writer.println("<Izvestaj>" + report + "</Izvestaj>");
			writer.close();
			String log = "PointOfSale: " + controller.getPointOfSale().getName() + "\n" +
						 "Izvestaj: " + report;
			Log.writeLog(controller.getUser(), log , controller.getSettings().getConnection());
			switch(report){
			case "1":
				LOGGER.info("Presek stanja");
				break;
			case "2":
				LOGGER.info("Promet artikala");
				break;
			case "3":
				LOGGER.info("Dnevni izveštaj");
				break;
			default:
				LOGGER.info("Izveštaj " + report);
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean printPeriodicni(Date datumOd, Date datumDo, ScreensController controller){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yy");
		PrintWriter writer;
		try {
			writer = new PrintWriter(controller.getSettings().getFiscalFolder() + sdf.format(new Date())+".xml", "UTF-8");
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			
			writer.println("<Periodicni>");
			writer.println("	<FromDate>" + sdf2.format(datumOd) + "</FromDate>");
			writer.println("	<ToDate>" + sdf2.format(datumDo) + "</ToDate>");
			writer.println("</Periodicni>");
			writer.close();
			
			String log = "PointOfSale: " + controller.getPointOfSale().getName() + "\n" +
						 "Periodični izveštaj: \n"
					   + "   DatumOd: " + sdf2.format(datumOd) + "\n"
					   + "   DatumDo: " + sdf2.format(datumDo);
			Log.writeLog(controller.getUser(), log , controller.getSettings().getConnection());
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean printNonFiscal(String fiscalFolder, String[] text){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
		PrintWriter writer;
		try {
			writer = new PrintWriter(fiscalFolder + sdf.format(new Date())+".xml", "UTF-8");
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			writer.println("<NefiskalnaStampa>");
			for (String row : text){
				writer.println("<Linija>" + row + "</Linija>");
			}
			writer.println("</NefiskalnaStampa>");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public static int printCopy(Document document,
									String fiscalFolder,
									User user,
									Connection connection){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
		PrintWriter writer;
		String text = "Kopija fiskalnog računa za dokument: \n" + 
					  "  ID: " + document.getDocumentID() + "\n" + 
					  "  Broj: " + document.getNumber() + "\n" + 
					  "  Date: " + sdf.format(document.getDate()) + "\n\n";
		try {
			writer = new PrintWriter(fiscalFolder + sdf.format(new Date()) + ".xml", "UTF-8");
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			writer.println("<FiscalRecipet>");

			String query = "select "
						 + "	p.ShortName,"
						 + "	dd.PriceWithDiscount,"
						 + "	dd.Quantity,"
						 + "	v.Code"
						 + " from documentdetails dd"
						 + " left join document d on d.DocumentID = dd.DocumentID"
						 + " left join product p on p.ProductID = dd.ProductID"
						 + " left join vat v on v.VatID = p.VatID"
						 + " where dd.DocumentID = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, document.getDocumentID());
			ResultSet rs = ps.executeQuery();
			double gotovina = 0;
			while (rs.next()){
				writer.println("    <FiscalItem>");
				writer.println("        <Naziv>" + rs.getString("ShortName") + "</Naziv>");
				writer.println("        <Cena>" + rs.getDouble("PriceWithDiscount") + "</Cena>");
				writer.println("        <Kolicina>" + rs.getDouble("Quantity") + "</Kolicina>");
				writer.println("        <PoreskaGrupa>" + rs.getString("Code") + "</PoreskaGrupa>");
				writer.println("    </FiscalItem>");
				gotovina += rs.getDouble("PriceWithDiscount") * rs.getDouble("Quantity");
				text += String.format("    %-36s %8.2f x %8.4f = %10.2f \n", 
									  rs.getString("ShortName"),
									  rs.getDouble("PriceWithDiscount"),
									  rs.getDouble("Quantity"),
									  rs.getDouble("Quantity") * rs.getDouble("PriceWithDiscount"));
			}
			writer.println("    <Placanje>");
			writer.println("        <Gotovina>" + gotovina + "</Gotovina>");
			writer.println("        <Cek>0</Cek>");
			writer.println("        <Kartica>0</Kartica>");
			writer.println("        <Virman>0</Virman>");
			writer.println("    </Placanje>");
			writer.println("    <Konobar>" + user.getUserName() + "</Konobar>");
			writer.println("</FiscalRecipet>");
			writer.close();
			
			text += String.format("Ukupan iznos računa: %10.2f",gotovina);
			Log.writeLog(user,text,connection);
			
		} catch (FileNotFoundException | 
				 UnsupportedEncodingException | 
				 SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	public static String getIBFM(String fiscalFolder){
		File f2 = new File(fiscalFolder + "fiscal.config");
		String result = "";
		try {
			result = new String(Files.readAllBytes(Paths.get(f2.getAbsolutePath()))).trim();
		} catch (IOException e) {}
		return result;
	}
	
	public static String getSummaryData(String fiscalFolder) throws IOException{
		String result = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
		String request = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
					   + "<Izvestaj>9</Izvestaj>";
		String filename = sdf.format(new Date());
		File f = File.createTempFile(filename, ".xml");
		FileWriter  writer  = new FileWriter(f.getAbsoluteFile());
		writer.write(request);
		writer.flush();
		writer.close();
		f.renameTo(new File(fiscalFolder + filename + ".xml"));
		
		File f2 = new File(fiscalFolder + "fromfp\\" + filename + ".txt");
		int vremeCekanja = 0;
		while(!(f2.exists() && !f2.isDirectory())) { 
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {}
			vremeCekanja += 200;
			if (vremeCekanja == 10000){
				LOGGER.warning("Vreme čekanja na odgovor kase veće od 10s");
				break;
			}
		}
		result = new String(Files.readAllBytes(Paths.get(f2.getAbsolutePath()))).trim();
		
		return result;
	}
}
