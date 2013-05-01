package edu.mit.sips.core.energy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.DefaultInfrastructureSystem;
import edu.mit.sips.core.Society;
import edu.mit.sips.hla.AttributeChangeListener;

/**
 * The Class DefaultEnergySystem.
 */
public abstract class DefaultEnergySystem implements EnergySystem {
	
	/**
	 * The Class Local.
	 */
	public static class Local extends DefaultInfrastructureSystem.Local implements EnergySystem.Local {
		private DefaultPetroleumSystem petroleumSystem;
		private DefaultElectricitySystem electricitySystem;
		
		/**
		 * Instantiates a new local.
		 */
		protected Local() {
			super("Energy");
			this.petroleumSystem = new DefaultPetroleumSystem();
			this.electricitySystem = new DefaultElectricitySystem();
		}
		
		/**
		 * Instantiates a new local.
		 *
		 * @param maxPetroleumReservoir the max petroleum reservoir
		 * @param initialPetroleumReservoir the initial petroleum reservoir
		 * @param elements the elements
		 */
		public Local(double maxPetroleumReservoir, 
				double initialPetroleumReservoir, 
				Collection<? extends EnergyElement> elements) {
			super("Energy");
			List<PetroleumElement> petroleumElements = 
					new ArrayList<PetroleumElement>();
			List<ElectricityElement> electricityElements = 
					new ArrayList<ElectricityElement>();
			for(EnergyElement element : elements) {
				if(element instanceof PetroleumElement) {
					petroleumElements.add((PetroleumElement)element);
				} else if(element instanceof ElectricityElement) {
					electricityElements.add((ElectricityElement)element);
				}
			}
			
			this.petroleumSystem = new DefaultPetroleumSystem(
					maxPetroleumReservoir, initialPetroleumReservoir, 
					petroleumElements);
			this.electricitySystem = new DefaultElectricitySystem(
					electricityElements);
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
		 * @see edu.mit.sips.core.energy.EnergySystem.Local#addElement(edu.mit.sips.core.energy.EnergyElement)
		 */
		@Override
		public synchronized boolean addElement(EnergyElement element) {
			if(element instanceof PetroleumElement) {
				return petroleumSystem.addElement((PetroleumElement)element);
			} else if(element instanceof ElectricityElement) {
				return electricitySystem.addElement((ElectricityElement)element);
			}
			return false;
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
		 * @see edu.mit.sips.hla.InfrastructureSystem#removeAttributeChangeListener(edu.mit.sips.hla.AttributeChangeListener)
		 */
		@Override
		public void removeAttributeChangeListener(AttributeChangeListener listener) {
			super.removeAttributeChangeListener(listener);
			petroleumSystem.removeAttributeChangeListener(listener);
			electricitySystem.removeAttributeChangeListener(listener);
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Local#removeElement(edu.mit.sips.core.energy.EnergyElement)
		 */
		@Override
		public synchronized boolean removeElement(EnergyElement element) {
			if(element instanceof PetroleumElement) {
				return petroleumSystem.removeElement((PetroleumElement)element);
			} else if(element instanceof ElectricityElement) {
				return electricitySystem.removeElement((ElectricityElement)element);
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
			fireAttributeChangeEvent(Arrays.asList(
					ELECTRICITY_CONSUMPTION_ATTRIBUTE));
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Remote#setPetroleumConsumption(double)
		 */
		@Override
		public void setPetroleumConsumption(double petroleumConsumption) {
			this.petroleumConsumption = petroleumConsumption;
			fireAttributeChangeEvent(Arrays.asList(
					PETROLEUM_CONSUMPTION_ATTRIBUTE));
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.energy.EnergySystem.Remote#setWaterConsumption(double)
		 */
		@Override
		public void setWaterConsumption(double waterConsumption) {
			this.waterConsumption = waterConsumption;
			fireAttributeChangeEvent(Arrays.asList(
					WATER_CONSUMPTION_ATTRIBUTE));
		}
	}
}
