package edu.mit.sips.gui;

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
import javax.swing.JComboBox;
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
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import edu.mit.sips.ElementTemplate;
import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.MutableInfrastructureElement;
import edu.mit.sips.core.Region;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.core.agriculture.MutableAgricultureElement;
import edu.mit.sips.core.energy.ElectricityElement;
import edu.mit.sips.core.energy.EnergyElement;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.energy.MutableElectricityElement;
import edu.mit.sips.core.energy.MutablePetroleumElement;
import edu.mit.sips.core.energy.PetroleumElement;
import edu.mit.sips.core.water.MutableWaterElement;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.gui.comp.ElementTreeNode;
import edu.mit.sips.gui.comp.NetworkTreeModel;
import edu.mit.sips.gui.comp.SocietyTreeNode;
import edu.mit.sips.io.Icons;
import edu.mit.sips.sim.Simulator;

/**
 * The Class ElementsPane.
 */
public class ElementsPane extends JPanel {
	private static final long serialVersionUID = 1265630285708384683L;
	
	private final JPopupMenu contextPopup;
	private final ElementPopup elementPopup;
	private final NetworkTreeModel elementsTreeModel;
	private final JTree elementsTree;
	private final Simulator simulator;

	private final ListCellRenderer templateRenderer = new DefaultListCellRenderer() {
		private static final long serialVersionUID = 3761951866857845749L;

		@Override
		public Component getListCellRendererComponent(JList list,
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
				case ENERGY:
					setIcon(Icons.ENERGY);
					break;
				case WATER:
					setIcon(Icons.WATER);
					break;
				}
			}
			return this;
		}
	};
	private final TreeCellRenderer elementCellRenderer = new DefaultTreeCellRenderer() {
		private static final long serialVersionUID = -923629724878442949L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree,
				Object value, boolean sel, boolean expanded, boolean leaf,
				int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			if(value instanceof SocietyTreeNode) {
				SocietyTreeNode node = (SocietyTreeNode) value;
				setText(node.getUserObject().getName());
				if(node.getUserObject() instanceof Country) {
					setIcon(Icons.COUNTRY);
				} else if(node.getUserObject() instanceof Region) {
					setIcon(Icons.REGION);
				} else if(node.getUserObject() instanceof City) {
					setIcon(Icons.CITY);
				}
			} else if(value instanceof ElementTreeNode) {
				ElementTreeNode node = (ElementTreeNode) value;
				setText(node.getUserObject().getName());
				if(node.getUserObject() instanceof AgricultureElement) {
					setIcon(Icons.AGRICULTURE);
				} else if(node.getUserObject() instanceof WaterElement) {
					setIcon(Icons.WATER);
				} else if(node.getUserObject() instanceof ElectricityElement) {
					setIcon(Icons.ELECTRICITY);
				} else if(node.getUserObject() instanceof PetroleumElement) {
					setIcon(Icons.PETROLEUM);
				}
			}
			return this;
		}
	};

	private final Action addElementTemplate = new AbstractAction(
			"Add from Template", Icons.ADD_WIZARD) {
		private static final long serialVersionUID = -6723630338741885195L;

		@Override
		public void actionPerformed(ActionEvent e) {
			addElementTemplateDialog();
		}
	};
	
	private final Action addElement = new AbstractAction(
			"Add Custom", Icons.ADD) {
		private static final long serialVersionUID = -3748518859196760510L;

		@Override
		public void actionPerformed(ActionEvent e) {
			addElementDialog();
		}
	};
	
	private final Action editElement = new AbstractAction( 
			"Edit Properties", Icons.EDIT) {
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
	
	/**
	 * Instantiates a new elements pane.
	 *
	 * @param simulator the simulator
	 */
	public ElementsPane(Simulator simulator) {
		this.simulator = simulator;
		
		contextPopup = new JPopupMenu();
		contextPopup.add(new JMenuItem(addElementTemplate));
		contextPopup.add(new JMenuItem(addElement));
		contextPopup.add(new JMenuItem(editElement));
		contextPopup.add(new JMenuItem(removeElement));
		
		elementPopup = new ElementPopup(simulator);
		
		addElementTemplate.putValue(Action.SHORT_DESCRIPTION, 
				"Add element from a template.");
		addElement.putValue(Action.SHORT_DESCRIPTION, 
				"Add custom element.");
		editElement.putValue(Action.SHORT_DESCRIPTION, 
				"Edit element.");
		removeElement.putValue(Action.SHORT_DESCRIPTION, 
				"Remove element.");
		
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

			private void maybeShowPopup(MouseEvent e) {
				if(e.isPopupTrigger()) {
					contextPopup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		elementsTree.addMouseMotionListener(new MouseAdapter() {
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
			@Override
			public void mouseExited(MouseEvent e) {
				elementPopup.setVisible(false);
			}
		});
		elementsTree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {
					@Override
					public void valueChanged(TreeSelectionEvent e) {
						boolean isElementSelected = elementsTreeModel.getElement(
								elementsTree.getSelectionPath()) != null;
						editElement.setEnabled(isElementSelected);
						removeElement.setEnabled(isElementSelected);
					}
		});
		add(new JScrollPane(elementsTree), BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		JButton addTemplateButton = new JButton(addElementTemplate);
		addTemplateButton.setText(null);
		buttonPanel.add(addTemplateButton);
		JButton addButton = new JButton(addElement);
		addButton.setText(null);
		buttonPanel.add(addButton);
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
		JList templateList = new JList(ElementTemplate.values());
		templateList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		templateList.setCellRenderer(templateRenderer);
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, new JScrollPane(templateList), 
				"Select Element Template", JOptionPane.OK_CANCEL_OPTION)) {
			if(templateList.getSelectedValue() instanceof ElementTemplate) {
				ElementTemplate template = (ElementTemplate)templateList.getSelectedValue();
				City selectedCity = (City)elementsTreeModel.getSociety(
						elementsTree.getSelectionPath());
				if(selectedCity == null) {
					selectedCity = simulator.getCountry().getCities().get(0);
				}
				MutableInfrastructureElement element = template.createElement(
						template.getTimeAvailable(), selectedCity.getName(), 
						selectedCity.getName()).getMutableElement();
				openElementDialog(element.createElement());
			}
		}
	}
	
	/**
	 * Adds the element dialog.
	 */
	private void addElementDialog() {
		JComboBox elementTypeCombo = new JComboBox(new String[]{
				"Agriculture", "Water", "Petroleum", "Electricity"});
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, elementTypeCombo, 
				"Select Element Type", JOptionPane.OK_CANCEL_OPTION)) {
			MutableInfrastructureElement element;
			if(elementTypeCombo.getSelectedItem().equals("Agriculture")) {
				element = new MutableAgricultureElement();
			} else if(elementTypeCombo.getSelectedItem().equals("Water")) {
				element = new MutableWaterElement();
			} else if(elementTypeCombo.getSelectedItem().equals("Petroleum")) {
				element = new MutablePetroleumElement();
			} else if(elementTypeCombo.getSelectedItem().equals("Electricity")) {
				element = new MutableElectricityElement();
			} else {
				throw new IllegalStateException(
						"Selection was not a known element type.");
			}
			element.setOrigin(((City)elementsTreeModel.getSociety(
					elementsTree.getSelectionPath())).getName());
			element.setDestination(((City)elementsTreeModel.getSociety(
					elementsTree.getSelectionPath())).getName());
			openElementDialog(element.createElement());
		}
	}
	
	/**
	 * Initialize.
	 */
	public void initialize() {
		elementsTreeModel.setState(simulator.getCountry());
		editElement.setEnabled(false);
		removeElement.setEnabled(false);
	}
	
	/**
	 * Edits the element dialog.
	 *
	 * @param mutableElement the mutable element
	 * @return the infrastructure element
	 */
	private InfrastructureElement editElementDialog(MutableInfrastructureElement mutableElement) {		
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(getTopLevelAncestor(), 
				ElementPanel.createElementPanel(simulator.getCountry(), mutableElement), 
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
	 * Open element dialog.
	 *
	 * @param element the element
	 */
	private void openElementDialog(InfrastructureElement element) {
		InfrastructureElement newElement = editElementDialog(element.getMutableElement());
		if(newElement != null) {
			City city = ((City)elementsTreeModel.getSociety(elementsTree.getSelectionPath()));
			if(element instanceof AgricultureElement 
					&& city.getAgricultureSystem() instanceof AgricultureSystem.Local) {
				AgricultureSystem.Local agricultureSystem = 
						(DefaultAgricultureSystem.Local) city.getAgricultureSystem();
				agricultureSystem.removeElement((AgricultureElement)element);
				agricultureSystem.addElement((AgricultureElement)newElement);
			} else if(element instanceof WaterElement
					&& city.getWaterSystem() instanceof WaterSystem.Local) {
				WaterSystem.Local waterSystem = 
						(WaterSystem.Local) city.getWaterSystem();
				waterSystem.removeElement((WaterElement)element);
				waterSystem.addElement((WaterElement)newElement);
			} else if(element instanceof EnergyElement
					&& city.getEnergySystem() instanceof EnergySystem.Local) {
				EnergySystem.Local energySystem = 
						(EnergySystem.Local) city.getEnergySystem();
				energySystem.removeElement((EnergyElement)element);
				energySystem.addElement((EnergyElement)newElement);
			} else {
				throw new IllegalStateException(
						"Element was not a known element type.");
			}
			elementsTreeModel.elementRemoved(element);
			if(simulator.getCountry().getInternalElements().contains(newElement)) {
				elementsTreeModel.elementAdded(newElement);
				elementsTree.setSelectionPath(elementsTreeModel.getPath(newElement));
			}
		}
	}
	
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
			} else if(selection instanceof EnergyElement
					&& city.getEnergySystem() instanceof EnergySystem.Local) {
				((EnergySystem.Local)city.getEnergySystem())
				.removeElement((EnergyElement)selection);
			} else {
				throw new IllegalStateException(
						"Selection was not a known element type.");
			}
			elementsTreeModel.elementRemoved(selection);
			elementsTree.setSelectionRow(Math.min(selectionIndex, 
					elementsTree.getRowCount() - 1));
		}
	}
}
