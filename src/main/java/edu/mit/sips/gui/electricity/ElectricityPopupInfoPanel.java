package edu.mit.sips.gui.electricity;

import java.text.NumberFormat;

import javax.swing.JLabel;

import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.gui.DefaultPopupInfoPanel;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Class ElectricityElementInfoPanel.
 */
public class ElectricityPopupInfoPanel extends DefaultPopupInfoPanel 
		implements CurrencyUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput, WaterUnitsOutput{
	private static final long serialVersionUID = -821170818354101199L;
	private static final CurrencyUnits currencyUnits = CurrencyUnits.Msim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.TWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.Mtoe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.km3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;

	private final ElectricityElement element;
	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JLabel productionLabel;
	private final JLabel inputLabel, outputLabel;
	private final JLabel expensesLabel, waterLabel, petroleumLabel;
	
	/**
	 * Instantiates a new water element info panel.
	 *
	 * @param element the element
	 */
	public ElectricityPopupInfoPanel(ElectricityElement element) {
		super(element);
		
		this.element = element;
		productionLabel = new JLabel(format.format(ElectricityUnits.convertFlow(
				element.getElectricityProduction(), element, this)), JLabel.RIGHT);
		inputLabel = new JLabel(format.format(ElectricityUnits.convertFlow(
				element.getElectricityInput(), element, this)), JLabel.RIGHT);
		outputLabel = new JLabel(format.format(ElectricityUnits.convertFlow(
				element.getElectricityOutput(), element, this)), JLabel.RIGHT);
		expensesLabel = new JLabel(format.format(CurrencyUnits.convertFlow(
				element.getTotalExpense(), element, this)), JLabel.RIGHT);
		waterLabel = new JLabel(format.format(WaterUnits.convertFlow(
				element.getWaterConsumption(), element, this)),	JLabel.RIGHT);
		petroleumLabel = new JLabel(format.format(OilUnits.convertFlow(
				element.getPetroleumConsumption(), element, this)), JLabel.RIGHT);


		if(element.getTemplateName() == null 
				|| element.getMaxElectricityProduction() > 0) {
			addField("Electricity Production:", productionLabel, 
					electricityUnits + "/" + electricityTimeUnits);
		}
		if(element.getTemplateName() == null 
				|| element.getMaxElectricityInput() > 0) {
			addField("Electricity Input:", inputLabel, 
					electricityUnits + "/" + electricityTimeUnits);
			addField("Electricity Output:", outputLabel, 
					electricityUnits + "/" + electricityTimeUnits);
		}
		addField("Expenses:", expensesLabel, 
				currencyUnits + "/" + currencyTimeUnits);
		addField("Water Use:", waterLabel, 
				waterUnits + "/" + waterTimeUnits);
		addField("Petroleum Use:", petroleumLabel, 
				oilUnits + "/" + oilTimeUnits);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.PopupInfoPanel#updateFields()
	 */
	@Override
	public void updateFields(long time) {
		super.updateFields(time);
		productionLabel.setText(format.format(ElectricityUnits.convertFlow(
				element.getElectricityProduction(), element, this)));
		inputLabel.setText(format.format(ElectricityUnits.convertFlow(
				element.getElectricityInput(), element, this)));
		outputLabel.setText(format.format(ElectricityUnits.convertFlow(
				element.getElectricityOutput(), element, this)));
		expensesLabel.setText(format.format(CurrencyUnits.convertFlow(
				element.getTotalExpense(), element, this)));
		waterLabel.setText(format.format(WaterUnits.convertFlow(
				element.getWaterConsumption(), element, this)));
		petroleumLabel.setText(format.format(OilUnits.convertFlow(
				element.getPetroleumConsumption(), element, this)));
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
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityTimeUnits()
	 */
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnits()
	 */
	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilTimeUnits()
	 */
	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilUnits()
	 */
	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
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
