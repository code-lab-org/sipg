package edu.mit.sips.core.energy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.DefaultInfrastructureSystem;

/**
 * The Class DefaultPetroleumSystem.
 */
public class DefaultPetroleumSystem extends DefaultInfrastructureSystem.Local implements PetroleumSystem {
	private final List<PetroleumElement> elements = 
			Collections.synchronizedList(new ArrayList<PetroleumElement>());
	private final double maxPetroleumReservoirVolume;
	private final double initialPetroleumReservoirVolume;
	private double petroleumReservoirVolume;
	private transient double nextPetroleumReservoirVolume;
	
	/**
	 * Instantiates a new default petroleum system.
	 *
	 * @param name the name
	 */
	public DefaultPetroleumSystem() {
		super("Petroleum");
		this.maxPetroleumReservoirVolume = 0;
		this.initialPetroleumReservoirVolume = 0;
	}
	
	/**
	 * Instantiates a new default petroleum system.
	 *
	 * @param maxPetroleumReservoirVolume the max petroleum reservoir volume
	 * @param initialPetroleumReservoirVolume the initial petroleum reservoir volume
	 * @param elements the elements
	 */
	public DefaultPetroleumSystem(double maxPetroleumReservoirVolume,
			double initialPetroleumReservoirVolume,
			Collection<? extends PetroleumElement> elements) {
		super("Petroleum");
		
		// Validate max petroleum reservoir.
		if(maxPetroleumReservoirVolume < 0) {
			throw new IllegalArgumentException(
					"Max petroleum reservoir volume cannot be negative.");
		}
		this.maxPetroleumReservoirVolume = maxPetroleumReservoirVolume;
		
		// Validate initial petroleum reservoir.
		if(initialPetroleumReservoirVolume < 0) {
			throw new IllegalArgumentException(
					"Initial petroleum reservoir volume cannot be negative.");
		} else if(initialPetroleumReservoirVolume > maxPetroleumReservoirVolume) {
			throw new IllegalArgumentException(
					"Initial petroleum reservoir volume cannot exceed maximum.");
		}
		this.initialPetroleumReservoirVolume = initialPetroleumReservoirVolume;
		
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
	public synchronized boolean addElement(PetroleumElement element) {
		return elements.add(element);
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
				* (getPetroleumOutDistribution() - getPetroleumOutDistributionLosses());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getEconomicProduction()
	 */
	@Override
	public double getDomesticProduction() {
		// add private consumption to base domestic production
		return super.getDomesticProduction() 
				+ getSociety().getGlobals().getPrivateConsumptionFromPetroleumProduction()
				* getPetroleumProduction();
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
		
		// see if country system is also local
		if(getSociety().getCountry().getEnergySystem()
				instanceof EnergySystem.Local) {
			PetroleumSystem system = ((EnergySystem.Local)
					getSociety().getCountry().getEnergySystem())
					.getPetroleumSystem();
			for(PetroleumElement element : system.getElements()) {
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
		return getSociety().getGlobals().getPetroleumImportPrice()
				* getPetroleumImport();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
	 */
	@Override
	public List<PetroleumElement> getInternalElements() {
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
	 * @see edu.mit.sips.PetroleumSystem#getMaxPetroleumReservoirVolume()
	 */
	@Override
	public double getMaxPetroleumReservoirVolume() {
		return maxPetroleumReservoirVolume;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumExport()
	 */
	@Override
	public double getPetroleumExport() {
		return Math.max(0, getPetroleumProduction() 
				+ getPetroleumInDistribution()
				- getPetroleumOutDistribution()
				- getSociety().getTotalPetroleumDemand());
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumImport()
	 */
	@Override
	public double getPetroleumImport() {
		return Math.max(0, getSociety().getTotalPetroleumDemand()
				+ getPetroleumOutDistribution()
				- getPetroleumInDistribution()
				- getPetroleumProduction());
	}			

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumInDistribution()
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
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumOutDistribution()
	 */
	@Override
	public double getPetroleumOutDistribution() {
		double distribution = 0;
		for(PetroleumElement e : getInternalElements()) {
			if(!getSociety().getCities().contains(
					getSociety().getCountry().getCity(e.getDestination()))) {
				distribution += e.getPetroleumInput();
			}
		}
		return distribution;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getPetroleumOutDistributionLosses()
	 */
	@Override
	public double getPetroleumOutDistributionLosses() {
		double distribution = 0;
		for(PetroleumElement e : getInternalElements()) {
			distribution += e.getPetroleumInput() - e.getPetroleumOutput();
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
	 * @see edu.mit.sips.PetroleumSystem#getPetroleumReservoirVolume()
	 */
	@Override
	public double getPetroleumReservoirVolume() {
		return petroleumReservoirVolume;
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
				* getSociety().getTotalPetroleumDemand();
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
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getProductionCost()
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
	 * @see edu.mit.sips.core.energy.PetroleumSystem#getSupplyCost()
	 */
	@Override
	public double getUnitSupplyProfit() {
		if(getTotalPetroleumSupply() > 0) {
			return getCashFlow() / getTotalPetroleumSupply();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		petroleumReservoirVolume = initialPetroleumReservoirVolume;
	}
	
	/**
	 * Removes the element.
	 *
	 * @param element the element
	 * @return true, if successful
	 */
	public synchronized boolean removeElement(PetroleumElement element) {
		return elements.remove(element);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tick()
	 */
	@Override
	public void tick() {
		nextPetroleumReservoirVolume = Math.min(maxPetroleumReservoirVolume, 
				petroleumReservoirVolume - getPetroleumWithdrawals());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tock()
	 */
	@Override
	public void tock() {
		petroleumReservoirVolume = nextPetroleumReservoirVolume;
	}
}
