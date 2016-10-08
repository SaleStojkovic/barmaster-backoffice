package rs.petcom.master.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentMethod {
	private int PaymentMethodID;
	private String Code;
	private String Name;
	private int Sort;
	private boolean IsDefault;
	private boolean Active;
	private int ParentPaymentMethodID;
	
	private boolean complex = false;
	
	public int getPaymentMethodID() {
		return PaymentMethodID;
	}
	public void setPaymentMethodID(int paymentMethodID) {
		PaymentMethodID = paymentMethodID;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getSort() {
		return Sort;
	}
	public void setSort(int sort) {
		Sort = sort;
	}
	public boolean isIsDefault() {
		return IsDefault;
	}
	public void setIsDefault(boolean isDefault) {
		IsDefault = isDefault;
	}
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
	}
	public int getParentPaymentMethodID() {
		return ParentPaymentMethodID;
	}
	public void setParentPaymentMethodID(int parentPaymentMethodID) {
		ParentPaymentMethodID = parentPaymentMethodID;
	}
	public boolean isComplex() {
		return complex;
	}
	public void setComplex(boolean complex) {
		this.complex = complex;
	}
	public static ArrayList<PaymentMethod> getList(Connection connection){
		ArrayList<PaymentMethod> list = new ArrayList<PaymentMethod>();
		
		try {
			PreparedStatement ps = connection.prepareStatement( "select * from paymentmethod pm "
															  + " where pm.Active = true "
															  + "   and pm.ParentPaymentMethodID is null "
															  + " 	and (select count(pm2.PaymentMethodID) "
															  + "	   from paymentmethod pm2 "
															  + "	  where pm2.ParentPaymentMethodID = pm.PaymentMethodID) = 0 "
															  + " order by pm.Sort ");
	
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				PaymentMethod pm = new PaymentMethod();
				pm.PaymentMethodID = rs.getInt("PaymentMethodID");
				pm.Code = rs.getString("Code");
				pm.Name = rs.getString("Name");
				pm.Sort = rs.getInt("Sort");
				pm.IsDefault = rs.getBoolean("IsDefault");
				pm.Active = rs.getBoolean("Active");
				pm.ParentPaymentMethodID = rs.getInt("ParentPaymentMethodID");
				pm.complex = false;
				list.add(pm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ArrayList<PaymentMethod> getComplexList(Connection connection){
		ArrayList<PaymentMethod> list = new ArrayList<PaymentMethod>();
		
		try {
			PreparedStatement ps = connection.prepareStatement( "select * from paymentmethod pm "
															  + " where pm.Active = true "
															  + "   and pm.ParentPaymentMethodID is null "
															  + " 	and (select count(pm2.PaymentMethodID) "
															  + "	   from paymentmethod pm2 "
															  + "	  where pm2.ParentPaymentMethodID = pm.PaymentMethodID) > 0 "
															  + " order by pm.Sort ");
	
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				PaymentMethod pm = new PaymentMethod();
				pm.PaymentMethodID = rs.getInt("PaymentMethodID");
				pm.Code = rs.getString("Code");
				pm.Name = rs.getString("Name");
				pm.Sort = rs.getInt("Sort");
				pm.IsDefault = rs.getBoolean("IsDefault");
				pm.Active = rs.getBoolean("Active");
				pm.ParentPaymentMethodID = rs.getInt("ParentPaymentMethodID");
				pm.complex = true;
				list.add(pm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ArrayList<PaymentMethod> getSubList(int parent, Connection connection){
		ArrayList<PaymentMethod> list = new ArrayList<PaymentMethod>();
		
		try {
			PreparedStatement ps = connection.prepareStatement( "select * from paymentmethod pm "
															  + " where pm.Active = true "
															  + " 	and ParentPaymentMethodID = ?"
															  + " order by pm.Sort ");
	
			ps.setInt(1, parent);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				PaymentMethod pm = new PaymentMethod();
				pm.PaymentMethodID = rs.getInt("PaymentMethodID");
				pm.Code = rs.getString("Code");
				pm.Name = rs.getString("Name");
				pm.Sort = rs.getInt("Sort");
				pm.IsDefault = rs.getBoolean("IsDefault");
				pm.Active = rs.getBoolean("Active");
				pm.ParentPaymentMethodID = rs.getInt("ParentPaymentMethodID");
				pm.complex = false;
				list.add(pm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static PaymentMethod getByName(String name, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select pm.*, "
															  + "  (select count(pm2.PaymentMethodID) "
															  + "		    from paymentmethod pm2 "
															  + "		   where pm2.ParentPaymentMethodID = pm.PaymentMethodID) as children "
															  + "  from paymentmethod pm "
															  + " where Name = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			rs.next();
			PaymentMethod pm = new PaymentMethod();
			pm.PaymentMethodID = rs.getInt("PaymentMethodID");
			pm.Code = rs.getString("Code");
			pm.Name = rs.getString("Name");
			pm.Sort = rs.getInt("Sort");
			pm.IsDefault = rs.getBoolean("IsDefault");
			pm.Active = rs.getBoolean("Active");
			pm.ParentPaymentMethodID = rs.getInt("ParentPaymentMethodID");
			pm.complex = rs.getInt("children") > 0;
			return pm;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static PaymentMethod getByCode(String code, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select * from paymentmethod where Code = ?");
			ps.setString(1, code);
			ResultSet rs = ps.executeQuery();
			rs.next();
			PaymentMethod pm = new PaymentMethod();
			pm.PaymentMethodID = rs.getInt("PaymentMethodID");
			pm.Code = rs.getString("Code");
			pm.Name = rs.getString("Name");
			pm.Sort = rs.getInt("Sort");
			pm.IsDefault = rs.getBoolean("IsDefault");
			pm.ParentPaymentMethodID = rs.getInt("ParentPaymentMethodID");
			pm.Active = rs.getBoolean("Active");
			return pm;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static PaymentMethod getByID(int id, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement( "select * from paymentmethod where PaymentMethodID = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			PaymentMethod pm = new PaymentMethod();
			pm.PaymentMethodID = rs.getInt("PaymentMethodID");
			pm.Code = rs.getString("Code");
			pm.Name = rs.getString("Name");
			pm.Sort = rs.getInt("Sort");
			pm.IsDefault = rs.getBoolean("IsDefault");
			pm.ParentPaymentMethodID = rs.getInt("ParentPaymentMethodID");
			pm.Active = rs.getBoolean("Active");
			return pm;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
