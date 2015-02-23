package com.variamos.gui.perspeditor.actions;

import java.awt.event.ActionEvent;

import com.variamos.gui.maineditor.AbstractEditorAction;
import com.variamos.gui.maineditor.BasicGraphEditor;

@SuppressWarnings("serial")
public class ExitAction extends AbstractEditorAction {
	/**
		 * 
		 */
	public void actionPerformed(ActionEvent e) {
		BasicGraphEditor editor = getEditor(e);

		if (editor != null) {
			editor.exit();
		}
	}
}