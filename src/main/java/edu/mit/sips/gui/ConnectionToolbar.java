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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.mit.sips.gui.event.ConnectionEvent;
import edu.mit.sips.gui.event.ConnectionListener;
import edu.mit.sips.io.Icons;

/**
 * A toolbar that shows the current state of a simulation connection.
 * 
 * @author Paul T. Grogan
 */
public final class ConnectionToolbar extends JPanel implements ConnectionListener {
	private static final long serialVersionUID = -2072804446821201002L;	
	private JLabel iconLabel;
	
	/**
	 * Instantiates a new connection toolbar.
	 */
	public ConnectionToolbar() {
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		setLayout(new BorderLayout());
		iconLabel = new JLabel("Not connected");
		iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		iconLabel.setIcon(Icons.NOT_CONNECTED_BULLET);
		iconLabel.setFont(iconLabel.getFont().deriveFont(Font.PLAIN).deriveFont(9f));
		add(iconLabel, BorderLayout.EAST);
	}

	@Override
	public void connectionEventOccurred(ConnectionEvent e) {
		iconLabel.setIcon(e.getConnection().isConnected()?
				Icons.CONNECTED_BULLET:Icons.NOT_CONNECTED_BULLET);
		iconLabel.setText(e.getConnection().isConnected()?
				e.getConnection().getFederationName():"Offline");
	}
}
