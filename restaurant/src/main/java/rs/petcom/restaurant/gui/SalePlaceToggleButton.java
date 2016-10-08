package rs.petcom.restaurant.gui;

import javafx.scene.control.ToggleButton;
import rs.petcom.restaurant.dal.SalePlace;

public class SalePlaceToggleButton extends ToggleButton{
	private SalePlace salePlace;

	public SalePlace getSalePlace() {
		return salePlace;
	}

	public void setSalePlace(SalePlace salePlace) {
		this.salePlace = salePlace;
		if (salePlace != null)
			this.setText(salePlace.getName());
		else
			this.setText("");
	}
	
}
