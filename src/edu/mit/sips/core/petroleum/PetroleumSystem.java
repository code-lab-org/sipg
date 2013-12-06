package edu.mit.sips.core.petroleum;

import java.util.List;

import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.OilUnitsOutput;

/**
 * The Interface PetroleumSystem.
 */
public interface PetroleumSystem extends InfrastructureSystem, 
		ElectricityUnitsOutput, OilUnitsOutput {
	
	/**
	 * The Interface Local.
	 */
	public interface Local extends PetroleumSystem, InfrastructureSystem.Local {

		/**
		 * Adds the element.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean addElement(PetroleumElement element);

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getElements()
		 */
		public List<? extends PetroleumElement> getElements();

		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getExternalElements()
		 */
		public List<? extends PetroleumElement> getExternalElements();
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.InfrastructureSystem#getInternalElements()
		 */
		public List<? extends PetroleumElement> getInternalElements();

		/**
		 * Gets the local petroleum fraction.
		 *
		 * @return the local petroleum fraction
		 */
		public double getLocalPetroleumFraction();

		/**
		 * Gets the max petroleum reservoir volume.
		 *
		 * @return the max petroleum reservoir volume
		 */
		public double getMaxPetroleumReservoirVolume();

		/**
		 * Gets the petroleum export.
		 *
		 * @return the petroleum export
		 */
		public double getPetroleumExport();

		/**
		 * Gets the petroleum import.
		 *
		 * @return the petroleum import
		 */
		public double getPetroleumImport();

		/**
		 * Gets the petroleum in distribution.
		 *
		 * @return the petroleum in distribution
		 */
		public double getPetroleumInDistribution();

		/**
		 * Gets the petroleum out distribution.
		 *
		 * @return the petroleum out distribution
		 */
		public double getPetroleumOutDistribution();

		/**
		 * Gets the petroleum out distribution losses.
		 *
		 * @return the petroleum out distribution losses
		 */
		public double getPetroleumOutDistributionLosses();
		
		/**
		 * Gets the petroleum production.
		 *
		 * @return the petroleum production
		 */
		public double getPetroleumProduction();

		/**
		 * Gets the petroleum reservoir volume.
		 *
		 * @return the petroleum reservoir volume
		 */
		public double getPetroleumReservoirVolume();

		/**
		 * Gets the petroleum withdrawals.
		 *
		 * @return the petroleum withdrawals
		 */
		public double getPetroleumWithdrawals();

		/**
		 * Gets the total petroleum supply.
		 *
		 * @return the total petroleum supply
		 */
		public double getTotalPetroleumSupply();

		/**
		 * Gets the unit production cost.
		 *
		 * @return the unit production cost
		 */
		public double getUnitProductionCost();
		
		/**
		 * Gets the unit supply cost.
		 *
		 * @return the unit supply cost
		 */
		public double getUnitSupplyProfit();
		
		/**
		 * Removes the element.
		 *
		 * @param element the element
		 * @return true, if successful
		 */
		public boolean removeElement(PetroleumElement element);
	}
	
	/**
	 * Gets the electricity consumed.
	 *
	 * @return the electricity consumed
	 */
	public double getElectricityConsumption();
	
	/**
	 * Gets the petroleum domestic price.
	 *
	 * @return the petroleum domestic price
	 */
	public double getPetroleumDomesticPrice();
	
	/**
	 * Gets the petroleum export price.
	 *
	 * @return the petroleum export price
	 */
	public double getPetroleumExportPrice();
	
	/**
	 * Gets the petroleum import price.
	 *
	 * @return the petroleum import price
	 */
	public double getPetroleumImportPrice();
}
