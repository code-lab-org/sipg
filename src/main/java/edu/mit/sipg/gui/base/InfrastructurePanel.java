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
package edu.mit.sipg.gui.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import edu.mit.sipg.core.City;
import edu.mit.sipg.core.Society;
import edu.mit.sipg.core.agriculture.AgricultureElement;
import edu.mit.sipg.core.agriculture.AgricultureSystem;
import edu.mit.sipg.core.base.DefaultInfrastructureElement;
import edu.mit.sipg.core.base.EditableInfrastructureElement;
import edu.mit.sipg.core.base.InfrastructureElement;
import edu.mit.sipg.core.electricity.ElectricityElement;
import edu.mit.sipg.core.electricity.ElectricitySystem;
import edu.mit.sipg.core.lifecycle.DefaultSimpleLifecycleModel;
import edu.mit.sipg.core.lifecycle.EditableSimpleLifecycleModel;
import edu.mit.sipg.core.petroleum.PetroleumElement;
import edu.mit.sipg.core.petroleum.PetroleumSystem;
import edu.mit.sipg.core.water.WaterElement;
import edu.mit.sipg.core.water.WaterSystem;
import edu.mit.sipg.gui.event.UpdateEvent;
import edu.mit.sipg.gui.event.UpdateListener;
import edu.mit.sipg.gui.tree.ElementTreeCellRenderer;
import edu.mit.sipg.gui.tree.NetworkTreeModel;
import edu.mit.sipg.io.Icons;
import edu.mit.sipg.scenario.ElementTemplate;
import edu.mit.sipg.sim.Simulator;

/**
 * Panel to display and edit infrastructure elements.
 * 
 * @author Paul T. Grogan
 */
public class InfrastructurePanel extends JPanel implements UpdateListener {
	private static final long serialVersionUID = 1265630285708384683L;
	
	private final JPopupMenu contextPopup;
	private final NetworkTreeModel elementsTreeModel;
	private final JTree elementsTree;
	private final Simulator simulator;

