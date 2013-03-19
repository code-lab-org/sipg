package edu.mit.sips.core;

import javax.swing.event.EventListenerList;

import edu.mit.sips.hla.AttributeChangeEvent;
import edu.mit.sips.hla.AttributeChangeListener;

/**
 * The Class DefaultInfrastructureSystem.
 */
public abstract class DefaultInfrastructureSystem implements InfrastructureSystem {
	/**
	 * The Class Local.
	 */
	public abstract static class Local extends DefaultInfrastructureSystem implements InfrastructureSystem.Local {
		private final String name;
		private transient Society society;
		
		/**
		 * Instantiates a new local.
		 */
		protected Local() {
			this.name = "Infrastructure";
		}
		
		/**
		 * Instantiates a new local.
		 *
		 * @param name the name
		 */
		public Local(String name) {
			// Validate name.
			if(name == null) {
				throw new IllegalArgumentException("Name cannot be null.");
			}
			this.name = name;
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
		 * @see edu.mit.sips.core.InfrastructureSystem#getName()
		 */
		@Override
		public final String getName() {
			return getSociety().getName() + " " + name;
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
		 * @see edu.mit.sips.core.InfrastructureSystem#getSociety()
		 */
		@Override
		public final Society getSociety() {
			return society;
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
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#setSociety(edu.mit.sips.core.Society)
		 */
		@Override
		public void setSociety(Society society) {
			this.society = society;
			fireAttributeChangeEvent(SOCIETY_ATTRIBUTE);
			fireAttributeChangeEvent(NAME_ATTRIBUTE);
		}
	}

	/**
	 * The Class Remote.
	 */
	public abstract static class Remote extends DefaultInfrastructureSystem implements InfrastructureSystem.Remote {
		private String name = "";
		private double cashFlow;
		private double domesticProduction;
		private Society society;

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
		 * @see edu.mit.sips.core.InfrastructureSystem#getName()
		 */
		@Override
		public final String getName() {
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
		 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setCashFlow(double)
		 */
		@Override
		public final void setCashFlow(double cashFlow) {
			this.cashFlow = cashFlow;
			fireAttributeChangeEvent(CASH_FLOW_ATTRIBUTE);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setDomesticProduction(double)
		 */
		@Override
		public final void setDomesticProduction(double domesticProduction) {
			this.domesticProduction = domesticProduction;
			fireAttributeChangeEvent(DOMESTIC_PRODUCTION_ATTRIBUTE);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setName(java.lang.String)
		 */
		@Override
		public final void setName(String name) {
			this.name = name;
			fireAttributeChangeEvent(NAME_ATTRIBUTE);
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Remote#setSociety(edu.mit.sips.core.Society)
		 */
		@Override
		public final void setSociety(Society society) {
			this.society = society;
			fireAttributeChangeEvent(SOCIETY_ATTRIBUTE);
		}
	}

	private transient EventListenerList listenerList = new EventListenerList();

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
	public final void fireAttributeChangeEvent(String propertyName) {
		AttributeChangeEvent evt = new AttributeChangeEvent(
				this, propertyName);
		AttributeChangeListener[] listeners = listenerList.getListeners(
				AttributeChangeListener.class);
		for(int i = 0; i < listeners.length; i++) {
			listeners[i].attributeChanged(evt);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.hla.InfrastructureSystem#removeAttributeChangeListener(edu.mit.sips.hla.AttributeChangeListener)
	 */
	@Override
	public void removeAttributeChangeListener(AttributeChangeListener listener) {
		listenerList.remove(AttributeChangeListener.class, listener);
	}

}
