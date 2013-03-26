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
	private final List<? extends Society> nestedSocieties;
	
	private AgricultureSystem agricultureSystem;
	private WaterSystem waterSystem;
	private EnergySystem energySystem;
	private SocialSystem socialSystem;
	
	/**
	 * Instantiates a new default society.
	 */
	protected DefaultSociety() {
		super("Society");
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
	public DefaultSociety(String name, List<? extends Society> nestedSocieties,
			AgricultureSystem agricultureSystem, WaterSystem waterSystem,
			EnergySystem energySystem, SocialSystem socialSystem) {
		super(name);
		
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

		this.agricultureSystem = agricultureSystem;
		this.agricultureSystem.setSociety(this);

		this.waterSystem = waterSystem;
		this.waterSystem.setSociety(this);

		this.energySystem = energySystem;
		this.energySystem.setSociety(this);

		this.socialSystem = socialSystem;
		this.socialSystem.setSociety(this);
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
		for(Society society : getNestedSocieties()) {
			society.initialize(time);
		}
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
	public void setAgricultureSystem(AgricultureSystem.Remote agricultureSystem) {
		agricultureSystem.setSociety(this);
		this.agricultureSystem = agricultureSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setEnergySystem(edu.mit.sips.core.energy.EnergySystem)
	 */
	@Override
	public void setEnergySystem(EnergySystem.Remote energySystem) {
		energySystem.setSociety(this);
		this.energySystem = energySystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setSocialSystem(edu.mit.sips.core.social.SocialSystem)
	 */
	@Override
	public void setSocialSystem(SocialSystem.Remote socialSystem) {
		socialSystem.setSociety(this);
		this.socialSystem = socialSystem;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.core.Society#setWaterSystem(edu.mit.sips.core.water.WaterSystem)
	 */
	@Override
	public void setWaterSystem(WaterSystem.Remote waterSystem) {
		waterSystem.setSociety(this);
		this.waterSystem = waterSystem;
	}
}
