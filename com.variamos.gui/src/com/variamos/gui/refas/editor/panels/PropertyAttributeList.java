package com.variamos.gui.refas.editor.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;

import com.variamos.gui.maineditor.VariamosGraphEditor;
import com.variamos.gui.refas.editor.panels.AttributeEditionPanel.DialogButtonAction;
import com.variamos.syntaxsupport.metamodelsupport.AbstractAttribute;
import com.variamos.syntaxsupport.metamodelsupport.AttributeElement;
import com.variamos.syntaxsupport.metamodelsupport.EditableElementAttribute;
import com.variamos.syntaxsupport.type.StringType;

/**
 * A class to support the property of syntax and semantic concepts for modeling.
 * Initially copied from VariabilityAttributeList on pl.editor. Part of PhD work
 * at University of Paris 1
 * 
 * @author Juan C. Mu�oz Fern�ndez <jcmunoz@gmail.com>
 * 
 * @version 1.1
 * @since 2014-12-28
 * @see com.variamos.gui.pl.editor.VariabilityAttributeList
 */
@SuppressWarnings("serial")
public class PropertyAttributeList extends JList<AbstractAttribute> {

	/**
	 * Reference to the editor required for Dialog
	 */
	private VariamosGraphEditor editor;
	/**
	 * Reference to the InstEnumeration required to validate Id
	 */
	private Map<String,AbstractAttribute> attributes;
	/**
	 * 
	 */
	private AbstractAttribute spoof = new AbstractAttribute("Add ...",
			StringType.IDENTIFIER, false, "Add ...", "");

	private AttributeEditionPanel attributeEdition;

	public PropertyAttributeList(VariamosGraphEditor editor) {
		this.editor = editor;
		init(null);
	}

	public PropertyAttributeList(VariamosGraphEditor editor, Map<String,AbstractAttribute> attributes,
			AttributeEditionPanel attributeEdition) {
		this.editor = editor;
		this.attributes = attributes;
		this.attributeEdition = attributeEdition;
		init(attributes);

	}

	
	private void init(Map<String, AbstractAttribute> varAttributes) {
		setModel(new DefaultListModel<AbstractAttribute>());
		final DefaultListModel<AbstractAttribute> model = (DefaultListModel<AbstractAttribute>) getModel();

		if (varAttributes != null)
			for (AbstractAttribute v : varAttributes.values())
				model.addElement(v);

		model.addElement(spoof);

		// setSize(new Dimension(150, 150));
		setPreferredSize(new Dimension(100, 80));
		setMaximumSize(new Dimension(100, 80));

		addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 1) {
					int index = locationToIndex(evt.getPoint());
					AbstractAttribute v = null;

					if (index != model.getSize() - 1)
						v = getModel().getElementAt(index);

					editItem(v);
				}
			}
		});
		setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(
					@SuppressWarnings("rawtypes") JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				JLabel lbl = (JLabel) super.getListCellRendererComponent(list,
						value, index, isSelected, cellHasFocus);
				lbl.setText((String) ((AbstractAttribute) value).getName()
						+ ":" + ((AbstractAttribute) value).getType());
				return lbl;
			}
		});
	}

	protected void editItem(AbstractAttribute var) {
		final boolean insert = (var == null);
		attributeEdition.setEnabled(true);
		this.setEnabled(false);
		if (insert) {
			// TODO move validation to a method on InstEnumeration
				boolean notFound = true;
			// Name
			var = new AbstractAttribute("EnumValue", StringType.IDENTIFIER,
					false, "Enumeration Value", "");

		}

		// HACK for accesing a non-final variable inside of an inner class
		final AbstractAttribute[] buffer = { var };
		Map<String,EditableElementAttribute> att = var.getEditableElementAttributes();
		
		final EditableElementAttribute name = att.get("Name");
		final EditableElementAttribute type = att.get("Type");
		final EditableElementAttribute ClassCanName = att.get("ClassCanName");
		final EditableElementAttribute MetaCInstType = att.get("MetaCInstType");
		final EditableElementAttribute displayName = att.get("DispName");
		final EditableElementAttribute defaultValue = att.get("DefaultValue");
		final EditableElementAttribute domain = att.get("Domain");
		final EditableElementAttribute hint = att.get("Hint");
		
		
		

		// SetDomain metaDomain = new SetDomain();
		// metaDomain.setIdentifier("MetaDomain");

		// DomainRegister reg = editor.getDomainRegister();
		// for( Type d : reg.getRegisteredDomains() )
		// metaDomain.getElements().add(d.getIdentifier());
		// reg.registerDomain(metaDomain);

		// String domainRepresentation = "0, 1";
		// if(!insert)
		// = var.getDomain().getStringRepresentation();
		
		attributeEdition.loadElementAttributes(editor, name, displayName, type,ClassCanName,MetaCInstType, defaultValue, domain, hint);	
		attributeEdition.revalidate();
		attributeEdition.repaint();
		attributeEdition.setOnAccept(new DialogButtonAction() {
			@Override
			public boolean onAction() {
				// This calls Pull on each parameter
				attributeEdition.getParameters();

				AbstractAttribute v = buffer[0];
				v.setName((String) name.getValue());
				v.setDisplayName((String)displayName.getValue());
			//	v.setDomain((Domain)domain.getValue());
				v.setHint((String) hint.getValue());
				v.setType((String) type.getAttributeType());
				v.setClassCanonicalName((String)ClassCanName.getValue());
				v.setMetaConceptInstanceType((String)MetaCInstType.getValue());
				v.setDefaultValue((String)defaultValue.getValue());
				//v.setDisplayName((String) name.getDisplayName());
				if (insert) {
					((DefaultListModel<AbstractAttribute>) getModel())
							.insertElementAt(v, getModel().getSize() - 1);
					attributes.put(name.getName(), v);
				}

				afterAction();
				return true;
			}
		});

		attributeEdition.setOnCancel(new DialogButtonAction() {

			@Override
			public boolean onAction() {
				afterAction();
				return true;
			}
		});
	}

	protected void afterAction() {
		updateUI();
	}

}
