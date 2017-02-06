package org.openbw.mapeditor.model;

import java.util.LinkedList;
import java.util.List;

import org.openbw.mapeditor.gui.ChangeListener;

public abstract class Notifier {

	private List<ChangeListener> listeners;
	
	public Notifier() {
		
		this.listeners = new LinkedList<ChangeListener>();
	}
	
	protected void notifyListeners() {
		for (ChangeListener listener : listeners) {
			listener.update();
		}
	}
	
	public void addListener(ChangeListener listener) {
		listeners.add(listener);
	}
}
