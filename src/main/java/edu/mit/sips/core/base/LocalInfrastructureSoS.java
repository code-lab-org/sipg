/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sips.core.base;

import java.util.List;

/**
 * A locally-controlled implementation of the infrastructure system-of-systems interface.
 * 
 * @author Paul T. Grogan
 */
public abstract class LocalInfrastructureSoS extends LocalInfrastructureSystem 
		implements InfrastructureSoS, InfrastructureSystem.Local {

	/**
	 * Instantiates a new locally-controlled infrastructure system-of-systems.
	 */
	protected LocalInfrastructureSoS() {
		super("Infrastructure");
	}

	/**
	 * Instantiates a new locally-controlled infrastructure system-of-systems.
	 *
	 * @param name the name
	 */
	public LocalInfrastructureSoS(String name) {
		super(name);
	}
	
	@Override
	public double getCapitalExpense() {
		double value = 0;
		for(InfrastructureSystem system : getNestedSystems()) {
			value += system.getCapitalExpense();
		}
		return value;
	}

	@Override
	public double getCashFlow() {
		double value = 0;
		for(InfrastructureSystem system : getNestedSystems()) {
			value += system.getCashFlow();
		}
		return value;
	}

	@Override
	public double getConsumptionExpense() {
		double value = 0;
		for(InfrastructureSystem.Local system : getNestedSystems()) {
			value += system.getConsumptionExpense();
		}
		return value;
	}

	@Override
	public double getDecommissionExpense() {
		double value = 0;
		for(InfrastructureSystem.Local system : getNestedSystems()) {
			value += system.getDecommissionExpense();
		}
		return value;
	}

	@Override
	public double getDistributionExpense() {
		double value = 0;
		for(InfrastructureSystem.Local system : getNestedSystems()) {
			value += system.getDistributionExpense();
		}
		return value;
	}

	@Override
	public double getDistributionRevenue() {
		double value = 0;
		for(InfrastructureSystem.Local system : getNestedSystems()) {
			value += system.getDistributionRevenue();
		}
		return value;
	}

	@Override
	public double getExportRevenue() {
		double value = 0;
		for(InfrastructureSystem.Local system : getNestedSystems()) {
			value += system.getExportRevenue();
		}
		return value;
	}

	@Override
	public double getImportExpense() {
		double value = 0;
		for(InfrastructureSystem.Local system : getNestedSystems()) {
			value += system.getImportExpense();
		}
		return value;
	}

	@Override
	public double getLifecycleExpense() {
		double value = 0;
		for(InfrastructureSystem.Local system : getNestedSystems()) {
			value += system.getLifecycleExpense();
		}
		return value;
	}

	@Override
	public abstract List<? extends InfrastructureSystem.Local> getNestedSystems();

	@Override
	public double getOperationsExpense() {
		double value = 0;
		for(InfrastructureSystem.Local system : getNestedSystems()) {
			value += system.getOperationsExpense();
		}
		return value;
	}

	@Override
	public double getSalesRevenue() {
		double value = 0;
		for(InfrastructureSystem.Local system : getNestedSystems()) {
			value += system.getSalesRevenue();
		}
		return value;
	}

	@Override
	public double getTotalExpense() {
		double value = 0;
		for(InfrastructureSystem.Local system : getNestedSystems()) {
			value += system.getTotalExpense();
		}
		return value;
	}

	@Override
	public double getTotalRevenue() {
		double value = 0;
		for(InfrastructureSystem.Local system : getNestedSystems()) {
			value += system.getTotalRevenue();
		}
		return value;
	}
}