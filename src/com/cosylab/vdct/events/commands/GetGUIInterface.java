package com.cosylab.vdct.events.commands;

import com.cosylab.vdct.events.Command;
import com.cosylab.vdct.graphics.*;

/**
 * Insert the type's description here.
 * Creation date: (3.2.2001 22:05:51)
 * @author: 
 */
public class GetGUIInterface extends com.cosylab.vdct.events.Command {
	GUIMenuInterface guiinterface;
/**
 * Insert the method's description here.
 * Creation date: (3.2.2001 22:08:10)
 * @param validator com.cosylab.vdct.graphics.GUIMenuInterface
 */
public GetGUIInterface(GUIMenuInterface guiinterface) {
	this.guiinterface=guiinterface;
}
/**
 * Insert the method's description here.
 * Creation date: (3.2.2001 22:05:51)
 */
public void execute() {}
/**
 * Insert the method's description here.
 * Creation date: (3.2.2001 22:09:16)
 * @return com.cosylab.vdct.graphics.GUIMenuInterface
 */
public GUIMenuInterface getGUIMenuInterface() {
	return guiinterface;
}
}
