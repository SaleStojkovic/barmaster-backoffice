package rs.petcom.master.event;

import javafx.event.Event;
import javafx.event.EventType;

public class SelectionEvent extends Event{
	
	private static final long serialVersionUID = 5995303790394341428L;
	private boolean selected = false;
	
	public static final EventType<SelectionEvent> SELECTED_DESCRIPTIONAL = new EventType<>(Event.ANY, "SELECTED_DESCRIPTIONAL");
	
	public SelectionEvent(EventType<? extends Event> eventType, boolean selected) {
		super(eventType);
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
