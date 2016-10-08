package rs.petcom.master.gui.menu;

import javafx.scene.control.Button;

public class MenuButton extends Button{
	
	String title = "";
	String menuCode = "";
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	
	public MenuButton(String title, String menuCode){
		this.title = title;
		this.menuCode = menuCode;
		setText(title);
		setMaxWidth(Double.MAX_VALUE);
		this.getStyleClass().add("menuButton");
	}
}
