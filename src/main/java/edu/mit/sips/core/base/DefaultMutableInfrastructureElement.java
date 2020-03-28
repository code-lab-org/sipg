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

import edu.mit.sips.core.lifecycle.DefaultLifecycleModel;
import edu.mit.sips.core.lifecycle.EditableLifecycleModel;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * A default implementation of the mutable infrastructure element interface.
 * 
 * @author Paul T. Grogan
 */
public abstract class DefaultMutableInfrastructureElement implements EditableInfrastructureElement {
	private String templateName;
	private String name = "";
	private String origin = "", destination = "";
	private EditableLifecycleModel lifecycleModel = new DefaultLifecycleModel();
	
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return lifecycleModel.getCurrencyTimeUnits();
	}
	
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return lifecycleModel.getCurrencyUnits();
	}
	
	@Override
	public final String getDestination() {
		return destination;
	}
	
	@Override
	public final EditableLifecycleModel getLifecycleModel() {
		return lifecycleModel;
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public final String getOrigin() {
		return origin;
	}
	
	@Override
	public final String getTemplateName() {
		return templateName;
	}
	
	@Override
	public final void setDestination(String destination) {
		this.destination = destination;
	}
	
	@Override
	public final void setLifecycleModel(EditableLifecycleModel lifecycleModel) {
		this.lifecycleModel = lifecycleModel;
	}
	
	@Override
	public final void setName(String name) {
		this.name = name;
	}

	@Override
	public final void setOrigin(String origin) {
		this.origin = origin;
	}

	@Override
	public final void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
}
