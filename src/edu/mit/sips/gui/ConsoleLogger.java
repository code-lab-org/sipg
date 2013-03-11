package edu.mit.sips.gui;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;

/**
 * The Class ConsoleLogger.
 */
public class ConsoleLogger implements UpdateListener {

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationInitialized(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationInitialized(UpdateEvent event) {
		printState(event.getCountry(), event.getTime());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationUpdated(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationUpdated(UpdateEvent event) {
		printState(event.getCountry(), event.getTime());
	}
	
	/**
	 * Prints the state.
	 */
	private void printState(Country country, long time) {
		System.out.printf("%-20s  %15s |", "*" + time 
				+ " Annual Report*", country.getName());
		for(City city : country.getCities()) {
			System.out.printf(" %15s", city.getName());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Funds", "SAR", country.getFunds());
		System.out.println();
		
		System.out.printf("%-15s %-5s %15d |", "Population", "-", country.getSocialSystem().getPopulation());
		for(City city : country.getCities()) {
			System.out.printf(" %15d", city.getSocialSystem().getPopulation());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Econ Production", "SAR", 
				country.getDomesticProduction());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", city.getDomesticProduction());
		}
		System.out.println();
		System.out.printf("%-15s %-5s %15.2f |", "  per capita", "SAR", 
				country.getDomesticProduction() / country.getSocialSystem().getPopulation());
		for(City city : country.getCities()) {
			System.out.printf(" %15.2f", city.getDomesticProduction() / city.getSocialSystem().getPopulation());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Net Cash Flow", "SAR", country.getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", city.getCashFlow());
		}
		System.out.println();
		System.out.printf("%-15s %-5s %15.0f |", "  Agriculture", "SAR", 
				country.getAgricultureSystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getCashFlow());
		}
		System.out.println();
		System.out.printf("%-15s %-5s %15.0f |", "  Water", "SAR", 
				country.getWaterSystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getCashFlow());
		}
		System.out.println();
		System.out.printf("%-15s %-5s %15.0f |", "  Oil", "SAR", 
				country.getEnergySystem().getPetroleumSystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getPetroleumSystem().getCashFlow());
		}
		System.out.println();
		System.out.printf("%-15s %-5s %15.0f |", "  Elect", "SAR", 
				country.getEnergySystem().getElectricitySystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getCashFlow());
		}
		System.out.println();
		
		System.out.println();
		
		System.out.printf("%-15s %-5s %6.0f / %6.0f |", "Land Area Used", "km^2", 
				country.getAgricultureSystem().getLandAreaUsed(),
				country.getAgricultureSystem().getArableLandArea());
		for(City city : country.getCities()) {
			System.out.printf(" %6.0f / %6.0f", 
					city.getAgricultureSystem().getLandAreaUsed(),
					city.getAgricultureSystem().getArableLandArea());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Food Product", "Cal", 
				country.getAgricultureSystem().getFoodProduction());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getFoodProduction());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
				country.getAgricultureSystem().getSalesRevenue());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getSalesRevenue());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Food Distrib", "Cal", 0d);
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getFoodInDistribution() 
					- city.getAgricultureSystem().getFoodOutDistribution());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Water Use", "m^3", 
				country.getAgricultureSystem().getWaterConsumption());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getWaterConsumption());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Total Supply", "Cal", 
				country.getAgricultureSystem().getLocalFoodSupply());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getLocalFoodSupply());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Food Consumpt", "Cal", 
				country.getSocialSystem().getFoodConsumption());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getSocialSystem().getFoodConsumption());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "  per capita", "Cal", 
				country.getSocialSystem().getFoodConsumptionPerCapita());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getSocialSystem().getFoodConsumptionPerCapita());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Total Demand", "Cal", 
				country.getTotalFoodDemand());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getTotalFoodDemand());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Food Trade", "Cal", 
				country.getAgricultureSystem().getFoodImport()
				- country.getAgricultureSystem().getFoodExport());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getFoodImport()
					- city.getAgricultureSystem().getFoodExport());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
				country.getAgricultureSystem().getImportExpense()
				- country.getAgricultureSystem().getExportRevenue());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getImportExpense()
					- city.getAgricultureSystem().getExportRevenue());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Capital Ex", "SAR", 
				country.getAgricultureSystem().getCapitalExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getCapitalExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Operations Ex", "SAR", 
				country.getAgricultureSystem().getOperationsExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getOperationsExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Decomm Ex", "SAR", 
				country.getAgricultureSystem().getDecommissionExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getDecommissionExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Cash Flow", "SAR", 
				country.getAgricultureSystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getAgricultureSystem().getCashFlow());
		}
		System.out.println();

		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Water Reservoir", "m^3", 
				country.getWaterSystem().getWaterReservoirVolume());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", city.getWaterSystem().getWaterReservoirVolume());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Water Withdraw", "m^3", 
				country.getWaterSystem().getReservoirWaterWithdrawals());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", city.getWaterSystem().getReservoirWaterWithdrawals());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Water Product", "m^3", 
				country.getWaterSystem().getWaterProduction());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getWaterProduction());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
				country.getWaterSystem().getSalesRevenue());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getSalesRevenue());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Water Distrib", "m^3", 0d);
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getWaterInDistribution()
					- city.getWaterSystem().getWaterOutDistribution());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Elect Use", "kWh", 
				country.getWaterSystem().getElectricityConsumption());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getElectricityConsumption());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Total Supply", "m^3",  
				country.getWaterSystem().getLocalWaterSupply());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getLocalWaterSupply());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Water Consumpt", "m^3", 
				country.getSocialSystem().getWaterConsumption());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getSocialSystem().getWaterConsumption());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "  per capita", "m^3", 
				country.getSocialSystem().getWaterConsumptionPerCapita());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getSocialSystem().getWaterConsumptionPerCapita());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Total Demand", "m^3",  
				country.getTotalWaterDemand());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getTotalWaterDemand());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Water Import", "m^3", 
				country.getWaterSystem().getWaterImport());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getWaterImport());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "  Expense", "SAR", 
				country.getWaterSystem().getImportExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getImportExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Water Wasted", "m^3", 
				country.getWaterSystem().getWaterWasted());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getWaterWasted());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Capital Ex", "SAR", 
				country.getWaterSystem().getCapitalExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getCapitalExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Operations Ex", "SAR", 
				country.getWaterSystem().getOperationsExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getOperationsExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Decomm Ex", "SAR", 
				country.getWaterSystem().getDecommissionExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getDecommissionExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Cash Flow", "SAR", 
				country.getWaterSystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getWaterSystem().getCashFlow());
		}
		System.out.println();

		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Oil Reservoir", "bbl", 
				country.getEnergySystem().getPetroleumSystem().getPetroleumReservoirVolume());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", city.getEnergySystem().getPetroleumSystem().getPetroleumReservoirVolume());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Oil Withdraw", "m^3", 
				country.getEnergySystem().getPetroleumSystem().getPetroleumWithdrawals());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", city.getEnergySystem().getPetroleumSystem().getPetroleumWithdrawals());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Oil Product", "bbl", 
				country.getEnergySystem().getPetroleumSystem().getPetroleumProduction());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getPetroleumSystem().getPetroleumProduction());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
				country.getEnergySystem().getPetroleumSystem().getSalesRevenue());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getPetroleumSystem().getSalesRevenue());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Oil Distrib", "bbl", 0d);
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getPetroleumSystem().getPetroleumInDistribution()
					- city.getEnergySystem().getPetroleumSystem().getPetroleumOutDistribution());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Oil Trade", "bbl", 
				country.getEnergySystem().getPetroleumSystem().getPetroleumExport());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getPetroleumSystem().getPetroleumExport());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
				country.getEnergySystem().getPetroleumSystem().getExportRevenue() 
				- country.getEnergySystem().getPetroleumSystem().getImportExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getPetroleumSystem().getExportRevenue() 
					- city.getEnergySystem().getPetroleumSystem().getImportExpense());
		}
		System.out.println();
					
		System.out.printf("%-15s %-5s %15.0f |", "Total Supply", "bbl", 
				country.getEnergySystem().getPetroleumSystem().getLocalPetroleumSupply());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getPetroleumSystem().getLocalPetroleumSupply());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Total Demand", "bbl", 
				country.getTotalPetroleumDemand());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getTotalPetroleumDemand());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Capital Ex", "SAR", 
				country.getEnergySystem().getPetroleumSystem().getCapitalExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getPetroleumSystem().getCapitalExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Operations Ex", "SAR", 
				country.getEnergySystem().getPetroleumSystem().getOperationsExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getPetroleumSystem().getOperationsExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Decomm Ex", "SAR", 
				country.getEnergySystem().getPetroleumSystem().getDecommissionExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getPetroleumSystem().getDecommissionExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Cash Flow", "SAR", 
				country.getEnergySystem().getPetroleumSystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getPetroleumSystem().getCashFlow());
		}
		System.out.println();
		
		System.out.println();
					
		System.out.printf("%-15s %-5s %15.0f |", "Elect Product", "kWh", 
				country.getEnergySystem().getElectricitySystem().getElectricityProduction());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getElectricityProduction());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
				country.getEnergySystem().getElectricitySystem().getSalesRevenue());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getSalesRevenue());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Elect Distrib", "kWh", 0d);
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getElectricityInDistribution()
					- city.getEnergySystem().getElectricitySystem().getElectricityOutDistribution());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Water Use", "m^3", 
				country.getEnergySystem().getElectricitySystem().getWaterConsumption());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getWaterConsumption());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Oil Use", "bbl", 
				country.getEnergySystem().getElectricitySystem().getPetroleumConsumption());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getPetroleumConsumption());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Total Supply", "kWh", 
				country.getEnergySystem().getElectricitySystem().getTotalElectricitySupply());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getTotalElectricitySupply());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Elect Consumpt", "kWh", 
				country.getSocialSystem().getElectricityConsumption());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getSocialSystem().getElectricityConsumption());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "  per capita", "kWh", 
				country.getSocialSystem().getElectricityConsumptionPerCapita());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getSocialSystem().getElectricityConsumptionPerCapita());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Total Demand", "kWh", 
				country.getTotalElectricityDemand());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getTotalElectricityDemand());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Elect from Oil", "kWh", 
				country.getEnergySystem().getElectricitySystem().getElectricityFromBurningPetroleum());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getElectricityFromBurningPetroleum());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "  Oil Burned", "bbl", 
				country.getEnergySystem().getElectricitySystem().getPetroleumBurned());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getPetroleumBurned());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Elect Wasted", "kWh", 
				country.getEnergySystem().getElectricitySystem().getElectricityWasted());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getElectricityWasted());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Capital Ex", "SAR", 
				country.getEnergySystem().getElectricitySystem().getCapitalExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getCapitalExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Operations Ex", "SAR", 
				country.getEnergySystem().getElectricitySystem().getOperationsExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getOperationsExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Decomm Ex", "SAR", 
				country.getEnergySystem().getElectricitySystem().getDecommissionExpense());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getDecommissionExpense());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %15.0f |", "Cash Flow", "SAR", 
				country.getEnergySystem().getElectricitySystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getElectricitySystem().getCashFlow());
		}
		System.out.println();
		
		System.out.println();
		
		System.out.println();
	}
}
