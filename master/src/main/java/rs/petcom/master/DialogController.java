package rs.petcom.master;

import java.util.HashMap;

public interface DialogController {
	
	public void setParameter(HashMap<String, Object> parameter);
	public HashMap<String, Object> getParameter();
	public int getModalResult();
	public void setModalResult(int modalResult);

}
