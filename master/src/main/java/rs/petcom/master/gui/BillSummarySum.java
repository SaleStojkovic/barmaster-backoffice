package rs.petcom.master.gui;

import java.text.NumberFormat;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import rs.petcom.master.dal.Bill;

public class BillSummarySum extends GridPane{
	
	private Label lBillTotalValue = new Label("0,00");
	private Label lBillDiscountValue = new Label("0,00");
	private Label lBillTotalSumValue = new Label("0,00");
    
	public BillSummarySum(){
		setMinHeight(64);
		setPrefHeight(64);
		setMaxHeight(Double.MAX_VALUE);
		
		getStyleClass().add("billSummarySum");
		getStyleClass().add("borderTopBottom");
		ColumnConstraints column1 = new ColumnConstraints(96,96,Double.MAX_VALUE);
		column1.setFillWidth(true);
		column1.setHgrow(Priority.ALWAYS);
		ColumnConstraints column2 = new ColumnConstraints(96,96,Double.MAX_VALUE);
		column2.setFillWidth(true);
		
	    column2.setHgrow(Priority.ALWAYS);
		getColumnConstraints().addAll(column1, column2);
		
	    RowConstraints row1 = new RowConstraints(16, 16, Double.MAX_VALUE);
	    row1.setVgrow(Priority.ALWAYS);;
	    row1.setValignment(VPos.CENTER);
	    getRowConstraints().add(row1);
	    RowConstraints row2 = new RowConstraints(16,16,Double.MAX_VALUE);
	    
	    row2.setVgrow(Priority.ALWAYS);;
	    row2.setValignment(VPos.CENTER);
	    getRowConstraints().add(row2);
	    
	    RowConstraints row3 = new RowConstraints(32,32,Double.MAX_VALUE);
	    row3.setVgrow(Priority.ALWAYS);;
	    row3.setValignment(VPos.CENTER);
	    getRowConstraints().add(row3);
	    
	    
	    Label lBillTotal = new Label("Total račun");
	    lBillTotal.getStyleClass().add("totalLabel");
	    lBillTotal.setPadding(new Insets(0, 0, 0, 5));
	    lBillTotal.setAlignment(Pos.CENTER_LEFT);
	    lBillTotal.setMinWidth(96);
	    lBillTotal.setPrefWidth(96);
	    lBillTotal.setMaxWidth(Double.MAX_VALUE);
	    lBillTotal.setMinHeight(16);
	    lBillTotal.setPrefHeight(16);
	    lBillTotal.setMaxHeight(Double.MIN_VALUE);
	    
	    Label lBillDiscount = new Label("Popust");
	    lBillDiscount.getStyleClass().add("totalLabel");
	    lBillDiscount.setPadding(new Insets(0, 0, 0, 5));
	    lBillDiscount.setAlignment(Pos.CENTER_LEFT);
	    lBillDiscount.setMinWidth(96);
	    lBillDiscount.setPrefWidth(96);
	    lBillDiscount.setMaxWidth(Double.MAX_VALUE);
	    lBillDiscount.setMinHeight(16);
	    lBillDiscount.setPrefHeight(16);
	    lBillDiscount.setMaxHeight(Double.MIN_VALUE);
	    
	    Label lBillTotalSum = new Label("Total za naplatu");
	    lBillTotalSum.getStyleClass().add("totalLabelSum");
	    lBillTotalSum.setPadding(new Insets(0, 0, 0, 5));
	    lBillTotalSum.setAlignment(Pos.CENTER_LEFT);
	    lBillTotalSum.setMinWidth(96);
	    lBillTotalSum.setPrefWidth(96);
	    lBillTotalSum.setMaxWidth(Double.MAX_VALUE);
	    lBillTotalSum.setMinHeight(32);
	    lBillTotalSum.setPrefHeight(32);
	    lBillTotalSum.setMaxHeight(Double.MIN_VALUE);
	    	    
	    lBillTotalValue.getStyleClass().add("totalValue");
	    lBillTotalValue.setPadding(new Insets(0, 5, 0, 0));
	    lBillTotalValue.setAlignment(Pos.CENTER_RIGHT);
	    lBillTotalValue.setMinWidth(96);
	    lBillTotalValue.setPrefWidth(96);
	    lBillTotalValue.setMaxWidth(Double.MAX_VALUE);
	    lBillTotalValue.setMinHeight(16);
	    lBillTotalValue.setPrefHeight(16);
	    lBillTotalValue.setMaxHeight(Double.MIN_VALUE);
	    
	    lBillDiscountValue.getStyleClass().add("totalValue");
	    lBillDiscountValue.setPadding(new Insets(0, 5, 0, 0));
	    lBillDiscountValue.setAlignment(Pos.CENTER_RIGHT);
	    lBillDiscountValue.setMinWidth(96);
	    lBillDiscountValue.setPrefWidth(96);
	    lBillDiscountValue.setMaxWidth(Double.MAX_VALUE);
	    lBillDiscountValue.setMinHeight(16);
	    lBillDiscountValue.setPrefHeight(16);
	    lBillDiscountValue.setMaxHeight(Double.MIN_VALUE);
	    
	    lBillTotalSumValue.getStyleClass().add("totalValueSum");
	    lBillTotalSumValue.setPadding(new Insets(0, 5, 0, 0));
	    lBillTotalSumValue.setAlignment(Pos.CENTER_RIGHT);
	    lBillTotalSumValue.setMinWidth(96);
	    lBillTotalSumValue.setPrefWidth(96);
	    lBillTotalSumValue.setMaxWidth(Double.MAX_VALUE);
	    lBillTotalSumValue.setMinHeight(32);
	    lBillTotalSumValue.setPrefHeight(32);
	    lBillTotalSumValue.setMaxHeight(Double.MIN_VALUE);
	    
	   
	    
		add(lBillTotal,0,0);
		add(lBillDiscount,0,1);
		add(lBillTotalSum,0,2);
		
		add(lBillTotalValue,1,0);
		add(lBillDiscountValue,1,1);
		add(lBillTotalSumValue,1,2);
	}
	
	public void refresh(Bill bill){
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(true);
		
		double value = bill.getSumWithDiscout();
		double discount = 0;
		try{
			discount = value * bill.getPerson().getDiscount()/100;
		}catch (NullPointerException e){}
		lBillTotalValue.setText(nf.format(value));
		lBillDiscountValue.setText(nf.format(discount));
		lBillTotalSumValue.setText(nf.format(value - discount));
		
	}
}
