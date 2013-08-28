package edu.mit.sips.core.agriculture;

import edu.mit.sips.core.DefaultInfrastructureElement;
import edu.mit.sips.core.LifecycleModel;

/**
 * The Class AgricultureElement.
 */
public final class DefaultAgricultureElement extends DefaultInfrastructureElement implements AgricultureElement {
	
	/**
	 * Instantiates a new distribution agriculture element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param efficiency the efficiency
	 * @param maxFoodInput the max food input
	 * @param initialFoodInput the initial food input
	 * @param variableOperationsCostOfFoodDistribution the variable operations cost of food distribution
	 * @return the default agriculture element
	 */
	public static DefaultAgricultureElement createDistributionElement(
			String templateName, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double efficiency,
			double maxFoodInput, double initialFoodInput, 
			double variableOperationsCostOfFoodDistribution) {
		return new DefaultAgricultureElement(templateName, name, origin, destination, lifecycleModel, 0, 0,
				AgricultureProduct.NONE, efficiency, maxFoodInput, initialFoodInput, 
				variableOperationsCostOfFoodDistribution);
	}
	
	/**
	 * Instantiates a new fixed agriculture element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param maxLandArea the max land area
	 * @param initialLandArea the initial land area
	 * @param product the product
	 * @return the agriculture element
	 */
	public static DefaultAgricultureElement createProductionElement(
			String templateName, String name, 
			String origin, String destination, 
			LifecycleModel lifecycleModel, double maxLandArea, 
			double initialLandArea, AgricultureProduct product) {
		return new DefaultAgricultureElement(templateName, name, origin, destination, lifecycleModel, maxLandArea, 
				initialLandArea, product, 0, 0, 0, 0);
	}
	
	private final double maxLandArea;
	private final double initialLandArea;
	private final AgricultureProduct product;

	private final double maxFoodInput;
	private final double initialFoodInput;
	private final double distributionEfficiency;
	private final double variableOperationsCostOfFoodDistribution;
	
	private double landArea;
	private double foodInput;
	
	/**
	 * Instantiates a new agriculture element.
	 */
	protected DefaultAgricultureElement() {
		super();
		
		this.maxLandArea = 0;
		this.initialLandArea = 0;
		this.product = AgricultureProduct.NONE;
		
		this.distributionEfficiency = 0;
		this.variableOperationsCostOfFoodDistribution = 0;
		
		this.maxFoodInput = 0;
		this.initialFoodInput = 0;
	}
	
