package rs.petcom.master.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import rs.petcom.master.dal.product.Group;

public class TreeGroupCell extends TreeCell<Group> {
	
	HBox cellBox = new HBox();
	CheckBox checkBox = new CheckBox();
	Label label = new Label("");
	
	public TreeGroupCell(){
		
		label.prefHeightProperty().bind(checkBox.heightProperty());
		checkBox.setSelected(false);
        cellBox.getChildren().addAll(checkBox, label);
		
		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
					Boolean newValue) {
				group.setSelected(newValue);
			}
		});
	}
	
	Group group = null;
	@Override
    protected void updateItem(Group item, boolean empty) {
        super.updateItem(item, empty);
        group = item;
        if (isEmpty()) {
            setGraphic(null);
            setText(null);
        } else {
            if (this.getTreeItem().isLeaf()) {
            	/*
                HBox cellBox = new HBox();
                
                checkBox.setSelected(item.isSelected());
                
                Label label = new Label(group.getName());
                checkBox.setSelected(group.isSelected());
                label.prefHeightProperty().bind(checkBox.heightProperty());
                cellBox.getChildren().addAll(checkBox, label);
                setGraphic(cellBox);
                setText(null);
                */
                 label.setText(group.getName());
                checkBox.setSelected(group.isSelected());
                setGraphic(cellBox);
            } else {
                setGraphic(null);
                setText(item.getName());
                checkBox.setSelected(false);
            }
        }
    }
}
