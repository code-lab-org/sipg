package edu.mit.sips.core;

import javax.swing.event.EventListenerList;

import edu.mit.sips.core.lifecycle.DefaultLifecycleModel;
import edu.mit.sips.core.lifecycle.LifecycleModel;
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class DefaultInfrastructureElement.
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
		
		// Validate the name.
		if(name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}
		this.name = name;
		
		// Validate the origin.
		if(origin == null) {
			throw new IllegalArgumentException("Origin cannot be null.");
		}
		this.origin = origin;
		
		// Validate the destination.
		if(destination == null) {
			throw new IllegalArgumentException("Destination cannot be null.");
		}
		this.destination = destination;
		
		// Validate the lifecycle model.
		if(lifecycleModel == null) {
			throw new IllegalArgumentException("Lifecycle model cannot be null.");
		}
		this.lifecycleModel = lifecycleModel;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureElement#addElementChangeListener(edu.mit.sips.core.ElementChangeListener)
	 */
	@Override
	public final void addElementChangeListener(ElementChangeListener listener) {
		listenerList.add(ElementChangeListener.class, listener);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureElement#fireElementChangeEvent()
	 */
	@Override
	public final void fireElementChangeEvent() {
		ElementChangeEvent evt = new ElementChangeEvent(this);
		ElementChangeListener[] listeners = listenerList.getListeners(
				ElementChangeListener.class);
		for(int i = 0; i < listeners.length; i++) {
			listeners[i].elementChanged(evt);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getCapitalExpense()
	 */
	@Override
	public final double getCapitalExpense() { 
		return lifecycleModel.getCapitalExpense();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsDenominator()
	 */
	@Override
	public TimeUnits getCurrencyTimeUnits() {
		return lifecycleModel.getCurrencyTimeUnits();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.CurrencyUnitsOutput#getCurrencyUnitsNumerator()
	 */
	@Override
	public CurrencyUnits getCurrencyUnits() {
		return lifecycleModel.getCurrencyUnits();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getDecommissionExpense()
	 */
	@Override
	public final double getDecommissionExpense() { 
		return lifecycleModel.getDecommissionExpense();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getDestination()
	 */
	@Override
	public final String getDestination() {
		return destination;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getFixedOperationsExpense()
	 */
	@Override
	public final double getFixedOperationsExpense() { 
		return lifecycleModel.getFixedOperationsExpense();
	}
	
	/**
	 * Gets the lifecycle model.
	 *
	 * @return the lifecycle model
	 */
	public LifecycleModel getLifecycleModel() {
		return lifecycleModel;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getName()
	 */
	@Override
	public final String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getOrigin()
	 */
	@Override
	public final String getOrigin() {
		return origin;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureElement#getTemplateName()
	 */
	@Override
	public String getTemplateName() {
		return templateName;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getTotalExpense()
	 */
	@Override
	public final double getTotalExpense() {
		return getCapitalExpense() 
				+ getTotalOperationsExpense() 
				+ getDecommissionExpense();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#initialize(int)
	 */
	@Override
	public void initialize(long time) {
		lifecycleModel.initialize(time);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#isExists()
	 */
	@Override
	public final boolean isExists() {
		return lifecycleModel.isExists();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#isOperational()
	 */
	@Override
	public final boolean isOperational() { 
		return lifecycleModel.isOperational();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureElement#removeElementChangeListener(edu.mit.sips.core.ElementChangeListener)
	 */
	@Override
	public final void removeElementChangeListener(ElementChangeListener listener) {
		listenerList.remove(ElementChangeListener.class, listener);
	}
	
	/**
	 * Sets the mutable fields.
	 *
	 * @param element the new mutable fields
	 */
	protected final void setMutableFields(DefaultMutableInfrastructureElement element) {
		element.setTemplateName(templateName);
		element.setName(name);
		element.setOrigin(origin);
		element.setDestination(destination);
		element.setLifecycleModel(lifecycleModel.getMutableLifecycleModel());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tick()
	 */
	@Override
	public void tick() {
		lifecycleModel.tick();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.SimEntity#tock()
	 */
	@Override
	public void tock() {
		lifecycleModel.tock();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return name;
	}
}
