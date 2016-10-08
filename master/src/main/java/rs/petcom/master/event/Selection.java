package rs.petcom.master.event;

import javax.swing.event.EventListenerList;

public class Selection {
		
	// Create the listener list
    protected EventListenerList listenerList = new EventListenerList();

    // This methods allows classes to register for Events
    public void addSelectionEventListener(SelectionEvenListener listener) {
        listenerList.add(SelectionEvenListener.class, listener);
    }

    // This methods allows classes to unregister for Events
    public void removeSelectionEventListener(SelectionEvenListener listener) {
        listenerList.remove(SelectionEvenListener.class, listener);
    }

    // This private class is used to fire Events
   public void fireSelectionEvent(SelectionEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i] == SelectionEvenListener.class) {
                ((SelectionEvenListener)listeners[i+1]).selection(evt);
            }
        }
    }

}
