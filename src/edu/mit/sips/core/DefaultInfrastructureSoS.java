package edu.mit.sips.core;


/**
 * The Class DefaultInfrastructureSoS.
 */
public abstract class DefaultInfrastructureSoS extends DefaultInfrastructureSystem implements InfrastructureSoS {
	
	/**
	 * Instantiates a new default infrastructure so s.
	 */
	protected DefaultInfrastructureSoS() {
		super("Infrastructure");
	}
	
	/**
	 * Instantiates a new default infrastructure so s.
	 *
	 * @param name the name
	 */
	public DefaultInfrastructureSoS(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getName()
	 */
	@Override
	public String getName() {
		if(getSociety() != null) {
			return getSociety().getName() + " " + super.getName();
		}
		return super.getName();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getCashFlow()
	 */
	@Override
	public double getCashFlow() {
		double value = 0;
		for(InfrastructureSystem system : getNestedSystems()) {
			value += system.getCashFlow();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getDomesticProduction()
	 */
	@Override
	public double getDomesticProduction() {
		double value = 0;
		for(InfrastructureSystem system : getNestedSystems()) {
			value += system.getCashFlow();
		}
		return value;
	}
}
