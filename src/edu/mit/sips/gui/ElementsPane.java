package edu.mit.sips.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.mit.sips.ElementTemplate;
import edu.mit.sips.core.City;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.MutableInfrastructureElement;
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
import edu.mit.sips.io.Icons;

/**
 * The Class ElementsPane.
 */
public class ElementsPane extends JPanel {
	private static final long serialVersionUID = 1265630285708384683L;
	
	private DefaultListModel elementsListModel;
	private JList elementsList;
	private final City city;
	private final Comparator<InfrastructureElement> elementsComparator = 
			new Comparator<InfrastructureElement>() {
		@Override
		public int compare(InfrastructureElement o1,
				InfrastructureElement o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	private final ListCellRenderer elementCellRenderer = 
			new DefaultListCellRenderer() {
				private static final long serialVersionUID = -923629724878442949L;
		
				@Override
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, 
							isSelected, cellHasFocus);
					if(value instanceof InfrastructureElement) {
						setText(((InfrastructureElement)value).getName());
						if(value instanceof AgricultureElement) {
							setIcon(Icons.AGRICULTURE);
						} else if(value instanceof WaterElement) {
							setIcon(Icons.WATER);
						} else if(value instanceof ElectricityElement) {
							setIcon(Icons.ELECTRICITY);
						} else if(value instanceof PetroleumElement) {
							setIcon(Icons.PETROLEUM);
						}
					}
					return this;
				}
			};
			
	private final Action addElementTemplate = new AbstractAction("Add*", 
			Icons.ADD) {
		private static final long serialVersionUID = -6723630338741885195L;

		@Override
		public void actionPerformed(ActionEvent e) {
			addElementTemplateDialog();
		}
	};
	
	private final Action addElement = new AbstractAction("Add", 
			Icons.ADD) {
		private static final long serialVersionUID = -3748518859196760510L;

		@Override
		public void actionPerformed(ActionEvent e) {
			addElementDialog();
		}
	};
	
	private final Action editElement = new AbstractAction("Edit", 
			Icons.EDIT) {
		private static final long serialVersionUID = -3748518859196760510L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Object selection = elementsList.getSelectedValue();
			if(selection instanceof InfrastructureElement) {
				openElementDialog((InfrastructureElement)selection);
			}
			elementsList.setSelectedValue(selection, true);
		}
	};
	
	private final Action removeElement = new AbstractAction("Remove", 
			Icons.DELETE) {
		private static final long serialVersionUID = -3748518859196760510L;

		@Override
		public void actionPerformed(ActionEvent e) {
			removeElementDialog();
		}
	};
	
	/**
	 * Instantiates a new elements pane.
	 *
	 * @param country the country
	 */
	public ElementsPane(City city) {
		this.city = city;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(150,100));
		elementsListModel = new DefaultListModel();
		elementsList = new JList(elementsListModel);
		elementsList.setCellRenderer(elementCellRenderer);
		
		elementsList.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, 0), "removeElement");
		elementsList.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_BACK_SPACE, 0), "removeElement");
		elementsList.getActionMap().put("removeElement", removeElement);
		elementsList.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, 0), "editElement");
		elementsList.getActionMap().put("editElement", editElement);
		
		elementsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1 
						&& e.getClickCount() == 2) {
					editElement.actionPerformed(
							new ActionEvent(elementsList, 
									(int) System.currentTimeMillis(), 
									"addElement"));
				}
			}
		});
		elementsList.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						editElement.setEnabled(elementsList.getSelectedIndex() >= 0);
						removeElement.setEnabled(elementsList.getSelectedIndex() >= 0);
					}
		});
		add(new JScrollPane(elementsList), BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		buttonPanel.add(new JButton(addElementTemplate));
		buttonPanel.add(new JButton(addElement));
		buttonPanel.add(new JButton(editElement));
		buttonPanel.add(new JButton(removeElement));
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void addElementTemplateDialog() {
		JComboBox elementTypeCombo = new JComboBox(ElementTemplate.values());
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, elementTypeCombo, 
				"Select Element Template", JOptionPane.OK_CANCEL_OPTION)) {
			// TODO get year
			MutableInfrastructureElement element = 
					((ElementTemplate)elementTypeCombo.getSelectedItem())
					.createElement(1950, city.getName()).getMutableElement();
			openElementDialog(element.createElement());
		}
	}
	
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
			element.setOrigin(city.getName());
			element.setDestination(city.getName());
			openElementDialog(element.createElement());
		}
	}
	
	/**
	 * Initialize.
	 */
	public void initialize() {
		elementsListModel.clear();
		List<InfrastructureElement> elements = 
				new ArrayList<InfrastructureElement>(city.getInternalElements());
		Collections.sort(elements, elementsComparator);
		
		for(InfrastructureElement element : elements) {
			elementsListModel.addElement(element);
		}
		
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
				ElementPanel.createElementPanel(city, mutableElement), 
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
			elementsListModel.removeElement(element);
			List<InfrastructureElement> elements = 
					new ArrayList<InfrastructureElement>(city.getInternalElements());
			Collections.sort(elements, elementsComparator);
			if(elements.contains(newElement)) {
				elementsListModel.add(elements.indexOf(newElement), newElement);
				elementsList.setSelectedValue(newElement, true);
			}
		}
	}
	
	private void removeElementDialog() {
		Object selection = elementsList.getSelectedValue();
		int selectionIndex = elementsList.getSelectedIndex();
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
			elementsListModel.removeElement(selection);
			elementsList.setSelectedIndex(Math.min(selectionIndex, 
					elementsListModel.getSize() - 1));
		}
	}
}
