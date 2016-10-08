package rs.petcom.restaurant.dal;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import rs.petcom.master.dal.PointOfSale;

public class SalePlace {
	private int SalePlaceID;
	private String Name;
	private int ObjectID;
	private boolean Active;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	private Image image;
	public int getSalePlaceID() {
		return SalePlaceID;
	}
	public void setSalePlaceID(int salePlaceID) {
		SalePlaceID = salePlaceID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getObjectID() {
		return ObjectID;
	}
	public void setObjectID(int objectID) {
		ObjectID = objectID;
	}
	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		Active = active;
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
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	
	public static ArrayList<SalePlace> getList(PointOfSale pos, Connection connection) throws SQLException, IOException{
		ArrayList<SalePlace> list = new ArrayList<>();
		String query = "select * from saleplace sp "
					 + " where sp.ObjectID in (select ObjectID from pointofsale p where p.PointOfSaleID =?) ";
		PreparedStatement ps = connection.prepareStatement(query);
		ps.setInt(1, pos.getObjectID());
		ResultSet rs = ps.executeQuery();
		
		BufferedImage bufferedImage = null;
        InputStream fis = null;
        
		
		while (rs.next()){
			
			SalePlace sp = new SalePlace();
			sp.SalePlaceID = rs.getInt("SalePlaceID");
			sp.Name = rs.getString("Name");
			sp.ObjectID = rs.getInt("ObjectID");
			sp.Active = rs.getBoolean("Active");
			sp.sysDTCreated = rs.getTimestamp("sysDTCreated");
			sp.sysDTEdit = rs.getTimestamp("sysDTEdit");
			sp.sysUserAddID = rs.getLong("sysUserAddID");
			sp.sysUserEditID = rs.getLong("sysUserEditID");
			if (rs.getBinaryStream("image") != null){
				fis = rs.getBinaryStream("image");
	            bufferedImage = javax.imageio.ImageIO.read(fis);
				sp.image = SwingFXUtils.toFXImage(bufferedImage, null);
			}
			list.add(sp);
		}
		return list;
	}
}
