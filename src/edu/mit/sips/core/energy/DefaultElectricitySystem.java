package edu.mit.sips.core.energy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import edu.mit.sips.core.City;
import edu.mit.sips.core.DefaultInfrastructureSystem;


/**
 * The Class DefaultElectricitySystem.
 */
public abstract class DefaultElectricitySystem extends DefaultInfrastructureSystem.Local implements
		ElectricitySystem {
	
	/**
	 * Instantiates a new default electricity system.
	 */
	public DefaultElectricitySystem(String name) {
		super(name);
	}
	
	/**
	 * Instantiates a new default electricity system.
	 */
	protected DefaultElectricitySystem() {
		
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getConsumptionExpense()
	 */
	@Override
	public double getConsumptionExpense() {
		return getSociety().getGlobals().getPetroleumDomesticPrice()
				* getPetroleumConsumption();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getDistributionExpense()
	 */
	@Override
	public double getDistributionExpense() {
		return getSociety().getGlobals().getElectricityDomesticPrice()
				* getElectricityInDistribution();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getDistributionRevenue()
	 */
	@Override
	public double getDistributionRevenue() {
		return getSociety().getGlobals().getElectricityDomesticPrice()
				* getElectricityOutDistribution();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
	 */
	@Override
	public double getDomesticProduction() {
		return (getSociety().getGlobals().getElectricityDomesticPrice()
				+ getSociety().getGlobals().getEconomicIntensityOfElectricityProduction())
				* getElectricityProduction();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.EnergySystem#getEnergyFromBurningPetroleum()
	 */
	@Override
	public double getElectricityFromBurningPetroleum() {
		return Math.max(0, getSociety().getTotalElectricityDemand()  
				+ getElectricityOutDistribution()
				- getElectricityInDistribution()
				- getElectricityProduction());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.ElectricitySystem#getNetElectricityDistribution()
	 */
	@Override
	public double getElectricityInDistribution() {
		double distribution = 0;
		for(ElectricityElement e : getExternalElements()) {
			distribution += e.getElectricityOutput();
		}
		return distribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.ElectricitySystem#getElectricityOutDistribution()
	 */
	@Override
	public double getElectricityOutDistribution() {
		double distribution = 0;
		for(ElectricityElement e : getInternalElements()) {
			if(e.getDestination() == e.getOrigin()) {
				// if a self-loop, only add distribution losses
				distribution += e.getElectricityInput() - e.getElectricityOutput();
			} else {
				distribution += e.getElectricityInput();
			}
		}
		return distribution;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.EnergySystem#getEnergyProduction()
	 */
	@Override
	public double getElectricityProduction() {
		double energyProduction = 0;
		for(ElectricityElement e : getInternalElements()) {
			energyProduction += e.getElectricityProduction();
		}
		return energyProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.EnergySystem#getEnergyWasted()
	 */
	@Override
	public double getElectricityWasted() {
		// Energy is wasted if supply exceeds maximum demand.
		return Math.max(0, getElectricityProduction() 
				+ getElectricityInDistribution()
				- getElectricityOutDistribution()
				- getSociety().getTotalElectricityDemand());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getElements()
	 */
	@Override
	public List<ElectricityElement> getElements() {
		List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
		elements.addAll(getInternalElements());
		elements.addAll(getExternalElements());
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getExportRevenue()
	 */
	@Override
	public double getExportRevenue() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getExternalElements()
	 */
	@Override
	public List<ElectricityElement> getExternalElements() {
		List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
		for(ElectricityElement element : getNationalElectricitySystem().getElements()) {
			// add element if origin is outside this society but destination
			// is within this society
			if(!getSociety().getCities().contains(
					getSociety().getCountry().getCity(element.getOrigin()))
					&& getSociety().getCities().contains(
							getSociety().getCountry().getCity(
									element.getDestination()))) {
				elements.add(element);
			}
		}
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getImportExpense()
	 */
	@Override
	public double getImportExpense() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
	 */
	@Override
	public List<ElectricityElement> getInternalElements() {
		List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
		for(ElectricityElement element : getNationalElectricitySystem().getElements()) {
			// add element if origin is inside this society
			if(getSociety().getCities().contains(
					getSociety().getCountry().getCity(element.getOrigin()))) {
				elements.add(element);
			}
		}
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getNationalElectricitySystem()
	 */
	@Override
	public ElectricitySystem getNationalElectricitySystem() {
		return getEnergySystem().getNationalEnergySystem().getElectricitySystem();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.ElectricitySystem#getPetroleumBurned()
	 */
	@Override
	public double getPetroleumBurned() {
		// Petroleum is burned to meet shortfall in energy demand.
		return getElectricityFromBurningPetroleum()
				/ getSociety().getGlobals().getElectricalIntensityOfBurningPetroleum();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.EnergySystem#getPetroleumConsumed()
	 */
	@Override
	public double getPetroleumConsumption() {
		double petroleumConsumption = getPetroleumBurned();
		for(ElectricityElement e : getInternalElements()) {
			petroleumConsumption += e.getPetroleumConsumption();
		}
		return petroleumConsumption;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.ElectricitySystem#getRenewableEnergyProduction()
	 */
	@Override
	public double getRenewableElectricityProduction() {
		double production = 0;
		for(ElectricityElement element : getInternalElements()) {
			if(element.isRenewableElectricity()) {
				production += element.getElectricityProduction();
			}
		}
		return production;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
	 */
	@Override
	public double getSalesRevenue() {
		return getSociety().getGlobals().getElectricityDomesticPrice()
				* getSociety().getTotalElectricityDemand();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.EnergySystem#getNetEnergySupply()
	 */
	@Override
	public double getTotalElectricitySupply() {
		return getElectricityProduction() 
				+ getElectricityInDistribution()
				- getElectricityOutDistribution();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.EnergySystem#getWaterConsumption()
	 */
	@Override
	public double getWaterConsumption() {
		double waterConsumption = 0;
		for(ElectricityElement e : getInternalElements()) {
			waterConsumption += e.getWaterConsumption();
		}
		return waterConsumption;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#optimizeElectricityDistribution()
	 */
	@Override
	public void optimizeElectricityDistribution() {
		// Make a list of cities and infrastructure elements. The vector
		// of decision variables includes the throughput of each distribution
		// element and the petroleum burning amounts in each city.
		List<City> cities = getSociety().getCities();
		List<ElectricityElement> elements = getInternalElements();
		
		// Count number of variables.
		int numVariables = elements.size() + cities.size();
		
		// Create a list to hold the linear constraints.
		List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

		double[] costCoefficients = new double[numVariables];
		double[] initialValues = new double[numVariables];

		for(ElectricityElement element : elements) {
			// Add constraints for distribution throughput, i.e. the
			// throughput for each distribution element cannot exceed the maximum.
			double[] distributionConstraint = new double[numVariables];
			distributionConstraint[elements.indexOf(element)] = 1;
			constraints.add(new LinearConstraint(distributionConstraint, 
					Relationship.LEQ, element.getMaxElectricityInput()));
			
			// Set distribution cost.
			costCoefficients[elements.indexOf(element)] 
					= element.getVariableOperationsCostOfElectricityDistribution();
			initialValues[elements.indexOf(element)] 
					= element.getElectricityInput();
		}
		
		for(City city : cities) {
			ElectricitySystem electricitySystem = ((EnergySystem.Local)
					city.getEnergySystem()).getElectricitySystem();
			
			// Add constraints for city supply/demand, i.e. the in-flow less
			// out-flow (corrected for efficiency of distribution) must equal
			// the total demand less any local production.
			double[] flowCoefficients = new double[numVariables];
			for(ElectricityElement element : elements) {
				if(city.getName().equals(element.getOrigin())) {
					// Set coefficient for in-flow to the distribution element.
					// Order origin first to never distribute in self loop.
					flowCoefficients[elements.indexOf(element)] = -1;
				} else if(city.getName().equals(element.getDestination())) {
					// Set coefficient for out-flow from the distribution element.
					flowCoefficients[elements.indexOf(element)] 
							= element.getDistributionEfficiency();
				}
			}
			// Allow petroleum burning in this city.
			flowCoefficients[elements.size() + cities.indexOf(city)] 
					= city.getGlobals().getElectricalIntensityOfBurningPetroleum();
			// Constrain in-flow to meet net demand.
			constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
					city.getTotalElectricityDemand() 
					- electricitySystem.getElectricityProduction()));

			// Set petroleum burn cost in each city.
			costCoefficients[elements.size() + cities.indexOf(city)] = 
					city.getGlobals().getPetroleumDomesticPrice();
			initialValues[elements.size() + cities.indexOf(city)] = 
					Math.max(0,electricitySystem.getPetroleumBurned());
		}
		
		try {
			// Run optimization and get results.
			PointValuePair output = new SimplexSolver().optimize(
					GoalType.MINIMIZE,
					new MaxIter(1000),
					new NonNegativeConstraint(true), 
					new LinearConstraintSet(constraints), 
					new LinearObjectiveFunction(costCoefficients, 0d),
					new InitialGuess(initialValues));
			
			// For each flow variable, set the input in the
			// corresponding distribution element.
			for(int i = 0; i < elements.size(); i++) {
				// Add Math.min checks in case error exceeds bounds.
				elements.get(i).setElectricityInput(Math.min(
						output.getPoint()[i], 
						elements.get(i).getMaxElectricityInput()));
			}
		} catch(TooManyIterationsException ignore) { 
			// Don't overwrite existing values.
			ignore.printStackTrace();
		} catch(NoFeasibleSolutionException ignore) {
			// Don't overwrite existing values.
			ignore.printStackTrace();
		}
	}

	public void optimizeElectricityProductionAndDistribution(double deltaProductionCost) {
		List<City> cities = getSociety().getCities();
		List<ElectricityElement> elements = getInternalElements();
		
		// Count number of variables.
		int numVariables = 2*elements.size() + cities.size();

		List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		
		double[] costCoefficients = new double[numVariables];
		double[] initialValues = new double[numVariables];
		
		for(ElectricityElement element : elements) {
			// Constrain maximum production in each fixed element.
			double[] productionConstraint = new double[numVariables];
			productionConstraint[elements.indexOf(element)] = 1;
			constraints.add(new LinearConstraint(productionConstraint, 
					Relationship.LEQ, element.getMaxElectricityProduction()));
			
			// Constrain maximum throughput in each distribution element.
			double[] throughputConstraint = new double[numVariables];
			throughputConstraint[elements.size() + elements.indexOf(element)] = 1;
			constraints.add(new LinearConstraint(throughputConstraint, 
					Relationship.LEQ, element.getMaxElectricityInput()));

			// Minimize costs - most obvious cost is importing, though also 
			// minimize transportation even if free.
			costCoefficients[elements.indexOf(element)] 
					= element.getVariableOperationsCostOfElectricityProduction()
	                		 + element.getWaterIntensityOfElectricityProduction()
	                		 * getSociety().getGlobals().getWaterDomesticPrice()
	                		 + element.getPetroleumIntensityOfElectricityProduction()
	                		 * getSociety().getGlobals().getPetroleumDomesticPrice()
	                		 + deltaProductionCost;
			initialValues[elements.indexOf(element)] 
					= element.getElectricityProduction();

			// Set distribution cost.
			costCoefficients[elements.size() + elements.indexOf(element)] 
					= element.getVariableOperationsCostOfElectricityDistribution();
			initialValues[elements.size() + elements.indexOf(element)] 
					= element.getElectricityInput();
		}
		
		for(City city : cities) {
			ElectricitySystem electricitySystem = ((EnergySystem.Local)
					city.getEnergySystem()).getElectricitySystem();
			
			// Constrain supply = demand in each city.
			double[] flowCoefficients = new double[numVariables];
			for(ElectricityElement element : elements) {
				if(city.getName().equals(element.getOrigin())) {
					flowCoefficients[elements.indexOf(element)] = 1;
				}
				
				if(city.getName().equals(element.getOrigin())) {
					// Set coefficient for in-flow to the distribution element.
					// Order origin first to never distribute in self loop.
					flowCoefficients[elements.size() + elements.indexOf(element)] 
							= -1;
				} else if(city.getName().equals(element.getDestination())) {
					// Set coefficient for out-flow from the distribution element.
					flowCoefficients[elements.size() + elements.indexOf(element)] 
							= element.getDistributionEfficiency();
				}
			}
			// Allow burning of petroleum in this city.
			flowCoefficients[2*elements.size() + cities.indexOf(city)] 
					= city.getGlobals().getElectricalIntensityOfBurningPetroleum();
			
			// Constrain in-flow and production to meet demand.
			constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
					city.getTotalElectricityDemand()));

			// Set petroleum burn cost in each city.
			costCoefficients[2*elements.size() + cities.indexOf(city)] = 
					city.getGlobals().getPetroleumDomesticPrice();
			initialValues[2*elements.size() + cities.indexOf(city)] = 
					Math.max(0,electricitySystem.getPetroleumBurned());
		}
		
		try {
			// Run optimization and get results.
			PointValuePair output = new SimplexSolver().optimize(
					GoalType.MINIMIZE,
					new MaxIter(1000),
					new NonNegativeConstraint(true), 
					new LinearConstraintSet(constraints), 
					new LinearObjectiveFunction(costCoefficients, 0d),
					new InitialGuess(initialValues));
	
			for(int i = 0; i < elements.size(); i++) {
				// Add Math.min checks in case error exceeds bounds.
				elements.get(i).setElectricityProduction(Math.min(
						output.getPoint()[i],
						elements.get(i).getMaxElectricityProduction()));
				elements.get(i).setElectricityInput(Math.min(
						output.getPoint()[elements.size() + i],
						elements.get(i).getMaxElectricityInput()));
			}
		} catch(TooManyIterationsException ignore) { 
			// Don't overwrite existing values.
			ignore.printStackTrace();
		} catch(NoFeasibleSolutionException ignore) {
			// Don't overwrite existing values.
			ignore.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getLocalElectricityFraction()
	 */
	@Override
	public double getLocalElectricityFraction() {
		if(getSociety().getTotalElectricityDemand() > 0) {
			double electricityFromBurningLocalPetroleum = 0;
			if(getPetroleumBurned() > 0) {
				electricityFromBurningLocalPetroleum = getElectricityFromBurningPetroleum()
						* Math.min(getPetroleumBurned(), 
								getEnergySystem().getPetroleumSystem().getPetroleumProduction()
								- getEnergySystem().getPetroleumSystem().getPetroleumOutDistribution()
								- getEnergySystem().getPetroleumSystem().getPetroleumExport())
						/ getPetroleumBurned();
			}
			
			return Math.max(0, getElectricityProduction()
					- getElectricityOutDistribution()
					- getElectricityWasted()
					+ electricityFromBurningLocalPetroleum)
					/ getSociety().getTotalElectricityDemand();
		}
		return 0;
	}
	
	/**
	 * Gets the energy system.
	 *
	 * @return the energy system
	 */
	private EnergySystem.Local getEnergySystem() {
		return (EnergySystem.Local) getSociety().getEnergySystem();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getRenewableElectricityFraction()
	 */
	@Override
	public double getRenewableElectricityFraction() {
		if(getElectricityProduction() + getElectricityFromBurningPetroleum() > 0) {
			return getRenewableElectricityProduction() / (getElectricityProduction() 
					+ getElectricityFromBurningPetroleum());
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getProductionCost()
	 */
	@Override
	public double getProductionCost() {
		if(getElectricityProduction() > 0) {
			return getLifecycleExpense() / getElectricityProduction();
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getSupplyCost()
	 */
	@Override
	public double getSupplyCost() {
		if(getTotalElectricitySupply() > 0) {
			return getTotalExpense() / getTotalElectricitySupply();
		}
		return 0;
	}
}