	private final ListCellRenderer<Object> templateRenderer = new DefaultListCellRenderer() {
		private static final long serialVersionUID = 3761951866857845749L;

		@Override
		public Component getListCellRendererComponent(JList<?> list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, 
					isSelected, cellHasFocus);
			if(value instanceof ElementTemplate) {
				setText(((ElementTemplate)value).getName());
				switch(((ElementTemplate)value).getSector()) {
				case AGRICULTURE:
					setIcon(Icons.AGRICULTURE);
					break;
				case ELECTRICITY:
					setIcon(Icons.ELECTRICITY);
					break;
				case PETROLEUM:
					setIcon(Icons.PETROLEUM);
					break;
				case WATER:
					setIcon(Icons.WATER);
					break;
				}
			}
			return this;
		}
	};
	private final TreeCellRenderer elementCellRenderer = new ElementTreeCellRenderer();
	private final Action addElementTemplate = new AbstractAction(
			"Add Project",Icons.ADD) {
		private static final long serialVersionUID = -6723630338741885195L;

		@Override
		public void actionPerformed(ActionEvent e) {
			addElementTemplateDialog();
		}
	};
	private final Action editElement = new AbstractAction( 
			"Edit Project", Icons.EDIT) {
		private static final long serialVersionUID = -3748518859196760510L;

		@Override
		public void actionPerformed(ActionEvent e) {
			InfrastructureElement selection = elementsTreeModel.getElement(
					elementsTree.getSelectionPath());
			if(selection != null) {
				openElementDialog((InfrastructureElement)selection);
				elementsTree.setSelectionPath(elementsTreeModel.getPath(selection));
			}
		}
	};
	private final Action removeElement = new AbstractAction(
			"Delete", Icons.DELETE) {
		private static final long serialVersionUID = -3748518859196760510L;

		@Override
		public void actionPerformed(ActionEvent e) {
			removeElementDialog();
		}
	};
	
	private boolean isSimulating = false;
	
	/**
	 * Instantiates a new elements pane.
	 *
	 * @param simulator the simulator
	 */
	public InfrastructurePanel(Simulator simulator) {
		this.simulator = simulator;
		simulator.addUpdateListener(this);
		
		contextPopup = new JPopupMenu();
		contextPopup.add(new JMenuItem(addElementTemplate));
		contextPopup.add(new JMenuItem(editElement));
		contextPopup.add(new JMenuItem(removeElement));
		
		addElementTemplate.putValue(Action.SHORT_DESCRIPTION, 
				"Add a new infrastructure project.");
		editElement.putValue(Action.SHORT_DESCRIPTION, 
				"Edit an existing infrastructure project.");
		removeElement.putValue(Action.SHORT_DESCRIPTION, 
				"Remove an existing infrastructure project.");
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(150,100));
		elementsTreeModel = new NetworkTreeModel();
		elementsTree = new JTree(elementsTreeModel);
		elementsTree.setRootVisible(false);
		elementsTree.setShowsRootHandles(true);
		elementsTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		elementsTree.setCellRenderer(elementCellRenderer);
		
		elementsTree.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, 0), "removeElement");
		elementsTree.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_BACK_SPACE, 0), "removeElement");
		elementsTree.getActionMap().put("removeElement", removeElement);
		elementsTree.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0), "editElement");
		elementsTree.getActionMap().put("editElement", editElement);
		
		elementsTree.addMouseListener(new MouseAdapter() {
			private void maybeShowPopup(MouseEvent e) {
				if(e.isPopupTrigger()) {
					contextPopup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1 
						&& e.getClickCount() == 2) {
					
					editElement.actionPerformed(
							new ActionEvent(elementsTree, 
									(int) System.currentTimeMillis(), 
									"addElement"));
				}
				maybeShowPopup(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
		});
		/*
		elementsTree.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				elementPopup.setVisible(false);
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				InfrastructureElement element = elementsTreeModel.getElement(
						elementsTree.getPathForLocation(e.getX(), e.getY()));
				if(element==null) {
					elementPopup.setVisible(false);
				} else {
					if(!element.equals(elementPopup.getElement())) {
						elementPopup.setElement(element);
					}
					elementPopup.show(e.getComponent(), e.getX()+10, e.getY()+10);
				}
			}
		});
		*/
		elementsTree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {
					@Override
					public void valueChanged(TreeSelectionEvent e) {
						updateActions();
					}
		});
		add(new JScrollPane(elementsTree), BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		JButton addTemplateButton = new JButton(addElementTemplate);
		addTemplateButton.setText(null);
		buttonPanel.add(addTemplateButton);
		JButton editButton = new JButton(editElement);
		editButton.setText(null);
		buttonPanel.add(editButton);
		JButton removeButton = new JButton(removeElement);
		removeButton.setText(null);
		buttonPanel.add(removeButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Adds the element template dialog.
	 */
	private void addElementTemplateDialog() {
		JList<ElementTemplate> templateList = new JList<ElementTemplate>(simulator.getScenario().getTemplates(
				simulator.getScenario().getCountry().getLocalSectors()).toArray(new ElementTemplate[0]));
		templateList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		templateList.setCellRenderer(templateRenderer);
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, new JScrollPane(templateList), 
				"Select Element Template", JOptionPane.OK_CANCEL_OPTION)) {
			if(templateList.getSelectedValue() instanceof ElementTemplate) {
				ElementTemplate template = (ElementTemplate)templateList.getSelectedValue();
				City selectedCity = (City)elementsTreeModel.getSociety(
						elementsTree.getSelectionPath());
				if(selectedCity == null) {
					selectedCity = simulator.getScenario().getCountry().getCities().get(0);
				}
				EditableInfrastructureElement element = template.createElement(
						template.getTimeAvailable(), selectedCity.getName(), 
						selectedCity.getName()).getMutableElement();
				if(element.getLifecycleModel() instanceof EditableSimpleLifecycleModel) {
					((EditableSimpleLifecycleModel)element.getLifecycleModel()).setTimeCommissionStart(
							simulator.getScenario().getPresentTime());
				}
				openElementDialog(element.createElement());
			}
		}
	}
	
	/**
	 * Edits the element dialog.
	 *
	 * @param mutableElement the mutable element
	 * @return the infrastructure element
	 */
	private InfrastructureElement editElementDialog(EditableInfrastructureElement mutableElement) {		
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(getTopLevelAncestor(), 
				ElementPanel.createElementPanel(simulator.getScenario(), mutableElement), 
				"Edit Element", JOptionPane.OK_CANCEL_OPTION)) {
			try {
				return mutableElement.createElement();
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(getTopLevelAncestor(), 
						ex.getMessage(), "Error", 
						JOptionPane.ERROR_MESSAGE);
				return editElementDialog(mutableElement);
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Initialize this infrastructure panel.
	 */
	public void initialize() {
		elementsTreeModel.initialize(simulator.getScenario().getCountry());
		
		for(int i = 0; i < elementsTree.getRowCount(); i++) {
			elementsTree.expandRow(i);
		}
		
		updateActions();
	}
	
	/**
	 * Open element dialog.
	 *
	 * @param element the element
	 */
	private void openElementDialog(InfrastructureElement element) {
		InfrastructureElement newElement = editElementDialog(element.getMutableElement());
		if(newElement != null) {
			City oldCity = simulator.getScenario().getCountry().getCity(element.getOrigin());
			City newCity = simulator.getScenario().getCountry().getCity(newElement.getOrigin());
			if(element instanceof AgricultureElement 
					&& oldCity.getAgricultureSystem() instanceof AgricultureSystem.Local
					&& newCity.getAgricultureSystem() instanceof AgricultureSystem.Local) {
				((AgricultureSystem.Local) oldCity.getAgricultureSystem())
					.removeElement((AgricultureElement)element);
				((AgricultureSystem.Local) newCity.getAgricultureSystem())
					.addElement((AgricultureElement)newElement);
			} else if(element instanceof WaterElement
					&& oldCity.getWaterSystem() instanceof WaterSystem.Local
					&& newCity.getWaterSystem() instanceof WaterSystem.Local) {
				((WaterSystem.Local) oldCity.getWaterSystem())
					.removeElement((WaterElement)element);
				((WaterSystem.Local) newCity.getWaterSystem())
					.addElement((WaterElement)newElement);
			} else if(element instanceof ElectricityElement
					&& oldCity.getElectricitySystem() instanceof ElectricitySystem.Local
					&& newCity.getElectricitySystem() instanceof ElectricitySystem.Local) {
				((ElectricitySystem.Local) oldCity.getElectricitySystem())
					.removeElement((ElectricityElement)element);
				((ElectricitySystem.Local) newCity.getElectricitySystem())
					.addElement((ElectricityElement)newElement);
			} else if(element instanceof PetroleumElement
					&& oldCity.getPetroleumSystem() instanceof PetroleumSystem.Local
					&& newCity.getPetroleumSystem() instanceof PetroleumSystem.Local) {
				((PetroleumSystem.Local) oldCity.getPetroleumSystem())
					.removeElement((PetroleumElement)element);
				((PetroleumSystem.Local) newCity.getPetroleumSystem())
					.addElement((PetroleumElement)newElement);
			} else {
				throw new IllegalStateException(
						"Element was not a known element type.");
			}
			elementsTreeModel.elementRemoved(element);
			if(simulator.getScenario().getCountry().getInternalElements().contains(newElement)) {
				elementsTreeModel.elementAdded(newElement);
				elementsTree.setSelectionPath(elementsTreeModel.getPath(newElement));
			}
		}
	}
	
	/**
	 * Removes the element dialog.
	 */
	private void removeElementDialog() {
		InfrastructureElement selection = elementsTreeModel.getElement(
				elementsTree.getSelectionPath());
		int selectionIndex = elementsTree.getSelectionRows()[0];
		City city = ((City)elementsTreeModel.getSociety(elementsTree.getSelectionPath()));
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, 
				"Remove " + selection + "?", "Remove Element", 
				JOptionPane.OK_CANCEL_OPTION)) {
			if(selection instanceof AgricultureElement
					&& city.getAgricultureSystem() instanceof AgricultureSystem.Local) {
				((AgricultureSystem.Local)city.getAgricultureSystem())
				.removeElement((AgricultureElement)selection);
			} else if(selection instanceof WaterElement
					&& city.getWaterSystem() instanceof WaterSystem.Local) {
				((WaterSystem.Local)city.getWaterSystem())
				.removeElement((WaterElement)selection);
			} else if(selection instanceof ElectricityElement
					&& city.getElectricitySystem() instanceof ElectricitySystem.Local) {
				((ElectricitySystem.Local)city.getElectricitySystem())
				.removeElement((ElectricityElement)selection);
			} else if(selection instanceof PetroleumElement
					&& city.getPetroleumSystem() instanceof PetroleumSystem.Local) {
				((PetroleumSystem.Local)city.getPetroleumSystem())
				.removeElement((PetroleumElement)selection);
			} else {
				throw new IllegalStateException(
						"Selection was not a known element type.");
			}
			elementsTreeModel.elementRemoved(selection);
			elementsTree.setSelectionRow(Math.min(selectionIndex, 
					elementsTree.getRowCount() - 1));
		}
	}
	
	@Override
	public void simulationCompleted(UpdateEvent event) {
		elementsTree.repaint();
		isSimulating = false;
		updateActions();
	}

	@Override
	public void simulationInitialized(UpdateEvent event) {
		elementsTree.repaint();
		isSimulating = false;
		updateActions();
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		isSimulating = true;
		elementsTree.repaint();
		updateActions();
	}

	/**
	 * Update actions.
	 */
	private void updateActions() {		
		Society society = elementsTreeModel.getSociety(
				elementsTree.getSelectionPath());
		addElementTemplate.setEnabled(!isSimulating && society != null);
		InfrastructureElement element = elementsTreeModel.getElement(
				elementsTree.getSelectionPath());
		editElement.setEnabled(!isSimulating && element != null);
		boolean canRemove = !isSimulating && element != null;
		
		if(element instanceof DefaultInfrastructureElement 
				&& ((DefaultInfrastructureElement)element).getLifecycleModel() 
				instanceof DefaultSimpleLifecycleModel) {
			DefaultSimpleLifecycleModel model = (DefaultSimpleLifecycleModel) 
					((DefaultInfrastructureElement) element).getLifecycleModel();
			canRemove = canRemove && model.getTimeCommissionStart() >= simulator.getScenario().getPresentTime();
		}
		removeElement.setEnabled(canRemove);
	}
}
