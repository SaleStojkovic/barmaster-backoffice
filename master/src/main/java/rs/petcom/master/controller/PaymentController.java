package rs.petcom.master.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import rs.petcom.master.ControlledScreen;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Bill;
import rs.petcom.master.dal.Log;
import rs.petcom.master.dal.PaymentMethod;
import rs.petcom.master.dal.WorkDay;
import rs.petcom.master.dal.document.Document;
import rs.petcom.master.dal.document.DocumentCorrection;
import rs.petcom.master.dal.document.DocumentPayment;
import rs.petcom.master.dal.document.Faktura;
import rs.petcom.master.dal.document.Order;
import rs.petcom.master.dal.document.OrderDetaile;
import rs.petcom.master.dal.person.Person;
import rs.petcom.master.fical.FiscalPrinter;
import rs.petcom.print.PosPrinter;

public class PaymentController implements ControlledScreen {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private ScreensController controller;
	private String stringValue = "";
	private Timeline timerSat;
	private DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getNumberInstance();
	private final SimpleDateFormat formatClock = new SimpleDateFormat("HH:mm:ss");
	private double ukupno = 0, uplaceno = 0, discount = 0, gotovina = 0.0, kartica = 0.0, cek = 0.0, virman = 0.0,
			ostatak = 0.0;
	private int selectedPaymentMethod = -1;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		controller = screenPage;
	}

	@Override
	public void setParameter(Object parameter) {
	}

	@Override
	public Object getParameter() {
		return null;
	}

	@Override
	public void init() {
		timerSat = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				clockLabel.setText(formatClock.format(new Date()));
			}
		}));
		timerSat.setCycleCount(Animation.INDEFINITE);
		timerSat.play();

		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);
		numberFormat.setGroupingUsed(true);

		fillPaymentMethodButtons();
		fillCPaymentMethodButtons();

		ukupno = 0;
		uplaceno = 0;
		discount = 0;
		gotovina = 0.0;
		kartica = 0.0;
		cek = 0.0;
		virman = 0.0;
		ostatak = 0.0;
		stringValue = "";

		try {
			discount = controller.getBillList().get(controller.getActiveBill()).getPerson().getDiscount() / 100;
		} catch (NullPointerException e) {
		}

		ukupno = controller.getBillList().get(controller.getActiveBill()).getSumWithDiscout() * (1 - discount);
		labelUkupno.setText(numberFormat.format(ukupno));
		labelUplaceno.setText(numberFormat.format(uplaceno));
		labelGotovinaValue.setText(numberFormat.format(gotovina));
		labelKarticaValue.setText(numberFormat.format(kartica));
		labelCekValue.setText(numberFormat.format(cek));
		labelVirmanValue.setText(numberFormat.format(virman));
		labelKusur.setText(numberFormat.format(ukupno));
		try {
			labelPerson
					.setText(controller.getBillList().get(controller.getActiveBill()).getPerson().getFirstLastName());
			labelDiscount.setText(numberFormat.format(discount * 100) + " %");
		} catch (NullPointerException e) {
			labelPerson.setText("");
			labelDiscount.setText("");
		}

		userLabel.setText(controller.getUser().getUserName());
		lokacijaLabel.setText(controller.getPointOfSale().getName());
		clockLabel.setText(formatClock.format(new Date()));

		controller.getCurrentBill()
				.setPaymentMethod(PaymentMethod.getByCode("CASH", controller.getSettings().getConnection()));
		paymentButton0.setSelected(true);
		combinedButton.setSelected(false);
		combinedPane.setVisible(false);
	}

	@FXML
	BorderPane rootPane;
	@FXML
	Label userLabel;
	@FXML
	Label lokacijaLabel;
	@FXML
	Label clockLabel;

	@FXML
	Button cancelButton;
	@FXML
	Button postButton;

	@FXML
	ToggleButton paymentButton0, paymentButton1, paymentButton2, paymentButton3, paymentButton4, paymentButton5,
			paymentButton6, paymentButton7;
	@FXML
	ToggleButton cPaymentButton0, cPaymentButton1, cPaymentButton2, cPaymentButton3, cPaymentButton4, cPaymentButton5,
			cPaymentButton6, cPaymentButton7;

	@FXML
	Button numpad0, numpad1, numpad2, numpad3, numpad4, numpad5, numpad6, numpad7, numpad8, numpad9, numpad00,
			numpadDel;

	@FXML
	Label labelUplaceno;
	@FXML
	Label labelKusur;
	@FXML
	Label labelUkupno;
	@FXML
	Label labelPerson;
	@FXML
	Label labelDiscount;

	@FXML
	VBox combinedPane;
	@FXML
	ToggleButton combinedButton;
	@FXML
	Label labelGotovina;
	@FXML
	Label labelGotovinaValue;
	@FXML
	Label Value;
	@FXML
	Label labelKarticaValue;
	@FXML
	Label labelCek;
	@FXML
	Label labelCekValue;
	@FXML
	Label labelVirman;
	@FXML
	Label labelVirmanValue;
	@FXML
	Label labelOstatak;
	@FXML
	Label labelOstatakValue;
	@FXML
	ToggleButton stampaGotovinskog;

	@FXML
	private void numButtonClick(ActionEvent event) {

		String decSep = "" + numberFormat.getDecimalFormatSymbols().getDecimalSeparator();

		String[] red = stringValue.split(decSep);
		if ((red.length == 1) || ((red.length > 1) && (red[1].length() < 2))) {
			System.out.println();
			if (stringValue.equals("0" + decSep + "00"))
				stringValue = "";
			if (((Button) event.getSource()) == numpad0)
				stringValue = stringValue + "0";
			if (((Button) event.getSource()) == numpad1)
				stringValue = stringValue + "1";
			if (((Button) event.getSource()) == numpad2)
				stringValue = stringValue + "2";
			if (((Button) event.getSource()) == numpad3)
				stringValue = stringValue + "3";
			if (((Button) event.getSource()) == numpad4)
				stringValue = stringValue + "4";
			if (((Button) event.getSource()) == numpad5)
				stringValue = stringValue + "5";
			if (((Button) event.getSource()) == numpad6)
				stringValue = stringValue + "6";
			if (((Button) event.getSource()) == numpad7)
				stringValue = stringValue + "7";
			if (((Button) event.getSource()) == numpad8)
				stringValue = stringValue + "8";
			if (((Button) event.getSource()) == numpad9)
				stringValue = stringValue + "9";
			if (((Button) event.getSource()) == numpad00)
				stringValue = stringValue + "00";
		}
		if ((((Button) event.getSource()) == numpadDel) && (stringValue.length() > 0)) {
			stringValue = stringValue.substring(0, stringValue.length() - 1);
			if (stringValue.endsWith(decSep))
				stringValue = stringValue.substring(0, stringValue.length() - 1);
		}

		if (stringValue.equals(""))
			stringValue = "0";

		if (!combinedButton.isSelected()) {
			try {
				uplaceno = numberFormat.parse(stringValue).doubleValue();
			} catch (ParseException e) {
			}
			labelUplaceno.setText(numberFormat.format(uplaceno));
			labelKusur.setText(numberFormat.format(uplaceno - ukupno));
		}
		if (selectedPaymentMethod == 0) {
			try {
				gotovina = numberFormat.parse(stringValue).doubleValue();
				ostatak = ukupno - gotovina - kartica - cek - virman;
			} catch (ParseException e) {
				Log.writeLog(controller.getUser(), "Error: " + e.getMessage(),
						controller.getSettings().getConnection());
				LOGGER.severe(e.getMessage());
			}
			labelGotovinaValue.setText(numberFormat.format(gotovina));
		}
		if (selectedPaymentMethod == 1) {
			try {
				kartica = numberFormat.parse(stringValue).doubleValue();
				ostatak = ukupno - gotovina - kartica - cek - virman;
			} catch (ParseException e) {
				Log.writeLog(controller.getUser(), "Error: " + e.getMessage(),
						controller.getSettings().getConnection());
				LOGGER.severe(e.getMessage());
			}
			labelKarticaValue.setText(numberFormat.format(kartica));
		}
		if (selectedPaymentMethod == 2) {
			try {
				cek = numberFormat.parse(stringValue).doubleValue();
				ostatak = ukupno - gotovina - kartica - cek - virman;
			} catch (ParseException e) {
				Log.writeLog(controller.getUser(), "Error: " + e.getMessage(),
						controller.getSettings().getConnection());
				LOGGER.severe(e.getMessage());
			}
			labelCekValue.setText(numberFormat.format(cek));
		}
		if (selectedPaymentMethod == 3) {
			try {
				virman = numberFormat.parse(stringValue).doubleValue();
				ostatak = ukupno - gotovina - kartica - cek - virman;
			} catch (ParseException e) {
				Log.writeLog(controller.getUser(), "Error: " + e.getMessage(),
						controller.getSettings().getConnection());
				LOGGER.severe(e.getMessage());
			}
			labelVirmanValue.setText(numberFormat.format(virman));
		}

		if (selectedPaymentMethod > 0) {
			labelOstatakValue.setText(numberFormat.format(ostatak));
		}
	}

	@FXML
	private void gotovinaClicked() {
		if (ostatak != 0)
			gotovina = ostatak;
		ostatak = 0;
		labelOstatakValue.setText(numberFormat.format(ostatak));
		labelGotovinaValue.setText(numberFormat.format(gotovina));
		selectedPaymentMethod = 0;
		stringValue = numberFormat.format(gotovina);
		unselectCombined();
		labelGotovinaValue.getStyleClass().add("backGroundOrange");
		labelGotovinaValue.getStyleClass().remove("backGroundTransparent");
	}

	@FXML
	private void karticaClicked() {
		if (ostatak != 0)
			kartica = ostatak;
		ostatak = 0;
		labelOstatakValue.setText(numberFormat.format(ostatak));
		labelKarticaValue.setText(numberFormat.format(kartica));
		selectedPaymentMethod = 1;
		stringValue = numberFormat.format(kartica);
		unselectCombined();
		labelKarticaValue.getStyleClass().add("backGroundOrange");
		labelKarticaValue.getStyleClass().remove("backGroundTransparent");
	}

	@FXML
	private void cekClicked() {
		if (ostatak != 0)
			cek = ostatak;
		ostatak = 0;
		labelOstatakValue.setText(numberFormat.format(ostatak));
		labelCekValue.setText(numberFormat.format(cek));
		selectedPaymentMethod = 2;
		stringValue = labelCekValue.getText();
		unselectCombined();
		labelCekValue.getStyleClass().add("backGroundOrange");
		labelCekValue.getStyleClass().remove("backGroundTransparent");
	}

	@FXML
	private void virmanClicked() {
		if (ostatak != 0)
			virman = ostatak;
		ostatak = 0;
		labelOstatakValue.setText(numberFormat.format(ostatak));
		labelVirmanValue.setText(numberFormat.format(virman));
		selectedPaymentMethod = 3;
		stringValue = labelVirmanValue.getText();
		unselectCombined();
		labelVirmanValue.getStyleClass().add("backGroundOrange");
		labelVirmanValue.getStyleClass().remove("backGroundTransparent");
	}

	private void unselectCombined() {
		labelGotovinaValue.getStyleClass().remove("backGroundOrange");
		labelKarticaValue.getStyleClass().remove("backGroundOrange");
		labelCekValue.getStyleClass().remove("backGroundOrange");
		labelVirmanValue.getStyleClass().remove("backGroundOrange");

		labelGotovinaValue.getStyleClass().add("backGroundTransparent");
		labelKarticaValue.getStyleClass().add("backGroundTransparent");
		labelCekValue.getStyleClass().add("backGroundTransparent");
		labelVirmanValue.getStyleClass().add("backGroundTransparent");
	}

	@FXML
	public void logout() {
		Log.logOut(controller.getUser().getUserID(), controller.getPointOfSale().getPointOfSaleID(),
				controller.getSettings().getConnection());
		controller.setUser(null);
		controller.setBillList(new ArrayList<Bill>());
		controller.getScreen(ScreensController.SCREEN_LOGIN, controller.getRoot(), ScreensController.SCREEN_PAYMENT);
	}

	@FXML
	public void toggleButtonClick(ActionEvent event) {
		ToggleButton button = ((ToggleButton) event.getSource());
		button.setSelected(true);
		try {
			if (!button.getText().equals("")) {
				PaymentMethod pm = PaymentMethod.getByName(button.getText(), controller.getSettings().getConnection());
				controller.getCurrentBill().setPaymentMethod(pm);
				if (pm.isComplex()) {
					HashMap<String, Object> hm = new HashMap<>();
					hm.put("controller", controller);
					controller.showModalDialog("Izbor načina plaćanja",
							ScreensController.SCREEN_PAYMENT_METHOD_SELECTION, hm, rootPane.getScene().getWindow());

				}
			}
		} catch (NullPointerException e) {
		}
	}

	@FXML
	public void combinedButtonClick() {

		combinedPane.setVisible(combinedButton.isSelected());
		if (combinedButton.isSelected()) {
			ostatak = ukupno;
			labelOstatakValue.setText(numberFormat.format(ostatak));
			controller.getCurrentBill()
					.setPaymentMethod(PaymentMethod.getByCode("COMBINED", controller.getSettings().getConnection()));
		}
	}

	@FXML
	public void cancelButton() {
		controller.getScreen(ScreensController.SCREEN_FASTFOOD, controller.getRoot(), ScreensController.SCREEN_PAYMENT);
	}

	@FXML
	public void postButtonClick() {
		if (!(cPaymentButton0.isSelected() || cPaymentButton1.isSelected())) {
			if(ukupno > uplaceno) {
				controller.alert("Neispravan uneti naplaćeni iznos.",
						"Ukupan naplaćeni iznos NE MOŽE biti manji od ukupnog iznosa računa",
						rootPane.getScene().getWindow());
				return;
			}
			//return;
		} 
		postButton.setDisable(true);

		// Otvaranje fioke ako je prazan račun
		if (controller.getCurrentBill().getLastRound().getItemList().size() == 0) {
			// Tražiti potvdu
			FiscalPrinter.openCassBox(controller);
			postButton.setDisable(false);
			return;
		}

		// Traženje potvrde za ček
		if ((controller.getCurrentBill().getPaymentMethod().getPaymentMethodID() == 3)
				&& (paymentButton1.isSelected())) {
			controller.setAuthorizationRequest(AuthorisationScreenController.PAYMENT_METHOD_CHECH);
			HashMap<String, Object> hm2 = new HashMap<>();
			hm2.put("controller", controller);
			int mr = controller.showModalDialog("Autorizacija", ScreensController.SCREEN_AUTHORISATION, hm2,
					rootPane.getScene().getWindow());
			if (mr != ScreensController.MODAL_RESULT_OK) {
				postButton.setDisable(false);
				return;
			}
		}

		// Ako nije prazan račun
		if ((controller.getCurrentBill().getPaymentMethod().getParentPaymentMethodID() == 0) && 
			(!combinedButton.isSelected() || (combinedButton.isSelected() && ostatak == 0))) {

			if (combinedButton.isSelected() && ostatak != 0) {
				controller.alert("Neispravan usnos kombinovanog plaćanja.",
						"Izabrano je kombinovano plaćanje, a suma načina plaćanja nije jednaka sumi računa.",
						rootPane.getScene().getWindow());
			}

			try {
				// Upisivanje računa u bazu
				long documentId = Bill.writeBill(controller.getBillList().get(controller.getActiveBill()),
						controller.getPointOfSale(), cPaymentButton7.isSelected(), "", controller);

				// Upisivanje Korekcije dokumenta ako je štampa bez fiskala
				if (cPaymentButton6.isSelected()) {
					DocumentCorrection dc = new DocumentCorrection();
					dc.setCorrectionTypeID(1);
					dc.setFiscalPrinterID(FiscalPrinter.getIBFM(controller.getSettings().getFiscalFolder()));
					dc.setDate(new Date());
					dc.setDoucmentID(documentId);
					dc.setWorkDayID(
							WorkDay.getFromDb(controller.getPointOfSale(), controller.getSettings().getConnection())
									.getWorkDayID());
					dc.setUserCreated(controller.getUser().getUserID());
					dc.insert(controller.getUser(), controller.getSettings().getConnection());
				}
				
				// Unos načina plaćanja u tabelu plaćanja
				if (controller.getCurrentBill().getPaymentMethod().getParentPaymentMethodID() < 1) {
					switch (controller.getCurrentBill().getPaymentMethod().getPaymentMethodID()) {
					case -1:
					case 1:
						DocumentPayment.insert(documentId, 1, ukupno, controller.getSettings().getConnection());
						break;
					case 2:
						DocumentPayment.insert(documentId, 2, ukupno, controller.getSettings().getConnection());
						break;
					case 3:
						DocumentPayment.insert(documentId, 3, ukupno, controller.getSettings().getConnection());
						break;
					case 4:
						DocumentPayment.insert(documentId, 4, ukupno, controller.getSettings().getConnection());
						break;
					case 5:
						DocumentPayment.insert(documentId, 5, ukupno, controller.getSettings().getConnection());
						break;
					case 6:
						if (gotovina > 0)
							DocumentPayment.insert(documentId, 1, gotovina, controller.getSettings().getConnection());
						if (kartica > 0)
							DocumentPayment.insert(documentId, 2, kartica, controller.getSettings().getConnection());
						if (cek > 0)
							DocumentPayment.insert(documentId, 3, cek, controller.getSettings().getConnection());
						if (virman > 0)
							DocumentPayment.insert(documentId, 4, virman, controller.getSettings().getConnection());
					}
				}

				// Upisivanje porudžbine u bazu
				if ((controller.getCurrentBill().getLastRound().createOrder())
						&& (controller.getCurrentBill().getPaymentMethod().getParentPaymentMethodID() < 1)
						&& (!cPaymentButton7.isSelected()) && (!cPaymentButton6.isSelected())) {

					long orderId = Order.insertOrder(controller.getCurrentBill().getLastRound(), documentId, controller);
					if (orderId > 0){
						///TODO Štampa porudžbine na fiskalu
						ObservableList<OrderDetaile> orderDetails = OrderDetaile.getListFP(orderId, controller);
						String[] orderKitchen = new String[orderDetails.size()];
						for(int i=0;i<orderDetails.size();i++) {
							if(orderDetails.get(i).getName().length() > 15) {
								orderKitchen[i] = String.format("%2.0f x%-24s",
										 orderDetails.get(i).getQuantity(),
										 orderDetails.get(i).getName().substring(0, 15).trim());
							} else {
								orderKitchen[i] = String.format("%2.0f x%-24s",
										 orderDetails.get(i).getQuantity(),
										 orderDetails.get(i).getName().trim());
							}																				
						}
						FiscalPrinter.printNonFiscal(controller.getSettings().getFiscalFolder(), orderKitchen);
					}					
				}

				// štampa na POS-u
				boolean posResult = PosPrinter.printRound(controller.getSettings().getPosFolder(),
						controller.getSettings().getPosPrinter(),
						controller.getBillList().get(controller.getActiveBill()).getRoundList().get(0),
						controller.getObject().getName(), controller.getPointOfSale().getName(), "", // Table
						"", // Department
						controller.getUser().getUserName(), controller.getSettings().getConnection());
				if (!posResult) {
					Log.writeLog(controller.getUser(),
							"Error: greška pri POS štampi PointOfSaleID ="
									+ controller.getPointOfSale().getPointOfSaleID(),
							controller.getSettings().getConnection());
					LOGGER.severe("Error: greška pri POS štampi PointOfSaleID ="
							+ controller.getPointOfSale().getPointOfSaleID());
					controller.alert("Greška pri POS štampi", "Greškla prilikom slanja na POS štampač!",
							rootPane.getScene().getWindow());
				}
				

				String BI = "";
				// štampa fiskalnog ukoliko nije prazan račun i nije izabrano
				// štampa bez fiskala
				if ((controller.getCurrentBill().getPaymentMethod().getParentPaymentMethodID() == 0)
						&& (!cPaymentButton7.isSelected()) && (!cPaymentButton6.isSelected())) {
					double g = 0;
					if (!combinedPane.isVisible()) {
						switch (controller.getCurrentBill().getPaymentMethod().getCode()) {
						case "CASH":
							g = uplaceno;
							kartica = 0;
							cek = 0;
							virman = 0;
							break;
						case "CARD":
							g = 0;
							kartica = ukupno;
							cek = 0;
							virman = 0;
							break;
						case "CHECK":
						case "F":
							g = 0;
							kartica = 0;
							cek = ukupno;
							virman = 0;
							break;
						case "TRANSFER":
							g = 0;
							kartica = 0;
							cek = 0;
							virman = ukupno;
							break;
						}
					}
					BI = FiscalPrinter.printBill(controller.getSettings().getFiscalFolder(),
							controller.getBillList().get(controller.getActiveBill()), g, kartica, cek, virman,
							controller.getUser().getUserName(), controller.getSettings().getConnection(),
							controller.getPointOfSale());

					// upisivanje povretnih informacija sa kase
					Document d = Document.getByID(documentId, controller.getSettings().getConnection());
					d.setFullNumber(BI);
					d.setAlternativeFullNumber(FiscalPrinter.getIBFM(controller.getSettings().getFiscalFolder()));
					d.update(controller);
				}

				// Štampa na A4 računa ako je selektovana štampa gotovinskog
				if (stampaGotovinskog.isSelected()){
					Document doc = Document.getByID(documentId, controller.getSettings().getConnection());
					doc.printInvoice(null,controller,rootPane.getScene().getWindow());
				}
				if (controller.getCurrentBill().getPaymetnMethodID() == 5){
					Document doc = new Document();
					doc.setDocumentTypeID(4);
					doc.setPaymentMethodID(5);
					doc.setNumber(Document.getNextNumber(4,controller.getObject(), controller));
					doc.setSum(controller.getCurrentBill().getSumWithDiscout());
					doc.setDiscount(0);
					doc.setSumWithDiscount(controller.getCurrentBill().getSumWithDiscout());
					
					
					HashMap<String, Object> hm = new HashMap<>();
					hm.put("controller", controller);
					int result = controller.showModalDialog("Izbor komitenta",ScreensController.DIALOG_SELECT_PERSON, hm,rootPane.getScene().getWindow());
					if (result == ScreensController.MODAL_RESULT_OK){
						doc.setPersonID(((Person)hm.get("person")).getPersonID());
					}
					doc.setDocumentStatusID(1);
					doc.setPointOfSaleID(controller.getPointOfSale().getPointOfSaleID());
					doc.setSysUserAddID(controller.getUser().getUserID());
					doc.insert(controller.getSettings().getConnection());
					doc.insertParent(1, documentId, controller.getSettings().getConnection());
					Faktura f = new Faktura();
					f.setDocument(doc);
					f.setParentDocumentID(documentId);
					f.print(controller, rootPane.getScene().getWindow());
				}
				
				stampaGotovinskog.setSelected(false);
				
				
				// Brisanje računa iz memorije i zatvaranje naplate

				controller.getBillList().remove(controller.getActiveBill());
				controller.setActiveBill(-1);
			} catch (SQLException | IOException e) {
				LOGGER.severe(e.getMessage());
				controller.alert("Greška prilikom snimanja računa!",
						"Došlo je do greške prilikom snimanja računa. \n" + "Poruka greške: \n" + e.getMessage(),
						rootPane.getScene().getWindow());
				Log.writeLog(controller.getUser(),
						"PointOfSale: " + controller.getPointOfSale().getName() + " " + e.getLocalizedMessage(),
						controller.getSettings().getConnection());
			}
		}
		
		try {
			if ((controller.getCurrentBill().getLastRound().getItemList().size() > 0)
					&& (controller.getCurrentBill().getPaymentMethod().getParentPaymentMethodID() != 0)
					&& (!combinedButton.isSelected() || (combinedButton.isSelected() && ostatak == 0))) {
				controller.setAuthorizationRequest(AuthorisationScreenController.PAYMENT_METHOD_ACTION);
				HashMap<String, Object> hm = new HashMap<>();
				hm.put("controller", controller);
				int mr = controller.showModalDialog("Autorizacija", ScreensController.SCREEN_AUTHORISATION, hm,
						rootPane.getScene().getWindow());
				if (mr == ScreensController.MODAL_RESULT_OK) {
					controller.getScreen(ScreensController.SCREEN_FASTFOOD, controller.getRoot(),
							ScreensController.SCREEN_PAYMENT);
				}
			}

			if (combinedButton.isSelected() && (combinedButton.isSelected() && ostatak != 0)) {
				controller.alert("Pogrešan izbor kombinovanog plaćanja",
						"Izabrano je kombinovano plaćanje, ali je ostatak veći od 0", rootPane.getScene().getWindow());
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			//
		}
		
		postButton.setDisable(false);

		controller.getScreen(ScreensController.SCREEN_FASTFOOD, controller.getRoot(), ScreensController.SCREEN_PAYMENT);
	}

	@FXML
	private void gotovinskiClick() {

	}

	private void fillPaymentMethodButtons() {
		ArrayList<PaymentMethod> list = PaymentMethod.getList(controller.getSettings().getConnection());
		for (int i = 0; i < 8; i++) {
			try {
				fillPaymentButton(i, list.get(i).getName(), list.get(i).isIsDefault());
			} catch (IndexOutOfBoundsException e) {
			}
		}
	}

	private void fillPaymentButton(int index, String name, boolean isDefault) {
		switch (index) {
		case 0:
			paymentButton0.setText(name);
			paymentButton0.setSelected(isDefault);
			break;
		case 1:
			paymentButton1.setText(name);
			paymentButton1.setSelected(isDefault);
			break;
		case 2:
			paymentButton2.setText(name);
			paymentButton2.setSelected(isDefault);
			break;
		case 3:
			paymentButton3.setText(name);
			paymentButton3.setSelected(isDefault);
			break;
		case 4:
			paymentButton4.setText(name);
			paymentButton4.setSelected(isDefault);
			break;
		case 5:
			paymentButton5.setText(name);
			paymentButton5.setSelected(isDefault);
			break;
		case 6:
			paymentButton6.setText(name);
			paymentButton6.setSelected(isDefault);
			break;
		case 7:
			paymentButton7.setText(name);
			paymentButton7.setSelected(isDefault);
			break;
		}
	}

	private void fillCPaymentMethodButtons() {
		ArrayList<PaymentMethod> list = PaymentMethod.getComplexList(controller.getSettings().getConnection());
		for (int i = 0; i < 6; i++) {
			try {
				fillCPaymentButton(i, list.get(i).getName(), list.get(i).isIsDefault());
			} catch (IndexOutOfBoundsException e) {
			}
		}

		if (controller.getUser().hasRole("manager")) {
			fillCPaymentButton(6, "Bez fiskalne štampe", false);
			cPaymentButton6.setDisable(false);
			fillCPaymentButton(7, "Ručno pisani fiskalni račun", false);
			cPaymentButton7.setDisable(false);
		} else {
			fillCPaymentButton(6, "", false);
			cPaymentButton6.setDisable(true);
			fillCPaymentButton(7, "", false);
			cPaymentButton7.setDisable(true);
		}
	}

	private void fillCPaymentButton(int index, String name, boolean isDefault) {
		switch (index) {
		case 0:
			cPaymentButton0.setText(name);
			cPaymentButton0.setSelected(isDefault);
			break;
		case 1:
			cPaymentButton1.setText(name);
			cPaymentButton1.setSelected(isDefault);
			break;
		case 2:
			cPaymentButton2.setText(name);
			cPaymentButton2.setSelected(isDefault);
			break;
		case 3:
			cPaymentButton3.setText(name);
			cPaymentButton3.setSelected(isDefault);
			break;
		case 4:
			cPaymentButton4.setText(name);
			cPaymentButton4.setSelected(isDefault);
			break;
		case 5:
			cPaymentButton5.setText(name);
			cPaymentButton5.setSelected(isDefault);
			break;
		case 6:
			cPaymentButton6.setText(name);
			cPaymentButton6.setSelected(isDefault);
			break;
		case 7:
			cPaymentButton7.setText(name);
			cPaymentButton7.setSelected(isDefault);
			break;
		}
	}
}
