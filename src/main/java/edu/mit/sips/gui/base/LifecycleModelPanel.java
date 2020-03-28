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
package edu.mit.sips.gui.base;

import javax.swing.JPanel;

import edu.mit.sips.core.lifecycle.DefaultLifecycleModel;
import edu.mit.sips.core.lifecycle.EditableLifecycleModel;
import edu.mit.sips.core.lifecycle.EditableSimpleLifecycleModel;
import edu.mit.sips.scenario.Scenario;

/**
 * A generic implementation of a panel to modify element lifecycle model properties.
 * 
 * @author Paul T. Grogan
 */
public class LifecycleModelPanel extends JPanel {
	private static final long serialVersionUID = 7776877869870401110L;
	
	/**
	 * Instantiates a new lifecycle model panel.
	 *
	 * @param lifecycleModel the lifecycle model
	 */
	public LifecycleModelPanel(EditableLifecycleModel lifecycleModel) { }
	
	/**
	 * Sets the template mode.
	 *
	 * @param templateName the new template mode
	 */
	public void setTemplateMode(String templateName) { }
	
	/**
	 * Creates a new LifecycleModelPanel object.
	 *
	 * @param lifecycleModel the lifecycle model
	 * @return the lifecycle model panel
	 */
	public static LifecycleModelPanel createLifecycleModelPanel(
			Scenario scenario, EditableLifecycleModel lifecycleModel) {
		if(lifecycleModel == null) {
			return new LifecycleModelPanel(lifecycleModel);
		} else if(lifecycleModel instanceof DefaultLifecycleModel) {
			return new LifecycleModelPanel(lifecycleModel);
		} else if(lifecycleModel instanceof EditableSimpleLifecycleModel) {
			return new SimpleLifecycleModelPanel2(scenario, (EditableSimpleLifecycleModel)lifecycleModel);
		} else {
			throw new IllegalArgumentException("Lifecycle model panel not implemented.");
		}
	}
}
