package edu.mit.sips.core.energy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.DefaultInfrastructureSystem;


/**
 * The Class DefaultElectricitySystem.
 */
public class DefaultElectricitySystem extends DefaultInfrastructureSystem.Local implements
		ElectricitySystem {
	private final List<ElectricityElement> elements = 
			Collections.synchronizedList(new ArrayList<ElectricityElement>());
	
	/**
	 * Instantiates a new default electricity system.
	 */
	public DefaultElectricitySystem() {
		super("Electricity");
	}
	
	/**
	 * Instantiates a new default electricity system.
	 *
	 * @param elements the elements
	 */
	public DefaultElectricitySystem(
			Collection<? extends ElectricityElement> elements) {
		super("Electricity");
		
		if(elements != null) {
			this.elements.addAll(elements);
		}
	}
	
	/**
	 * Adds the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean addElement(ElectricityElement element) {
		return elements.add(element);
	}

	/**
	 * Removes the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean removeElement(ElectricityElement element) {
		return elements.remove(element);
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
				* (getElectricityOutDistribution() - getElectricityOutDistributionLosses());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
	 */
	@Override
	public double getDomesticProduction() {
		// add private consumption to base domestic production
		return super.getDomesticProduction() 
				+ getSociety().getGlobals().getPrivateConsumptionFromElectricityProduction()
				* (getElectricityProduction() + getElectricityFromBurningPetroleum());
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
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityInDistribution()
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
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityOutDistribution()
	 */
	@Override
	public double getElectricityOutDistribution() {
		double distribution = 0;
		for(ElectricityElement e : getInternalElements()) {
			if(!getSociety().getCities().contains(
					getSociety().getCountry().getCity(e.getDestination()))) {
				distribution += e.getElectricityInput();
			}
		}
		return distribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getElectricityOutDistributionLosses()
	 */
	@Override
	public double getElectricityOutDistributionLosses() {
		double distribution = 0;
		for(ElectricityElement e : getInternalElements()) {
			distribution += e.getElectricityInput() - e.getElectricityOutput();
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

	/**
	 * Gets the energy system.
	 *
	 * @return the energy system
	 */
	private EnergySystem.Local getEnergySystem() {
		return (EnergySystem.Local) getSociety().getEnergySystem();
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
		
		// see if country system is also local
		if(getSociety().getCountry().getEnergySystem()
				instanceof EnergySystem.Local) {
			ElectricitySystem system = ((EnergySystem.Local)
					getSociety().getCountry().getEnergySystem())
					.getElectricitySystem();
			for(ElectricityElement element : system.getElements()) {
				City origin = getSociety().getCountry().getCity(
						element.getOrigin());
				City dest = getSociety().getCountry().getCity(
						element.getDestination());
				// add element if origin is outside this society but 
				// destination is within this society
				if(!getSociety().getCities().contains(origin)
						&& getSociety().getCities().contains(dest)) {
					elements.add(element);
				}
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
		return Collections.unmodifiableList(elements);
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
								getEnergySystem().getPetroleumSystem().getPetroleumProduction())
						/ getPetroleumBurned();
			}
			
			return Math.min(1, getElectricityProduction()
					+ electricityFromBurningLocalPetroleum)
					/ getSociety().getTotalElectricityDemand();
		}
		return 0;
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
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getRenewableElectricityFraction()
	 */
	@Override
	public double getRenewableElectricityFraction() {
		if(getSociety().getTotalElectricityDemand() > 0) {
			return getRenewableElectricityProduction() / 
					getSociety().getTotalElectricityDemand();
		}
		return 0;
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
				* (getSociety().getTotalElectricityDemand() - getElectricityFromBurningPetroleum());
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
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getUnitProductionCost()
	 */
	@Override
	public double getUnitProductionCost() {
		if(getElectricityProduction() > 0) {
			return (getLifecycleExpense() + getConsumptionExpense()) 
					/ getElectricityProduction();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.ElectricitySystem#getUnitSupplyCost()
	 */
	@Override
	public double getUnitSupplyProfit() {
		if(getTotalElectricitySupply() > 0) {
			return getCashFlow() / getTotalElectricitySupply();
		}
		return 0;
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
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) { }

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
