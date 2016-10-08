package rs.petcom.master.gui;

import rs.petcom.master.ScreensController;

public class DialogData {
	
	String title;
	String text;
	Object parameter;
	int modalResult;
	
	public DialogData(String title, String text){
		this.title = title;
		this.text = text;
		this.parameter = null;
		this.modalResult = ScreensController.MODAL_RESULT_CANCEL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getModalResult() {
		return modalResult;
	}
	public void setModalResult(int modalResult) {
		this.modalResult = modalResult;
	}

	public Object getParameter() {
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

}
