package rs.petcom.master.dal.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class DocumentRelationship {
	private long DocumentRelationshipID;
	private int RelationshipTypeID;
	private long DocumentParentID;
	private long DocumentChildID;
	private boolean Active;
	private Date sysDTCreated;
	private Date sysDTEdit;
	private long sysUserAddID;
	private long sysUserEditID;
	public long getDocumentRelationshipID() {
		return DocumentRelationshipID;
	}
	public void setDocumentRelationshipID(long documentRelationshipID) {
		DocumentRelationshipID = documentRelationshipID;
	}
	public int getRelationshipTypeID() {
		return RelationshipTypeID;
	}
	public void setRelationshipTypeID(int relationshipTypeID) {
		RelationshipTypeID = relationshipTypeID;
	}
	public long getDocumentParentID() {
		return DocumentParentID;
	}
	public void setDocumentParentID(long documentParentID) {
		DocumentParentID = documentParentID;
	}
	public long getDocumentChildID() {
		return DocumentChildID;
	}
	public void setDocumentChildID(long documentChildID) {
		DocumentChildID = documentChildID;
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
	
	public void insert(Connection connection){
		String query = "INSERT INTO documentrelationship( "
					 + "	RelationshipTypeID,DocumentParentID, "
					 + "	DocumentChildID,"
					 + "	Active,"
					 + "	sysDTCreated,"
					 + "	sysDTEdit,"
					 + "	sysUserAddID,"
					 + "	sysUserEditID)"
					 + " VALUES (?,?,?,?,?,?,?,?);";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, RelationshipTypeID);
			ps.setLong(2, DocumentParentID);
			if (DocumentChildID != 0)
				ps.setLong(3, DocumentChildID);
			else 
				ps.setNull(3, java.sql.Types.BIGINT);
			ps.setBoolean(4, Active);
			ps.setTimestamp(5, new Timestamp(sysDTCreated.getTime()));
			try{
				ps.setTimestamp(6, new Timestamp(sysDTEdit.getTime()));
			} catch (NullPointerException e){
				ps.setNull(6, java.sql.Types.TIMESTAMP);
			}
			ps.setLong(7, sysUserAddID);
			if (sysUserEditID != 0)
				ps.setLong(8, sysUserEditID);
			else
				ps.setNull(8, java.sql.Types.BIGINT);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
