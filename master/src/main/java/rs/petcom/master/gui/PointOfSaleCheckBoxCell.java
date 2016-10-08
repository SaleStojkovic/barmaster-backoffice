package rs.petcom.master.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import rs.petcom.master.dal.PointOfSale;

public class PointOfSaleCheckBoxCell extends TreeCell<PointOfSale> {
	
	HBox cellBox = new HBox();
	CheckBox checkBox = new CheckBox();
	Label label = new Label("");
	 
	PointOfSale pos;
	
	public PointOfSaleCheckBoxCell(){
		
		label.prefHeightProperty().bind(checkBox.heightProperty());
		checkBox.setSelected(false);
        cellBox.getChildren().addAll(checkBox, label);
		
		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
					Boolean newValue) {
				pos.setSelected(newValue);
			}
		});
	}
	
	@Override
    protected void updateItem(PointOfSale item, boolean empty) {
        super.updateItem(item, empty);
        pos = item;
        if (isEmpty()) {
            setGraphic(null);
            setText(null);
        } else {
            if (this.getTreeItem().isLeaf()) {
                label.setText(pos.getName());
                checkBox.setSelected(pos.isSelected());
                setGraphic(cellBox);
            } else {
                setGraphic(null);
                setText(item.getName());
                checkBox.setSelected(false);
            }
        }
    }
}
