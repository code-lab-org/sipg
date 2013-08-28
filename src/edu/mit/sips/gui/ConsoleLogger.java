package edu.mit.sips.gui;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.FoodUnits.DenominatorUnits;
import edu.mit.sips.sim.util.FoodUnits.NumeratorUnits;

/**
 * The Class ConsoleLogger.
 */
public class ConsoleLogger implements UpdateListener, FoodUnitsOutput, CurrencyUnitsOutput {

	private final FoodUnits.NumeratorUnits foodUnitsNumerator = 
			FoodUnits.NumeratorUnits.GJ;
	private final FoodUnits.DenominatorUnits foodUnitsDenominator = 
			FoodUnits.DenominatorUnits.year;
	
	private final CurrencyUnits.NumeratorUnits currencyUnitsNumerator = 
			CurrencyUnits.NumeratorUnits.Bsim;
	private final CurrencyUnits.DenominatorUnits currencyUnitsDenominator = 
			CurrencyUnits.DenominatorUnits.year;
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsDenominator()
	 */
	@Override
	public CurrencyUnits.DenominatorUnits getCurrencyUnitsDenominator() {
		return currencyUnitsDenominator;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsNumerator()
	 */
	@Override
	public CurrencyUnits.NumeratorUnits getCurrencyUnitsNumerator() {
		return currencyUnitsNumerator;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsDenominator()
	 */
	@Override
	public DenominatorUnits getFoodUnitsDenominator() {
		return foodUnitsDenominator;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsNumerator()
	 */
	@Override
	public NumeratorUnits getFoodUnitsNumerator() {
		return foodUnitsNumerator;
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
	 * Gets the local electricity systems.
	 *
	 * @param cities the cities
	 * @return the local electricity systems
	 */
	private List<ElectricitySystem.Local> getLocalElectricitySystems(List<City> cities) {
		List<ElectricitySystem.Local> systems = new ArrayList<ElectricitySystem.Local>();
		for(City city : cities) {
			if(city.getElectricitySystem() instanceof ElectricitySystem.Local){
				systems.add((ElectricitySystem.Local)city.getElectricitySystem());
			}
		}
		return systems;
	}
	
	/**
	 * Gets the local petroleum systems.
	 *
	 * @param cities the cities
	 * @return the local petroleum systems
	 */
	private List<PetroleumSystem.Local> getLocalPetroleumSystems(List<City> cities) {
		List<PetroleumSystem.Local> systems = new ArrayList<PetroleumSystem.Local>();
		for(City city : cities) {
			if(city.getPetroleumSystem() instanceof PetroleumSystem.Local){
				systems.add((PetroleumSystem.Local)city.getPetroleumSystem());
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
	 *
	 * @param country the country
	 * @param time the time
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
		if(country.getSocialSystem().getPopulation() > 0) {
			System.out.printf("%-15s %-5s %,15.0f |", "  per capita", "SAR", 
					country.getSocialSystem().getDomesticProduct() 
					/ country.getSocialSystem().getPopulation());
			for(City city : country.getCities()) {
				if(city.getSocialSystem().getPopulation() > 0) {
					System.out.printf(" %,15.0f", 
							city.getSocialSystem().getDomesticProduct() 
							/ country.getSocialSystem().getPopulation());
				}
			}
			System.out.println();
		}
		
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
		System.out.printf("%-15s %-5s %,15.0f |", "  Electricity", "SAR", 
				country.getElectricitySystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %,15.0f", 
					city.getElectricitySystem().getCashFlow());
		}
		System.out.println();
		System.out.printf("%-15s %-5s %,15.0f |", "  Petroleum", "SAR", 
				country.getPetroleumSystem().getCashFlow());
		for(City city : country.getCities()) {
			System.out.printf(" %,15.0f", 
					city.getPetroleumSystem().getCashFlow());
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
			
			System.out.printf("%-15s %-5s %,15.0f |", "Food Source", 
					foodUnitsNumerator.getAbbreviation() + "/" + foodUnitsDenominator.getAbbreviation(), 
					FoodUnits.convert(agricultureSystem.getFoodProduction() + 
							agricultureSystem.getFoodInDistribution() + 
							agricultureSystem.getFoodImport(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						FoodUnits.convert(system.getFoodProduction()
						+ system.getFoodInDistribution()
						+ system.getFoodImport(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Production",
					foodUnitsNumerator.getAbbreviation() + "/" + foodUnitsDenominator.getAbbreviation(), 
					FoodUnits.convert(agricultureSystem.getFoodProduction(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						FoodUnits.convert(system.getFoodProduction(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  In-Distrib.",
					foodUnitsNumerator.getAbbreviation() + "/" + foodUnitsDenominator.getAbbreviation(), 
					FoodUnits.convert(agricultureSystem.getFoodInDistribution(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						FoodUnits.convert(system.getFoodInDistribution(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Import",
					foodUnitsNumerator.getAbbreviation() + "/" + foodUnitsDenominator.getAbbreviation(), 
					FoodUnits.convert(agricultureSystem.getFoodImport(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						FoodUnits.convert(system.getFoodImport(), system, this));
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Food Use",
					foodUnitsNumerator.getAbbreviation() + "/" + foodUnitsDenominator.getAbbreviation(), 
					FoodUnits.convert(country.getSocialSystem().getFoodConsumption()
					+ agricultureSystem.getFoodOutDistribution()
					+ agricultureSystem.getFoodExport(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						FoodUnits.convert(system.getSociety().getSocialSystem().getFoodConsumption()
						+ system.getFoodOutDistribution()
						+ system.getFoodExport(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Out-Distrib.",
					foodUnitsNumerator.getAbbreviation() + "/" + foodUnitsDenominator.getAbbreviation(), 
					FoodUnits.convert(agricultureSystem.getFoodOutDistribution(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						FoodUnits.convert(system.getFoodOutDistribution(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Export",
					foodUnitsNumerator.getAbbreviation() + "/" + foodUnitsDenominator.getAbbreviation(), 
					FoodUnits.convert(agricultureSystem.getFoodExport(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						FoodUnits.convert(system.getFoodExport(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Society",
					foodUnitsNumerator.getAbbreviation() + "/" + foodUnitsDenominator.getAbbreviation(), 
					FoodUnits.convert(country.getSocialSystem().getFoodConsumption(), country.getSocialSystem(), this));
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						FoodUnits.convert(city.getSocialSystem().getFoodConsumption(), city.getSocialSystem(), this));
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Water Use", "m^3", 
					agricultureSystem.getWaterConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getAgricultureSystem().getWaterConsumption());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Revenue", currencyUnitsNumerator.getAbbreviation(), 
					CurrencyUnits.convert(agricultureSystem.getTotalRevenue(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						CurrencyUnits.convert(system.getTotalRevenue(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Sales", currencyUnitsNumerator.getAbbreviation(), 
					CurrencyUnits.convert(agricultureSystem.getSalesRevenue(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						CurrencyUnits.convert(system.getSalesRevenue(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", currencyUnitsNumerator.getAbbreviation(), 
					CurrencyUnits.convert(agricultureSystem.getDistributionRevenue(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						CurrencyUnits.convert(system.getDistributionRevenue(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Export", currencyUnitsNumerator.getAbbreviation(), 
					CurrencyUnits.convert(agricultureSystem.getExportRevenue(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						CurrencyUnits.convert(system.getExportRevenue(), system, this));
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Expenses", currencyUnitsNumerator.getAbbreviation(), 
					CurrencyUnits.convert(agricultureSystem.getTotalExpense(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						CurrencyUnits.convert(system.getTotalExpense(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Capital", currencyUnitsNumerator.getAbbreviation(), 
					CurrencyUnits.convert(agricultureSystem.getCapitalExpense(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						CurrencyUnits.convert(system.getCapitalExpense(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Operations", currencyUnitsNumerator.getAbbreviation(), 
					CurrencyUnits.convert(agricultureSystem.getOperationsExpense(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						CurrencyUnits.convert(system.getOperationsExpense(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Resources", currencyUnitsNumerator.getAbbreviation(), 
					CurrencyUnits.convert(agricultureSystem.getConsumptionExpense(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						CurrencyUnits.convert(system.getConsumptionExpense(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Decommission", currencyUnitsNumerator.getAbbreviation(), 
					CurrencyUnits.convert(agricultureSystem.getDecommissionExpense(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						CurrencyUnits.convert(system.getDecommissionExpense(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", currencyUnitsNumerator.getAbbreviation(), 
					CurrencyUnits.convert(agricultureSystem.getDistributionExpense(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						CurrencyUnits.convert(system.getDistributionExpense(), system, this));
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Import", currencyUnitsNumerator.getAbbreviation(), 
					CurrencyUnits.convert(agricultureSystem.getImportExpense(), agricultureSystem, this));
			for(AgricultureSystem.Local system : getLocalAgricultureSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						CurrencyUnits.convert(system.getImportExpense(), system, this));
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
					+ country.getElectricitySystem().getWaterConsumption()
					+ waterSystem.getWaterWasted());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getWaterOutDistribution()
						+ system.getSociety().getSocialSystem().getWaterConsumption()
						+ system.getSociety().getElectricitySystem().getWaterConsumption()
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
					country.getElectricitySystem().getWaterConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getElectricitySystem().getWaterConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Wasted", "m^3", 
					waterSystem.getWaterWasted());
			for(WaterSystem.Local system : getLocalWaterSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getWaterWasted());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Electricity Use", "toe", 
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
		
		if(country.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			PetroleumSystem.Local energySystem = (PetroleumSystem.Local) country.getPetroleumSystem(); 
			
			System.out.printf("%-15s %-5s %,15.0f |", "Reservoir Vol.", "toe", 
					energySystem.getPetroleumReservoirVolume());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", system.getPetroleumReservoirVolume());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Withdrawals", "toe", 
					energySystem.getPetroleumWithdrawals());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumWithdrawals());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "Petrol. Source", "toe", 
					energySystem.getPetroleumProduction()
					+ energySystem.getPetroleumInDistribution()
					+ energySystem.getPetroleumImport());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumProduction()
						+ system.getPetroleumInDistribution()
						+ system.getPetroleumImport());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Production", "toe", 
					energySystem.getPetroleumProduction());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumProduction());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  In-Distrib.", "toe", 
					energySystem.getPetroleumInDistribution());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumInDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Import", "toe", 
					energySystem.getPetroleumImport());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumImport());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Petroleum Use", "toe", 
					energySystem.getPetroleumOutDistribution()
					+ country.getElectricitySystem().getPetroleumConsumption()
					+ energySystem.getPetroleumExport());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumOutDistribution()
						+ system.getSociety().getElectricitySystem().getPetroleumConsumption()
						+ system.getPetroleumExport());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Out-Distrib.", "toe", 
					energySystem.getPetroleumOutDistribution());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumOutDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Energy", "toe", 
					country.getElectricitySystem().getPetroleumConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getElectricitySystem().getPetroleumConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Export", "toe", 
					energySystem.getPetroleumExport());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getPetroleumExport());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Electricity Use", "toe", 
					energySystem.getElectricityConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getPetroleumSystem().getElectricityConsumption());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Revenue", "SAR", 
					energySystem.getTotalRevenue());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getTotalRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Sales", "SAR", 
					energySystem.getSalesRevenue());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getSalesRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					energySystem.getDistributionRevenue());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDistributionRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Export", "SAR", 
					energySystem.getExportRevenue());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getExportRevenue());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Expenses", "SAR", 
					energySystem.getTotalExpense());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getTotalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Capital", "SAR", 
					energySystem.getCapitalExpense());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getCapitalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Operations", "SAR", 
					energySystem.getOperationsExpense());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getOperationsExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Resources", "SAR", 
					energySystem.getConsumptionExpense());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getConsumptionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Decommission", "SAR", 
					energySystem.getDecommissionExpense());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDecommissionExpense());
			}
			
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					energySystem.getDistributionExpense());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDistributionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Import", "SAR", 
					energySystem.getImportExpense());
			for(PetroleumSystem.Local system : getLocalPetroleumSystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getImportExpense());
			}
			System.out.println();
		}
		
		System.out.println();

		if(country.getElectricitySystem() instanceof ElectricitySystem.Local) {
			ElectricitySystem.Local energySystem = (ElectricitySystem.Local) country.getElectricitySystem(); 
			
			System.out.printf("%-15s %-5s %,15.0f |", "Elect. Source", "toe", 
					energySystem.getElectricityProduction()
					+ energySystem.getElectricityInDistribution()
					+ energySystem.getElectricityFromBurningPetroleum());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricityProduction()
						+ system.getElectricityInDistribution()
						+ system.getElectricityFromBurningPetroleum());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Production", "toe", 
					energySystem.getElectricityProduction());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricityProduction());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  In-Distrib.", "toe", 
					energySystem.getElectricityInDistribution());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricityInDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Direct-Burn", "toe", 
					energySystem.getElectricityFromBurningPetroleum());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricityFromBurningPetroleum());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Electricity Use", "toe", 
					energySystem.getElectricityOutDistribution()
					+ country.getSocialSystem().getElectricityConsumption()
					+ country.getPetroleumSystem().getElectricityConsumption()
					+ country.getWaterSystem().getElectricityConsumption()
					+ energySystem.getElectricityWasted());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricityOutDistribution()
						+ system.getSociety().getSocialSystem().getElectricityConsumption()
						+ system.getSociety().getPetroleumSystem().getElectricityConsumption()
						+ system.getSociety().getWaterSystem().getElectricityConsumption()
						+ system.getElectricityWasted());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Out-Distrib.", "toe", 
					energySystem.getElectricityOutDistribution());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricityOutDistribution());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Society", "toe", 
					country.getSocialSystem().getElectricityConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getSocialSystem().getElectricityConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Energy", "toe", 
					country.getPetroleumSystem().getElectricityConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getPetroleumSystem().getElectricityConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Water", "toe", 
					country.getWaterSystem().getElectricityConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getWaterSystem().getElectricityConsumption());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Wasted", "toe", 
					energySystem.getElectricityWasted());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getElectricityWasted());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Petroleum Use", "toe", 
					energySystem.getPetroleumConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getElectricitySystem().getPetroleumConsumption());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Water Use", "m^3", 
					energySystem.getWaterConsumption());
			for(City city : country.getCities()) {
				System.out.printf(" %,15.0f", 
						city.getElectricitySystem().getWaterConsumption());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Revenue", "SAR", 
					energySystem.getTotalRevenue());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getTotalRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Sales", "SAR", 
					energySystem.getSalesRevenue());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getSalesRevenue());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					energySystem.getDistributionRevenue());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDistributionRevenue());
			}
			System.out.println();

			System.out.printf("%-15s %-5s %,15.0f |", "Total Expenses", "SAR", 
					energySystem.getTotalExpense());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getTotalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Capital", "SAR", 
					energySystem.getCapitalExpense());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getCapitalExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Operations", "SAR", 
					energySystem.getOperationsExpense());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getOperationsExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Resources", "SAR", 
					energySystem.getConsumptionExpense());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getConsumptionExpense());
			}
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Decommission", "SAR", 
					energySystem.getDecommissionExpense());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDecommissionExpense());
			}
			
			System.out.println();
			
			System.out.printf("%-15s %-5s %,15.0f |", "  Distribution", "SAR", 
					energySystem.getDistributionExpense());
			for(ElectricitySystem.Local system : getLocalElectricitySystems(country.getCities())) {
				System.out.printf(" %,15.0f", 
						system.getDistributionExpense());
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
