package edu.mit.sips.core;

/**
 * The Class InfrastructureElement.
 * 
 * @author Paul T. Grogan, ptgrogan@mit.edu
 */
public abstract class DefaultInfrastructureElement implements InfrastructureElement {
	private final String name;
	private final String origin, destination;
	private final LifecycleModel lifecycleModel;
	
	/**
	 * Instantiates a new infrastructure element.
	 *
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 */
	public DefaultInfrastructureElement(String name, String origin, 
			String destination, LifecycleModel lifecycleModel) {
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
	
	/**
	 * Instantiates a new default infrastructure element.
	 */
	protected DefaultInfrastructureElement() {
		this.name = "";
		this.origin = "";
		this.destination = "";
		this.lifecycleModel = new DefaultLifecycleModel();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getCapitalExpense()
	 */
	@Override
	public final double getCapitalExpense() { 
		return lifecycleModel.getCapitalExpense();
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
	
	/**
	 * Sets the mutable fields.
	 *
	 * @param element the new mutable fields
	 */
	protected final void setMutableFields(DefaultMutableInfrastructureElement element) {
		element.setName(name);
		element.setOrigin(origin);
		element.setDestination(destination);
		element.setLifecycleModel(lifecycleModel.getMutableLifecycleModel());
	}
}
