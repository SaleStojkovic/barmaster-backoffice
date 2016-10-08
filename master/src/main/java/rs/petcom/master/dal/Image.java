package rs.petcom.master.dal;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.embed.swing.SwingFXUtils;

public class Image {
	
	public static javafx.scene.image.Image getImage(int id, Connection connection){
		BufferedImage bufferedImage = null;
        InputStream fis = null;

        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT slika FROM slike WHERE id = " + id);
            rs.next();
            fis = rs.getBinaryStream(1);
            bufferedImage = javax.imageio.ImageIO.read(fis);
            
        } catch (Exception e){
        	e.printStackTrace();
        }
        javafx.scene.image.Image image = SwingFXUtils.toFXImage(bufferedImage, null);
		return image;
	}
	
	public static void insertImage(int id, String naziv, String filename, Connection connection){
		
		try {
			PreparedStatement ps = connection.prepareStatement("insert into slike(id,naziv,slika) values(?,?,?)");
			ps.setInt(1, id);
			ps.setString(2, naziv);
			FileInputStream fis = new FileInputStream(filename);
			ps.setBinaryStream(3, (InputStream)fis);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
