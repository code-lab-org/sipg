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
import edu.mit.sips.hla.AttributeChangeListener;

/**
 * The Class DefaultEnergySystem.
 */
public abstract class DefaultEnergySystem implements EnergySystem {
	
	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSystem.Local implements EnergySystem.Local {
		private PetroleumSystem petroleumSystem;
		private ElectricitySystem electricitySystem;
		
		/**
		 * Instantiates a new local.
		 */
		protected Local() {
			super("Energy");
		}
		
		/**
		 * Instantiates a new local.
		 *
		 * @param petroleumSystem the petroleum system
		 * @param electricitySystem the electricity system
		 */
		public Local(PetroleumSystem petroleumSystem, ElectricitySystem electricitySystem)  {
			super("Energy");
			this.petroleumSystem = petroleumSystem;
			this.electricitySystem = electricitySystem;
		}


		/* (non-Javadoc)
		 * @see edu.mit.sips.hla.InfrastructureSystem#addAttributeChangeListener(edu.mit.sips.hla.AttributeChangeListener)
		 */
		@Override
		public void addAttributeChangeListener(AttributeChangeListener listener) {
			super.addAttributeChangeListener(listener);
			petroleumSystem.addAttributeChangeListener(listener);
			electricitySystem.addAttributeChangeListener(listener);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getConsumptionExpense()
		 */
		@Override
		public double getConsumptionExpense() {
			return petroleumSystem.getConsumptionExpense()
					+ electricitySystem.getConsumptionExpense();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getDistributionExpense()
		 */
		@Override
		public double getDistributionExpense() {
			return petroleumSystem.getDistributionExpense()
					+ electricitySystem.getDistributionExpense();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getDistributionRevenue()
		 */
		@Override
		public double getDistributionRevenue() {
			return petroleumSystem.getDistributionRevenue()
					+ electricitySystem.getDistributionRevenue();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem#getDomesticProduction()
		 */
		@Override
		public double getDomesticProduction() {
			return petroleumSystem.getDomesticProduction() 
					+ electricitySystem.getDomesticProduction();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem#getElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			return petroleumSystem.getElectricityConsumption();
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
		public List<EnergyElement> getElements() {
			List<EnergyElement> elements = new ArrayList<EnergyElement>();
			elements.addAll(petroleumSystem.getElements());
			elements.addAll(electricitySystem.getElements());
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getExportRevenue()
		 */
		@Override
		public double getExportRevenue() {
			return petroleumSystem.getExportRevenue()
					+ electricitySystem.getExportRevenue();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getExternalElements()
		 */
		@Override
		public List<EnergyElement> getExternalElements() {
			List<EnergyElement> elements = new ArrayList<EnergyElement>();
			elements.addAll(petroleumSystem.getExternalElements());
			elements.addAll(electricitySystem.getExternalElements());
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getImportExpense()
		 */
		@Override
		public double getImportExpense() {
			return petroleumSystem.getImportExpense()
					+ electricitySystem.getImportExpense();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getInternalElements()
		 */
		@Override
		public List<EnergyElement> getInternalElements() {
			List<EnergyElement> elements = new ArrayList<EnergyElement>();
			elements.addAll(petroleumSystem.getInternalElements());
			elements.addAll(electricitySystem.getInternalElements());
			return Collections.unmodifiableList(elements);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Local#getNationalEnergySystem()
		 */
		@Override
		public EnergySystem.Local getNationalEnergySystem() {
			return (EnergySystem.Local) getSociety().getCountry().getEnergySystem();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem#getPetroleumConsumption()
		 */
		@Override
		public double getPetroleumConsumption() {
			return electricitySystem.getPetroleumConsumption();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Local#getPetroleumSystem()
		 */
		@Override
		public PetroleumSystem getPetroleumSystem() {
			return petroleumSystem;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getSalesRevenue()
		 */
		@Override
		public double getSalesRevenue() {
			return petroleumSystem.getSalesRevenue()
					+ electricitySystem.getSalesRevenue();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem#getWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			return electricitySystem.getWaterConsumption();
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
		 * @see edu.mit.sips.core.energy.EnergySystem.Local#optimizeEnergyDistribution()
		 */
		@Override
		public void optimizeEnergyDistribution() {
			petroleumSystem.optimizePetroleumDistribution();
			electricitySystem.optimizeElectricityDistribution();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Local#optimizeEnergyProductionAndDistribution(double, double)
		 */
		@Override
		public void optimizeEnergyProductionAndDistribution(
				double deltaPetroleumProductionCost, 
				double deltaElectricityProductionCost) {
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
		                		 + deltaPetroleumProductionCost;
				initialValues[petroElements.indexOf(element)] 
						= element.getPetroleumProduction();

				// Set distribution cost.
				costCoefficients[petroElements.size() + petroElements.indexOf(element)] 
						= element.getVariableOperationsCostOfPetroleumDistribution()
						+ element.getElectricalIntensityOfPetroleumDistribution()
						* getSociety().getGlobals().getElectricityDomesticPrice();
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
				                		 * getSociety().getGlobals().getWaterDomesticPrice()
				                		 + element.getPetroleumIntensityOfElectricityProduction()
				                		 * getSociety().getGlobals().getPetroleumDomesticPrice()
				                		 + deltaElectricityProductionCost;
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
						energySystem.getElectricitySystem().getPetroleumConsumption()));
				
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
						electFlow[electElements.size() 
						          + electElements.indexOf(element)] = -1;
					} else if(city.getName().equals(element.getDestination())) {
						// Set coefficient for out-flow from the distribution element.
						electFlow[electElements.size() 
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
				costCoefficients[2*petroElements.size() + 2*electElements.size() 
						          + 2*cities.size() + cities.indexOf(city)] 
						        		  = city.getGlobals().getPetroleumDomesticPrice();
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
		 * @see edu.mit.sips.hla.InfrastructureSystem#removeAttributeChangeListener(edu.mit.sips.hla.AttributeChangeListener)
		 */
		@Override
		public void removeAttributeChangeListener(AttributeChangeListener listener) {
			super.removeAttributeChangeListener(listener);
			petroleumSystem.removeAttributeChangeListener(listener);
			electricitySystem.removeAttributeChangeListener(listener);
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
	 * The Class Remote.
	 */
	public static class Remote extends DefaultInfrastructureSystem.Remote implements EnergySystem.Remote {
		private double waterConsumption;
		private double electricityConsumption;
		private double petroleumConsumption;
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem#getElectricityConsumption()
		 */
		@Override
		public double getElectricityConsumption() {
			return electricityConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem#getPetroleumConsumption()
		 */
		@Override
		public double getPetroleumConsumption() {
			return petroleumConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem#getWaterConsumption()
		 */
		@Override
		public double getWaterConsumption() {
			return waterConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Remote#setElectricityConsumption(double)
		 */
		@Override
		public void setElectricityConsumption(double electricityConsumption) {
			this.electricityConsumption = electricityConsumption;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Remote#setPetroleumConsumption(double)
		 */
		@Override
		public void setPetroleumConsumption(double petroleumConsumption) {
			this.petroleumConsumption = petroleumConsumption;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Remote#setWaterConsumption(double)
		 */
		@Override
		public void setWaterConsumption(double waterConsumption) {
			this.waterConsumption = waterConsumption;
		}
		
	}
}
