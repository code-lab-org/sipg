package edu.mit.sips.gui.agriculture;

import java.text.NumberFormat;

import javax.swing.JLabel;

import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.gui.base.DefaultPopupInfoPanel;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Class FoodElementInfoPanel.
 */
public class AgriculturePopupInfoPanel extends DefaultPopupInfoPanel 
		implements WaterUnitsOutput, CurrencyUnitsOutput, FoodUnitsOutput {
	private static final long serialVersionUID = -821170818354101199L;

	private final AgricultureElement element;
	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JLabel productionLabel, landLabel;
	private final JLabel inputLabel, outputLabel;
	private final JLabel expensesLabel, waterLabel;

	private final CurrencyUnits currencyUnits = CurrencyUnits.Msim;
	private final TimeUnits currencyTimeUnits = TimeUnits.year;
	private final FoodUnits foodUnits = FoodUnits.EJ;
	private final TimeUnits foodTimeUnits = TimeUnits.year;
	private final WaterUnits waterUnits = WaterUnits.MCM;
	private final TimeUnits waterTimeUnits = TimeUnits.year;
	
	/**
	 * Instantiates a new water element info panel.
	 *
	 * @param element the element
	 */
	public AgriculturePopupInfoPanel(AgricultureElement element) {
		super(element);
		
		this.element = element;
		productionLabel = new JLabel(format.format(
				FoodUnits.convertFlow(element.getFoodProduction(), element, this)),
				JLabel.RIGHT);
		landLabel = new JLabel(format.format(
				element.getLandArea()),
				JLabel.RIGHT);
		inputLabel = new JLabel(format.format(
				FoodUnits.convertFlow(element.getFoodInput(), element, this)),
				JLabel.RIGHT);
		outputLabel = new JLabel(format.format(
				FoodUnits.convertFlow(element.getFoodOutput(), element, this)),
				JLabel.RIGHT);
		expensesLabel = new JLabel(format.format(
				CurrencyUnits.convertFlow(element.getTotalExpense(), element, this)),
				JLabel.RIGHT);
		waterLabel = new JLabel(format.format(
				WaterUnits.convertFlow(element.getWaterConsumption(), element, this)),
				JLabel.RIGHT);


		if(element.getTemplateName() == null 
				|| element.getMaxLandArea() > 0) {
			addField("Food Production:", productionLabel, 
					foodUnits + "/" + foodTimeUnits);
			addField("Land Use:", landLabel, 
					"km^2");
		}
		if(element.getTemplateName() == null 
				|| element.getMaxFoodInput() > 0) {
			addField("Food Input:", inputLabel, 
					foodUnits + "/" + foodTimeUnits);
			addField("Food Output:", outputLabel, 
					foodUnits + "/" + foodTimeUnits);
		}
		addField("Expenses:", expensesLabel, 
				currencyUnits + "/" + currencyTimeUnits);
		addField("Water Use:", waterLabel, 
				waterUnits + "/" + waterTimeUnits);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.PopupInfoPanel#updateFields()
	 */
	@Override
	public void updateFields(long time) {
		super.updateFields(time);
		productionLabel.setText(format.format(
				FoodUnits.convertFlow(element.getFoodProduction(), 
						element, this)));
		landLabel.setText(format.format(
				element.getLandArea()));
		inputLabel.setText(format.format(
				FoodUnits.convertFlow(element.getFoodInput(), 
						element, this)));
		outputLabel.setText(format.format(
				FoodUnits.convertFlow(element.getFoodOutput(), 
						element, this)));
		expensesLabel.setText(format.format(
				CurrencyUnits.convertFlow(element.getTotalExpense(), 
						element, this)));
		waterLabel.setText(format.format(
				WaterUnits.convertFlow(element.getWaterConsumption(), 
						element, this)));
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyTimeUnits()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnits()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodTimeUnits()
	 */
	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnits()
	 */
	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterTimeUnits()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnits()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}
}
