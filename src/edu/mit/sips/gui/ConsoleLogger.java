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
	 * Prints the state.
	 */
	private void printState(Country country, long time) {
		System.out.printf("%-20s  %15s |", "*" + time 
				+ " Annual Report*", country.getName());
		for(City city : country.getCities()) {
			System.out.printf(" %15s", city.getName());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %,15.0f |", "Nat'l Funds", "SAR", 
				country.getFunds());
		System.out.println();
		
		System.out.printf("%-15s %-5s %,15d |", "Population", "-", 
				country.getSocialSystem().getPopulation());
		for(City city : country.getCities()) {
			System.out.printf(" %,15d", 
					city.getSocialSystem().getPopulation());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %,15.0f |", "Domest. Product", "SAR", 
				country.getSocialSystem().getDomesticProduct());
		for(City city : country.getCities()) {
			System.out.printf(" %,15.0f", 
					city.getSocialSystem().getDomesticProduct());
		}
		System.out.println();
		System.out.printf("%-15s %-5s %,15.0f |", "  per capita", "SAR", 
				country.getSocialSystem().getDomesticProductPerCapita());
		for(City city : country.getCities()) {
			System.out.printf(" %,15.0f", 
					city.getSocialSystem().getDomesticProductPerCapita());
		}
		System.out.println();
		
		System.out.printf("%-15s %-5s %,15.0f |", "Net Cash Flow", "SAR", 
				country.getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %,15.0f", 
					city.getCashFlow());
		}
		System.out.println();
		System.out.printf("%-15s %-5s %,15.0f |", "  Agriculture", "SAR", 
				country.getAgricultureSystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %,15.0f", 
					city.getAgricultureSystem().getCashFlow());
		}
		System.out.println();
		System.out.printf("%-15s %-5s %,15.0f |", "  Water", "SAR", 
				country.getWaterSystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %,15.0f", 
					city.getWaterSystem().getCashFlow());
		}
		System.out.println();
		System.out.printf("%-15s %-5s %,15.0f |", "  Energy", "SAR", 
				country.getEnergySystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %,15.0f", 
					city.getEnergySystem().getCashFlow());
		}
		System.out.println();
		
		System.out.println();
		
		if(country.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = 
					(AgricultureSystem.Local) country.getAgricultureSystem();
			System.out.printf("%-15s %-5s %,6.0f / %,6.0f |", "Land Area Used", "km^2", 
					agricultureSystem.getLandAreaUsed(),
					agricultureSystem.getArableLandArea());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,6.0f / %,6.0f", 
						system.getLandAreaUsed(),
						system.getArableLandArea());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "Food Source", "GJ", 
					agricultureSystem.getFoodProduction()
					+ agricultureSystem.getFoodInDistribution()
					+ agricultureSystem.getFoodImport());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getFoodProduction()
						+ system.getFoodInDistribution()
						+ system.getFoodImport());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Production", "GJ", 
					agricultureSystem.getFoodProduction());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getFoodProduction());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  In-Distrib.", "GJ", 
					agricultureSystem.getFoodInDistribution());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getFoodInDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Import", "GJ", 
					agricultureSystem.getFoodImport());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getFoodImport());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Food Use", "GJ", 
					country.getSocialSystem().getFoodConsumption()
					+ agricultureSystem.getFoodOutDistribution()
					+ agricultureSystem.getFoodExport());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getSociety().getSocialSystem().getFoodConsumption()
						+ system.getFoodOutDistribution()
						+ system.getFoodExport());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Out-Distrib.", "GJ", 
					agricultureSystem.getFoodOutDistribution());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getFoodOutDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Export", "GJ", 
					agricultureSystem.getFoodExport());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getFoodExport());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Society", "GJ", 
					country.getSocialSystem().getFoodConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getSocialSystem().getFoodConsumption());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Water Use", "m^3", 
					agricultureSystem.getWaterConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getAgricultureSystem().getWaterConsumption());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Revenue", "SAR", 
					agricultureSystem.getTotalRevenue());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getTotalRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Sales", "SAR", 
					agricultureSystem.getSalesRevenue());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getSalesRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					agricultureSystem.getDistributionRevenue());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDistributionRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Export", "SAR", 
					agricultureSystem.getExportRevenue());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getExportRevenue());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Expenses", "SAR", 
					agricultureSystem.getTotalExpense());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getTotalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Capital", "SAR", 
					agricultureSystem.getCapitalExpense());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getCapitalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Operations", "SAR", 
					agricultureSystem.getOperationsExpense());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getOperationsExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Resources", "SAR", 
					agricultureSystem.getConsumptionExpense());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getConsumptionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Decommission", "SAR", 
					agricultureSystem.getDecommissionExpense());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDecommissionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					agricultureSystem.getDistributionExpense());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDistributionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Import", "SAR", 
					agricultureSystem.getImportExpense());
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getImportExpense());
			}
			System.out.println();
		}

		System.out.println();
		
		if(country.getWaterSystem() instanceof WaterSystem.Local){
			WaterSystem.Local waterSystem = (WaterSystem.Local) country.getWaterSystem();
			
			System.out.printf("%-15s %-5s %,15.0f |", "Aquifer Volume", "m^3", 
					waterSystem.getWaterReservoirVolume());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", system.getWaterReservoirVolume());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Withdrawals", "m^3", 
					waterSystem.getReservoirWaterWithdrawals()
					+ waterSystem.getWaterFromArtesianWell());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getReservoirWaterWithdrawals()
						+ waterSystem.getWaterFromArtesianWell());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "Water Source", "m^3", 
					waterSystem.getWaterProduction()
					+ waterSystem.getWaterInDistribution()
					+ waterSystem.getWaterFromArtesianWell()
					+ waterSystem.getWaterImport());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getWaterProduction()
						+ system.getWaterInDistribution()
						+ system.getWaterFromArtesianWell()
						+ system.getWaterImport());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Production", "m^3", 
					waterSystem.getWaterProduction());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getWaterProduction());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  In-Distrib.", "m^3", 
					waterSystem.getWaterInDistribution());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getWaterInDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Direct-Other", "m^3", 
					waterSystem.getWaterFromArtesianWell());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getWaterFromArtesianWell());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Import", "m^3", 
					waterSystem.getWaterImport());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getWaterImport());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Water Use", "m^3", 
					waterSystem.getWaterOutDistribution()
					+ country.getSocialSystem().getWaterConsumption()
					+ country.getEnergySystem().getWaterConsumption()
					+ waterSystem.getWaterWasted());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getWaterOutDistribution()
						+ system.getSociety().getSocialSystem().getWaterConsumption()
						+ system.getSociety().getEnergySystem().getWaterConsumption()
						+ system.getWaterWasted());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Out-Distrib.", "m^3", 
					waterSystem.getWaterOutDistribution());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getWaterOutDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Society", "m^3", 
					country.getSocialSystem().getWaterConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getSocialSystem().getWaterConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Energy", "m^3", 
					country.getEnergySystem().getWaterConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getEnergySystem().getWaterConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Wasted", "m^3", 
					waterSystem.getWaterWasted());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getWaterWasted());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Electricity Use", "MWh", 
					waterSystem.getElectricityConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getWaterSystem().getElectricityConsumption());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Revenue", "SAR", 
					waterSystem.getTotalRevenue());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getTotalRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Sales", "SAR", 
					waterSystem.getSalesRevenue());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getSalesRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					waterSystem.getDistributionRevenue());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDistributionRevenue());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Expenses", "SAR", 
					waterSystem.getTotalExpense());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getTotalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Capital", "SAR", 
					waterSystem.getCapitalExpense());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getCapitalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Operations", "SAR", 
					waterSystem.getOperationsExpense());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getOperationsExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Resources", "SAR", 
					waterSystem.getConsumptionExpense());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getConsumptionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Decommission", "SAR", 
					waterSystem.getDecommissionExpense());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDecommissionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					waterSystem.getDistributionExpense());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDistributionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Import", "SAR", 
					waterSystem.getImportExpense());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getImportExpense());
			}
			System.out.println();
		}

		System.out.println();
		
		if(country.getEnergySystem() instanceof EnergySystem.Local) {
			EnergySystem.Local energySystem = (EnergySystem.Local) country.getEnergySystem(); 
			

			System.out.printf("%-15s %-5s %,15.0f |", "Reservoir Vol.", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumReservoirVolume());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", system.getPetroleumSystem().getPetroleumReservoirVolume());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Withdrawals", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumWithdrawals());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getPetroleumWithdrawals());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "Petrol. Source", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumProduction()
					+ energySystem.getPetroleumSystem().getPetroleumInDistribution()
					+ energySystem.getPetroleumSystem().getPetroleumImport());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getPetroleumProduction()
						+ system.getPetroleumSystem().getPetroleumInDistribution()
						+ system.getPetroleumSystem().getPetroleumImport());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Production", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumProduction());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getPetroleumProduction());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  In-Distrib.", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumInDistribution());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getPetroleumInDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Import", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumImport());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getPetroleumImport());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Petroleum Use", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumOutDistribution()
					+ country.getEnergySystem().getPetroleumConsumption()
					+ energySystem.getPetroleumSystem().getPetroleumExport());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getPetroleumOutDistribution()
						+ system.getSociety().getEnergySystem().getPetroleumConsumption()
						+ system.getPetroleumSystem().getPetroleumExport());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Out-Distrib.", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumOutDistribution());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getPetroleumOutDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Energy", "bbl", 
					country.getEnergySystem().getPetroleumConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getEnergySystem().getPetroleumConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Export", "bbl", 
					energySystem.getPetroleumSystem().getPetroleumExport());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getPetroleumExport());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Electricity Use", "MWh", 
					energySystem.getPetroleumSystem().getElectricityConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getEnergySystem().getElectricityConsumption());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Revenue", "SAR", 
					energySystem.getPetroleumSystem().getTotalRevenue());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getTotalRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Sales", "SAR", 
					energySystem.getPetroleumSystem().getSalesRevenue());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getSalesRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					energySystem.getPetroleumSystem().getDistributionRevenue());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getDistributionRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Export", "SAR", 
					energySystem.getPetroleumSystem().getExportRevenue());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getExportRevenue());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Expenses", "SAR", 
					energySystem.getPetroleumSystem().getTotalExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getTotalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Capital", "SAR", 
					energySystem.getPetroleumSystem().getCapitalExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getCapitalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Operations", "SAR", 
					energySystem.getPetroleumSystem().getOperationsExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getOperationsExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Resources", "SAR", 
					energySystem.getPetroleumSystem().getConsumptionExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getConsumptionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Decommission", "SAR", 
					energySystem.getPetroleumSystem().getDecommissionExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getDecommissionExpense());
			}
			
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					energySystem.getPetroleumSystem().getDistributionExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getDistributionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Import", "SAR", 
					energySystem.getPetroleumSystem().getImportExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumSystem().getImportExpense());
			}
			System.out.println();
			
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "Elect. Source", "MWh", 
					energySystem.getElectricitySystem().getElectricityProduction()
					+ energySystem.getElectricitySystem().getElectricityInDistribution()
					+ energySystem.getElectricitySystem().getElectricityFromBurningPetroleum());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getElectricityProduction()
						+ system.getElectricitySystem().getElectricityInDistribution()
						+ system.getElectricitySystem().getElectricityFromBurningPetroleum());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Production", "MWh", 
					energySystem.getElectricitySystem().getElectricityProduction());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getElectricityProduction());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  In-Distrib.", "MWh", 
					energySystem.getElectricitySystem().getElectricityInDistribution());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getElectricityInDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Direct-Burn", "MWh", 
					energySystem.getElectricitySystem().getElectricityFromBurningPetroleum());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getElectricityFromBurningPetroleum());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Electricity Use", "MWh", 
					energySystem.getElectricitySystem().getElectricityOutDistribution()
					+ country.getSocialSystem().getElectricityConsumption()
					+ country.getEnergySystem().getElectricityConsumption()
					+ country.getWaterSystem().getElectricityConsumption()
					+ energySystem.getElectricitySystem().getElectricityWasted());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getElectricityOutDistribution()
						+ system.getSociety().getSocialSystem().getElectricityConsumption()
						+ system.getSociety().getEnergySystem().getElectricityConsumption()
						+ system.getSociety().getWaterSystem().getElectricityConsumption()
						+ system.getElectricitySystem().getElectricityWasted());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Out-Distrib.", "MWh", 
					energySystem.getElectricitySystem().getElectricityOutDistribution());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getElectricityOutDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Society", "MWh", 
					country.getSocialSystem().getElectricityConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getSocialSystem().getElectricityConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Energy", "MWh", 
					country.getEnergySystem().getElectricityConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getEnergySystem().getElectricityConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Water", "MWh", 
					country.getWaterSystem().getElectricityConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getWaterSystem().getElectricityConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Wasted", "MWh", 
					energySystem.getElectricitySystem().getElectricityWasted());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getElectricityWasted());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Petroleum Use", "bbl", 
					energySystem.getElectricitySystem().getPetroleumConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getEnergySystem().getPetroleumConsumption());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Water Use", "m^3", 
					energySystem.getElectricitySystem().getWaterConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getEnergySystem().getWaterConsumption());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Revenue", "SAR", 
					energySystem.getElectricitySystem().getTotalRevenue());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getTotalRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Sales", "SAR", 
					energySystem.getElectricitySystem().getSalesRevenue());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getSalesRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					energySystem.getElectricitySystem().getDistributionRevenue());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getDistributionRevenue());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Expenses", "SAR", 
					energySystem.getElectricitySystem().getTotalExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getTotalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Capital", "SAR", 
					energySystem.getElectricitySystem().getCapitalExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getCapitalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Operations", "SAR", 
					energySystem.getElectricitySystem().getOperationsExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getOperationsExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Resources", "SAR", 
					energySystem.getElectricitySystem().getConsumptionExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getConsumptionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Decommission", "SAR", 
					energySystem.getElectricitySystem().getDecommissionExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getDecommissionExpense());
			}
			
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					energySystem.getElectricitySystem().getDistributionExpense());
			for(EnergySystem.Local system : getLocalEnergySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricitySystem().getDistributionExpense());
			}
			System.out.println();
		}
		
		System.out.println();
		
		System.out.println();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.UpdateListener#simulationCompleted(edu.mit.sips.gui.UpdateEvent)
	 */
	@Override
	public void simulationCompleted(UpdateEvent event) {
		// nothing to do
	}
	
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
}
