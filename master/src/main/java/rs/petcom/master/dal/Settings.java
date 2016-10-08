package rs.petcom.master.dal;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.TextFormatter;
public class Settings {
	
	private String dbserver = "localhost";
	private String database = "masterdb2";
	private String username = "root";
	private String password = "928374";
	private String fiscalFolder = "";
	private int departmentId = 1;
	private String posPrinter = "";
	private String posFolder = "";
	private String defaultArticleColor = "";
	private String defaultAdditionalColor = "";
	private String defaultDescriptionalColor = "";
	private int defaultQuantity = 1;
	private String posType = "";
	private String posCode = "";
	private int wdProductsReport = 0;
	private int wdOperatorReport = 0;
	private int wdStateReport = 0;
	private int wdDailyReport = 0;
	private int wdPluReport = 0;
	private boolean fullscreen = false;
	private int objectId = 0;
	private int kitchenMonitorId = 0;
	
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private Connection connection;
	
	public String getDbserver() {
		return dbserver;
	}
	public void setDbserver(String dbserver) {
		this.dbserver = dbserver;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPosCode() {
		return posCode;
	}
	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}
	
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public String getFiscalFolder() {
		return fiscalFolder;
	}
	public void setFiscalFolder(String fiscalFolder) {
		this.fiscalFolder = fiscalFolder;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public String getPosPrinter() {
		return posPrinter;
	}
	public void setPosPrinter(String posPrinter) {
		this.posPrinter = posPrinter;
	}
	public String getPosFolder() {
		return posFolder;
	}
	public void setPosFolder(String posFolder) {
		this.posFolder = posFolder;
	}
	public String getDefaultArticleColor() {
		return defaultArticleColor;
	}
	public void setDefaultArticleColor(String defaultArticleColor) {
		this.defaultArticleColor = defaultArticleColor;
	}
	public String getDefaultAdditionalColor() {
		return defaultAdditionalColor;
	}
	public void setDefaultAdditionalColor(String defaultAdditionalColor) {
		this.defaultAdditionalColor = defaultAdditionalColor;
	}
	public String getDefaultDescriptionalColor() {
		return defaultDescriptionalColor;
	}
	public void setDefaultDescriptionalColor(String defaultDescriptionalColor) {
		this.defaultDescriptionalColor = defaultDescriptionalColor;
	}	
	public int getDefaultQuantity() {
		return defaultQuantity;
	}
	public void setDefaultQuantity(int defaultQuantity) {
		this.defaultQuantity = defaultQuantity;
	}
	public String getPosType() {
		return posType;
	}
	public void setPosType(String posType) {
		this.posType = posType;
	}
	public int getWdProductsReport() {
		return wdProductsReport;
	}
	public void setWdProductsReport(int wdProductsReport) {
		this.wdProductsReport = wdProductsReport;
	}
	public int getWdOperatorReport() {
		return wdOperatorReport;
	}
	public void setWdOperatorReport(int wdOperatorReport) {
		this.wdOperatorReport = wdOperatorReport;
	}
	public int getWdStateReport() {
		return wdStateReport;
	}
	public void setWdStateReport(int wdStateReport) {
		this.wdStateReport = wdStateReport;
	}
	public int getWdDailyReport() {
		return wdDailyReport;
	}
	public void setWdDailyReport(int wdDailyReport) {
		this.wdDailyReport = wdDailyReport;
	}
	public int getWdPluReport() {
		return wdPluReport;
	}
	public void setWdPluReport(int wdPluReport) {
		this.wdPluReport = wdPluReport;
	}
	public boolean isFullscreen() {
		return fullscreen;
	}
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}		
	public int getObjectId() {
		return objectId;
	}
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}	
	public int getKitchenMonitorId() {
		return kitchenMonitorId;
	}
	public void setKitchenMonitorId(int kitchenMonitorId) {
		this.kitchenMonitorId = kitchenMonitorId;
	}
	
	public Settings() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		try {
	      CustomLogger.setup();
	    } catch (IOException e) {
	      e.printStackTrace();
	      throw new RuntimeException("Problems with creating the log files");
	    }
		
		LOGGER.setLevel(Level.FINEST);
		LOGGER.info("Program start");

		String line;
		try {
			InputStream fis = new FileInputStream("setup.cnf");
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			
			while ((line = br.readLine()) != null) {
				try{
					String[] red = line.split("=");
					switch (red[0].trim()){
					case "dbserver":					dbserver = red[1].trim(); break;
					case "database":					database = red[1].trim(); break;
					case "posCode":						posCode = red[1].trim(); break;
					case "departmentId":				departmentId = Integer.parseInt(red[1].trim()); break;
					case "fiscalFolder":			 	fiscalFolder = red[1].trim(); break;
					case "posPrinter": 					posPrinter = red[1].trim(); break;
					case "posFolder": 					posFolder = red[1].trim(); break;
					case "defaultArticleColor": 		defaultArticleColor = red[1].trim(); break;
					case "defaultAdditionalColor": 		defaultAdditionalColor = red[1].trim(); break;
					case "defaultDescriptionalColor": 	defaultDescriptionalColor = red[1].trim(); break;
					case "defaultQuantity": 			defaultQuantity = Integer.parseInt(red[1].trim()); break;
					case "posType": 					posType = red[1].trim(); break;
					case "workDayClose.productsReport":	wdProductsReport = Integer.parseInt(red[1].trim()); break;
					case "workDayClose.operatorReport":	wdOperatorReport = Integer.parseInt(red[1].trim()); break;
					case "workDayClose.pluReport": 		wdPluReport = Integer.parseInt(red[1].trim()); break;
					case "workDayClose.stateReport": 	wdStateReport = Integer.parseInt(red[1].trim()); break;
					case "workDayClose.dailyReport": 	wdDailyReport = Integer.parseInt(red[1].trim()); break;
					case "fullscreen": 					fullscreen = red[1].trim().equals("true");break;
					case "objectId": 					objectId = Integer.parseInt(red[1].trim()); break;
					case "kitchenMonitorId":			kitchenMonitorId = Integer.parseInt(red[1].trim()); break;
					}
				} catch (ArrayIndexOutOfBoundsException e){}
			}
			br.close();
			
	            Class.forName("com.mysql.jdbc.Driver").newInstance();
	            connection = DriverManager.getConnection("jdbc:mysql://" + 
	            									     dbserver + "/" + database + 
	            									     "?user=root&password=928374"
	            									     + "&useSSL=false");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static SimpleDateFormat getTimeFromatter(){
		return new SimpleDateFormat("HH:mm:ss");
	}
	public static SimpleDateFormat getDateFromatter(){
		return new SimpleDateFormat("dd.MM.yyyy");
	}
	
	public static SimpleDateFormat getDateTimeFromatter(){
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	}
	
	public static NumberFormat getNumberFormat(int fractionDigits, boolean useGrouping){
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumFractionDigits(fractionDigits);
		nf.setMaximumFractionDigits(fractionDigits);
		nf.setGroupingUsed(useGrouping);
		return nf;
	}
		
	public static UnaryOperator<TextFormatter.Change> getDoubleFilter(){
		DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getNumberInstance();
		String decSep = "" + numberFormat.getDecimalFormatSymbols().getDecimalSeparator();
		
		UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {

            @Override
            public TextFormatter.Change apply(TextFormatter.Change t) {

                if (t.isReplaced()) 
                    if(t.getText().matches("[^0-9]"))
                        t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));
                

                if (t.isAdded()) {
                    if (t.getControlText().contains(decSep)) {
                        if (t.getText().matches("[^0-9]")) {
                            t.setText("");
                        }
                    } else if (t.getText().matches("[^0-9"+decSep+"]")) {
                        t.setText("");
                    }
                }

                return t;
            }
        };
        
        return filter;
	}
	
	public static Date fromLocalDate(LocalDate date) {
	    Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
	    return Date.from(instant);
	  }
	
	public static Date DatefromLocalDateTime(LocalDateTime date) {
	    Instant instant = date.atZone(ZoneId.systemDefault()).toInstant();
	    return Date.from(instant);
	  }
	
	public static LocalDate fromDate(Date date){
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static LocalDateTime LocalDateTimefromDate(Date date){
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	public String getSetting(String setting, PointOfSale pos){
		String query = "select s.actual "
					 + "  from settings s "
					 + " where s.name = ? "
					 + "   and s.PointOfSaleID = ? "
					 + "union all "
					 + "select s.actual"
					 + "  from settings s"
					 + " where s.name = ?"
					 + "   and s.PointOfSaleID is null "
					 + "limit 1";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, setting);
			if (pos != null) 
				ps.setInt(2, pos.getPointOfSaleID());
			else
				ps.setNull(2, java.sql.Types.INTEGER);
			ps.setString(3, setting);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getString(1);
		} catch (SQLException e) {
			return null;
		}		
	}
}
