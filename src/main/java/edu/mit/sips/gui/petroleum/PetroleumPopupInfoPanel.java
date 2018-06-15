package edu.mit.sips.gui.petroleum;

import java.text.NumberFormat;

import javax.swing.JLabel;

import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.gui.DefaultPopupInfoPanel;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class PetroleumElementInfoPanel.
 */
public class PetroleumPopupInfoPanel extends DefaultPopupInfoPanel 
		implements CurrencyUnitsOutput, ElectricityUnitsOutput, OilUnitsOutput{
	private static final long serialVersionUID = -821170818354101199L;
	private static final CurrencyUnits currencyUnits = CurrencyUnits.Msim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.Mtoe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;

	private final PetroleumElement element;
	private final NumberFormat format = NumberFormat.getNumberInstance();
	private final JLabel productionLabel, withdrawalsLabel;
	private final JLabel inputLabel, outputLabel;
	private final JLabel expensesLabel, electricityLabel;
	
	/**
	 * Instantiates a new water element info panel.
	 *
	 * @param element the element
	 */
	public PetroleumPopupInfoPanel(PetroleumElement element) {
		super(element);
		
		this.element = element;
		productionLabel = new JLabel(format.format(OilUnits.convertFlow(
				element.getPetroleumProduction(), element, this)), JLabel.RIGHT);
		withdrawalsLabel = new JLabel(format.format(OilUnits.convertFlow(
				element.getPetroleumWithdrawals(), element, this)), JLabel.RIGHT);
		inputLabel = new JLabel(format.format(OilUnits.convertFlow(
				element.getPetroleumInput(), element, this)), JLabel.RIGHT);
		outputLabel = new JLabel(format.format(OilUnits.convertFlow(
				element.getPetroleumOutput(), element, this)), JLabel.RIGHT);
		expensesLabel = new JLabel(format.format(CurrencyUnits.convertFlow(
				element.getTotalExpense(), element, this)), JLabel.RIGHT);
		electricityLabel = new JLabel(format.format(ElectricityUnits.convertFlow(
				element.getElectricityConsumption(), element, this)), JLabel.RIGHT);


		if(element.getTemplateName() == null 
				|| element.getMaxPetroleumProduction() > 0) {
			addField("Petroleum Production:", productionLabel, 
					oilUnits + "/" + oilTimeUnits);
			addField("Reserves Use:", withdrawalsLabel, 
					oilUnits + "/" + oilTimeUnits);
		}
		if(element.getTemplateName() == null 
				|| element.getMaxPetroleumInput() > 0) {
			addField("Petroleum Input:", inputLabel, 
					oilUnits + "/" + oilTimeUnits);
			addField("Petroleum Output:", outputLabel, 
					oilUnits + "/" + oilTimeUnits);
		}
		addField("Total Expenses:", expensesLabel, 
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
		productionLabel.setText(
				format.format(element.getPetroleumProduction()));
		withdrawalsLabel.setText(
				format.format(element.getPetroleumWithdrawals()));
		inputLabel.setText(
				format.format(element.getPetroleumInput()));
		outputLabel.setText(
				format.format(element.getPetroleumOutput()));
		expensesLabel.setText(
				format.format(element.getTotalExpense()));
		electricityLabel.setText(
				format.format(element.getElectricityConsumption()));
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
}
