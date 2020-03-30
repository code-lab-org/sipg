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
package edu.mit.sips.gui.base;

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
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import edu.mit.sips.core.City;
import edu.mit.sips.core.agriculture.EditableAgricultureElement;
import edu.mit.sips.core.base.EditableInfrastructureElement;
import edu.mit.sips.core.electricity.EditableElectricityElement;
import edu.mit.sips.core.lifecycle.DefaultLifecycleModel;
import edu.mit.sips.core.lifecycle.EditableSimpleLifecycleModel;
import edu.mit.sips.core.petroleum.EditablePetroleumElement;
import edu.mit.sips.core.water.EditableWaterElement;
import edu.mit.sips.gui.agriculture.AgricultureElementPanel;
import edu.mit.sips.gui.electricity.ElectricityElementPanel;
import edu.mit.sips.gui.event.DocumentChangeListener;
import edu.mit.sips.gui.petroleum.PetroleumElementPanel;
import edu.mit.sips.gui.water.WaterElementPanel;
import edu.mit.sips.io.Icons;
import edu.mit.sips.scenario.Scenario;

/**
 * Generic panel to edit properties of infrastructure elements.
 * 
 * @author Paul T. Grogan
 */
public class ElementPanel extends JPanel {
	private static final long serialVersionUID = -5595402005206587525L;

	/**
	 * Creates the element panel.
	 *
	 * @param scenario the scenario
	 * @param element the element
	 * @return the element panel
	 */
	public static ElementPanel createElementPanel(Scenario scenario, 
			EditableInfrastructureElement element) {
		if(element instanceof EditableAgricultureElement) {
			return new AgricultureElementPanel(scenario, (EditableAgricultureElement)element, false);
		} else if(element instanceof EditableWaterElement) {
			return new WaterElementPanel(scenario, (EditableWaterElement)element, false);
		} else if(element instanceof EditablePetroleumElement) {
			return new PetroleumElementPanel(scenario, (EditablePetroleumElement)element, false);
		} else if(element instanceof EditableElectricityElement) {
			return new ElectricityElementPanel(scenario, (EditableElectricityElement)element, false);
		} else {
			throw new IllegalArgumentException("Element panel not implemented.");
		}
	}
	
	private final Scenario scenario;
	private final EditableInfrastructureElement element;
	protected final boolean detailed;
	private final JTextField nameText;
	private final JComboBox<City> originCombo;
	private final JComboBox<City> destinationCombo;
	private final JPanel lifecycleModelContainer;
	private LifecycleModelPanel lifecycleModelPanel;
	private final ListCellRenderer<Object> cityRenderer = new DefaultListCellRenderer() {
		private static final long serialVersionUID = 3761951866857845749L;

		@Override
		public Component getListCellRendererComponent(JList<?> list,
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
	
	private final Action selectLifecycleModel = new AbstractAction("Change") {
		private static final long serialVersionUID = 5851360899423844664L;

		public void actionPerformed(ActionEvent e) {
			selectLifecycleModelDialog();
		}
	};

	/**
	 * Instantiates a new element panel.
	 *
	 * @param scenario the scenario
	 * @param element the element
	 * @param detailed the detailed
	 */
	public ElementPanel(final Scenario scenario, 
			final EditableInfrastructureElement element,
			final boolean detailed) {
		this.scenario = scenario;
		this.element = element;
		this.detailed = detailed;
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
		c.gridwidth = 2;
		defaultElementPanel.add(nameText, c);
		c.gridy++;
		c.gridx--;
		c.gridwidth = 1;
		
		
		originCombo = new JComboBox<City>(scenario.getCountry().getCities().toArray(new City[0]));
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

		destinationCombo = new JComboBox<City>(scenario.getCountry().getCities().toArray(new City[0]));
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
		if(element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()).isTransport()) {
			addInput(defaultElementPanel, c, "Origin", originCombo, "");
			addInput(defaultElementPanel, c, "Destination", destinationCombo, "");
		} else {
			addInput(defaultElementPanel, c, "Location", originCombo, "");
		}
		
		c.gridx = 0;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		lifecycleModelContainer = new JPanel();
		lifecycleModelContainer.setBorder(
				BorderFactory.createTitledBorder("Lifecycle Model"));
		lifecycleModelContainer.setLayout(new BorderLayout());
		lifecycleModelPanel = LifecycleModelPanel.
				createLifecycleModelPanel(scenario, element.getLifecycleModel());
		lifecycleModelContainer.add(lifecycleModelPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		// disable editing lifecycle model (for now)
		// buttonPanel.add(new JButton(selectLifecycleModel));
		lifecycleModelContainer.add(buttonPanel, BorderLayout.SOUTH);
		defaultElementPanel.add(lifecycleModelContainer, c);
		
		long commissionStart = 0;
		if(element.getLifecycleModel() instanceof EditableSimpleLifecycleModel) {
			commissionStart = ((EditableSimpleLifecycleModel)element.getLifecycleModel()).getTimeCommissionStart();
		}
		nameText.setEnabled(element.getTemplateName()  == null);
		originCombo.setEnabled(commissionStart >= scenario.getPresentTime());
		destinationCombo.setEnabled(commissionStart >= scenario.getPresentTime()
				&& (element.getTemplateName() == null 
				|| scenario.getTemplate(element.getTemplateName()) == null
				|| scenario.getTemplate(element.getTemplateName()).isTransport()));
		selectLifecycleModel.setEnabled(element.getTemplateName()  == null);
		lifecycleModelPanel.setTemplateMode(element.getTemplateName());
	}
	
	/**
	 * Adds the input.
	 *
	 * @param panel the panel
	 * @param c the c
	 * @param labelText the label text
	 * @param component the component
	 * @param units the units
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
	 * Adds the input.
	 *
	 * @param panel the panel
	 * @param c the c
	 * @param labelText1 the label text1
	 * @param component1 the component1
	 * @param units1 the units1
	 * @param labelText2 the label text2
	 * @param component2 the component2
	 * @param units2 the units2
	 */
	protected void addInput(JPanel panel, GridBagConstraints c, 
			String labelText1, JComponent component1, String units1, 
			String labelText2, JComponent component2, String units2) {
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		panel.add(new JLabel(labelText1), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		panel.add(component1, c);
		c.gridx++;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		panel.add(new JLabel(units1), c);
		c.gridx++;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		panel.add(new JLabel(labelText2), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		panel.add(component2, c);
		c.gridx++;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		panel.add(new JLabel(units2), c);
		c.gridy++;
		c.gridx-=5;
	}
	
	/**
	 * Select lifecycle model dialog.
	 */
	private void selectLifecycleModelDialog() {
		JComboBox<String> modelTypeCombo = new JComboBox<String>(new String[]{
				"Default", "Simple"});
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, 
				modelTypeCombo, "Select Lifecycle Model", JOptionPane.OK_CANCEL_OPTION)) {
			if(modelTypeCombo.getSelectedItem().equals("Default")) {
				element.setLifecycleModel(new DefaultLifecycleModel());
			} else if(modelTypeCombo.getSelectedItem().equals("Simple")) {
				element.setLifecycleModel(new EditableSimpleLifecycleModel());
			}
			lifecycleModelContainer.remove(lifecycleModelPanel);
			lifecycleModelPanel = LifecycleModelPanel.
					createLifecycleModelPanel(scenario, element.getLifecycleModel());
			lifecycleModelContainer.add(lifecycleModelPanel, BorderLayout.CENTER);
			if(getTopLevelAncestor() instanceof Window) {
				((Window)getTopLevelAncestor()).pack();
			}
		}
	}
}
