package edu.mit.sips.core;

import java.util.List;

import edu.mit.sips.hla.AttributeChangeListener;

/**
 * The Interface InfrastructureSystem.
 */
public interface InfrastructureSystem {
	/**
	 * The Interface Local.
	 */
	public static interface Local extends InfrastructureSystem, SimEntity {
		
		/**
		 * Gets the capital expense.
		 *
		 * @return the capital expense
		 */
		public double getCapitalExpense();
		
		/**
		 * Gets the consumption expense.
		 *
		 * @return the consumption expense
		 */
		public double getConsumptionExpense();
		
		/**
		 * Gets the decommission expense.
		 *
		 * @return the decommission expense
		 */
		public double getDecommissionExpense();
		
		/**
		 * Gets the distribution expense.
		 *
		 * @return the distribution expense
		 */
		public double getDistributionExpense();
		
		/**
		 * Gets the distribution revenue.
		 *
		 * @return the distribution revenue
		 */
		public double getDistributionRevenue();
		
		/**
		 * Gets the elements.
		 *
		 * @return the elements
		 */
		public List<? extends InfrastructureElement> getElements();
		
		/**
		 * Gets the export revenue.
		 *
		 * @return the export revenue
		 */
		public double getExportRevenue();
		
		/**
		 * Gets the external elements.
		 *
		 * @return the external elements
		 */
		public List<? extends InfrastructureElement> getExternalElements();
		
		/**
		 * Gets the import expense.
		 *
		 * @return the import expense
		 */
		public double getImportExpense();
		
		/**
		 * Gets the internal elements.
		 *
		 * @return the internal elements
		 */
		public List<? extends InfrastructureElement> getInternalElements();
		
		/**
		 * Gets the lifecycle expense.
		 *
		 * @return the lifecycle expense
		 */
		public double getLifecycleExpense();
		
		/**
		 * Gets the operations expense.
		 *
		 * @return the operations expense
		 */
		public double getOperationsExpense();
		
		/**
		 * Gets the sales revenue.
		 *
		 * @return the sales revenue
		 */
		public double getSalesRevenue();
		
		/**
		 * Gets the total expense.
		 *
		 * @return the total expense
		 */
		public double getTotalExpense();
		
		/**
		 * Gets the total revenue.
		 *
		 * @return the total revenue
		 */
		public double getTotalRevenue();
	}
	
	/**
	 * The Interface Remote.
	 */
	public static interface Remote extends InfrastructureSystem {
		
		/**
		 * Sets the cash flow.
		 *
		 * @param cashFlow the new cash flow
		 */
		public void setCashFlow(double cashFlow);
				
		/**
		 * Sets the domestic production.
		 *
		 * @param domesticProduction the new domestic production
		 */
		public void setDomesticProduction(double domesticProduction);

		/**
		 * Sets the name.
		 *
		 * @param name the new name
		 */
		public void setName(String name);
	}
	
	public static final String NAME_ATTRIBUTE = "name", 
			SOCIETY_ATTRIBUTE = "society", 
			CASH_FLOW_ATTRIBUTE = "cashFlow", 
			DOMESTIC_PRODUCTION_ATTRIBUTE = "domesticProduction";
	
	/**
	 * Adds the property change listener.
	 *
	 * @param listener the listener
	 */
	public void addAttributeChangeListener(AttributeChangeListener listener);
	
	/**
	 * Fire attribute change event.
	 *
	 * @param propertyName the property name
	 */
	public void fireAttributeChangeEvent(String propertyName);
	
	/**
	 * Gets the future cash flow.
	 *
	 * @return the future cash flow
	 */
	public double getCashFlow();
	
	/**
	 * Gets the domestic production.
	 *
	 * @return the domestic production
	 */
	public double getDomesticProduction();
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the society.
	 *
	 * @return the society
	 */
	public Society getSociety();
	
	/**
	 * Removes the property change listener.
	 *
	 * @param listener the listener
	 */
	public void removeAttributeChangeListener(AttributeChangeListener listener);
	
	/**
	 * Gets the society.
	 *
	 * @return the new society
	 */
	public void setSociety(Society society);
}
