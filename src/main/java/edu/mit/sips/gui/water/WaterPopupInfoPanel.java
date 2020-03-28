package edu.mit.sips.gui.water;

import java.text.NumberFormat;

import javax.swing.JLabel;

import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.gui.base.DefaultPopupInfoPanel;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Class WaterElementInfoPanel.
 */
public class WaterPopupInfoPanel extends DefaultPopupInfoPanel 
		implements CurrencyUnitsOutput, WaterUnitsOutput, ElectricityUnitsOutput {
	private static final long serialVersionUID = -821170818354101199L;

	private final WaterElement element;
	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JLabel productionLabel, withdrawalsLabel;
	private final JLabel inputLabel, outputLabel;
	private final JLabel expensesLabel, electricityLabel;

	private final CurrencyUnits currencyUnits = CurrencyUnits.Msim;
	private final TimeUnits currencyTimeUnits = TimeUnits.year;
	private final ElectricityUnits electricityUnits = ElectricityUnits.GWh;
	private final TimeUnits electricityTimeUnits = TimeUnits.year;
	private final WaterUnits waterUnits = WaterUnits.MCM;
	private final TimeUnits waterTimeUnits = TimeUnits.year;
	
	/**
	 * Instantiates a new water element info panel.
	 *
	 * @param element the element
	 */
	public WaterPopupInfoPanel(WaterElement element) {
		super(element);
		
		this.element = element;
		productionLabel = new JLabel(
				format.format(WaterUnits.convertFlow(
						element.getWaterProduction(), element, this)),
						JLabel.RIGHT);
		withdrawalsLabel = new JLabel(
				format.format(WaterUnits.convertFlow(
						element.getAquiferWithdrawals(), element, this)),
						JLabel.RIGHT);
		inputLabel = new JLabel(
				format.format(WaterUnits.convertFlow(
						element.getWaterInput(), element, this)),
						JLabel.RIGHT);
		outputLabel = new JLabel(
				format.format(WaterUnits.convertFlow(
						element.getWaterOutput(), element, this)),
						JLabel.RIGHT);
		expensesLabel = new JLabel(
				format.format(CurrencyUnits.convertFlow(
						element.getTotalExpense(), element, this)),
						JLabel.RIGHT);
		electricityLabel = new JLabel(
				format.format(ElectricityUnits.convertFlow(
						element.getElectricityConsumption(), element, this)),
						JLabel.RIGHT);

		if(element.getTemplateName() == null 
				|| element.getMaxWaterProduction() > 0) {
			addField("Water Production:", productionLabel, 
					waterUnits + "/" + waterTimeUnits);
			addField("Aquifer Withdrawals:", withdrawalsLabel, 
					waterUnits.toString());
		}
		if(element.getTemplateName() == null 
				|| element.getMaxWaterInput() > 0) {
			addField("Water Input:", inputLabel, 
					waterUnits + "/" + waterTimeUnits);
			addField("Water Output:", outputLabel, 
					waterUnits + "/" + waterTimeUnits);
		}
		addField("Expenses:", expensesLabel, 
				currencyUnits + "/" + currencyTimeUnits);
		addField("Electricity Use:", electricityLabel, 
				electricityUnits + "/" + electricityTimeUnits);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.PopupInfoPanel#updateFields()
	 */
	@Override
	public void updateFields(long time) {
		super.updateFields(time);
		productionLabel.setText(format.format(WaterUnits.convertFlow(
				element.getWaterProduction(), 
						element, this)));
		withdrawalsLabel.setText(format.format(WaterUnits.convertFlow(
				element.getAquiferWithdrawals(), 
						element, this)));
		inputLabel.setText(format.format(WaterUnits.convertFlow(
				element.getWaterInput(),
						element, this)));
		outputLabel.setText(format.format(WaterUnits.convertFlow(
				element.getWaterOutput(), 
						element, this)));
		expensesLabel.setText(format.format(CurrencyUnits.convertFlow(
				element.getTotalExpense(), 
						element, this)));
		electricityLabel.setText(format.format(ElectricityUnits.convertFlow(
				element.getElectricityConsumption(), 
						element, this)));
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnits()
	 */
	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityTimeUnits()
	 */
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnits()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyTimeUnits()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnits()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterTimeUnits()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}
}
