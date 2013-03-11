package edu.mit.sips.core;

import java.util.List;

import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterSystem;

public class City implements Society {

	@Override
	public List<? extends InfrastructureSystem> getSystems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCashFlow() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDomesticProduction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Society getSociety() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AgricultureSystem getAgricultureSystem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<City> getCities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnergySystem getEnergySystem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends Society> getNestedSocieties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SocialSystem getSocialSystem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getTotalElectricityDemand() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTotalFoodDemand() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTotalPetroleumDemand() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTotalWaterDemand() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public WaterSystem getWaterSystem() {
		// TODO Auto-generated method stub
		return null;
	}

}
