package edu.mit.sips.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The Class InitializationInputPanel.
 */
public class InitializationInputPanel extends JPanel {
	private static final long serialVersionUID = 1080043229916477760L;
	
	private long startTime = 1950;
	private long endTime = 2010;
	
	/**
	 * Instantiates a new initialization input panel.
	 */
	public InitializationInputPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Start Year"), c);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx++;
		final JTextField startText = new JTextField(10);
		startText.setText(new Long(startTime).toString());
		startText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					startTime = Long.parseLong(startText.getText());
					startText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					startText.setForeground(Color.red);
				}
			}
		});
		add(startText, c);
		c.gridy++;

		c.gridx--;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("End Year"), c);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx++;
		final JTextField endText = new JTextField(10);
		endText.setText(new Long(endTime).toString());
		endText.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override
			public void documentChanged() {
				try {
					endTime = Long.parseLong(endText.getText());
					endText.setForeground(Color.black);
				} catch(NumberFormatException ex) {
					endText.setForeground(Color.red);
				}
			}
		});
		add(endText, c);
		
		startText.setEnabled(false);
		endText.setEnabled(false);
	}
	
	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public long getStartTime() {
		return startTime;
	}
	
	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public long getEndTime() {
		return endTime;
	}
}
