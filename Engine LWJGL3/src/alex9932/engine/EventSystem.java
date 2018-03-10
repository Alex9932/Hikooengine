package alex9932.engine;

import java.util.ArrayList;

import alex9932.script.IEventHandler;

// Simple event system

public class EventSystem {
	private ArrayList<IEventHandler> eventHandlers = new ArrayList<IEventHandler>();
	
	public EventSystem() {
		
	}
	
	public void addEventHandler(IEventHandler handler) {
		eventHandlers.add(handler);
	}
	
	public void sendSignal(Event event) {
		for (int i = 0; i < eventHandlers.size(); i++) {
			eventHandlers.get(i).handle(event);
		}
	}
}