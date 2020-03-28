/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
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

	@Override
	public void insertUpdate(DocumentEvent e) {
		documentChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		documentChanged();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		documentChanged();
	}

	/**
	 * Document changed.
	 */
	public abstract void documentChanged();
}