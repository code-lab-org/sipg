package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import edu.mit.sips.core.City;
import edu.mit.sips.core.MutableInfrastructureElement;
import edu.mit.sips.core.agriculture.MutableAgricultureElement;
import edu.mit.sips.core.electricity.MutableElectricityElement;
import edu.mit.sips.core.lifecycle.DefaultLifecycleModel;
import edu.mit.sips.core.lifecycle.MutableSimpleLifecycleModel;
import edu.mit.sips.core.petroleum.MutablePetroleumElement;
import edu.mit.sips.core.water.MutableWaterElement;
import edu.mit.sips.gui.agriculture.AgricultureElementPanel;
import edu.mit.sips.gui.electricity.ElectricityElementPanel;
import edu.mit.sips.gui.petroleum.PetroleumElementPanel;
import edu.mit.sips.gui.water.WaterElementPanel;
import edu.mit.sips.io.Icons;
import edu.mit.sips.scenario.Scenario;

/**
 * The Class ElementPanel.
 */
public class ElementPanel extends JPanel {
	private static final long serialVersionUID = -5595402005206587525L;
	
	private final MutableInfrastructureElement element;
	
	private final JTextField nameText;
	private final JComboBox originCombo, destinationCombo;
	private final JPanel lifecycleModelContainer;
	private LifecycleModelPanel lifecycleModelPanel;
	private final ListCellRenderer cityRenderer = new DefaultListCellRenderer() {
		private static final long serialVersionUID = 3761951866857845749L;

		@Override
		public Component getListCellRendererComponent(JList list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, 
					isSelected, cellHasFocus);
			if(value instanceof City) {
				setText(((City)value).getName());
				setIcon(Icons.CITY);
			}
			return this;
		}
	};
	
	/**
	 * Instantiates a new element panel.
	 *
	 * @param scenario the scenario
	 * @param element the element
	 */
	public ElementPanel(final Scenario scenario, final MutableInfrastructureElement element) {
		this.element = element;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JPanel defaultElementPanel = new JPanel();
		defaultElementPanel.setLayout(new GridBagLayout());
		add(defaultElementPanel);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);

		c.gridx = 0;
		nameText = new JTextField(10);
		nameText.setText(element.getName());
		nameText.getDocument().addDocumentListener(new DocumentChangeListener() {
			public void documentChanged() {
				element.setName(nameText.getText());
			}
		});
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		defaultElementPanel.add(new JLabel( "Name"), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridwidth = 5;
		defaultElementPanel.add(nameText, c);
		c.gridy++;
		c.gridx--;
		c.gridwidth = 1;
		
		originCombo = new JComboBox(scenario.getCountry().getCities().toArray());
		originCombo.setRenderer(cityRenderer);
		originCombo.setSelectedItem(scenario.getCountry().getCity(element.getOrigin()));
		originCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(originCombo.getSelectedItem() instanceof City) {
					element.setOrigin(((City)originCombo.getSelectedItem()).getName());
				}
				if(element.getTemplateName() != null 
						&& scenario.getTemplate(element.getTemplateName()) != null
						&& !scenario.getTemplate(element.getTemplateName()).isTransport()) {
					destinationCombo.setSelectedItem(originCombo.getSelectedItem());
				}
			}
		});
		addInput(defaultElementPanel, c, "Location", originCombo, "");
		
		c.gridy--;
		c.gridx = 3;
		destinationCombo = new JComboBox(scenario.getCountry().getCities().toArray());
		destinationCombo.setRenderer(cityRenderer);
		destinationCombo.setSelectedItem(scenario.getCountry().getCity(element.getDestination()));
		destinationCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(destinationCombo.getSelectedItem() instanceof City) {
					element.setDestination(((City)destinationCombo.getSelectedItem()).getName());
				}
			}
		});
		addInput(defaultElementPanel, c, "Destination", destinationCombo, "");
		
		c.gridx = 0;
		c.gridwidth = 6;
		c.fill = GridBagConstraints.HORIZONTAL;
		lifecycleModelContainer = new JPanel();
		lifecycleModelContainer.setBorder(
				BorderFactory.createTitledBorder("Lifecycle Model"));
		lifecycleModelContainer.setLayout(new BorderLayout());
		lifecycleModelPanel = LifecycleModelPanel.
				createLifecycleModelPanel(element.getLifecycleModel());
		lifecycleModelContainer.add(lifecycleModelPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		buttonPanel.add(new JButton(selectLifecycleModel));
		lifecycleModelContainer.add(buttonPanel, BorderLayout.SOUTH);
		defaultElementPanel.add(lifecycleModelContainer, c);
		
		// set input enabled state
		destinationCombo.setEnabled(element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()) == null
				|| scenario.getTemplate(element.getTemplateName()).isTransport());
		selectLifecycleModel.setEnabled(element.getTemplateName()  == null);
		lifecycleModelPanel.setTemplateMode(element.getTemplateName());
	}
	
	private final Action selectLifecycleModel = 
			new AbstractAction("Change") {
		private static final long serialVersionUID = 5851360899423844664L;

		public void actionPerformed(ActionEvent e) {
			selectLifecycleModelDialog();
		}
	};

	/**
	 * Select lifecycle model dialog.
	 */
	private void selectLifecycleModelDialog() {
		JComboBox modelTypeCombo = new JComboBox(new String[]{
				"Default", "Simple"});
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, 
				modelTypeCombo, "Select Lifecycle Model", JOptionPane.OK_CANCEL_OPTION)) {
			if(modelTypeCombo.getSelectedItem().equals("Default")) {
				element.setLifecycleModel(new DefaultLifecycleModel());
			} else if(modelTypeCombo.getSelectedItem().equals("Simple")) {
				element.setLifecycleModel(new MutableSimpleLifecycleModel());
			}
			lifecycleModelContainer.remove(lifecycleModelPanel);
			lifecycleModelPanel = LifecycleModelPanel.
					createLifecycleModelPanel(element.getLifecycleModel());
			lifecycleModelContainer.add(lifecycleModelPanel, BorderLayout.CENTER);
			if(getTopLevelAncestor() instanceof Window) {
				((Window)getTopLevelAncestor()).pack();
			}
		}
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
	 * @param scenario the scenario
	 * @param element the element
	 * @return the element panel
	 */
	public static ElementPanel createElementPanel(Scenario scenario, 
			MutableInfrastructureElement element) {
		if(element instanceof MutableAgricultureElement) {
			return new AgricultureElementPanel(scenario, (MutableAgricultureElement)element);
		} else if(element instanceof MutableWaterElement) {
			return new WaterElementPanel(scenario, (MutableWaterElement)element);
		} else if(element instanceof MutablePetroleumElement) {
			return new PetroleumElementPanel(scenario, (MutablePetroleumElement)element);
		} else if(element instanceof MutableElectricityElement) {
			return new ElectricityElementPanel(scenario, (MutableElectricityElement)element);
		} else {
			throw new IllegalArgumentException("Element panel not implemented.");
		}
	}
}
