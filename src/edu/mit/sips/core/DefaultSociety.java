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
public abstract class DefaultSociety extends DefaultInfrastructureSystem implements Society {
	private final String name;
	private final List<? extends Society> nestedSocieties;
	private transient Society society;
	
	private AgricultureSystem.Local agricultureSystem;
	private WaterSystem.Local waterSystem;
	private EnergySystem.Local energySystem;
	private SocialSystem.Local socialSystem;
	
	private transient AgricultureSystem.Remote remoteAgricultureSystem;
	private transient WaterSystem.Remote remoteWaterSystem;
	private transient EnergySystem.Remote remoteEnergySystem;
	private transient SocialSystem.Remote remoteSocialSystem;
	
	/**
	 * Instantiates a new default society.
	 */
	protected DefaultSociety() {
		this.name = "";
		this.nestedSocieties = Collections.unmodifiableList(
				new ArrayList<Society>());
		this.remoteAgricultureSystem = new DefaultAgricultureSystem.Remote();
		remoteAgricultureSystem.setSociety(this);
		this.remoteWaterSystem = new DefaultWaterSystem.Remote();
		remoteWaterSystem.setSociety(this);
		this.remoteEnergySystem = new DefaultEnergySystem.Remote();
		remoteEnergySystem.setSociety(this);
		this.remoteSocialSystem = new DefaultSocialSystem.Remote();
		remoteSocialSystem.setSociety(this);
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
			this.remoteAgricultureSystem = new DefaultAgricultureSystem.Remote();
		} else {
			this.agricultureSystem = agricultureSystem;
		}
		getAgricultureSystem().setSociety(this);
		
		if(waterSystem == null) {
			this.remoteWaterSystem = new DefaultWaterSystem.Remote();
		} else {
			this.waterSystem = waterSystem;
		}
		getWaterSystem().setSociety(this);
		
		if(energySystem == null) {
			this.remoteEnergySystem = new DefaultEnergySystem.Remote();
		} else {
			this.energySystem = energySystem;
		}
		getEnergySystem().setSociety(this);
		
		if(socialSystem == null) {
			this.remoteSocialSystem = new DefaultSocialSystem.Remote();
		} else {
			this.socialSystem = socialSystem;
		}
		getSocialSystem().setSociety(this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getAgricultureSystem()
	 */
	@Override
	public AgricultureSystem getAgricultureSystem() {
		if(agricultureSystem != null) {
			return agricultureSystem;
		}
		return remoteAgricultureSystem;
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
		if(energySystem != null) {
			return energySystem;
		}
		return remoteEnergySystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getInfrastructureSystems()
	 */
	@Override
	public List<? extends InfrastructureSystem> getInfrastructureSystems() {
		return Arrays.asList(getAgricultureSystem(), getWaterSystem(), 
				getEnergySystem(), getSocialSystem());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getInternalElements()
	 */
	@Override
	public List<InfrastructureElement> getInternalElements() {
		List<InfrastructureElement> elements = 
				new ArrayList<InfrastructureElement>();
		for(InfrastructureSystem system : getInfrastructureSystems()) {
			if(system instanceof InfrastructureSystem.Local) {
				elements.addAll(((InfrastructureSystem.Local)system)
						.getInternalElements());
			}
		}
		return Collections.unmodifiableList(elements);
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
		if(socialSystem != null) {
			return socialSystem;
		}
		return remoteSocialSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.InfrastructureSystem#getSociety()
	 */
	@Override
	public Society getSociety() {
		return society;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getSocieties()
	 */
	@Override
	public List<? extends Society> getSocieties() {
		List<Society> societies = new ArrayList<Society>();
		societies.add(this);
		for(Society society : getNestedSocieties()) {
			societies.addAll(society.getSocieties());
		}
		return societies;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalElectricityDemand()
	 */
	@Override
	public double getTotalElectricityDemand() {
		return getWaterSystem().getElectricityConsumption() 
				+ getEnergySystem().getElectricityConsumption()
				+ getSocialSystem().getElectricityConsumption();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalFoodDemand()
	 */
	@Override
	public double getTotalFoodDemand() {
		return getSocialSystem().getFoodConsumption();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalPetroleumDemand()
	 */
	@Override
	public double getTotalPetroleumDemand() {
		return getEnergySystem().getPetroleumConsumption();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getTotalWaterDemand()
	 */
	@Override
	public double getTotalWaterDemand() {
		return getAgricultureSystem().getWaterConsumption()
				+ getEnergySystem().getWaterConsumption()
				+ getSocialSystem().getWaterConsumption();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#getWaterSystem()
	 */
	@Override
	public WaterSystem getWaterSystem() {
		if(waterSystem != null) {
			return waterSystem;
		}
		return remoteWaterSystem;
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
		for(Society society : getNestedSocieties()) {
			society.initialize(time);
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
		for(Society society : getNestedSocieties()) {
			society.tick();
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
		for(Society society : getNestedSocieties()) {
			society.tock();
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setAgricultureSystem(edu.mit.sips.core.agriculture.AgricultureSystem)
	 */
	@Override
	public void setAgricultureSystem(AgricultureSystem agricultureSystem) {
		agricultureSystem.setSociety(this);
		if(agricultureSystem instanceof AgricultureSystem.Local) {
			this.agricultureSystem = (AgricultureSystem.Local) agricultureSystem;
			this.remoteAgricultureSystem = null;
		} else if(agricultureSystem instanceof AgricultureSystem.Remote) {
			this.agricultureSystem = null;
			this.remoteAgricultureSystem = (AgricultureSystem.Remote) agricultureSystem;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setEnergySystem(edu.mit.sips.core.energy.EnergySystem)
	 */
	@Override
	public void setEnergySystem(EnergySystem energySystem) {
		energySystem.setSociety(this);
		if(energySystem instanceof EnergySystem.Local) {
			this.energySystem = (EnergySystem.Local) energySystem;
			this.remoteEnergySystem = null;
		} else if(energySystem instanceof EnergySystem.Remote) {
			this.energySystem = null;
			this.remoteEnergySystem = (EnergySystem.Remote) energySystem;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setSocialSystem(edu.mit.sips.core.social.SocialSystem)
	 */
	@Override
	public void setSocialSystem(SocialSystem socialSystem) {
		socialSystem.setSociety(this);
		if(socialSystem instanceof SocialSystem.Local) {
			this.socialSystem = (SocialSystem.Local) socialSystem;
			this.remoteSocialSystem = null;
		} else if(socialSystem instanceof SocialSystem.Remote) {
			this.socialSystem = null;
			this.remoteSocialSystem = (SocialSystem.Remote) socialSystem;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setWaterSystem(edu.mit.sips.core.water.WaterSystem)
	 */
	@Override
	public void setWaterSystem(WaterSystem waterSystem) {
		waterSystem.setSociety(this);
		if(waterSystem instanceof WaterSystem.Local) {
			this.waterSystem = (WaterSystem.Local) waterSystem;
			this.remoteWaterSystem = null;
		} else if(waterSystem instanceof WaterSystem.Remote) {
			this.waterSystem = null;
			this.remoteWaterSystem = (WaterSystem.Remote) waterSystem;
		}
	}
}
