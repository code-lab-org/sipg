package edu.mit.sips.gui;

import javax.swing.JPanel;

import edu.mit.sips.core.DefaultLifecycleModel;
import edu.mit.sips.core.MutableLifecycleModel;
import edu.mit.sips.core.MutableSimpleLifecycleModel;

public class LifecycleModelPanel extends JPanel {
	private static final long serialVersionUID = 7776877869870401110L;
	
	/**
	 * Instantiates a new lifecycle model panel.
	 *
	 * @param lifecycleModel the lifecycle model
	 */
	public LifecycleModelPanel(MutableLifecycleModel lifecycleModel) {
		
	}
	
	/**
	 * Creates a new LifecycleModelPanel object.
	 *
	 * @param lifecycleModel the lifecycle model
	 * @return the lifecycle model panel
	 */
	public static LifecycleModelPanel createLifecycleModelPanel(
			MutableLifecycleModel lifecycleModel) {
		if(lifecycleModel == null) {
			return new LifecycleModelPanel(lifecycleModel);
		} else if(lifecycleModel instanceof DefaultLifecycleModel) {
			return new LifecycleModelPanel(lifecycleModel);
		} else if(lifecycleModel instanceof MutableSimpleLifecycleModel) {
			return new SimpleLifecycleModelPanel((MutableSimpleLifecycleModel)lifecycleModel);
		} else {
			throw new IllegalArgumentException("Lifecycle model panel not implemented.");
		}
	}
}