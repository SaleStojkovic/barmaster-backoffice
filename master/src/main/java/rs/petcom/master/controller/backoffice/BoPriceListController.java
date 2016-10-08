package rs.petcom.master.controller.backoffice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.PriceList;
import rs.petcom.master.dal.Settings;
import rs.petcom.print.JRPrintPreviewPane;

public class BoPriceListController  implements ControlledScreen{
	
	ScreensController controller;
	
	@FXML TableView<PriceList> priceListTable;
	@FXML TableColumn<PriceList, String> titleCol;
	@FXML TableColumn<PriceList, Date> dateFromCol;
	@FXML TableColumn<PriceList, Date> dateToCol;
	@FXML TableColumn<PriceList, Integer> priorityCol;
	@FXML BorderPane reportPane;

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
		priceListTable.setItems(PriceList.getObeservableList(controller.getObject(),
															 controller.getSettings().getConnection()));
		
		titleCol.setCellValueFactory(new PropertyValueFactory<PriceList, String>("Title"));
		
		dateFromCol.setCellValueFactory(new PropertyValueFactory<PriceList, Date>("DateFrom"));
		dateFromCol.setCellFactory((TableColumn<PriceList, Date> column) -> {
			   return new TableCell<PriceList, Date>() {
				      @Override
				      protected void updateItem(Date item, boolean empty) {
				         super.updateItem(item, empty);
				         if (item == null || empty) {
				            setText(null);
				         } else {
				            setText( Settings.getDateFromatter().format(item));
				         }
				      }
				   };
				});
		
		dateToCol.setCellValueFactory(new PropertyValueFactory<PriceList, Date>("DateTo"));
		dateToCol.setCellFactory((TableColumn<PriceList, Date> column) -> {
			   return new TableCell<PriceList, Date>() {
				      @Override
				      protected void updateItem(Date item, boolean empty) {
				         super.updateItem(item, empty);
				         if (item == null || empty) {
				            setText(null);
				         } else {
				            setText( Settings.getDateFromatter().format(item));
				         }
				      }
				   };
				});
		
		priorityCol.setCellValueFactory(new PropertyValueFactory<PriceList, Integer>("Priority"));
		
		priceListTable.setPlaceholder(new Label(""));
		
		priceListTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<PriceList>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends PriceList> c) {
				showPriceList();
			}
		});
	}
	
	@FXML 
	public void showPriceList(){
		String reportFileName = "rs/petcom/master/jasper/PriceListReport.jasper";
		
		Map<String, Object> hm = new HashMap<>();
		hm.put("priceList", priceListTable.getSelectionModel().getSelectedItem().getPriceListID());
		hm.put("podaciCenovnika", "Naziv cenovnika\nVaži od: \nvažiDo\n");
		
		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(JRLoader.getResourceInputStream(reportFileName));
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, hm, controller.getSettings().getConnection());
			JRPrintPreviewPane printViewPane = new JRPrintPreviewPane(jprint);
			reportPane.setCenter(printViewPane);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
