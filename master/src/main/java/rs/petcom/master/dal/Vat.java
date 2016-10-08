package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Vat {
	
	private int VatID;
	private String Name;
	private double Value;
	private double Value2;
	private double Value3;
	private boolean Active;
	private String Code;
	
	private ArrayList<Vat> list;
	
	public int getVatID() {
		return VatID;
	}
	public void setVatID(int vatID) {
		VatID = vatID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public double getValue() {
		return Value;
	}
	public void setValue(double value) {
		Value = value;
	}
	public double getValue2() {
		return Value2;
	}
	public void setValue2(double value2) {
		Value2 = value2;
	}
	public double getValue3() {
		return Value3;
	}
	public void setValue3(double value3) {
		Value3 = value3;
	}
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	
	@Override
	public String toString(){
		return Name;
	}
	
	public Vat getById(int id, Connection connection){
		if (list == null){
			try {
				PreparedStatement ps = connection.prepareStatement("select * from Vat where Active = true");
				ResultSet rs = ps.executeQuery();
				list = new ArrayList<Vat>();
				while (rs.next()){
					Vat vat = new Vat();
					vat.VatID = rs.getInt("VatID");
					vat.Name = rs.getString("Name");
					vat.Value = rs.getDouble("Value");
					vat.Value2 = rs.getDouble("Value2");
					vat.Value3 = rs.getDouble("Value3");
					vat.Active = rs.getBoolean("Active");
					vat.Code = rs.getString("Code");
					list.add(vat);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Vat vat = null;
		for (int i=0;i<list.size();i++){
			if (list.get(i).getVatID() == id){
				vat = list.get(i);
			}
		}
		return vat;
	}
	public static ObservableList<Vat> getObeservableList(Connection connection){

		ObservableList<Vat> list = FXCollections.observableArrayList();

		String query = "select * from vat";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Vat vat = new Vat();
				vat.VatID = rs.getInt("VatID");
				vat.Name = rs.getString("Name");
				vat.Value = rs.getDouble("Value");
				vat.Value2 = rs.getDouble("Value2");
				vat.Value3 = rs.getDouble("Value3");
				vat.Active = rs.getBoolean("Active");
				vat.Code = rs.getString("Code");

				list.add(vat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
