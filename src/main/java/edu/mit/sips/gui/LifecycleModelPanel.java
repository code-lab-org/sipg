package edu.mit.sips.gui;

import javax.swing.JPanel;

import edu.mit.sips.core.lifecycle.DefaultLifecycleModel;
import edu.mit.sips.core.lifecycle.EditableLifecycleModel;
import edu.mit.sips.core.lifecycle.EditableSimpleLifecycleModel;

public class LifecycleModelPanel extends JPanel {
	private static final long serialVersionUID = 7776877869870401110L;
	
	/**
	 * Instantiates a new lifecycle model panel.
	 *
	 * @param lifecycleModel the lifecycle model
	 */
	public LifecycleModelPanel(EditableLifecycleModel lifecycleModel) {
		
	}
	
	/**
	 * Sets the template mode.
	 *
	 * @param templateName the new template mode
	 */
	public void setTemplateMode(String templateName) {
		
	}
	
	/**
	 * Creates a new LifecycleModelPanel object.
	 *
	 * @param lifecycleModel the lifecycle model
	 * @return the lifecycle model panel
	 */
	public static LifecycleModelPanel createLifecycleModelPanel(
			EditableLifecycleModel lifecycleModel) {
		if(lifecycleModel == null) {
			return new LifecycleModelPanel(lifecycleModel);
		} else if(lifecycleModel instanceof DefaultLifecycleModel) {
			return new LifecycleModelPanel(lifecycleModel);
		} else if(lifecycleModel instanceof EditableSimpleLifecycleModel) {
			//TODO return new SimpleLifecycleModelPanel((MutableSimpleLifecycleModel)lifecycleModel);
			return new SimpleLifecycleModelPanel2((EditableSimpleLifecycleModel)lifecycleModel);
		} else {
			throw new IllegalArgumentException("Lifecycle model panel not implemented.");
		}
	}
}
