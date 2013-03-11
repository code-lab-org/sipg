package edu.mit.sips.core;

import java.util.List;

public class DefaultInfrastructureSoS implements InfrastructureSoS {
	private String name;

	@Override
	public final double getCashFlow() {
		double value = 0;
		for(InfrastructureSystem system : getSystems()) {
			value += system.getCashFlow();
		}
		return value;
	}

	@Override
	public final double getDomesticProduction() {
		double value = 0;
		for(InfrastructureSystem system : getSystems()) {
			value += system.getDomesticProduction();
		}
		return value;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public Society getSociety() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends InfrastructureSystem> getSystems() {
		// TODO Auto-generated method stub
		return null;
	}

}
