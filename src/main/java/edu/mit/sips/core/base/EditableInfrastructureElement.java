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

import edu.mit.sips.core.lifecycle.EditableLifecycleModel;
import edu.mit.sips.sim.util.CurrencyUnitsOutput;

/**
 * An interface for infrastructure elements that are editable. 
 * This interface is used to facilitate user-modifiable templates.
 * 
 * @author Paul T. Grogan
 */
public interface EditableInfrastructureElement extends CurrencyUnitsOutput {
	
	/**
	 * Creates the immutable version of this infrastructure element.
	 *
	 * @return the infrastructure element
	 */
	public InfrastructureElement createElement();
	
	/**
	 * Gets the destination of this infrastructure element.
	 *
	 * @return the destination
	 */
	public String getDestination();
	
	/**
	 * Gets the lifecycle model for this infrastructure element.
	 *
	 * @return the lifecycle model
	 */
	public EditableLifecycleModel getLifecycleModel();
	
	/**
	 * Gets the name of this infrastructure element.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the origin of this infrastructure element.
	 *
	 * @return the origin
	 */
	public String getOrigin();
	
	/**
	 * Gets the template name of this infrastructure element.
	 *
	 * @return the template name
	 */
	public String getTemplateName();
	
	/**
	 * Sets the destination of this infrastructure element.
	 *
	 * @param destination the new destination
	 */
	public void setDestination(String destination);
	
	/**
	 * Sets the lifecycle model for this infrastructure element.
	 *
	 * @param lifecycleModel the new lifecycle model
	 */
	public void setLifecycleModel(EditableLifecycleModel lifecycleModel);
	
	/**
	 * Sets the name of this infrastructure element.
	 *
	 * @param name the new name
	 */
	public void setName(String name);
	
	/**
	 * Sets the origin of this infrastructure element.
	 *
	 * @param origin the new origin
	 */
	public void setOrigin(String origin);

	/**
	 * Sets the template name of this infrastructure element.
	 *
	 * @param templateName the new template name
	 */
	public void setTemplateName(String templateName);
}
