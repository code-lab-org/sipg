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
 * The Class DefaultEnergySoS.
 */
public class DefaultEnergySoS extends DefaultInfrastructureSoS implements EnergySoS {

	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSoS.Local implements EnergySoS.Local {
		private DefaultPetroleumSoS petroleumSystem;
		private DefaultElectricitySoS electricitySystem;
		
		/**
		 * Instantiates a new local.
		 */
		public Local() {
			super("Energy");
			petroleumSystem = new DefaultPetroleumSoS();
			electricitySystem = new DefaultElectricitySoS();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Local#addElement(edu.mit.sips.core.energy.EnergyElement)
		 */
		@Override
		public boolean addElement(EnergyElement element) {
			for(EnergySystem.Local system : getNestedSystems()) {
				if(system.getSociety().getName().equals(element.getOrigin())) {
					return system.addElement(element);
				}
			}
			return false;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem#getElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			double value = 0;
			for(EnergySystem system : getNestedSystems()) {
				value += system.getElectricityConsumption();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Local#getElectricitySystem()
		 */
		@Override
		public ElectricitySystem getElectricitySystem() {
			return electricitySystem;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getElements()
		 */
		@Override
		public List<? extends EnergyElement> getElements() {
			List<EnergyElement> elements = new ArrayList<EnergyElement>();
			elements.addAll(getInternalElements());
			elements.addAll(getExternalElements());
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getExternalElements()
		 */
		@Override
		public List<? extends EnergyElement> getExternalElements() {
			List<EnergyElement> elements = new ArrayList<EnergyElement>();
			for(EnergySystem.Local system : getNestedSystems()) {
				elements.addAll(system.getExternalElements());
			}
			elements.removeAll(getInternalElements());
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getInternalElements()
		 */
		@Override
		public List<? extends EnergyElement> getInternalElements() {
			List<EnergyElement> elements = new ArrayList<EnergyElement>();
			for(EnergySystem.Local system : getNestedSystems()) {
				elements.addAll(system.getInternalElements());
			}
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.DefaultInfrastructureSoS.Local#getNestedSystems()
		 */
		@Override
		public List<EnergySystem.Local> getNestedSystems() {
			List<EnergySystem.Local> systems = new ArrayList<EnergySystem.Local>();
			for(Society society : getSociety().getNestedSocieties()) {
				if(society.getEnergySystem() instanceof EnergySystem.Local){ 
					systems.add((EnergySystem.Local) society.getEnergySystem());
				}
			}
			return Collections.unmodifiableList(systems);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem#getPetroleumConsumption()
		 */
		@Override
		public double getPetroleumConsumption() {
			double value = 0;
			for(EnergySystem system : getNestedSystems()) {
				value += system.getPetroleumConsumption();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Local#getPetroleumSystem()
		 */
		@Override
		public PetroleumSystem getPetroleumSystem() {
			return petroleumSystem;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem#getWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			double value = 0;
			for(EnergySystem system : getNestedSystems()) {
				value += system.getWaterConsumption();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#initialize(long)
		 */
		@Override
		public void initialize(long time) {
			petroleumSystem.initialize(time);
			electricitySystem.initialize(time);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySoS.Local#optimizeEnergyDistribution()
		 */
		public void optimizeEnergyDistribution() {
			petroleumSystem.optimizePetroleumDistribution();
			electricitySystem.optimizeElectricityDistribution();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySoS.Local#optimizeEnergyProductionAndDistribution(double, double)
		 */
		public void optimizeEnergyProductionAndDistribution(OptimizationOptions optimizationOptions) {
			List<City> cities = getSociety().getCities();
			List<? extends PetroleumElement> petroElements = 
					getPetroleumSystem().getInternalElements();
			List<? extends ElectricityElement> electElements = 
					getElectricitySystem().getInternalElements();
			
			// Count number of variables.
			int numVariables = 2*petroElements.size() 
					+ 2*electElements.size() + 3*cities.size();

			List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
			
			double[] costCoefficients = new double[numVariables];
			double[] initialValues = new double[numVariables];
			
			for(PetroleumElement element : petroElements) {
				// Constrain maximum production in each fixed element.
				double[] productionConstraint = new double[numVariables];
				productionConstraint[petroElements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(productionConstraint, 
						Relationship.LEQ, element.getMaxPetroleumProduction()));
				
				// Constrain maximum throughput in each distribution element.
				double[] throughputConstraint = new double[numVariables];
				throughputConstraint[petroElements.size() + petroElements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(throughputConstraint, 
						Relationship.LEQ, element.getMaxPetroleumInput()));

				// Minimize costs - most obvious cost is importing, though also 
				// minimize transportation even if free.
				costCoefficients[petroElements.indexOf(element)] 
						= element.getVariableOperationsCostOfPetroleumProduction()
								+ element.getReservoirIntensityOfPetroleumProduction() 
								* optimizationOptions.getDeltaReservoirOilPrice();
				initialValues[petroElements.indexOf(element)] 
						= element.getPetroleumProduction();

				// Set distribution cost.
				costCoefficients[petroElements.size() + petroElements.indexOf(element)] 
						= element.getVariableOperationsCostOfPetroleumDistribution();
		                // TODO removed because internalized as export capability
						//+ element.getElectricalIntensityOfPetroleumDistribution()
						//* getSociety().getGlobals().getElectricityDomesticPrice();
						// optimizationOptions.getDeltaDomesticOilPrice());
				initialValues[petroElements.size() + petroElements.indexOf(element)] 
						= element.getPetroleumInput();
			}
			
			for(ElectricityElement element : electElements) {
				// Constrain maximum production in each fixed element.
				double[] productionConstraint = new double[numVariables];
				productionConstraint[2*petroElements.size() 
				                     + electElements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(productionConstraint, 
						Relationship.LEQ, element.getMaxElectricityProduction()));
				
				// Constrain maximum throughput in each distribution element.
				double[] throughputConstraint = new double[numVariables];
				throughputConstraint[2*petroElements.size() 
				                     + electElements.size() 
				                     + electElements.indexOf(element)] = 1;
				constraints.add(new LinearConstraint(throughputConstraint, 
						Relationship.LEQ, element.getMaxElectricityInput()));

				// Minimize costs - most obvious cost is importing, though also 
				// minimize transportation even if free.
				costCoefficients[2*petroElements.size() 
				                 + electElements.indexOf(element)] 
				                		 = element.getVariableOperationsCostOfElectricityProduction() 
				                		 + element.getWaterIntensityOfElectricityProduction()
				                		 * (getSociety().getGlobals().getWaterDomesticPrice()
				                				 + optimizationOptions.getDeltaDomesticWaterPrice());
				                		 // TODO removed because internalized as export capability
				                		 //+ element.getPetroleumIntensityOfElectricityProduction()
				                		 //* (getSociety().getGlobals().getPetroleumDomesticPrice()
										 // optimizationOptions.getDeltaDomesticOilPrice());
				initialValues[2*petroElements.size() 
				              + electElements.indexOf(element)] 
				            		  = element.getElectricityProduction();

				// Set distribution cost.
				costCoefficients[2*petroElements.size() 
					              + electElements.size() 
					              + electElements.indexOf(element)] 
					            		  = element.getVariableOperationsCostOfElectricityDistribution();
				initialValues[2*petroElements.size() 
				              + electElements.size() 
				              + electElements.indexOf(element)]
				            		  = element.getElectricityInput();
			}
			
			for(City city : cities) {
				if(!(city.getEnergySystem() instanceof EnergySystem.Local)) {
					continue;
				}

				EnergySystem.Local energySystem = (EnergySystem.Local) city.getEnergySystem();
				// Constrain maximum resource in each city.
				double[] resourceConstraint = new double[numVariables];
				for(PetroleumElement element : petroElements) {
					if(city.getName().equals(element.getOrigin())) {
						resourceConstraint[petroElements.indexOf(element)] 
								= element.getReservoirIntensityOfPetroleumProduction();
					}
				}
				constraints.add(new LinearConstraint(resourceConstraint, 
						Relationship.LEQ, energySystem.getPetroleumSystem().getPetroleumReservoirVolume()));
				
				// Constrain petroleum supply = demand in each city.
				double[] petroFlow = new double[numVariables];
				for(PetroleumElement element : petroElements) {
					if(city.getName().equals(element.getOrigin())) {
						petroFlow[petroElements.indexOf(element)] = 1;
					}
					
					if(city.getName().equals(element.getOrigin())) {
						// Set coefficient for in-flow to the distribution element.
						// Order origin first to never distribute in self loop.
						petroFlow[petroElements.size() 
						          + petroElements.indexOf(element)] = -1;
					} else if(city.getName().equals(element.getDestination())) {
						// Set coefficient for out-flow from the distribution element.
						petroFlow[petroElements.size() 
						          + petroElements.indexOf(element)] 
						        		  = element.getDistributionEfficiency();
					}
				}
				for(ElectricityElement element : electElements) {
					// Set coefficient for normal electricity production.
					if(city.getName().equals(element.getOrigin())) {
						petroFlow[2*petroElements.size() 
						          + electElements.indexOf(element)] 
						        		  = -element.getPetroleumIntensityOfElectricityProduction();
					}
				}
				// Allow petroleum import in this city.
				petroFlow[2*petroElements.size() + 2*electElements.size() 
				          + cities.indexOf(city)] = 1;
				// Allow petroleum export from this city.
				petroFlow[2*petroElements.size() + 2*electElements.size() 
				          + cities.size() + cities.indexOf(city)] = -1;
				// Allow burning of petroleum in this city.
				petroFlow[2*petroElements.size() + 2*electElements.size() 
				          + 2*cities.size() + cities.indexOf(city)] = -1;
				
				// Constrain in-flow and production to meet demand.
				constraints.add(new LinearConstraint(petroFlow, Relationship.EQ, 
						0));
				// TODO		energySystem.getElectricitySystem().getPetroleumConsumption()));
				
				// Constrain electricity supply = demand in each city.
				double[] electFlow = new double[numVariables];
				for(ElectricityElement element : electElements) {
					if(city.getName().equals(element.getOrigin())) {
						electFlow[2*petroElements.size() 
					                 + electElements.indexOf(element)] = 1;
					}
					
					if(city.getName().equals(element.getOrigin())) {
						// Set coefficient for in-flow to the distribution element.
						// Order origin first to never distribute in self loop.
						electFlow[2*petroElements.size() 
					                 + electElements.size() 
					                 + electElements.indexOf(element)] = -1;
					} else if(city.getName().equals(element.getDestination())) {
						// Set coefficient for out-flow from the distribution element.
						electFlow[2*petroElements.size() 
					                 + electElements.size() 
					                 + electElements.indexOf(element)] 
						        		  = element.getDistributionEfficiency();
					}
				}
				// Allow burning of petroleum in this city.
				electFlow[2*petroElements.size() + 2*electElements.size() 
				          + 2*cities.size() + cities.indexOf(city)] 
						= city.getGlobals().getElectricalIntensityOfBurningPetroleum();
				
				// Constrain in-flow and production to meet demand.
				constraints.add(new LinearConstraint(electFlow, Relationship.EQ, 
						city.getTotalElectricityDemand()));


				// Set import cost in each city.
				costCoefficients[2*petroElements.size() + 2*electElements.size()
				                 + cities.indexOf(city)] 
				                		 = city.getGlobals().getPetroleumImportPrice();
				initialValues[2*petroElements.size() + 2*electElements.size()
				                 + cities.indexOf(city)] 
				                		 = energySystem.getPetroleumSystem().getPetroleumImport();
				// Set export price in each city.
				costCoefficients[2*petroElements.size() + 2*electElements.size()
				                 + cities.size() + cities.indexOf(city)] 
				                		 = -city.getGlobals().getPetroleumExportPrice();
				initialValues[2*petroElements.size() + 2*electElements.size()
				                 + cities.size() + cities.indexOf(city)] 
				                		 = energySystem.getPetroleumSystem().getPetroleumExport();
				// Set petroleum burn cost in each city.
				/*TODO removed because internalized in export capability
				costCoefficients[2*petroElements.size() + 2*electElements.size() 
						          + 2*cities.size() + cities.indexOf(city)] 
						        		  = city.getGlobals().getPetroleumDomesticPrice()
						        		  + optimizationOptions.getDeltaDomesticOilPrice();
				 */
				initialValues[2*petroElements.size() + 2*electElements.size() 
					          + 2*cities.size() + cities.indexOf(city)] = Math.max(0,
					        		  energySystem.getElectricitySystem().getPetroleumBurned());
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
		
				for(int i = 0; i < petroElements.size(); i++) {
					// Add Math.min checks in case error exceeds bounds.
					petroElements.get(i).setPetroleumProduction(Math.min(
							output.getPoint()[i],
							petroElements.get(i).getMaxPetroleumProduction()));
					petroElements.get(i).setPetroleumInput(Math.min(
							output.getPoint()[petroElements.size() + i],
							petroElements.get(i).getMaxPetroleumInput()));
				}
				for(int i = 0; i < electElements.size(); i++) {
					// Add Math.min checks in case error exceeds bounds.
					electElements.get(i).setElectricityProduction(Math.min(
							output.getPoint()[2*petroElements.size() + i],
							electElements.get(i).getMaxElectricityProduction()));
					electElements.get(i).setElectricityInput(Math.min(
							output.getPoint()[2*petroElements.size() + electElements.size() + i],
							electElements.get(i).getMaxElectricityInput()));
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
		 * @see edu.mit.sips.core.energy.EnergySystem.Local#removeElement(edu.mit.sips.core.energy.EnergyElement)
		 */
		@Override
		public boolean removeElement(EnergyElement element) {
			for(EnergySystem.Local system : getNestedSystems()) {
				if(system.getSociety().getName().equals(element.getOrigin())) {
					return system.removeElement(element);
				}
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.DefaultInfrastructureSystem.Local#setSociety(edu.mit.sips.core.Society)
		 */
		@Override
		public void setSociety(Society society) {
			super.setSociety(society);
			petroleumSystem.setSociety(society);
			electricitySystem.setSociety(society);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#tick()
		 */
		@Override
		public void tick() {
			petroleumSystem.tick();
			electricitySystem.tick();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.SimEntity#tock()
		 */
		@Override
		public void tock() {
			petroleumSystem.tock();
			electricitySystem.tock();
		}
	}
	
	/**
	 * Instantiates a new default energy so s.
	 */
	public DefaultEnergySoS() {
		super("Energy");
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.EnergySystem#getElectricityConsumption()
	 */
	@Override
	public double getElectricityConsumption() {
		double value = 0;
		for(EnergySystem system : getNestedSystems()) {
			value += system.getElectricityConsumption();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSoS#getNestedSystems()
	 */
	@Override
	public List<EnergySystem> getNestedSystems() {
		List<EnergySystem> systems = new ArrayList<EnergySystem>();
		for(Society society : getSociety().getNestedSocieties()) {
			systems.add(society.getEnergySystem());
		}
		return Collections.unmodifiableList(systems);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.EnergySystem#getPetroleumConsumption()
	 */
	@Override
	public double getPetroleumConsumption() {
		double value = 0;
		for(EnergySystem system : getNestedSystems()) {
			value += system.getPetroleumConsumption();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.EnergySystem#getWaterConsumption()
	 */
	@Override
	public double getWaterConsumption() {
		double value = 0;
		for(EnergySystem system : getNestedSystems()) {
			value += system.getWaterConsumption();
		}
		return value;
	}
}
