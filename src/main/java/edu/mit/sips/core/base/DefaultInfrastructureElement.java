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

import javax.swing.event.EventListenerList;

import edu.mit.sips.core.lifecycle.DefaultLifecycleModel;
import edu.mit.sips.core.lifecycle.LifecycleModel;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The default implementation of the infrastructure element interface.
 * 
 * @author Paul T. Grogan
 */
public abstract class DefaultInfrastructureElement implements InfrastructureElement {
	private final String templateName;
	private final String name;
	private final String origin, destination;
	private final LifecycleModel lifecycleModel;
	protected transient EventListenerList listenerList = new EventListenerList();

	/**
	 * Instantiates a new default infrastructure element.
	 */
	protected DefaultInfrastructureElement() {
		this.templateName = null;
		this.name = "";
		this.origin = "";
		this.destination = "";
		this.lifecycleModel = new DefaultLifecycleModel();
	}
	
	/**
	 * Instantiates a new default infrastructure element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 */
	public DefaultInfrastructureElement(String templateName, 
			String name, String origin, 
			String destination, LifecycleModel lifecycleModel) {
		this.templateName = templateName;
		
		if(name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		this.name = name;
		
		if(origin == null) {
			throw new IllegalArgumentException("Origin cannot be null.");
		}
		this.origin = origin;
		
		if(destination == null) {
			throw new IllegalArgumentException("Destination cannot be null.");
		}
		this.destination = destination;
		
		if(lifecycleModel == null) {
			throw new IllegalArgumentException("Lifecycle model cannot be null.");
		}
		this.lifecycleModel = lifecycleModel;
	}
	
	@Override
	public final void addElementChangeListener(ElementChangeListener listener) {
		listenerList.add(ElementChangeListener.class, listener);
	}
	
	@Override
	public final void fireElementChangeEvent() {
		ElementChangeEvent evt = new ElementChangeEvent(this);
		ElementChangeListener[] listeners = listenerList.getListeners(
				ElementChangeListener.class);
		for(int i = 0; i < listeners.length; i++) {
			listeners[i].elementChanged(evt);
		}
	}

	@Override
	public final double getCapitalExpense() { 
		return lifecycleModel.getCapitalExpense();
	}

	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return lifecycleModel.getCurrencyTimeUnits();
	}
	
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return lifecycleModel.getCurrencyUnits();
	}
	
	@Override
	public final double getDecommissionExpense() { 
		return lifecycleModel.getDecommissionExpense();
	}
	
	@Override
	public final String getDestination() {
		return destination;
	}
	
	@Override
	public final double getFixedOperationsExpense() { 
		return lifecycleModel.getFixedOperationsExpense();
	}
	
	@Override
	public LifecycleModel getLifecycleModel() {
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
	public String getTemplateName() {
		return templateName;
	}
	
	@Override
	public final double getTotalExpense() {
		return getCapitalExpense() 
				+ getTotalOperationsExpense() 
				+ getDecommissionExpense();
	}

	@Override
	public void initialize(long time) {
		lifecycleModel.initialize(time);
	}

	@Override
	public final boolean isExists() {
		return lifecycleModel.isExists();
	}

	@Override
	public final boolean isOperational() { 
		return lifecycleModel.isOperational();
	}

	@Override
	public final void removeElementChangeListener(ElementChangeListener listener) {
		listenerList.remove(ElementChangeListener.class, listener);
	}
	
	/**
	 * Sets mutable fields from values in a mutable element.
	 *
	 * @param element the mutable element containing new field values
	 */
	protected final void setMutableFields(DefaultMutableInfrastructureElement element) {
		element.setTemplateName(templateName);
		element.setName(name);
		element.setOrigin(origin);
		element.setDestination(destination);
		element.setLifecycleModel(lifecycleModel.getMutableLifecycleModel());
	}
	
	@Override
	public void tick() {
		lifecycleModel.tick();
	}
	
	@Override
	public void tock() {
		lifecycleModel.tock();
	}
	
	@Override
	public final String toString() {
		return name;
	}
}
