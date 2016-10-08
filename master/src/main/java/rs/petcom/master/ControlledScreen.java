package rs.petcom.master;

public interface ControlledScreen {
	
	public void setScreenParent(ScreensController screenPage);
	public void init();
	public void setParameter(Object parameter);
	public Object getParameter();
}
