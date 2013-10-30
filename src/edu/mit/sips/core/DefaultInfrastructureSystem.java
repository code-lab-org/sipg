package edu.mit.sips.core;

import java.util.Arrays;
import java.util.List;

import javax.swing.event.EventListenerList;

import edu.mit.sips.hla.AttributeChangeEvent;
import edu.mit.sips.hla.AttributeChangeListener;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class DefaultInfrastructureSystem.
 */
public abstract class DefaultInfrastructureSystem implements InfrastructureSystem {
	
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
		public final double getCapitalExpense() {
			double value = 0;
			for(InfrastructureElement e : getInternalElements()) {
				value += e.getCapitalExpense();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem#getCashFlow()
		 */
		@Override
		public final double getCashFlow() {
			return getTotalRevenue() 
					- getTotalExpense();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getDecommissionExpense()
		 */
		@Override
		public final double getDecommissionExpense() {
			double value = 0;
			for(InfrastructureElement e : getInternalElements()) {
				value += e.getDecommissionExpense();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getLifecycleExpense()
		 */
		@Override
		public final double getLifecycleExpense() {
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
		public final double getOperationsExpense() {
			double value = 0;
			for(InfrastructureElement e : getInternalElements()) {
				value += e.getTotalOperationsExpense();
			}
			return value;
		}


		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getTotalExpense()
		 */
		@Override
		public final double getTotalExpense() {
			return getLifecycleExpense()  
					+ getConsumptionExpense()
					+ getDistributionExpense()
					+ getImportExpense();
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getTotalRevenue()
		 */
		@Override
		public final double getTotalRevenue() {
			return getSalesRevenue() 
					+ getDistributionRevenue()
					+ getExportRevenue();
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setSociety(edu.mit.sips.core.Society)
		 */
		@Override
		public void setSociety(Society society) {
			super.setSociety(society);
			fireAttributeChangeEvent(Arrays.asList(NAME_ATTRIBUTE));
		}
	}

	/**
	 * The Class Remote.
	 */
	public abstract static class Remote extends DefaultInfrastructureSystem implements InfrastructureSystem.Remote {
		private double cashFlow;
		private double domesticProduction;

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem#getCashFlow()
		 */
		@Override
		public final double getCashFlow() {
			return cashFlow;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem#getDomesticProduction()
		 */
		@Override
		public final double getDomesticProduction() {
			return domesticProduction;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setCashFlow(double)
		 */
		@Override
		public final void setCashFlow(double cashFlow) {
			this.cashFlow = cashFlow;
			fireAttributeChangeEvent(Arrays.asList(CASH_FLOW_ATTRIBUTE));
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setDomesticProduction(double)
		 */
		@Override
		public final void setDomesticProduction(double domesticProduction) {
			this.domesticProduction = domesticProduction;
			fireAttributeChangeEvent(Arrays.asList(DOMESTIC_PRODUCTION_ATTRIBUTE));
		}
	}

	private String name;
	private transient Society society;
	private transient EventListenerList listenerList = new EventListenerList();
	
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
	 * @see edu.mit.sips.hla.InfrastructureSystem#addAttributeChangeListener(edu.mit.sips.hla.AttributeChangeListener)
	 */
	@Override
	public void addAttributeChangeListener(AttributeChangeListener listener) {
		listenerList.add(AttributeChangeListener.class, listener);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.InfrastructureSystem#fireAttributeChangeEvent(java.lang.String)
	 */
	@Override
	public final void fireAttributeChangeEvent(List<String> propertyNames) {
		AttributeChangeEvent evt = new AttributeChangeEvent(
				this, propertyNames);
		AttributeChangeListener[] listeners = listenerList.getListeners(
				AttributeChangeListener.class);
		for(int i = 0; i < listeners.length; i++) {
			listeners[i].attributeChanged(evt);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsNumerator()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return CurrencyUnits.sim;
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
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsDenominator()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return TimeUnits.year;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.InfrastructureSystem#removeAttributeChangeListener(edu.mit.sips.hla.AttributeChangeListener)
	 */
	@Override
	public void removeAttributeChangeListener(AttributeChangeListener listener) {
		listenerList.remove(AttributeChangeListener.class, listener);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
		fireAttributeChangeEvent(Arrays.asList(NAME_ATTRIBUTE));
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setSociety(edu.mit.sips.core.Society)
	 */
	@Override
	public void setSociety(Society society) {
		this.society = society;
		fireAttributeChangeEvent(Arrays.asList(SOCIETY_ATTRIBUTE));
	}
}
