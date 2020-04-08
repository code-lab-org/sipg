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
package edu.mit.sipg.gui.agriculture;

import edu.mit.sipg.core.agriculture.AgricultureSystem;
import edu.mit.sipg.gui.base.InfrastructureSystemPanel;

/**
 * An abstract version of an infrastructure system panel for the agriculture sector.
 * 
 * @author Paul T. Grogan
 */
public abstract class AgricultureSystemPanel extends InfrastructureSystemPanel {
	private static final long serialVersionUID = -3258858158886524542L;

	/**
	 * Instantiates a new agriculture system panel.
	 *
	 * @param agricultureSystem the agriculture system
	 */
	public AgricultureSystemPanel(AgricultureSystem agricultureSystem) {
		super(agricultureSystem);
	}
}
