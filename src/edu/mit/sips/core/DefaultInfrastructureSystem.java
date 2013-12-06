package edu.mit.sips.core;

import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class DefaultInfrastructureSystem.
 */
public class DefaultInfrastructureSystem implements InfrastructureSystem {
	
	/**
	 * The Class Local.
	 */
	public abstract static class Local extends DefaultInfrastructureSystem implements InfrastructureSystem.Local {
		
		/**
		 * Instantiates a new local.
		 */
		protected Local() {
			super("Infrastructure");
		}
		
		/**
		 * Instantiates a new local.
		 *
		 * @param name the name
		 */
		public Local(String name) {
			super(name);
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getCapitalExpense()
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
	}

	protected static final CurrencyUnits currencyUnits = CurrencyUnits.sim;
	protected static final TimeUnits currencyTimeUnits = TimeUnits.year;
	
	private String name;
	private transient Society society;
	
	/**
	 * Instantiates a new default infrastructure system.
	 */
	protected DefaultInfrastructureSystem() {
		this.name = "";
	}
	
	/**
	 * Instantiates a new default infrastructure system.
	 *
	 * @param name the name
	 */
	public DefaultInfrastructureSystem(String name) {
		// Validate name.
		if(name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getCashFlow()
	 */
	@Override
	public double getCashFlow() {
		return 0;
	}


	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsDenominator()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return currencyTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsNumerator()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return currencyUnits;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getDomesticProduction()
	 */
	@Override
	public double getDomesticProduction() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getSociety()
	 */
	@Override
	public final Society getSociety() {
		return society;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setSociety(edu.mit.sips.core.Society)
	 */
	@Override
	public void setSociety(Society society) {
		this.society = society;
	}
}
