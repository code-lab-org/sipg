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
package edu.mit.sipg.gui.electricity;

import edu.mit.sipg.core.electricity.ElectricitySystem;
import edu.mit.sipg.gui.base.InfrastructureSystemPanel;

/**
 * An abstract version of an infrastructure system panel for the electricity sector.
 * 
 * @author Paul T. Grogan
 */
public abstract class ElectricitySystemPanel extends InfrastructureSystemPanel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7588345364891776159L;

	/**
	 * Instantiates a new electricity system panel.
	 *
	 * @param electricitySystem the electricity system
	 */
	public ElectricitySystemPanel(ElectricitySystem electricitySystem) {
		super(electricitySystem);
	}
}
