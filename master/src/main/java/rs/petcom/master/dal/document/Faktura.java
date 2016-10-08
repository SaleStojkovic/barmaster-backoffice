package rs.petcom.master.dal.document;

import java.util.HashMap;
import java.util.Map;

import javafx.stage.Window;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.person.Address;
import rs.petcom.master.dal.person.Person;
import rs.petcom.print.JRPrintPreviewPane;

public class Faktura {
	private Document document;
	private long parentDocumentID;
	private Person customer;
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public long getParentDocumentID() {
		return parentDocumentID;
	}
	public void setParentDocumentID(long parentDocumentID) {
		this.parentDocumentID = parentDocumentID;
	}
	public Person getCustomer() {
		return customer;
	}
	public void setCustomer(Person customer) {
		this.customer = customer;
	}

	
	public void print(ScreensController controller, Window owner){
		String reportFileName = "rs/petcom/master/jasper/Invoice.jasper";

		customer = Person.getById(document.getPersonID(), controller.getSettings().getConnection());
		
		Map<String, Object> hm = new HashMap<>();
		hm.put("memorandum", controller.getSettings().getSetting("company.memorandum", controller.getPointOfSale()));
		hm.put("customer", customer);
		try {
			hm.put("customerAddress", Address.getList(customer, controller.getSettings().getConnection()).get(0));
		} catch (IndexOutOfBoundsException e) {}
		
		Document parentDoc = Document.getByID(parentDocumentID, controller.getSettings().getConnection());
		
		hm.put("document", parentDoc);
		hm.put("documentId", parentDocumentID);
		hm.put("gotovinski", false);
		hm.put("docNumber",String.valueOf(document.getNumber()));

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
	
}
