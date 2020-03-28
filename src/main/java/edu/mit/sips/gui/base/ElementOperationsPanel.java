package edu.mit.sips.gui.base;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.gui.agriculture.AgricultureElementOperationsPanel;

/**
 * The Class ElementOperationsPanel.
 */
public class ElementOperationsPanel extends JPanel {
	private static final long serialVersionUID = -7019424706322479152L;
		
	/**
	 * Instantiates a new element operations panel.
	 *
	 * @param element the element
	 */
	public ElementOperationsPanel(InfrastructureElement element) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JPanel defaultElementPanel = new JPanel();
		defaultElementPanel.setLayout(new GridBagLayout());
		add(defaultElementPanel);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);

		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		defaultElementPanel.add(new JLabel("Name"), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		defaultElementPanel.add(new JLabel(element.getName()), c);
		c.gridy++;
		
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		defaultElementPanel.add(new JLabel("Location"), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		defaultElementPanel.add(new JLabel(element.getOrigin() + 
				(element.getDestination().equals(element.getOrigin()) ? "" 
						: " - " + element.getDestination())), c);
	}
	
	/**
	 * Adds the input.
	 *
	 * @param panel the panel
	 * @param c the c
	 * @param labelText the label text
	 * @param component the component
	 */
	protected void addInput(JPanel panel, GridBagConstraints c, String labelText, 
			JComponent component, String units) {
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		panel.add(new JLabel(labelText), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		panel.add(component, c);
		c.gridx++;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		panel.add(new JLabel(units), c);
		c.gridy++;
		c.gridx-=2;
	}
	
	/**
	 * Creates a new ElementPanel object.
	 *
	 * @param element the element
	 * @return the element panel
	 */
	public static ElementOperationsPanel createElementOperationsPanel(
			InfrastructureElement element) {
		if(element instanceof AgricultureElement) {
			return new AgricultureElementOperationsPanel((AgricultureElement)element);
			/*
		} else if(element instanceof WaterElement) {
			return new WaterElementOperationsPanel((WaterElement)element);
		} else if(element instanceof PetroleumElement) {
			return new PetroleumElementOperationsPanel((PetroleumElement)element);
		} else if(element instanceof ElectricityElement) {
			return new ElectricityElementOperationsPanel((ElectricityElement)element);
			*/
		} else {
			throw new IllegalArgumentException("Element panel not implemented.");
		}
	}
}
