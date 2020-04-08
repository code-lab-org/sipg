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
package edu.mit.sipg.core.price;

import edu.mit.sipg.core.SimEntity;

/**
 * An interface to a price model that quantifies resource costs.
 * 
 * @author Paul T. Grogan
 */
public interface PriceModel extends SimEntity {
	
	/**
	 * Gets the price.
	 *
	 * @param amount the amount
	 * @return the price
	 */
	public double getPrice(double amount);
	
	/**
	 * Gets the unit price.
	 *
	 * @return the unit price
	 */
	public double getUnitPrice();
}
