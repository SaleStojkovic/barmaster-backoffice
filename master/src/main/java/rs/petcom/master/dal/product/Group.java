package rs.petcom.master.dal.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rs.petcom.master.dal.PointOfSale;

public class Group {
	private int GroupID;
	private String Name;
	private int ParentGroupID;
	private boolean Active;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	private String color;
	private int position;
	private boolean Selected;
	private Date availableFrom;
	private Date availableTo;
		
	public Group(){}
	public Group(int id, String Name){
		this.GroupID = id;
		this.Name = Name;		
	}
	
	public int getGroupID() {
		return GroupID;
	}
	public void setGroupID(int groupID) {
		GroupID = groupID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getParentGroupID() {
		return ParentGroupID;
	}
	public void setParentGroupID(int parentGroupID) {
		ParentGroupID = parentGroupID;
	}
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean Active) {
		this.Active = Active;
	}
	public Date getSysDTCreated() {
		return sysDTCreated;
	}
	public void setSysDTCreated(Date sysDTCreated) {
		this.sysDTCreated = sysDTCreated;
	}
	public Date getSysDTEdit() {
		return sysDTEdit;
	}
	public void setSysDTEdit(Date sysDTEdit) {
		this.sysDTEdit = sysDTEdit;
	}
	public long getSysUserAddID() {
		return sysUserAddID;
	}
	public void setSysUserAddID(long sysUserAddID) {
		this.sysUserAddID = sysUserAddID;
	}
	public long getSysUserEditID() {
		return sysUserEditID;
	}
	public void setSysUserEditID(long sysUserEditID) {
		this.sysUserEditID = sysUserEditID;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}	
	public boolean isSelected() {
		return Selected;
	}
	public void setSelected(boolean selected) {
		Selected = selected;
	}
	public Date getAvailableFrom() {
		return availableFrom;
	}
	public void setAvailableFrom(Date availableFrom) {
		this.availableFrom = availableFrom;
	}
	public Date getAvailableTo() {
		return availableTo;
	}
	public void setAvailableTo(Date availableTo) {
		this.availableTo = availableTo;
	}
	
	public static ArrayList<Group> getList(int pos, int grupa, Connection connection){
		ArrayList<Group> list = new ArrayList<Group>();
		String query = " select "
					 + "	g.GroupID,"
					 + "	g.Name, "
					 + "	g.ParentGroupID, "
					 + "	g.Active, "
					 + "	g.sysDTCreated, "
					 + "	g.sysDTEdit,"
					 + "	g.sysUserAddID,"
					 + "	g.sysUserEditID,"
					 + "	g.color, "
					 + "	gps.Sort as position,"
					 + "	gps.AvailableFrom,"
					 + "	gps.AvailableTo "
					 + "  from `group` g "
					 + "  left join grouppointofsale gps on gps.GroupID = g.GroupID "
					 + " where (((? = 0) and (gps.ParentGroupId is null)) "
					 + "        or ((? > 0) and (gps.ParentGroupId = ?))) "
					 + "   and gps.PointOfSaleID = ?"
					 + " order by gps.Sort";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, grupa);
			ps.setInt(2, grupa);
			ps.setInt(3, grupa);
			ps.setInt(4, pos);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Group group = new Group();
				group.GroupID = rs.getInt("GroupID");
				group.Name = rs.getString("Name");
				try{
					group.ParentGroupID = rs.getInt("ParentGroupID");
				} catch (NullPointerException e1){
					group.ParentGroupID = 0;
				};
				group.Active = rs.getBoolean("Active");
				try{
					group.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				} catch (NullPointerException e1){};
				try{
					group.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e1){};
				group.sysUserAddID = rs.getLong("sysUserAddID");
				try{
					group.sysUserEditID = rs.getLong("sysUserEditID");
				} catch (NullPointerException e1){};
				group.color = rs.getString("color");
				try{
					group.position = rs.getInt("position");
				} catch (NullPointerException e1) {
					group.position = 0;
				}
				try{
					group.availableFrom = rs.getTime("AvailableFrom");
				}catch (NullPointerException e){}
				try{
					group.availableTo = rs.getTime("AvailableTo");
				}catch (NullPointerException e){}
				
				list.add(group);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static boolean imaPodgrupa(int grupa, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement("select count(*) from grouppointofsale where ParentGroupId = ?");
			ps.setInt(1, grupa);
			ResultSet rs = ps.executeQuery();
			rs.next();
			if (rs.getInt(1) > 0) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static ObservableList<Group> getInsertList(PointOfSale pos, 
													  Product product, 
													  String filter,
													  Connection connection){
		ObservableList<Group> list = FXCollections.observableArrayList();

		String query = " select distinct g.* "
					 + "   from grouppointofsale gp"
					 + "   left join `group` g on gp.GroupID = g.GroupID"
					 + "  where gp.GroupID not in (select gp1.GroupID "
					 + "   							 from grouppointofsaleproduct gp1"
					 + "							where gp1.ProductID = ?"
					 + "						      and gp1.PointOfSaleID = ?)";
	
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setLong(1, product.getProductID());
			ps.setInt(2, pos.getPointOfSaleID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Group group = new Group();
				group.GroupID = rs.getInt("GroupID");
				group.Name = rs.getString("Name");
				try{
					group.ParentGroupID = rs.getInt("ParentGroupID");
				} catch (NullPointerException e1){
					group.ParentGroupID = 0;
				};
				group.Active = rs.getBoolean("Active");
				try{
					group.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				} catch (NullPointerException e1){};
				try{
					group.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e1){};
				group.sysUserAddID = rs.getLong("sysUserAddID");
				try{
					group.sysUserEditID = rs.getLong("sysUserEditID");
				} catch (NullPointerException e1){};
				group.color = rs.getString("color");
				try{
					group.position = rs.getInt("position");
				} catch (NullPointerException e1) {
					group.position = 0;
				}
				list.add(group);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ArrayList<Group> getList(int groupId, Connection connection){
		ArrayList<Group> list = new ArrayList<Group>();
		
		String query = "select "
					 + "	g.GroupID,"
					 + "	g.Name,"
					 + "	g.ParentGroupID,"
					 + "	g.Active,"
					 + "	g.sysDTCreated,"
					 + "	g.sysDTEdit,"
					 + "	g.sysUserAddID,"
					 + "	g.sysUserEditID,"
					 + "	g.color,"
					 + "	g.position"
					 + "  from `group` g"
					 + " where ((? = 0) and (g.ParentGroupID is null)) "
					 + "	or ((? > 0) and (g.ParentGroupID = ?))"
					 + " order by g.position";
		
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, groupId);
			ps.setInt(2, groupId);
			ps.setInt(3, groupId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Group group = new Group();
				group.GroupID = rs.getInt("GroupID");
				group.Name = rs.getString("Name");
				try{
					group.ParentGroupID = rs.getInt("ParentGroupID");
				} catch (NullPointerException e1){
					group.ParentGroupID = 0;
				};
				group.Active = rs.getBoolean("Active");
				try{
					group.sysDTCreated = new Date(rs.getDate("sysDTCreated").getTime());
				} catch (NullPointerException e1){};
				try{
					group.sysDTEdit = new Date(rs.getDate("sysDTEdit").getTime());
				} catch (NullPointerException e1){};
				group.sysUserAddID = rs.getLong("sysUserAddID");
				try{
					group.sysUserEditID = rs.getLong("sysUserEditID");
				} catch (NullPointerException e1){};
				group.color = rs.getString("color");
				try{
					group.position = rs.getInt("position");
				} catch (NullPointerException e1) {
					group.position = 0;
				}
				
				list.add(group);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
