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
package edu.mit.sips.core.price;

/**
 * The default implementation of the price model interface.
 * 
 * @author Paul T. Grogan
 */
public class DefaultPriceModel implements PriceModel {
	
	/**
	 * Instantiates a new default price model.
	 */
	public DefaultPriceModel() { }
	
	@Override
	public void initialize(long time) { }

	@Override
	public void tick() { }

	@Override
	public void tock() { }

	@Override
	public double getPrice(double amount) {
		return 0;
	}
	
	@Override
	public double getUnitPrice() {
		return 0;
	}
}
