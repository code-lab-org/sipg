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
package edu.mit.sipg.core.base;

/**
 * The default implementation of the infrastructure system-of-systems (SoS) interface.
 * 
 * @author Paul T. Grogan
 */
public abstract class DefaultInfrastructureSoS extends DefaultInfrastructureSystem implements InfrastructureSoS {
	
	/**
	 * Instantiates a new default infrastructure system-of-systems.
	 */
	protected DefaultInfrastructureSoS() {
		super("Infrastructure");
	}
	
	/**
	 * Instantiates a new default infrastructure system-of-systems.
	 *
	 * @param name the name
	 */
	public DefaultInfrastructureSoS(String name) {
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
	public String getName() {
		if(getSociety() != null) {
			return getSociety().getName() + " " + super.getName();
		}
		return super.getName();
	}
}
