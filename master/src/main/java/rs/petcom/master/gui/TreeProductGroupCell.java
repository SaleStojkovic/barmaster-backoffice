package rs.petcom.master.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import rs.petcom.master.dal.product.ProductGroup;

public class TreeProductGroupCell extends TreeCell<ProductGroup> {
    @Override
    protected void updateItem(ProductGroup item, boolean empty) {
        super.updateItem(item, empty);
        if (isEmpty()) {
            setGraphic(null);
            setText(null);
        } else {
            if (this.getTreeItem().isLeaf()) {
                HBox cellBox = new HBox();
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(item.isSelected());
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						item.setSelected(newValue);
					}
				});
                Label label = new Label(item.getName());
                label.prefHeightProperty().bind(checkBox.heightProperty());
                cellBox.getChildren().addAll(checkBox, label);
                setGraphic(cellBox);
                setText(null);
            } else {
                setGraphic(null);
                setText(item.getName());
            }
        }
    }
}