	/**
	 * Instantiates a new agriculture element.
	 *
	 * @param templateName the template name
	 * @param name the name
	 * @param origin the origin
	 * @param destination the destination
	 * @param lifecycleModel the lifecycle model
	 * @param maxLandArea the max land area
	 * @param initialLandArea the initial land area
	 * @param product the product
	 * @param distributionEfficiency the distribution efficiency
	 * @param maxFoodInput the max food input
	 * @param initialFoodInput the initial food input
	 * @param variableOperationsCostOfFoodDistribution the variable operations cost of food distribution
	 */
	protected DefaultAgricultureElement(String templateName, String name, 
			String origin, String destination,
			LifecycleModel lifecycleModel, double maxLandArea, 
			double initialLandArea, AgricultureProduct product,
			double distributionEfficiency, double maxFoodInput, 
			double initialFoodInput, double variableOperationsCostOfFoodDistribution) {
		super(templateName, name, origin, destination, lifecycleModel);
		
		// Validate maximum land area.
		if(maxLandArea < 0) {
			throw new IllegalArgumentException(
					"Maximum land area cannot be negative.");
		}
		this.maxLandArea = maxLandArea;
		
		// Validate initial land area parameter.
		if(initialLandArea > maxLandArea) {
			throw new IllegalArgumentException(
					"Initial land area cannot exceed maximum.");
		} else if(initialLandArea < 0) {
			throw new IllegalArgumentException(
					"Initial land area cannot be negative.");
		}
		this.initialLandArea = initialLandArea;
		
		// Validate agriculture product.
		if(product == null) {
			throw new IllegalArgumentException(
					"Agriculture product cannot be null.");
		}
		this.product = product;
		
		// Validate efficiency.
		if(distributionEfficiency < 0 || distributionEfficiency > 1) {
			throw new IllegalArgumentException(
					"Efficiency must be between 0 and 1.");
		}
		this.distributionEfficiency = distributionEfficiency;
		
		// Validate maximum food input.
		if(maxFoodInput < 0) {
			throw new IllegalArgumentException(
					"Maximum food input cannot be negative.");
		}
		this.maxFoodInput = maxFoodInput;
		
		// Validate initial food input.
		if(initialFoodInput > maxFoodInput) {
			throw new IllegalArgumentException(
					"Initial food input cannot exceed maximum.");
		} else if(initialFoodInput < 0) {
			throw new IllegalArgumentException(
					"Initial food input cannot be negative.");
		}
		this.initialFoodInput = initialFoodInput;

		// Validate distribution cost.
		if(variableOperationsCostOfFoodDistribution < 0) {
			throw new IllegalArgumentException(
					"Variable cost of distribution cannot be negative.");
		}
		this.variableOperationsCostOfFoodDistribution = variableOperationsCostOfFoodDistribution;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#getVariableOperationsCostOfFoodDistribution()
	 */
	@Override
	public double getVariableOperationsCostOfFoodDistribution() {
		return variableOperationsCostOfFoodDistribution;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#getDistributionEfficiency()
	 */
	@Override
	public double getDistributionEfficiency() {
		if(isOperational()) {
			return distributionEfficiency;
		} else {
			return 0;
		}
	}


	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#getFoodInput()
	 */
	@Override
	public double getFoodInput() {
		if(isOperational()) {
			return foodInput;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#getFoodOutput()
	 */
	@Override
	public double getFoodOutput() {
		if(isOperational()) {
			return foodInput * distributionEfficiency;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#getFoodProduction()
	 */
	@Override
	public double getFoodProduction() {
		if(isOperational()) {
			return product.getFoodIntensityOfLandUsed() * landArea;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#getLandArea()
	 */
	@Override
	public double getLandArea() {
		if(isOperational()) {
			return landArea;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#getMaxFoodInput()
	 */
	@Override
	public double getMaxFoodInput() {
		if(isOperational()) {
			return maxFoodInput;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#getMaxLandArea()
	 */
	@Override
	public double getMaxLandArea() {
		if(isOperational()) {
			return maxLandArea;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#getProduct()
	 */
	@Override
	public AgricultureProduct getProduct() {
		return product;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getTotalOperationsExpense()
	 */
	@Override
	public double getTotalOperationsExpense() {
		return getFixedOperationsExpense() 
				+ product.getCostIntensityOfLandUsed() * landArea 
				+ variableOperationsCostOfFoodDistribution * foodInput;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#getWaterConsumption()
	 */
	@Override
	public double getWaterConsumption() {
		if(isOperational()) {
			return product.getWaterIntensityOfLandUsed() * landArea;
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		super.initialize(time);
		
		// Use mutator method to validate food input.
		setFoodInput(initialFoodInput);

		// Use mutator method to validate land area.
		setLandArea(initialLandArea);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#setFoodInput(double)
	 */
	@Override
	public void setFoodInput(double foodInput) {
		// Validate food input.
		if(foodInput < 0) {
			throw new IllegalArgumentException(
					"Food input cannot be negative.");
		} else if(foodInput > maxFoodInput) {
			throw new IllegalArgumentException(
					"Food input cannot exceed maximum.");
		}
		this.foodInput = foodInput;
		fireElementChangeEvent();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.agriculture.AgricultureElement#setLandArea(double)
	 */
	@Override
	public void setLandArea(double landArea) {
		// Validate land area parameter.
		if(landArea > maxLandArea) {
			throw new IllegalArgumentException(
					"Land area cannot exceed maximum (" 
							+ landArea + ">" + maxLandArea + ").");
		} else if(landArea < 0) {
			throw new IllegalArgumentException(
					"Land area cannot be negative.");
		}
		this.landArea = landArea;
		fireElementChangeEvent();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.InfrastructureElement#getMutableElement()
	 */
	@Override
	public MutableAgricultureElement getMutableElement() {
		MutableAgricultureElement element = new MutableAgricultureElement();
		setMutableFields(element);
		element.setMaxLandArea(maxLandArea);
		element.setInitialLandArea(initialLandArea);
		element.setProduct(product);
		element.setMaxFoodInput(maxFoodInput);
		element.setInitialFoodInput(initialFoodInput);
		element.setDistributionEfficiency(distributionEfficiency);
		element.setVariableOperationsCostOfFoodDistribution(
				variableOperationsCostOfFoodDistribution);
		return element;
	}
}