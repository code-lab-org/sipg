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
 * The Class DefaultPetroleumSystem.
 */
public abstract class DefaultPetroleumSystem extends DefaultInfrastructureSystem.Local implements PetroleumSystem {
	
	/**
	 * Instantiates a new default petroleum system.
	 */
	public DefaultPetroleumSystem() {
		super("Petroleum");
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getConsumptionExpense()
	 */
	@Override
	public double getConsumptionExpense() {
		return getSociety().getGlobals().getElectricityDomesticPrice()
				* getElectricityConsumption();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getDistributionExpense()
	 */
	@Override
	public double getDistributionExpense() {
		return getSociety().getGlobals().getPetroleumDomesticPrice()
				* getPetroleumInDistribution();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getDistributionRevenue()
	 */
	@Override
	public double getDistributionRevenue() {
		return getSociety().getGlobals().getPetroleumDomesticPrice()
				* getPetroleumOutDistribution();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
	 */
	@Override
	public double getDomesticProduction() {
		return (getSociety().getGlobals().getPetroleumDomesticPrice()
				+ getSociety().getGlobals().getEconomicIntensityOfPetroleumProduction())
				* getPetroleumProduction()
				+ getExportRevenue() - getImportExpense();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		double consumption = 0;
		for(PetroleumElement e : getInternalElements()) {
			consumption += e.getElectricityConsumption();
		}
		return consumption;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getElements()
	 */
	@Override
	public List<PetroleumElement> getElements() {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
		elements.addAll(getInternalElements());
		elements.addAll(getExternalElements());
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getTradeRevenue()
	 */
	@Override
	public double getExportRevenue() {
		return getSociety().getGlobals().getPetroleumExportPrice() 
				* getPetroleumExport();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getExternalElements()
	 */
	@Override
	public List<PetroleumElement> getExternalElements() {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
		for(PetroleumElement element : getNationalPetroleumSystem().getElements()) {
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
		return getSociety().getGlobals().getPetroleumImportPrice()
				* getPetroleumImport();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
	 */
	@Override
	public List<PetroleumElement> getInternalElements() {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
		for(PetroleumElement element : getNationalPetroleumSystem().getElements()) {
			// add element if origin is inside this society
			if(getSociety().getCities().contains(
					getSociety().getCountry().getCity(element.getOrigin()))) {
				elements.add(element);
			}
		}
		return Collections.unmodifiableList(elements);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getLocalPetroleumSupply()
	 */
	@Override
	public double getLocalPetroleumSupply() {
		return getPetroleumProduction() 
				+ getPetroleumInDistribution()
				- getPetroleumOutDistribution();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getNationalPetroleumSystem()
	 */
	@Override
	public PetroleumSystem getNationalPetroleumSystem() {
		return ((EnergySystem.Local)getSociety().getEnergySystem())
				.getNationalEnergySystem().getPetroleumSystem();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumExport()
	 */
	@Override
	public double getPetroleumExport() {
		return Math.max(0, getPetroleumProduction() 
				+ getPetroleumInDistribution()
				- getPetroleumOutDistribution()
				- ((EnergySystem.Local)getSociety().getEnergySystem())
				.getElectricitySystem().getPetroleumConsumption());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumImport()
	 */
	@Override
	public double getPetroleumImport() {
		return Math.max(0, ((EnergySystem.Local)getSociety().getEnergySystem())
				.getElectricitySystem().getPetroleumConsumption()
				+ getPetroleumOutDistribution()
				- getPetroleumInDistribution()
				- getPetroleumProduction());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumInDistribution()
	 */
	@Override
	public double getPetroleumInDistribution() {
		double distribution = 0;
		for(PetroleumElement e : getExternalElements()) {
			distribution += e.getPetroleumOutput();
		}
		return distribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumOutDistribution()
	 */
	@Override
	public double getPetroleumOutDistribution() {
		double distribution = 0;
		for(PetroleumElement e : getInternalElements()) {
			if(e.getDestination() == e.getOrigin()) {
				// if a self-loop, only add distribution losses
				distribution += e.getPetroleumInput() - e.getPetroleumOutput();
			} else {
				distribution += e.getPetroleumInput();
			}
		}
		return distribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumProduction()
	 */
	@Override
	public double getPetroleumProduction() {
		double petroleumProduction = 0;
		for(PetroleumElement e : getInternalElements()) {
			petroleumProduction += e.getPetroleumProduction();
		}
		return petroleumProduction;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumWithdrawals()
	 */
	@Override
	public double getPetroleumWithdrawals() {
		double petroleumWithdrawals = 0;
		for(PetroleumElement e : getInternalElements()) {
			petroleumWithdrawals += e.getPetroleumWithdrawals();
		}
		return petroleumWithdrawals;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getProductionRevenue()
	 */
	@Override
	public double getSalesRevenue() {
		return getSociety().getGlobals().getPetroleumDomesticPrice() 
				* ((EnergySystem.Local)getSociety().getEnergySystem())
				.getElectricitySystem().getPetroleumConsumption();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getTotalPetroleumSupply()
	 */
	@Override
	public double getTotalPetroleumSupply() {
		return getLocalPetroleumSupply() 
				+ getPetroleumImport() 
				- getPetroleumExport();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#optimizePetroleumDistribution()
	 */
	@Override
	public void optimizePetroleumDistribution() {
		// Make a list of cities and infrastructure elements. The vector
		// of decision variables includes the throughput of each distribution
		// element and the import and export amounts in each city.
		List<City> cities = getSociety().getCities();
		List<PetroleumElement> elements = getInternalElements();
		
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
					energySystem.getElectricitySystem().getPetroleumConsumption() 
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
	 * @see edu.mit.sips.core.energy.PetroleumSystem#optimizePetroleumProductionAndDistribution(double)
	 */
	@Override
	public void optimizePetroleumProductionAndDistribution(double deltaProductionCost) {
		List<City> cities = getSociety().getCities();
		List<PetroleumElement> elements = getInternalElements();
		
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
					= element.getVariableOperationsCostOfPetroleumProduction() 
					+ deltaProductionCost;
			initialValues[elements.indexOf(element)] 
					= element.getPetroleumProduction();

			// Set distribution cost.
			costCoefficients[elements.size() + elements.indexOf(element)] 
					= element.getVariableOperationsCostOfPetroleumDistribution()
					+ element.getElectricalIntensityOfPetroleumDistribution()
					* getSociety().getGlobals().getElectricityDomesticPrice();
			initialValues[elements.size() + elements.indexOf(element)] 
					= element.getPetroleumInput();
		}
		
		for(City city : cities) {
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
					energySystem.getElectricitySystem().getPetroleumConsumption()));

			// Set import cost in each city.
			costCoefficients[2*elements.size() + cities.indexOf(city)] 
					= city.getGlobals().getPetroleumImportPrice();
			initialValues[2*elements.size() + cities.indexOf(city)] 
					= Math.max(0, -energySystem.getPetroleumSystem().getPetroleumExport());
			// Set export price in each city.
			costCoefficients[2*elements.size() + cities.size() + cities.indexOf(city)] 
					= -city.getGlobals().getPetroleumExportPrice();
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
}
