package rs.petcom.master.controller.backoffice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Settings;
import rs.petcom.master.dal.WorkDay;

public class DataExportController  implements ControlledScreen{
	
	ScreensController controller;
	
	StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter = 
            DateTimeFormatter.ofPattern("dd.MM.yyyy");
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }
        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };             
	
	@FXML private DatePicker dateFrom;
	@FXML private DatePicker dateTo;
	@FXML private BorderPane reportPane;
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}
	@Override
	public void setParameter(Object parameter) {}
	@Override
	public Object getParameter() {
		return null;
	}
	@Override
	public void init() {
		dateFrom.setConverter(converter);
		dateTo.setConverter(converter);
		
		dateFrom.setValue(LocalDate.now());
		dateTo.setValue(LocalDate.now());
	}

	@FXML 
	private void export(){
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(controller.getBundle().getString("selectFileForExport"));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showSaveDialog(controller.getRoot().getScene().getWindow());
		
		if (file != null){
			String filePath = file.getAbsolutePath();
			if (!filePath.toUpperCase().endsWith(".XML")) filePath += ".xml";
			
			String query = "select "
						 + "	d.DocumentID as docId,"
						 + "    d.DocumentTypeID,"
						 + "	d.Date as docDate,"
						 + "	d.Number as docNumber,"
						 + "	d.Year as docYear,"
						 + "	d.PaymentMethodID,"
						 + "    dd.DocumentDetailsID,"
						 + "	dd.ProductID,"
						 + "	p.Code,"
						 + "	dd.Quantity,"
						 + "	dd.Price,"
						 + "	dd.Discount "
						 + "  from document d"
						 + "  left join documentdetails dd on dd.DocumentID = d.DocumentID"
						 + "  left join documentdetailsdescription ddd on ddd.DocumentDetailsID = dd.DocumentDetailsID"
						 + "  left join description de on de.DescriptionID = ddd.DescritptionID"
						 + "  left join pointofsale ps on ps.PointOfSaleID = d.PointOfSaleID"
						 + "  left join product p on p.ProductID = dd.ProductID"
						 + " where ps.ObjectID = ?"
						 + "   and d.Date >= ?"
						 + "   and d.Date < ?"
						 + " order by d.DocumentID";
			try {
				PreparedStatement ps = controller.getSettings().getConnection().prepareStatement(query);
				ps.setInt(1, controller.getObject().getObjectID());
				ps.setTimestamp(2, WorkDay.start(dateFrom.getValue(), controller.getSettings().getConnection()));
				ps.setTimestamp(3, WorkDay.end(dateTo.getValue(), controller.getSettings().getConnection()));
				
				ResultSet rs = ps.executeQuery();
				
				PrintWriter writer;
				writer = new PrintWriter(filePath, "UTF-8");
				writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
				writer.println("<DocumentExport>");
				writer.println("	<ExportHeader>");
				writer.println("		<ExportDateFrom>" + converter.toString(dateFrom.getValue()) + "</ExportDateFrom>");
				writer.println("		<ExportDateTo>" + converter.toString(dateTo.getValue()) + "</ExportDateTo>");
				writer.println("		<ObjectID>" + controller.getObject().getObjectID() + "</ObjectID>");	
				writer.println("		<ObjectCode>" + controller.getObject().getCode() + "</ObjectCode>");			
				writer.println("		<ObjectName>" + controller.getObject().getName() + "</ObjectName>");	
				writer.println("	</ExportHeader>");
				writer.println("	<Documents>");	
				long documentId = -1;
				long documentDetailId = -1;
				while (rs.next()){
					if ((rs.getLong("docId") != documentId) && (documentId != -1)) {
						if (documentDetailId != -1)
							writer.println("			</DocumentDetailes>");
						writer.println("		</Document>");
						documentDetailId = -1;
					}
					if (rs.getLong("docId") != documentId ){
						writer.println("		<Document>");
						documentId = rs.getLong("docId");
						writer.println("			<DocumentID>"+ String.valueOf(documentId) + "</DocumentID>" );
						writer.println("			<DocumentTypeID>"+ String.valueOf(rs.getInt("DocumentTypeID")) + "</DocumentTypeID>" );
						writer.println("			<DocumentDate>"+ rs.getTimestamp("docDate").toString() + "</DocumentDate>" );
						writer.println("			<DocumentNumber>"+ String.valueOf(rs.getInt("docNumber")) + "</DocumentNumber>" );
						writer.println("			<DocumentYear>"+ String.valueOf(rs.getInt("docYear")) + "</DocumentYear>" );
						writer.println("			<PaymentMethodID>"+ String.valueOf(rs.getInt("PaymentMethodID")) + "</PaymentMethodID>" );
					}
					
					if ((rs.getLong("DocumentDetailsID") != documentDetailId) && (documentDetailId != -1)) 
						writer.println("			</DocumentDetailes>");
					if (rs.getLong("DocumentDetailsID") != documentDetailId ){	
						documentDetailId = rs.getLong("DocumentDetailsID");
						writer.println("			<DocumentDetailes>");
						writer.println("				<DocumentDetailesID>" + String.valueOf(documentDetailId) + "</DocumentDetailesID>");	
						writer.println("				<ProductID>" + String.valueOf(rs.getLong("ProductID")) + "</ProductID>");	
						writer.println("				<ProductCode>" + String.valueOf(rs.getString("Code")) + "</ProductCode>");
						writer.println("				<Quantity>" + Settings.getNumberFormat(4, false).format(rs.getDouble("Quantity")) + "</Quantity>");	
						writer.println("				<Price>" + Settings.getNumberFormat(2, false).format(rs.getDouble("Price")) + "</Price>");	
						writer.println("				<Discount>" + Settings.getNumberFormat(2, false).format(rs.getDouble("Discount")) + "</Discount>");	
					}
				}
				if (documentDetailId != -1) 	
					writer.println("			</DocumentDetailes>");
				if (documentId != -1) 
					writer.println("		</Document>");
				writer.println("	</Documents>");
				writer.println("</DocumentExport>");
				writer.close();
			} catch (SQLException | FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
