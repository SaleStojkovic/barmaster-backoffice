package rs.petcom.master.gui;

import java.text.ParseException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import rs.petcom.master.dal.Settings;

public class EditingTableCellDouble extends TableCell<Object, Double> {
	 
    private TextField textField;
    private int decimalPlaces;
    private boolean useGrouping;

    public EditingTableCellDouble(int decimalPlaces, boolean useGrouping) {
    	this.decimalPlaces = decimalPlaces;
    	this.useGrouping = useGrouping;
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(Settings.getNumberFormat(2, true).format((double)getItem()));
        setGraphic(null);
    }

    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                	textField.setText(Settings.getNumberFormat(decimalPlaces, useGrouping).format((double)getItem()));
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(Settings.getNumberFormat(decimalPlaces, useGrouping).format((double)getItem()));
                setGraphic(null);
            }
        }
    }
    private void createTextField() {
        textField = new TextField(getString());
        textField.getStyleClass().add("textFieldEdit");
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
        textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
        	@Override
            public void changed(ObservableValue<? extends Boolean> arg0, 
                Boolean arg1, Boolean arg2) {
                    if (!arg2) {
                        try {
							commitEdit(Settings.getNumberFormat(decimalPlaces, useGrouping).parse(textField.getText()).doubleValue());
						} catch (ParseException e) {}
                    }
            }
        });
        
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
           @Override
            public void handle(KeyEvent t) {
          	  if ((t.getCode() == KeyCode.ENTER) ||
              	  (t.getCode() == KeyCode.TAB)) {
                    try {
						commitEdit(Settings.getNumberFormat(decimalPlaces, useGrouping).parse(textField.getText()).doubleValue());
					} catch (ParseException e) {
						e.printStackTrace();
					}
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : Settings.getNumberFormat(decimalPlaces, useGrouping).format((double)getItem());
    }
}
