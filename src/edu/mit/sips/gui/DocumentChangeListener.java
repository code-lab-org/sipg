package edu.mit.sips.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * The listener interface for receiving documentChange events.
 * The class that is interested in processing a documentChange
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDocumentChangeListener<code> method. When
 * the documentChange event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DocumentChangeEvent
 */
public abstract class DocumentChangeListener implements DocumentListener {

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		documentChanged();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		documentChanged();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		documentChanged();
	}

	/**
	 * Document changed.
	 */
	public abstract void documentChanged();
}