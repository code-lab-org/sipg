package edu.mit.sips.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sips.core.energy.DefaultEnergySystem;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.social.DefaultSocialSystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.DefaultWaterSystem;
import edu.mit.sips.core.water.WaterSystem;

/**
 * The Class DefaultSociety.
 */
public abstract class DefaultSociety implements Society {
	private final String name;
	private final List<? extends Society> nestedSocieties;
	private transient Society society;
	
	private final AgricultureSystem agricultureSystem;
	private final WaterSystem waterSystem;
	private final EnergySystem energySystem;
	private final SocialSystem socialSystem;
	
	/**
	 * Instantiates a new default society.
	 */
	protected DefaultSociety() {
		this.name = "";
		this.nestedSocieties = Collections.unmodifiableList(
				new ArrayList<Society>());
		this.agricultureSystem = new DefaultAgricultureSystem.Remote();
		agricultureSystem.setSociety(this);
		this.waterSystem = new DefaultWaterSystem.Remote();
		waterSystem.setSociety(this);
		this.energySystem = new DefaultEnergySystem.Remote();
		energySystem.setSociety(this);
		this.socialSystem = new DefaultSocialSystem.Remote();
		socialSystem.setSociety(this);
	}
	
	/**
	 * Instantiates a new default society.
	 *
	 * @param name the name
	 * @param nestedSocieties the nested societies
	 * @param agricultureSystem the agriculture system
	 * @param waterSystem the water system
	 * @param energySystem the energy system
	 * @param socialSystem the social system
	 */
	public DefaultSociety(String name, 
			List<? extends Society> nestedSocieties,
			AgricultureSystem.Local agricultureSystem,
			WaterSystem.Local waterSystem,
			EnergySystem.Local energySystem,
			SocialSystem.Local socialSystem) {
		// Validate name.
		if(name == null) {
			throw new IllegalArgumentException(
					"Name cannot be null.");
		}
		this.name = name;
		
		// Validate cities.
		if(nestedSocieties == null) {
			throw new IllegalArgumentException(
					"Nested societies list cannot be null.");
		}
		for(Society nestedSociety : nestedSocieties) {
			if(nestedSociety == null) {
				throw new IllegalArgumentException(
						"Nested society cannot be null.");
			}
		}
		this.nestedSocieties = Collections.unmodifiableList(
				new ArrayList<Society>(nestedSocieties));
		for(Society nestedSociety : nestedSocieties) {
			nestedSociety.setSociety(this);
		}
		
		if(agricultureSystem == null) {
			this.agricultureSystem = new DefaultAgricultureSystem.Remote();
		} else {
			this.agricultureSystem = agricultureSystem;
		}
		agricultureSystem.setSociety(this);
		
		if(waterSystem == null) {
			this.waterSystem = new DefaultWaterSystem.Remote();
		} else {
			this.waterSystem = waterSystem;
		}
		waterSystem.setSociety(this);
		
		if(energySystem == null) {
			this.energySystem = new DefaultEnergySystem.Remote();
		} else {
			this.energySystem = energySystem;
		}
		energySystem.setSociety(this);
		
		if(socialSystem == null) {
			this.socialSystem = new DefaultSocialSystem.Remote();
		} else {
			this.socialSystem = socialSystem;
		}
		socialSystem.setSociety(this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getAgricultureSystem()
	 */
	@Override
	public AgricultureSystem getAgricultureSystem() {
		return agricultureSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getCashFlow()
	 */
	@Override
	public double getCashFlow() {
		double value = 0;
		for(InfrastructureSystem system : getInfrastructureSystems()) {
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
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			value += system.getDomesticProduction();
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getEnergySystem()
	 */
	@Override
	public EnergySystem getEnergySystem() {
		return energySystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getInfrastructureSystems()
	 */
	@Override
	public List<? extends InfrastructureSystem> getInfrastructureSystems() {
		return Arrays.asList(agricultureSystem, waterSystem, 
				energySystem, socialSystem);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getNestedSocieties()
	 */
	@Override
	public List<? extends Society> getNestedSocieties() {
		return nestedSocieties;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getSocialSystem()
	 */
	@Override
	public SocialSystem getSocialSystem() {
		return socialSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getSociety()
	 */
	@Override
	public Society getSociety() {
		return society;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalElectricityDemand()
	 */
	@Override
	public double getTotalElectricityDemand() {
		return waterSystem.getElectricityConsumption() 
				+ socialSystem.getElectricityConsumption();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalFoodDemand()
	 */
	@Override
	public double getTotalFoodDemand() {
		return socialSystem.getFoodConsumption();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalWaterDemand()
	 */
	@Override
	public double getTotalWaterDemand() {
		return agricultureSystem.getWaterConsumption()
				+ energySystem.getWaterConsumption()
				+ socialSystem.getWaterConsumption();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getWaterSystem()
	 */
	@Override
	public WaterSystem getWaterSystem() {
		return waterSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#initialize(long)
	 */
	@Override
	public void initialize(long time) {
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			if(system instanceof InfrastructureSystem.Local) {
				((InfrastructureSystem.Local)system).initialize(time);
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#setSociety(edu.mit.sips.core.Society)
	 */
	@Override
	public void setSociety(Society society) {
		this.society = society;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tick()
	 */
	@Override
	public void tick() {
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			if(system instanceof InfrastructureSystem.Local) {
				((InfrastructureSystem.Local)system).tick();
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.SimEntity#tock()
	 */
	@Override
	public void tock() {
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			if(system instanceof InfrastructureSystem.Local) {
				((InfrastructureSystem.Local)system).tock();
			}
		}
	}
	
}
