package rs.petcom.master.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import rs.petcom.master.DialogController;
import rs.petcom.master.ScreensController;
import rs.petcom.master.dal.Bill;
import rs.petcom.master.dal.Log;
import rs.petcom.master.dal.Round;
import rs.petcom.master.dal.User;

public class AuthorisationScreenController implements DialogController {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	ScreensController controller;

	HashMap<String, Object> parameter;
	int modalResult = ScreensController.MODAL_RESULT_CANCEL;

	public static final int STORNO_ACTION = 1;
	public static final int PAYMENT_METHOD_ACTION = 2;
	public static final int REOPEN_WORKDAY = 3;
	public static final int PROGRAM_EXIT = 4;
	public static final int LOGOUT = 5;
	public static final int ZAKLJUCENJE_DANA = 6;
	public static final int ZAKLJUCENJE_DANA_SA_MINUSOM = 7;
	public static final int PAYMENT_METHOD_CHECH = 8;
	public static final int ZAKLJUCENJE_DANA_BEZ_FISKALA = 9;

	EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent event) {
			switch (event.getCode()) {
			case ENTER:
				commitCode(null);
				break;
			case BACK_SPACE:
			case DELETE:
				if (password.getText().length() > 0)
					password.setText(password.getText().substring(0, password.getText().length() - 1));
			default:
				if (event.getCode().isDigitKey()) {
					password.setText(password.getText() + event.getText());
				}
			}
		}
	};

	@FXML
	private BorderPane rootPane;
	@FXML
	private Label labelInfo;
	@FXML
	private GridPane numericPane;
	@FXML
	private PasswordField password;
	@FXML
	private Button button0;
	@FXML
	private Button button1;
	@FXML
	private Button button2;
	@FXML
	private Button button3;
	@FXML
	private Button button4;
	@FXML
	private Button button5;
	@FXML
	private Button button6;
	@FXML
	private Button button7;
	@FXML
	private Button button8;
	@FXML
	private Button button9;
	@FXML
	private Button buttonEnter;
	@FXML
	private Button buttonDelete;

	@Override
	public void setParameter(HashMap<String, Object> parameter) {
		this.parameter = parameter;
		this.controller = (ScreensController) parameter.get("controller");

		switch (controller.getAuthorizationRequest()) {
		case AuthorisationScreenController.STORNO_ACTION:
			labelInfo.setText("Storniranje savke");
			break;

		default:
			break;
		}
		password.setText("");
		rootPane.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
	}

	@Override
	public HashMap<String, Object> getParameter() {
		return null;
	}

	@FXML
	private void handleButtonAction(ActionEvent event) {
		if (event.getSource() != buttonDelete) {
			password.setText(password.getText() + ((Button) event.getSource()).getText());
		} else {
			try {
				String code = password.getText().substring(0, password.getText().length() - 1);
				password.setText(code);
			} catch (StringIndexOutOfBoundsException e) {
			}

		}
	}

	@FXML
	private void commitCode(ActionEvent event) {
		if (password.getText().length() > 0) {
			User user = User.getFromDb(password.getText(), controller.getSettings().getPosCode(),
					controller.getSettings().getConnection());

			switch (controller.getAuthorizationRequest()) {
			case AuthorisationScreenController.STORNO_ACTION:
				if ((user != null) && (user.hasRole("storno")))
					storno(user.getUserID());
				else
					labelInfo.setText("Pogrešna autorizacija");
				password.setText("");
				break;
			case AuthorisationScreenController.PAYMENT_METHOD_ACTION:
				if ((user != null) && (user.hasRole("manager"))) {
					saveBill();
				}
				break;
			case AuthorisationScreenController.PAYMENT_METHOD_CHECH:
				if ((user != null) && (user.hasRole("manager"))) {
					modalResult = ScreensController.MODAL_RESULT_OK;
					rootPane.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
					((Stage) rootPane.getScene().getWindow()).close();
				}
				break;
			case AuthorisationScreenController.REOPEN_WORKDAY:
				if ((user != null) && (user.hasRole("manager"))) {
					modalResult = ScreensController.MODAL_RESULT_OK;
					rootPane.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
					((Stage) rootPane.getScene().getWindow()).close();
				}
				break;
			case AuthorisationScreenController.LOGOUT:
				if ((user != null) && (user.hasRole("manager"))) {
					modalResult = ScreensController.MODAL_RESULT_OK;
					rootPane.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
					((Stage) rootPane.getScene().getWindow()).close();
				}
				break;
			case AuthorisationScreenController.ZAKLJUCENJE_DANA:
				if ((user != null) && (user.hasRole("manager"))) {
					parameter.put("user", user);
					modalResult = ScreensController.MODAL_RESULT_OK;
					rootPane.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
					((Stage) rootPane.getScene().getWindow()).close();
				}
				break;
			case AuthorisationScreenController.ZAKLJUCENJE_DANA_BEZ_FISKALA:

				modalResult = ScreensController.MODAL_RESULT_NO;
				while(modalResult!=ScreensController.MODAL_RESULT_OK){
					if ((user != null) && (user.hasRole("zatvaranje.dana"))) {
						parameter.put("user", user);
						modalResult = ScreensController.MODAL_RESULT_OK;
						rootPane.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
						((Stage) rootPane.getScene().getWindow()).close();
					}else{
						controller.alert("Upozorenje", "Pogresna sifra", rootPane.getScene().getWindow());
						password.setText("");
						return;
					}
				}
				
				break;
			case AuthorisationScreenController.ZAKLJUCENJE_DANA_SA_MINUSOM:
				if ((user != null) && (user.hasRole("minus"))) {
					parameter.put("user", user);
					modalResult = ScreensController.MODAL_RESULT_OK;
					rootPane.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
					((Stage) rootPane.getScene().getWindow()).close();
				} else {
					controller.alert("Nedostatak prava", "Nemate pravo da zatvorite dan sa minusom u lageru",
							rootPane.getScene().getWindow());
					password.setText("");
				}
				break;
			default:
				break;
			}
		}
	}

	@FXML
	private void cancelAction() {
		modalResult = ScreensController.MODAL_RESULT_CANCEL;
		rootPane.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
		((Stage) rootPane.getScene().getWindow()).close();
	}

	private void storno(long authId) {
		// brisanje ture
		if ((controller.getAuthBill() == 0) && (controller.getAuthRound() == 0) && (controller.getAuthItem() == -1)
				&& (controller.getAuthAdditional() == -1) && (controller.getAuthDesciptional() == -1)) {

			Log.storno(controller.getUser().getUserID(), authId, controller.getPointOfSale().getPointOfSaleID(),
					controller.getBillList().get(controller.getActiveBill()).getLastRound(), controller.getSettings(),
					controller.getSettings().getConnection());

			controller.getBillList().get(controller.getActiveBill()).getRoundList().clear();

			controller.getBillList().get(controller.getActiveBill()).getRoundList().add(new Round());

			modalResult = ScreensController.MODAL_RESULT_OK;
			((Stage) rootPane.getScene().getWindow()).close();
		}

		// brisanje stavke
		if ((controller.getAuthBill() > -1) && (controller.getAuthRound() > -1) && (controller.getAuthItem() > -1)
				&& (controller.getAuthAdditional() == -1) && (controller.getAuthDesciptional() == -1)) {

			Log.storno(controller.getUser().getUserID(), authId, controller.getPointOfSale().getPointOfSaleID(),
					controller.getBillList().get(controller.getAuthBill()).getRoundList().get(controller.getAuthRound())
							.getItemList().get(controller.getAuthItem()),
					controller.getSettings(), controller.getSettings().getConnection());

			Double currentQuantity = controller.getBillList().get(controller.getAuthBill()).getRoundList()
					.get(controller.getAuthRound()).getItemList().get(controller.getAuthItem()).getQuantity();

			if (controller.getAuthQuantity() != 0 && currentQuantity > controller.getAuthQuantity()) {

				controller.getBillList().get(controller.getAuthBill()).getRoundList().get(controller.getAuthRound())
						.getItemList().get(controller.getAuthItem())
						.setQuantity(currentQuantity - controller.getAuthQuantity());
			} else {
				controller.getBillList().get(controller.getAuthBill()).getRoundList().get(controller.getAuthRound())
						.getItemList().remove(controller.getAuthItem());
			}
			modalResult = ScreensController.MODAL_RESULT_OK;
			((Stage) rootPane.getScene().getWindow()).close();
		}

		// brisanje descriptional
		if ((controller.getAuthBill() > -1) && (controller.getAuthRound() > -1) && (controller.getAuthItem() > -1)
				&& (controller.getAuthAdditional() == -1) && (controller.getAuthDesciptional() > -1)) {

			Double currentQuantity = controller.getBillList().get(controller.getAuthBill()).getRoundList()
					.get(controller.getAuthRound()).getItemList().get(controller.getAuthItem()).getDescriptionalList()
					.get(controller.getAuthDesciptional()).getQuantity();

			if (controller.getAuthQuantity() != 0 && currentQuantity > controller.getAuthQuantity()) {

				controller.getBillList().get(controller.getAuthBill()).getRoundList().get(controller.getAuthRound())
						.getItemList().get(controller.getAuthItem()).getDescriptionalList()
						.get(controller.getAuthDesciptional())
						.setQuantity(currentQuantity - controller.getAuthQuantity());
			} else {
				controller.getBillList().get(controller.getAuthBill()).getRoundList().get(controller.getAuthRound())
						.getItemList().get(controller.getAuthItem()).getDescriptionalList()
						.remove(controller.getAuthDesciptional());

			}
			modalResult = ScreensController.MODAL_RESULT_OK;
			((Stage) rootPane.getScene().getWindow()).close();
		}
		// brisanje additional
				if ((controller.getAuthBill() > -1) && (controller.getAuthRound() > -1) && (controller.getAuthItem() > -1)
						&& (controller.getAuthAdditional() > -1) && (controller.getAuthDesciptional() == -1)) {

					Double currentQuantity = controller.getBillList().get(controller.getAuthBill()).getRoundList()
							.get(controller.getAuthRound()).getItemList().get(controller.getAuthItem()).getAdditionalList()
							.get(controller.getAuthAdditional()).getQuantity();

					if (controller.getAuthQuantity() != 0 && currentQuantity > controller.getAuthQuantity()) {

						controller.getBillList().get(controller.getAuthBill()).getRoundList().get(controller.getAuthRound())
								.getItemList().get(controller.getAuthItem()).getAdditionalList()
								.get(controller.getAuthAdditional())
								.setQuantity(currentQuantity - controller.getAuthQuantity());
					} else {
						controller.getBillList().get(controller.getAuthBill()).getRoundList().get(controller.getAuthRound())
								.getItemList().get(controller.getAuthItem()).getAdditionalList()
								.remove(controller.getAuthAdditional());

					}
					modalResult = ScreensController.MODAL_RESULT_OK;
					((Stage) rootPane.getScene().getWindow()).close();
				}
	}

	private void saveBill() {
		try {
			Bill.writeBill(controller.getBillList().get(controller.getActiveBill()), controller.getPointOfSale(), false,
					"", controller);
			controller.getBillList().remove(controller.getActiveBill());
			controller.setActiveBill(-1);
			modalResult = ScreensController.MODAL_RESULT_OK;
			((Stage) rootPane.getScene().getWindow()).close();
		} catch (ArrayIndexOutOfBoundsException e) {
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
			Log.writeLog(controller.getUser(),
					"Error! PointOfSaleID = " + controller.getPointOfSale().getPointOfSaleID() + "\t" + e.getMessage(),
					controller.getSettings().getConnection());
			controller.alert("Greška pri upisivanju u bazu!", "\n\n Poruka o grešci:\n\n" + e.getLocalizedMessage(),
					rootPane.getScene().getWindow());
		}
	}

	@Override
	public int getModalResult() {
		return modalResult;
	}

	@Override
	public void setModalResult(int modalResult) {
		this.modalResult = modalResult;
	}
}
