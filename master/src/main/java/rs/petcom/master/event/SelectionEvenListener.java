package rs.petcom.master.event;

import java.util.EventListener;

public interface SelectionEvenListener  extends EventListener {
	public void selection(SelectionEvent evt);
}
