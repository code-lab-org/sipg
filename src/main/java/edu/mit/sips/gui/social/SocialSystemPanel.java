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
package edu.mit.sips.gui.social;

import org.jfree.data.xy.DefaultTableXYDataset;

import edu.mit.sips.core.Country;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.base.InfrastructureSystem;
import edu.mit.sips.core.social.SocialSoS;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.gui.PlottingUtils;
import edu.mit.sips.gui.base.InfrastructureSystemPanel;
import edu.mit.sips.gui.event.UpdateEvent;
import edu.mit.sips.io.Icons;
import edu.mit.sips.units.CurrencyUnits;
import edu.mit.sips.units.CurrencyUnitsOutput;
import edu.mit.sips.units.TimeUnits;

/**
 * An implementation of the infrastructure system panel for the social sector.
 * 
 * @author Paul T. Grogan
 */
public class SocialSystemPanel extends InfrastructureSystemPanel implements CurrencyUnitsOutput {
	private static final long serialVersionUID = -8472419089458128152L;
	
	private static final CurrencyUnits currencyUnits = CurrencyUnits.Bsim;
	private static final TimeUnits currencyTimeUnits = TimeUnits.year;

	DefaultTableXYDataset infrastructureSystemRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset infrastructureSystemNetRevenue = new DefaultTableXYDataset();
	DefaultTableXYDataset populationDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset populationTotalDataset = new DefaultTableXYDataset();
	DefaultTableXYDataset domesticProduct = new DefaultTableXYDataset();
	DefaultTableXYDataset domesticProductPerCapita = new DefaultTableXYDataset();
	DefaultTableXYDataset cashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset netCashFlow = new DefaultTableXYDataset();
	DefaultTableXYDataset cumulativeBalance = new DefaultTableXYDataset();
	DefaultTableXYDataset capitalExpense = new DefaultTableXYDataset();
	DefaultTableXYDataset capitalExpenseTotal = new DefaultTableXYDataset();
	DefaultTableXYDataset cumulativeCapitalExpense = new DefaultTableXYDataset();

	/**
	 * Instantiates a new social system panel.
	 *
	 * @param socialSystem the social system
	 */
	public SocialSystemPanel(SocialSystem socialSystem) {
		super(socialSystem);

		addTab("Net Revenue", Icons.REVENUE, createStackedAreaChart(
				getSocialSystem().getName() + " Net Revenue",
				"Annual Net Revenue (" + getCurrencyUnits() + ")", infrastructureSystemRevenue, 
				PlottingUtils.getSystemColors(getSociety().getInfrastructureSystems()), 
				infrastructureSystemNetRevenue,
				"Cumulative Net Revenue (" + getCurrencyUnits() + ")", cumulativeBalance));
		addTab("Budget", Icons.INVESTMENT, createStackedAreaChart(
				getSocialSystem().getName() + " Budget",
				"Annual Capital Expenses (" + getCurrencyUnits() + ")", capitalExpense, 
				PlottingUtils.getSystemColors(getSociety().getInfrastructureSystems()),
				capitalExpenseTotal));
	}

	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}

	/**
	 * Gets the social system.
	 *
	 * @return the social system
	 */
	public SocialSystem getSocialSystem() {
		return (SocialSystem) getInfrastructureSystem(); 
	}

	/**
	 * Initialize this social system panel.
	 */
	private void initialize() {
		cumulativeBalance.removeAllSeries();
		infrastructureSystemRevenue.removeAllSeries();
		infrastructureSystemNetRevenue.removeAllSeries();
		populationDataset.removeAllSeries();
		populationTotalDataset.removeAllSeries();
		domesticProduct.removeAllSeries();
		domesticProductPerCapita.removeAllSeries();
		cashFlow.removeAllSeries();
		netCashFlow.removeAllSeries();
		capitalExpense.removeAllSeries();
		capitalExpenseTotal.removeAllSeries();
		cumulativeCapitalExpense.removeAllSeries();
	}


	@Override
	public void simulationCompleted(UpdateEvent event) { }

	@Override
	public void simulationInitialized(UpdateEvent event) {
		initialize();
	}

	@Override
	public void simulationUpdated(UpdateEvent event) {
		update((int)event.getTime());
	}

	/**
	 * Update.
	 *
	 * @param year the year
	 */
	private void update(int year) {
		if(getSociety() instanceof Country) {
			updateSeries(cumulativeBalance, "Cumulative Net Revenue", year, 
					CurrencyUnits.convertStock(((Country)getSociety()).getFunds(), 
							getSociety(), this));
		} else {
			updateSeries(cumulativeBalance, "Cumulative Net Revenue", year, 
					CurrencyUnits.convertStock(getSociety().getCumulativeCashFlow(), 
							getSociety(), this));
		}
		updateSeries(infrastructureSystemNetRevenue, "Annual Net Revenue", year, 
				CurrencyUnits.convertFlow(getSociety().getTotalCashFlow(), 
						getSociety(), this));

		
		for(InfrastructureSystem nestedSociety : getSociety().getInfrastructureSystems()) {
			updateSeries(infrastructureSystemRevenue, nestedSociety.getName(), year, 
					CurrencyUnits.convertFlow(nestedSociety.getCashFlow(), 
							nestedSociety, this));
		}
		
		if(getSocialSystem() instanceof SocialSoS) {
			for(Society nestedSociety : getSociety().getNestedSocieties()) {
				updateSeries(cashFlow, nestedSociety.getName(), year, 
						CurrencyUnits.convertFlow(nestedSociety.getTotalCashFlow(), 
								nestedSociety, this));
			}
			updateSeries(netCashFlow, "Net Revenue", year, 
					CurrencyUnits.convertFlow(getSociety().getTotalCashFlow(), 
							getSociety(), this));
		}

		for(InfrastructureSystem nestedSystem : getSociety().getInfrastructureSystems()) {
			updateSeries(capitalExpense, nestedSystem.getName(), year, 
					CurrencyUnits.convertFlow(nestedSystem.getCapitalExpense(), 
							nestedSystem, this));
		}
		updateSeries(capitalExpenseTotal, "Annual Capital Expenses", year, 
				CurrencyUnits.convertFlow(getSociety().getTotalCapitalExpense(), 
						getSociety(), this));
		if(getSociety() instanceof Country) {
			updateSeries(capitalExpenseTotal, "Annual Maximum Budget", year,
					((Country) getSociety()).getCapitalBudgetLimit());
		}

		if(getSociety().getSocialSystem() instanceof SocialSystem.Local) { 
			updateSeries(cumulativeCapitalExpense, "Cumulative Total", year, 
					CurrencyUnits.convertFlow(
							getSociety().getCumulativeCapitalExpense(), 
							getSociety().getSocialSystem(), this));
		}
	}
}
