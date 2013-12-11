package edu.mit.sips.core;

import edu.mit.sips.sim.util.CurrencyUnits;

/**
 * The Class LocalInfrastructureSystem.
 */
public abstract class LocalInfrastructureSystem extends DefaultInfrastructureSystem implements InfrastructureSystem.Local {
	private double cumulativeCapitalExpense;
	private transient double nextTotalCapitalExpense;
	private double cumulativeCashFlow;
	private transient double nextTotalCashFlow;
	
	/**
	 * Instantiates a new local.
	 */
	protected LocalInfrastructureSystem() {
		super("Infrastructure");
	}
	
	/**
	 * Instantiates a new local.
	 *
	 * @param name the name
	 */
	public LocalInfrastructureSystem(String name) {
		super(name);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultInfrastructureSystem#getCapitalExpense()
	 */
	@Override
	public double getCapitalExpense() {
		double value = 0;
		for(InfrastructureElement e : getInternalElements()) {
			value += CurrencyUnits.convertFlow(e.getCapitalExpense(), e, this);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getCashFlow()
	 */
	@Override
	public double getCashFlow() {
		return getTotalRevenue() 
				- getTotalExpense();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getDecommissionExpense()
	 */
	@Override
	public double getDecommissionExpense() {
		double value = 0;
		for(InfrastructureElement e : getInternalElements()) {
			value += CurrencyUnits.convertFlow(e.getDecommissionExpense(), e, this);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getLifecycleExpense()
	 */
	@Override
	public double getLifecycleExpense() {
		 return getCapitalExpense() 
			+ getOperationsExpense() 
			+ getDecommissionExpense();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.DefaultInfrastructureSystem#getName()
	 */
	@Override
	public String getName() {
		if(getSociety() != null) {
			return getSociety().getName() + " " + super.getName();
		}
		return super.getName();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getOperationsExpense()
	 */
	@Override
	public double getOperationsExpense() {
		double value = 0;
		for(InfrastructureElement e : getInternalElements()) {
			value += CurrencyUnits.convertFlow(e.getTotalOperationsExpense(), e, this);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getTotalExpense()
	 */
	@Override
	public double getTotalExpense() {
		return getLifecycleExpense()  
				+ getConsumptionExpense()
				+ getDistributionExpense()
				+ getImportExpense();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getTotalRevenue()
	 */
	@Override
	public double getTotalRevenue() {
		return getSalesRevenue() 
				+ getDistributionRevenue()
				+ getExportRevenue();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		cumulativeCapitalExpense = 0;
		cumulativeCashFlow = 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tick()
	 */
	@Override
	public void tick() {
		nextTotalCapitalExpense = getCapitalExpense();
		nextTotalCashFlow = getCashFlow();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tock()
	 */
	@Override
	public void tock() {
		this.cumulativeCapitalExpense += nextTotalCapitalExpense;
		this.cumulativeCashFlow += nextTotalCashFlow;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getCumulativeCapitalExpense()
	 */
	@Override
	public double getCumulativeCapitalExpense() {
		return cumulativeCapitalExpense;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Local#getCumulativeCashFlow()
	 */
	@Override
	public double getCumulativeCashFlow() {
		return cumulativeCashFlow;
	}
}