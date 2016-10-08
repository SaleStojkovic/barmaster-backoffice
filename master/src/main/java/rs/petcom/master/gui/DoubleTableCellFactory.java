package rs.petcom.master.gui;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class DoubleTableCellFactory implements Callback<TableColumn<Object, Double>, TableCell<Object, Double>>{

	private int decimalPlaces;
	private boolean useGrouping;
	
	public DoubleTableCellFactory(int decimalPlaces, boolean useGroupint){
		this.decimalPlaces = decimalPlaces;
		this.useGrouping = useGroupint;
	}
	@Override
	public TableCell<Object, Double> call(TableColumn<Object, Double> param) {
		return new EditingTableCellDouble(decimalPlaces,useGrouping);
	}

}
