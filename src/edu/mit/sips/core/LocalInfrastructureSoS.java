package edu.mit.sips.core;

import java.util.List;

public abstract class LocalInfrastructureSoS extends DefaultInfrastructureSoS implements InfrastructureSystem.Local {
		
		/**
		 * Instantiates a new local.
		 */
		protected LocalInfrastructureSoS() {
			super("Infrastructure");
		}
		
		/**
		 * Instantiates a new local.
		 *
		 * @param name the name
		 */
		public LocalInfrastructureSoS(String name) {
			super(name);
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getCapitalExpense()
		 */
		@Override
		public double getCapitalExpense() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getCapitalExpense();
			}
			return value;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getConsumptionExpense()
		 */
		@Override
		public double getConsumptionExpense() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getConsumptionExpense();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getDecommissionExpense()
		 */
		@Override
		public double getDecommissionExpense() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getDecommissionExpense();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getDistributionExpense()
		 */
		@Override
		public double getDistributionExpense() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getDistributionExpense();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getDistributionRevenue()
		 */
		@Override
		public double getDistributionRevenue() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getDistributionRevenue();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getExportRevenue()
		 */
		@Override
		public double getExportRevenue() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getExportRevenue();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getImportExpense()
		 */
		@Override
		public double getImportExpense() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getImportExpense();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getLifecycleExpense()
		 */
		@Override
		public double getLifecycleExpense() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getLifecycleExpense();
			}
			return value;
		}
		
		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSoS#getNestedSystems()
		 */
		@Override
		public abstract List<? extends InfrastructureSystem.Local> getNestedSystems();

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getOperationsExpense()
		 */
		@Override
		public double getOperationsExpense() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getOperationsExpense();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getSalesRevenue()
		 */
		@Override
		public double getSalesRevenue() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getSalesRevenue();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getTotalExpense()
		 */
		@Override
		public double getTotalExpense() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getTotalExpense();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see edu.mit.sips.core.InfrastructureSystem.Local#getTotalRevenue()
		 */
		@Override
		public double getTotalRevenue() {
			double value = 0;
			for(InfrastructureSystem.Local system : getNestedSystems()) {
				value += system.getTotalRevenue();
			}
			return value;
		}
	}