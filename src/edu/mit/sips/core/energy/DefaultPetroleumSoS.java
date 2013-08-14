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
import edu.mit.sips.core.DefaultInfrastructureSoS;
import edu.mit.sips.core.OptimizationOptions;
import edu.mit.sips.core.Society;

/**
 * The Class DefaultPetroleumSoS.
 */
public class DefaultPetroleumSoS extends DefaultInfrastructureSoS.Local implements PetroleumSoS {
	
	/**
	 * Instantiates a new default petroleum so s.
	 */
	public DefaultPetroleumSoS() {
		super("Petroleum");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getElectricityConsumption();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getElements()
	 */
	@Override
	public List<? extends PetroleumElement> getElements() {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
		elements.addAll(getInternalElements());
		elements.addAll(getExternalElements());
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getExternalElements()
	 */
	@Override
	public List<? extends PetroleumElement> getExternalElements() {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
		if(!getSociety().equals(getSociety().getCountry())) {
			for(PetroleumSystem system : getNestedSystems()) {
				elements.addAll(system.getExternalElements());
			}
			elements.removeAll(getInternalElements());
		}
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getInternalElements()
	 */
	@Override
	public List<? extends PetroleumElement> getInternalElements() {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
		for(PetroleumSystem system : getNestedSystems()) {
			elements.addAll(system.getInternalElements());
		}
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getLocalPetroleumSupply()
	 */
	@Override
	public double getLocalPetroleumSupply() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getLocalPetroleumSupply();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getMaxPetroleumReservoirVolume()
	 */
	@Override
	public double getMaxPetroleumReservoirVolume() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getMaxPetroleumReservoirVolume();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultInfrastructureSoS.Local#getNestedSystems()
	 */
	@Override
	public List<PetroleumSystem> getNestedSystems() {
		List<PetroleumSystem> systems = new ArrayList<PetroleumSystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			if(society.getEnergySystem() instanceof EnergySystem.Local){ 
				systems.add(((EnergySystem.Local) society.getEnergySystem()).getPetroleumSystem());
			}
		}
		return Collections.unmodifiableList(systems);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumExport()
	 */
	@Override
	public double getPetroleumExport() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getPetroleumExport();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumImport()
	 */
	@Override
	public double getPetroleumImport() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getPetroleumImport();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumInDistribution()
	 */
	@Override
	public double getPetroleumInDistribution() {
		double value = 0;
		for(PetroleumElement e : getExternalElements()) {
			value += e.getPetroleumOutput();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumOutDistribution()
	 */
	@Override
	public double getPetroleumOutDistribution() {
		double value = 0;
		for(PetroleumElement e : getInternalElements()) {
			if(!getSociety().getCities().contains(
					getSociety().getCountry().getCity(e.getDestination()))) {
				value += e.getPetroleumInput();
			}
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumOutDistributionLosses()
	 */
	@Override
	public double getPetroleumOutDistributionLosses() {
		double value = 0;
		for(PetroleumElement e : getInternalElements()) {
			value += e.getPetroleumInput() - e.getPetroleumOutput();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumProduction()
	 */
	@Override
	public double getPetroleumProduction() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getPetroleumProduction();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumReservoirVolume()
	 */
	@Override
	public double getPetroleumReservoirVolume() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getPetroleumReservoirVolume();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumWithdrawals()
	 */
	@Override
	public double getPetroleumWithdrawals() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getPetroleumWithdrawals();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getTotalPetroleumSupply()
	 */
	@Override
	public double getTotalPetroleumSupply() {
		double value = 0;
		for(PetroleumSystem system : getNestedSystems()) {
			value += system.getTotalPetroleumSupply();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getUnitProductionCost()
	 */
	@Override
	public double getUnitProductionCost() {
		if(getPetroleumProduction() > 0) {
			return (getLifecycleExpense() + getConsumptionExpense()) 
					/ getPetroleumProduction();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getUnitSupplyProfit()
	 */
	@Override
	public double getUnitSupplyProfit() {
		if(getTotalPetroleumSupply() > 0) {
			return getCashFlow() / getTotalPetroleumSupply();
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) { }
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSoS#optimizePetroleumDistribution()
	 */
	@Override
	public void optimizePetroleumDistribution() {
		// Make a list of cities and infrastructure elements. The vector
		// of decision variables includes the throughput of each distribution
		// element and the import and export amounts in each city.
		List<City> cities = getSociety().getCities();
		List<? extends PetroleumElement> elements = getInternalElements();
		
		// Count number of variables.
		int numVariables = elements.size() + 2*cities.size();
		
		// Create a list to hold the linear constraints.
		List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

		double[] costCoefficients = new double[numVariables];
		double[] initialValues = new double[numVariables];

		for(PetroleumElement element : elements) {
			// Add constraints for distribution throughput, i.e. the
			// throughput for each distribution element cannot exceed the maximum.
			double[] distributionConstraint = new double[numVariables];
			distributionConstraint[elements.indexOf(element)] = 1;
			constraints.add(new LinearConstraint(distributionConstraint, 
					Relationship.LEQ, element.getMaxPetroleumInput()));
			
			// Set distribution cost.
			costCoefficients[elements.indexOf(element)] 
					= element.getVariableOperationsCostOfPetroleumDistribution()
					+ element.getElectricalIntensityOfPetroleumDistribution() * 
					getSociety().getGlobals().getElectricityDomesticPrice();
			initialValues[elements.indexOf(element)] 
					= element.getPetroleumInput();
		}
		
		for(City city : cities) {
			if(!(city.getEnergySystem() instanceof EnergySystem.Local)) {
				continue;
			}
			
			EnergySystem.Local energySystem = (EnergySystem.Local) city.getEnergySystem();
			
			// Add constraints for city supply/demand, i.e. the in-flow less
			// out-flow (corrected for efficiency of distribution) must equal
			// the total demand less any local production.
			double[] flowCoefficients = new double[numVariables];
			for(PetroleumElement element : elements) {
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
			// Allow import in this city.
			flowCoefficients[elements.size() + cities.indexOf(city)] = 1;
			// Allow export from this city.
			flowCoefficients[elements.size() + cities.size() + cities.indexOf(city)] = -1;
			// Constrain in-flow to meet net demand.
			constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
					energySystem.getSociety().getTotalPetroleumDemand() 
					- energySystem.getPetroleumSystem().getPetroleumProduction()));

			// Set import cost in each city.
			costCoefficients[elements.size() + cities.indexOf(city)] 
					= city.getGlobals().getPetroleumImportPrice();
			initialValues[elements.size() + cities.indexOf(city)] 
					= energySystem.getPetroleumSystem().getPetroleumImport();
			// Set export price in each city.
			costCoefficients[elements.size() + cities.size() + cities.indexOf(city)] 
					= -city.getGlobals().getPetroleumExportPrice();
			initialValues[elements.size() + cities.size() + cities.indexOf(city)] 
					= energySystem.getPetroleumSystem().getPetroleumExport();
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
			
			// For each flow variable, set the food input in the
			// corresponding distribution element.
			for(int i = 0; i < elements.size(); i++) {
				// Add Math.min checks in case error exceeds bounds.
				elements.get(i).setPetroleumInput(Math.min(output.getPoint()[i],
						elements.get(i).getMaxPetroleumInput()));
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
	 * @see edu.mit.sips.core.energy.PetroleumSoS#optimizePetroleumProductionAndDistribution(double)
	 */
	@Override
	public void optimizePetroleumProductionAndDistribution(OptimizationOptions optimizationOptions) {
		List<City> cities = getSociety().getCities();
		List<? extends PetroleumElement> elements = getInternalElements();
		
		// Count number of variables.
		int numVariables = 2*elements.size() + 2*cities.size();

		List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		
		double[] costCoefficients = new double[numVariables];
		double[] initialValues = new double[numVariables];
		
		for(PetroleumElement element : elements) {
			// Constrain maximum production in each fixed element.
			double[] productionConstraint = new double[numVariables];
			productionConstraint[elements.indexOf(element)] = 1;
			constraints.add(new LinearConstraint(productionConstraint, 
					Relationship.LEQ, element.getMaxPetroleumProduction()));
			
			// Constrain maximum throughput in each distribution element.
			double[] throughputConstraint = new double[numVariables];
			throughputConstraint[elements.size() + elements.indexOf(element)] = 1;
			constraints.add(new LinearConstraint(throughputConstraint, 
					Relationship.LEQ, element.getMaxPetroleumInput()));

			// Minimize costs - most obvious cost is importing, though also 
			// minimize transportation even if free.
			costCoefficients[elements.indexOf(element)] 
					= element.getVariableOperationsCostOfPetroleumProduction();
			initialValues[elements.indexOf(element)] 
					= element.getPetroleumProduction();

			// Set distribution cost.
			costCoefficients[elements.size() + elements.indexOf(element)] 
					= element.getVariableOperationsCostOfPetroleumDistribution()
					+ element.getReservoirIntensityOfPetroleumProduction() 
					* optimizationOptions.getDeltaReservoirOilPrice()
					+ element.getElectricalIntensityOfPetroleumDistribution()
					* (getSociety().getGlobals().getElectricityDomesticPrice()
							+ optimizationOptions.getDeltaDomesticElectricityPrice());
			initialValues[elements.size() + elements.indexOf(element)] 
					= element.getPetroleumInput();
		}
		
		for(City city : cities) {
			if(!(city.getEnergySystem() instanceof EnergySystem.Local)) {
				continue;
			}
			
			EnergySystem.Local energySystem = (EnergySystem.Local) city.getEnergySystem();
			
			// Constrain maximum resource in each city.
			double[] resourceConstraint = new double[numVariables];
			for(PetroleumElement element : elements) {
				if(city.getName().equals(element.getOrigin())) {
					resourceConstraint[elements.indexOf(element)] 
							= element.getReservoirIntensityOfPetroleumProduction();
				}
			}
			constraints.add(new LinearConstraint(resourceConstraint, 
					Relationship.LEQ, energySystem.getPetroleumSystem().getPetroleumReservoirVolume()));
			
			// Constrain supply = demand in each city.
			double[] flowCoefficients = new double[numVariables];
			for(PetroleumElement element : elements) {
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
			// Allow import in this city.
			flowCoefficients[2*elements.size() + cities.indexOf(city)] = 1;
			// Allow export from this city.
			flowCoefficients[2*elements.size() + cities.size() + cities.indexOf(city)] = -1;
			
			// Constrain in-flow and production to meet demand.
			constraints.add(new LinearConstraint(flowCoefficients, Relationship.EQ, 
					energySystem.getSociety().getTotalPetroleumDemand()));

			// Set import cost in each city.
			costCoefficients[2*elements.size() + cities.indexOf(city)] 
					= city.getGlobals().getPetroleumImportPrice() 
					+ optimizationOptions.getDeltaImportOilPrice();
			initialValues[2*elements.size() + cities.indexOf(city)] 
					= Math.max(0, -energySystem.getPetroleumSystem().getPetroleumExport());
			// Set export price in each city.
			costCoefficients[2*elements.size() + cities.size() + cities.indexOf(city)] 
					= -city.getGlobals().getPetroleumExportPrice() 
					- optimizationOptions.getDeltaExportOilPrice();
			initialValues[2*elements.size() + cities.size() + cities.indexOf(city)] 
					= Math.max(0, energySystem.getPetroleumSystem().getPetroleumExport());
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
				elements.get(i).setPetroleumProduction(Math.min(
						output.getPoint()[i],
						elements.get(i).getMaxPetroleumProduction()));
				elements.get(i).setPetroleumInput(Math.min(
						output.getPoint()[elements.size() + i],
						elements.get(i).getMaxPetroleumInput()));
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
	 * @see edu.mit.sips.core.SimEntity#tick()
	 */
	@Override
	public void tick() { }

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tock()
	 */
	@Override
	public void tock() { }

}
