package com.cosylab.vdct.undo;

/**
 * This type was created in VisualAge.
 */
public class UndoManager {

	private static UndoManager instance = null;
	
	private final int lowerbound = -1;
	
	private int pos;
	private int first, last; 
	private int bufferSize;
	private ActionObject[] actions;

	private boolean monitor = false;

	private ComposedActionInterface composedAction = null;
/**
 * UndoManager constructor comment.
 */
protected UndoManager(int steps2remember) {
	bufferSize = steps2remember;
	actions = new ActionObject[bufferSize];
	instance = this; // to prevent dead-loop reset-getInstance
	reset();
}
/**
 * This method was created in VisualAge.
 * @return int
 */
public int actions2redo() {
	int redos = 0;
	int np = pos;
	while (np!=last) {
		redos++;
		np = increment(np);
	}
	return redos;
}
/**
 * This method was created in VisualAge.
 * @return int
 */
public int actions2undo() {
	if (pos==lowerbound) return 0;
	
	int undos = 1;
	int np = pos;
	while (np!=first) {
		undos++;
		np = decrement(np);
	}
	return undos;
}
/**
 * This method was created in VisualAge.
 * @param action epics.undo.ActionObject
 */
public void addAction(ActionObject action) {
	if (!monitor) return;

	if (composedAction!=null)
	{
		composedAction.addAction(action);
		//System.out.println("Composing: "+action.getDescription());
		return;
	}

	//System.out.println("New action: "+action.getDescription());
	com.cosylab.vdct.graphics.DrawingSurface.getInstance().setModified(true);

	if (pos==lowerbound) pos=last=increment(pos);
	else {
		pos=last=increment(pos);
		if (last==first) first=increment(first);		// lose first (the "oldest" action)
	}
	actions[pos]=action;

	int np = increment(last);							// clear lost actions -> finalization!
	while (np!=first) {
		actions[np]=null;
		np=increment(np);
	}
	
	com.cosylab.vdct.graphics.DSGUIInterface.getInstance().updateMenuItems();
}
/**
 * This method was created in VisualAge.
 * @return int
 * @param pos int
 */
private int decrement(int pos) {
	if (pos==first) return lowerbound;
	else {
		int np = pos-1;
		if (np<0) np=bufferSize-1;
		return np;
	}
}
/**
 * Insert the method's description here.
 * Creation date: (3.5.2001 20:45:26)
 * @return com.cosylab.vdct.undo.ComposedActionInterface
 */
public ComposedActionInterface getComposedAction() {
	return composedAction;
}
/**
 * Insert the method's description here.
 * Creation date: (22.4.2001 15:56:37)
 * @return com.cosylab.vdct.undo.UndoManager
 */
public static UndoManager getInstance() {
	if (instance==null) instance = new UndoManager(com.cosylab.vdct.Constants.UNDO_STEPS_TO_REMEMBER);
	return instance;
}
/**
 * This method was created in VisualAge.
 * @return int
 * @param pos int
 */
private int increment(int pos) {
	if (pos==lowerbound) return first;
	else return ((pos+1) % bufferSize);
}
/**
 * Insert the method's description here.
 * Creation date: (3.5.2001 15:36:00)
 * @return boolean
 */
public boolean isMonitor() {
	return monitor;
}
/**
 * This method was created in VisualAge.
 */
public void redo() {
	if (pos!=last) {
		boolean m = monitor;
		monitor = false;
		pos=increment(pos);
		actions[pos].redo();
		//System.out.println("Redo: "+actions[pos].getDescription());
		com.cosylab.vdct.graphics.DrawingSurface.getInstance().setModified(true);
		com.cosylab.vdct.graphics.DSGUIInterface.getInstance().updateMenuItems();
		monitor = m;
	}
}
/**
 * This method was created in VisualAge.
 */
public void reset() {
	first=0;
	pos=last=lowerbound;
	for (int i=0; i < bufferSize; i++)
		actions[i]=null;
	monitor = false;

	com.cosylab.vdct.graphics.DSGUIInterface.getInstance().updateMenuItems();
}
/**
 * Insert the method's description here.
 * Creation date: (3.5.2001 15:36:00)
 * @param newMonitor boolean
 */
public void setMonitor(boolean newMonitor) {
	monitor = newMonitor;
}
/**
 * Insert the method's description here.
 * Creation date: (3.5.2001 20:43:57)
 */
public void startMacroAction() {
	ComposedAction action = new ComposedAction();
	addAction(action);
	this.composedAction = action;
}
/**
 * Insert the method's description here.
 * Creation date: (3.5.2001 20:43:57)
 * @param composedAction com.cosylab.vdct.undo.ComposedActionInterface
 */
public void startMacroAction(ComposedActionInterface composedAction) {
	this.composedAction=composedAction;
}
/**
 * Insert the method's description here.
 * Creation date: (3.5.2001 20:44:21)
 */
public void stopMacroAction() {
	composedAction=null;
	//System.out.println("Stopped composing");
}
/**
 * This method was created in VisualAge.
 */
public void undo() {
	if (pos!=lowerbound)  {
		boolean m = monitor;
		monitor = false;
		actions[pos].undo();
		//System.out.println("Undo: "+actions[pos].getDescription());
		pos=decrement(pos);
		com.cosylab.vdct.graphics.DrawingSurface.getInstance().setModified(true);
		com.cosylab.vdct.graphics.DSGUIInterface.getInstance().updateMenuItems();
		monitor = m;
	}
}
}
