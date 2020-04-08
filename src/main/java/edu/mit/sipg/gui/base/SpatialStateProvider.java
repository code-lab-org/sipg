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
package edu.mit.sipg.gui.base;

import java.util.List;

import edu.mit.sipg.core.Society;
import edu.mit.sipg.core.base.InfrastructureElement;

/**
 * The spatial state provider interface defines information required to 
 * generalize spatial state for various infrastructure systems.
 */
public interface SpatialStateProvider {
	
	/**
	 * Gets the consumption.
	 *
	 * @param society the society
	 * @return the consumption
	 */
	public double getConsumption(Society society);
	
	/**
	 * Gets the distribution in.
	 *
	 * @param society the society
	 * @param origin the origin
	 * @return the distribution in
	 */
	public double getDistributionIn(Society society, Society origin);
	
	/**
	 * Gets the distribution out.
	 *
	 * @param society the society
	 * @param destination the destination
	 * @return the distribution out
	 */
	public double getDistributionOut(Society society, Society destination);
	
	/**
	 * Gets the elements.
	 *
	 * @param society the society
	 * @return the elements
	 */
	public List<? extends InfrastructureElement> getElements(Society society);
	
	/**
	 * Gets the export.
	 *
	 * @param society the society
	 * @return the export
	 */
	public double getExport(Society society);
	
	/**
	 * Gets the import.
	 *
	 * @param society the society
	 * @return the import
	 */
	public double getImport(Society society);
	
	/**
	 * Gets the input.
	 *
	 * @param element the element
	 * @return the input
	 */
	public double getInput(InfrastructureElement element);
	
	/**
	 * Gets the net flow.
	 *
	 * @param society the society
	 * @return the net flow
	 */
	public double getNetFlow(Society society);
	
	/**
	 * Gets the other distribution in.
	 *
	 * @param society the society
	 * @return the other distribution in
	 */
	public double getOtherDistributionIn(Society society);
	
	/**
	 * Gets the other distribution out.
	 *
	 * @param society the society
	 * @return the other distribution out
	 */
	public double getOtherDistributionOut(Society society);
	
	/**
	 * Gets the other production.
	 *
	 * @param society the society
	 * @return the other production
	 */
	public double getOtherProduction(Society society);
	
	/**
	 * Gets the other production label.
	 *
	 * @return the other production label
	 */
	public String getOtherProductionLabel();
	
	/**
	 * Gets the output.
	 *
	 * @param element the element
	 * @return the output
	 */
	public double getOutput(InfrastructureElement element);
	
	/**
	 * Gets the production.
	 *
	 * @param element the element
	 * @return the production
	 */
	public double getProduction(InfrastructureElement element);
	
	/**
	 * Gets the units.
	 *
	 * @return the units
	 */
	public String getUnits();
	
	/**
	 * Checks if is distribution.
	 *
	 * @param element the element
	 * @return true, if is distribution
	 */
	public boolean isDistribution(InfrastructureElement element);
	
	/**
	 * Checks if is export allowed.
	 *
	 * @return true, if is export allowed
	 */
	public boolean isExportAllowed();
	
	/**
	 * Checks if is import allowed.
	 *
	 * @return true, if is import allowed
	 */
	public boolean isImportAllowed();
	
	/**
	 * Checks if is other production allowed.
	 *
	 * @return true, if is other production allowed
	 */
	public boolean isOtherProductionAllowed();
	
	/**
	 * Checks if is production.
	 *
	 * @param element the element
	 * @return true, if is production
	 */
	public boolean isProduction(InfrastructureElement element);
}
