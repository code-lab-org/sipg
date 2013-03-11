package edu.mit.sips.gui;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.water.WaterSystem;

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
	 * Gets the local agriculture systems.
	 *
	 * @param cities the cities
	 * @return the local agriculture systems
	 */
	private List<AgricultureSystem.Local> getLocalAgricultureSystems(List<City> cities) {
		List<AgricultureSystem.Local> systems = new ArrayList<AgricultureSystem.Local>();
		for(City city : cities) {
			if(city.getAgricultureSystem() instanceof AgricultureSystem.Local){
				systems.add((AgricultureSystem.Local)city.getAgricultureSystem());
			}
		}
		return systems;
	}

	/**
	 * Gets the local water systems.
	 *
	 * @param cities the cities
	 * @return the local water systems
	 */
	private List<WaterSystem.Local> getLocalWaterSystems(List<City> cities) {
		List<WaterSystem.Local> systems = new ArrayList<WaterSystem.Local>();
		for(City city : cities) {
			if(city.getWaterSystem() instanceof WaterSystem.Local){
				systems.add((WaterSystem.Local)city.getWaterSystem());
			}
		}
		return systems;
	}

	/**
	 * Gets the local energy systems.
	 *
	 * @param cities the cities
	 * @return the local energy systems
	 */
	private List<EnergySystem.Local> getLocalEnergySystems(List<City> cities) {
		List<EnergySystem.Local> systems = new ArrayList<EnergySystem.Local>();
		for(City city : cities) {
			if(city.getEnergySystem() instanceof EnergySystem.Local){
				systems.add((EnergySystem.Local)city.getEnergySystem());
			}
		}
		return systems;
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
		
		System.out.printf("%-15s %-5s %15.0f |", "Dom Product", "SAR", 
				country.getSocialSystem().getDomesticProduct());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", city.getSocialSystem().getDomesticProduct());
		}
		System.out.println();
		System.out.printf("%-15s %-5s %15.2f |", "  per capita", "SAR", 
				country.getSocialSystem().getDomesticProductPerCapita());
		for(City city : country.getCities()) {
			System.out.printf(" %15.2f", city.getSocialSystem().getDomesticProductPerCapita());
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
		System.out.printf("%-15s %-5s %15.0f |", "  Energy", "SAR", 
				country.getEnergySystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %15.0f", 
					city.getEnergySystem().getCashFlow());
		}
		System.out.println();
		
		System.out.println();
		
		if(country.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = 
					(AgricultureSystem.Local) country.getAgricultureSystem();
			System.out.printf("%-15s %-5s %6.0f / %6.0f |", "Land Area Used", "km^2", 
					agricultureSystem.getLandAreaUsed(),
					agricultureSystem.getArableLandArea());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %6.0f / %6.0f", 
						system.getLandAreaUsed(),
						system.getArableLandArea());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Food Product", "Cal", 
					agricultureSystem.getFoodProduction());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getFoodProduction());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
					agricultureSystem.getSalesRevenue());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getSalesRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Food Distrib", "Cal", 0d);
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getFoodInDistribution() 
						- system.getFoodOutDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Water Use", "m^3", 
					agricultureSystem.getWaterConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %15.0f", 
						city.getAgricultureSystem().getWaterConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Total Supply", "Cal", 
					agricultureSystem.getLocalFoodSupply());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getLocalFoodSupply());
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
					agricultureSystem.getFoodImport()
					- agricultureSystem.getFoodExport());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getFoodImport()
						- system.getFoodExport());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
					agricultureSystem.getImportExpense()
					- agricultureSystem.getExportRevenue());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getImportExpense()
						- system.getExportRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Capital Ex", "SAR", 
					agricultureSystem.getCapitalExpense());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getCapitalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Operations Ex", "SAR", 
					agricultureSystem.getOperationsExpense());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getOperationsExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Decomm Ex", "SAR", 
					agricultureSystem.getDecommissionExpense());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getDecommissionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Cash Flow", "SAR", 
					country.getAgricultureSystem().getCashFlow());
			for(City city : country.getCities()) {
				System.out.printf(" %15.0f", 
						city.getAgricultureSystem().getCashFlow());
			}
			System.out.println();
		}

		System.out.println();
		
		if(country.getWaterSystem() instanceof WaterSystem.Local){
			WaterSystem.Local waterSystem = (WaterSystem.Local) country.getWaterSystem();
			
			System.out.printf("%-15s %-5s %15.0f |", "Water Reservoir", "m^3", 
					waterSystem.getWaterReservoirVolume());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", system.getWaterReservoirVolume());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Water Withdraw", "m^3", 
					waterSystem.getReservoirWaterWithdrawals());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", system.getReservoirWaterWithdrawals());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Water Product", "m^3", 
					waterSystem.getWaterProduction());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", system.getWaterProduction());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
					waterSystem.getSalesRevenue());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", system.getSalesRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Water Distrib", "m^3", 0d);
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getWaterInDistribution()
						- system.getWaterOutDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Elect Use", "kWh", 
					waterSystem.getElectricityConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %15.0f", 
						city.getWaterSystem().getElectricityConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Total Supply", "m^3",  
					waterSystem.getLocalWaterSupply());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", system.getLocalWaterSupply());
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
					waterSystem.getWaterImport());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getWaterImport());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "  Expense", "SAR", 
					waterSystem.getImportExpense());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getImportExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Water Wasted", "m^3", 
					waterSystem.getWaterWasted());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getWaterWasted());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Capital Ex", "SAR", 
					waterSystem.getCapitalExpense());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getCapitalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Operations Ex", "SAR", 
					waterSystem.getOperationsExpense());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getOperationsExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Decomm Ex", "SAR", 
					waterSystem.getDecommissionExpense());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getDecommissionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Cash Flow", "SAR", 
					waterSystem.getCashFlow());
			for(City city : country.getCities()) {
				System.out.printf(" %15.0f", 
						city.getWaterSystem().getCashFlow());
			}
			System.out.println();
		}

		System.out.println();
		
		if(country.getEnergySystem() instanceof EnergySystem.Local) {
			EnergySystem.Local energySystem = (EnergySystem.Local) country.getEnergySystem(); 
			
			System.out.printf("%-15s %-5s %15.0f |", "Oil Reservoir", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumReservoirVolume());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", system.getPetroleumSystem().getPetroleumReservoirVolume());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Oil Withdraw", "m^3", 
					energySystem.getPetroleumSystem().getPetroleumWithdrawals());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", system.getPetroleumSystem().getPetroleumWithdrawals());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Oil Product", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumProduction());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getPetroleumSystem().getPetroleumProduction());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
					energySystem.getPetroleumSystem().getSalesRevenue());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getPetroleumSystem().getSalesRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Oil Distrib", "bbl", 0d);
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getPetroleumSystem().getPetroleumInDistribution()
						- system.getPetroleumSystem().getPetroleumOutDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Oil Trade", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumExport());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getPetroleumSystem().getPetroleumExport());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
					energySystem.getPetroleumSystem().getExportRevenue() 
					- energySystem.getPetroleumSystem().getImportExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getPetroleumSystem().getExportRevenue() 
						- system.getPetroleumSystem().getImportExpense());
			}
			System.out.println();
						
			System.out.printf("%-15s %-5s %15.0f |", "Total Supply", "bbl", 
					energySystem.getPetroleumSystem().getLocalPetroleumSupply());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getPetroleumSystem().getLocalPetroleumSupply());
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
					energySystem.getPetroleumSystem().getCapitalExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getPetroleumSystem().getCapitalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Operations Ex", "SAR", 
					energySystem.getPetroleumSystem().getOperationsExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getPetroleumSystem().getOperationsExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Decomm Ex", "SAR", 
					energySystem.getPetroleumSystem().getDecommissionExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getPetroleumSystem().getDecommissionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Cash Flow", "SAR", 
					energySystem.getPetroleumSystem().getCashFlow());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getPetroleumSystem().getCashFlow());
			}
			System.out.println();
			
			System.out.println();
						
			System.out.printf("%-15s %-5s %15.0f |", "Elect Product", "kWh", 
					energySystem.getElectricitySystem().getElectricityProduction());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getElectricityProduction());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "  Revenue", "SAR", 
					energySystem.getElectricitySystem().getSalesRevenue());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getSalesRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Elect Distrib", "kWh", 0d);
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getElectricityInDistribution()
						- system.getElectricitySystem().getElectricityOutDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Water Use", "m^3", 
					energySystem.getElectricitySystem().getWaterConsumption());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getWaterConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Oil Use", "bbl", 
					energySystem.getElectricitySystem().getPetroleumConsumption());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getPetroleumConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Total Supply", "kWh", 
					energySystem.getElectricitySystem().getTotalElectricitySupply());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getTotalElectricitySupply());
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
					energySystem.getElectricitySystem().getElectricityFromBurningPetroleum());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getElectricityFromBurningPetroleum());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "  Oil Burned", "bbl", 
					energySystem.getElectricitySystem().getPetroleumBurned());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getPetroleumBurned());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Elect Wasted", "kWh", 
					energySystem.getElectricitySystem().getElectricityWasted());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getElectricityWasted());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Capital Ex", "SAR", 
					energySystem.getElectricitySystem().getCapitalExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getCapitalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Operations Ex", "SAR", 
					energySystem.getElectricitySystem().getOperationsExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getOperationsExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Decomm Ex", "SAR", 
					energySystem.getElectricitySystem().getDecommissionExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getDecommissionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %15.0f |", "Cash Flow", "SAR", 
					energySystem.getElectricitySystem().getCashFlow());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %15.0f", 
						system.getElectricitySystem().getCashFlow());
			}
			System.out.println();
		}
		
		System.out.println();
		
		System.out.println();
	}
}
