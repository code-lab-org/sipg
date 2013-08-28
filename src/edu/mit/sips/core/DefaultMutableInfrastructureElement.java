package edu.mit.sips.core;


/**
 * The Class DefaultMutableElement.
 */
public abstract class DefaultMutableInfrastructureElement implements MutableInfrastructureElement {
	private String templateName;
	private String name = "";
	private String origin = "", destination = "";
	private MutableLifecycleModel lifecycleModel = new DefaultLifecycleModel();
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableInfrastructureElement#getDestination()
	 */
	@Override
	public final String getDestination() {
		return destination;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableInfrastructureElement#getLifecycleModel()
	 */
	@Override
	public final MutableLifecycleModel getLifecycleModel() {
		return lifecycleModel;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableInfrastructureElement#getName()
	 */
	@Override
	public final String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableInfrastructureElement#getOrigin()
	 */
	@Override
	public final String getOrigin() {
		return origin;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.MutableInfrastructureElement#getTemplate()
	 */
	@Override
	public final String getTemplateName() {
		return templateName;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableInfrastructureElement#setDestination(String)
	 */
	@Override
	public final void setDestination(String destination) {
		this.destination = destination;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableInfrastructureElement#setLifecycleModel(edu.mit.sips.LifecycleModel)
	 */
	@Override
	public final void setLifecycleModel(MutableLifecycleModel lifecycleModel) {
		this.lifecycleModel = lifecycleModel;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableInfrastructureElement#setName(java.lang.String)
	 */
	@Override
	public final void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.MutableInfrastructureElement#setOrigin(int)
	 */
	@Override
	public final void setOrigin(String origin) {
		this.origin = origin;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.MutableInfrastructureElement#setTemplateName(java.lang.String)
	 */
	@Override
	public final void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
}
