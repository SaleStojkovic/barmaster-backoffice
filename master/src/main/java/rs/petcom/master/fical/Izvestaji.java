package rs.petcom.master.fical;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rs.petcom.master.dal.PointOfSale;
import rs.petcom.master.dal.WorkDay;

public class Izvestaji {
	
	public static void PrometPoOperaterima(String fiscalFolder, Long userId, int pointOfSalieID, Connection connection){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
		PrintWriter writer;
		try {
			writer = new PrintWriter(fiscalFolder + sdf.format(new Date())+".xml", "UTF-8");
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			writer.println("<NefiskalnaStampa>");
			
			writer.println("<Linija>=====================</Linija>");
			writer.println("<Linija>PROMET PO OPERATERIMA</Linija>");
			writer.println("<Linija>=====================</Linija>");
			
			String upit = "select u.UserName, pm.Code, sum(dd.PriceWithDiscount * dd.Quantity) as Price "
					    + "  from documentdetails dd "
					    + "  left join document d on d.DocumentID = dd.DocumentID"
					    + "  left join user u on u.UserID = d.sysUserAddID "
					    + "  left join paymentmethod pm on pm.PaymentMethodID = d.PaymentMethodID "
					    + " where d.Date > (select min(w1.DateFrom) "
					    + "                   from workday w1 "
					    + "				     where w1.PointOfSaleID = ? "
					    + "				       and w1.DateTo is null)";
			
			if (userId.longValue() != 0){
					upit += "and dd.sysUserAddID = " + userId.longValue();
			}
				  upit += "  group by u.UserName,pm.Code "
					    + "  order by u.UserName, pm.Code";
			try {
				PreparedStatement ps = connection.prepareStatement(upit);
				ps.setInt(1, pointOfSalieID);
				ResultSet rs = ps.executeQuery();
				String operater = "";
				
				double suma = 0;
				double sumaOperater = 0;
				
				while (rs.next()){
					suma += rs.getDouble("Price"); 
					
					if (!operater.equals(rs.getString("UserName"))){
						sumaOperater = 0;
						
						if (!operater.equals("")){
							writer.println(String.format("<Linija>%-10s %9.2f</Linija>",
														 "Uk.operater",
														 sumaOperater));
							writer.println("<Linija>+++++++++++++++++++++</Linija>");
							writer.println("<Linija></Linija>");
						}
						operater = rs.getString("UserName");
						writer.println("<Linija>" + operater + "</Linija>");
					}
					
					sumaOperater += rs.getDouble("Price");
					writer.println(String.format("<Linija> - %-10s %7.2f</Linija>",
												 rs.getString("Code"),
												 rs.getDouble("Price")));
					
				}
				if (!operater.equals("")){
					writer.println(String.format("<Linija>%-10s %9.2f</Linija>",
												 "Uk.operater",
												 sumaOperater));
					writer.println("<Linija>+++++++++++++++++++++</Linija>");
					writer.println("<Linija></Linija>");
				}
				if (userId.longValue() == 0)
					writer.println(String.format("<Linija>%-10s %9.2f</Linija>",
							 					 "Ukupno ",
							 					 suma));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			writer.println("</NefiskalnaStampa>");
			
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void ProdatiArtikli(String fiscalFolder, Long userId, PointOfSale pos, Connection connection){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
		PrintWriter writer;
		try {
			writer = new PrintWriter(fiscalFolder + sdf.format(new Date())+".xml", "UTF-8");
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			writer.println("<NefiskalnaStampa>");
			
			writer.println("<Linija>+++++++++++++++++++++</Linija>");
			writer.println("<Linija>   PRODATI AKTIKLI   </Linija>");
			writer.println("<Linija>+++++++++++++++++++++</Linija>");
			
			String upit = "select "
						+ "		u.UserName,"
						+ "		p.ShortName, "
						+ "		count(*) as Count ,"
						+ "		sum(dd.PriceWithDiscount * dd.Quantity) as Price"
						+ " from documentdetails dd"
						+ " left join document d on d.DocumentID = dd.DocumentID"
						+ " left join user u on d.sysUserAddID = u.UserID"
						+ " inner join workday w on w.WorkDayID = d.WorkDayID and w.Date = ?"
						+ " left join product p on p.ProductID = dd.ProductID"
						+ " where d.DocumentTypeID = 3"
						+ "   and d.PointOfSaleID = ?";
			
			if (userId.longValue() != 0){
					upit += " and dd.sysUserAddID = " + userId.longValue();
			}
				  upit += "  group by u.UserName,p.ShortName "
					    + "  order by u.UserName, p.ShortName";
			try {
				PreparedStatement ps = connection.prepareStatement(upit);
				WorkDay wd = WorkDay.getFromDb(pos, connection);
				ps.setDate(1, new java.sql.Date(wd.getDate().getTime()));
				ps.setInt(2, pos.getPointOfSaleID());
				ResultSet rs = ps.executeQuery();
				String operater = "";
				
				double suma = 0;
				double sumaOperater = 0;
				
				while (rs.next()){
					suma += rs.getDouble("Price"); 
					
					if (!operater.equals(rs.getString("UserName"))){
						sumaOperater = 0;
						
						if (!operater.equals("")){
							writer.println(String.format("<Linija>%-10s %9.2f</Linija>",
														 "Uk.operater",
														 sumaOperater));
							writer.println("<Linija>+++++++++++++++++++++</Linija>");
							writer.println("<Linija></Linija>");
						}
						operater = rs.getString("UserName");
						writer.println("<Linija>" + operater + "</Linija>");
					}
					
					sumaOperater += rs.getDouble("Price");
					writer.println(String.format("<Linija> - %-18s</Linija>", rs.getString("ShortName")));
					writer.println(String.format("<Linija>  %-5.2fx %12.2f</Linija>", 
												 rs.getDouble("Count"),
												 rs.getDouble("Price")));
					
				}
				if (!operater.equals("")){
					writer.println(String.format("<Linija>%-10s %9.2f</Linija>",
												 "Uk.operater",
												 sumaOperater));
					writer.println("<Linija>+++++++++++++++++++++</Linija>");
					writer.println("<Linija></Linija>");
				}
				if (userId.longValue() == 0)
					writer.println(String.format("<Linija>%-10s %9.2f</Linija>",
							 					 "Ukupno ",
							 					 suma));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			writer.println("</NefiskalnaStampa>");
			
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
