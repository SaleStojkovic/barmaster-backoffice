package rs.petcom.restaurant.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import rs.petcom.restaurant.dal.SaleTable;

public class SaleTableButton extends Button{
	
	private SaleTable saleTable;
	private Font font = new Font("Sans", 13);
	
	public SaleTableButton(){
		setWrapText(true);
		setAlignment(Pos.CENTER);
		setTextAlignment(TextAlignment.CENTER);
		setContentDisplay(ContentDisplay.CENTER);
		
		font = Font.font("sans", FontWeight.BOLD, 13);
		setFont(font);
		
	}
	
	public SaleTable getSaleTable() {
		return saleTable;
	}
	public void setSaleTable(SaleTable saleTable) {
		this.saleTable = saleTable;
		setTranslateX(saleTable.getOffsetX());
		setTranslateY(saleTable.getOffsetX());
		setPrefWidth(saleTable.getWidth());
		setPrefHeight(saleTable.getHeight());
		
		String text = saleTable.getName() + "\n";
		if (saleTable.getUser() != null)
			text += saleTable.getUser().getUserName();
		text += "\n"; 
		text += saleTable.getTableSum();
		
		setText(text);	
	}
}
