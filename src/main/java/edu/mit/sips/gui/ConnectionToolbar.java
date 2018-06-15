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
 * The Class ConnectionToolbar.
 * 
 * @author Paul T Grogan, ptgrogan@mit.edu
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

	/* (non-Javadoc)
	 * @see edu.mit.clover.gui.event.ConnectionListener#connectionEventOccurred(edu.mit.clover.gui.event.ConnectionEvent)
	 */
	public void connectionEventOccurred(ConnectionEvent e) {
		iconLabel.setIcon(e.getConnection().isConnected()?
				Icons.CONNECTED_BULLET:Icons.NOT_CONNECTED_BULLET);
		iconLabel.setText(e.getConnection().isConnected()?
				e.getConnection().getFederationName():"Offline");
	}
}
