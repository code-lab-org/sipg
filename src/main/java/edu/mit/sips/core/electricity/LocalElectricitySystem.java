/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sips.core.electricity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.sips.core.City;
import edu.mit.sips.core.LocalInfrastructureSystem;
import edu.mit.sips.core.price.DefaultPriceModel;
import edu.mit.sips.core.price.PriceModel;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The locally-controlled implementation of the electricity system interface.
 * 
 * @author Paul T. Grogan
 */
public class LocalElectricitySystem extends LocalInfrastructureSystem implements ElectricitySystem.Local {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;

	private final double petroleumIntensityOfPrivateProduction;
	private final PriceModel domesticPriceModel;
	private final List<ElectricityElement> elements = 
			Collections.synchronizedList(new ArrayList<ElectricityElement>());
	
	private transient final Map<Long, Double> petroleumConsumptionLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> waterConsumptionLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> electricityDomesticPriceLog = 
			new HashMap<Long, Double>();

	/**
	 * Instantiates a new local electricity system.
	 */
	public LocalElectricitySystem() {
		super("Electricity");
		this.petroleumIntensityOfPrivateProduction = 0;
		this.domesticPriceModel = new DefaultPriceModel();
	}

	/**
	 * Instantiates a new local electricity system.
	 *
	 * @param petroleumIntensityOfPrivateProduction the electrical intensity of burning petroleum
	 * @param elements the elements
	 * @param domesticPriceModel the domestic price model
	 */
	public LocalElectricitySystem(double petroleumIntensityOfPrivateProduction,
			Collection<? extends ElectricityElement> elements,
			PriceModel domesticPriceModel) {
		super("Electricity");

		if(petroleumIntensityOfPrivateProduction < 0){ 
			throw new IllegalArgumentException(
					"Electrical intensity of burning petrleum cannot be negative.");
		}
		this.petroleumIntensityOfPrivateProduction = petroleumIntensityOfPrivateProduction;

		if(elements != null) {
			this.elements.addAll(elements);
		}

		if(domesticPriceModel == null) {
			throw new IllegalArgumentException(
					"Domestic price model cannot be null.");
		}
		this.domesticPriceModel = domesticPriceModel;
	}
	
	@Override
	public synchronized boolean addElement(ElectricityElement element) {
		return elements.add(element);
	}
	
	@Override
	public double getConsumptionExpense() {
		return DefaultUnits.convert(getSociety().getPetroleumSystem().getPetroleumDomesticPrice(),
				getSociety().getPetroleumSystem().getCurrencyUnits(),
				getSociety().getPetroleumSystem().getOilUnits(),
				getCurrencyUnits(), getOilUnits())
				* getPetroleumConsumptionFromPublicProduction();
	}

	@Override
	public double getDistributionExpense() {
		return getElectricityDomesticPrice() * getElectricityInDistribution();
	}

	@Override
	public double getDistributionRevenue() {
		return getElectricityDomesticPrice() * (getElectricityOutDistribution() 
				- getElectricityOutDistributionLosses());
	}

	@Override
	public double getElectricityDomesticPrice() {
		return domesticPriceModel.getUnitPrice();
	}

	/**
	 * Gets the electricity domestic price log.
	 *
	 * @return the electricity domestic price log
	 */
	public Map<Long, Double> getElectricityDomesticPriceLog() {
		return new HashMap<Long, Double>(electricityDomesticPriceLog);
	}

	@Override
	public double getElectricityFromPrivateProduction() {
		return Math.max(0, getSocietyDemand()
				+ getElectricityOutDistribution()
				- getElectricityInDistribution()
				- getElectricityProduction());
	}

	@Override
	public double getElectricityInDistribution() {
		double distribution = 0;
		for(ElectricityElement e : getExternalElements()) {
			distribution += ElectricityUnits.convertFlow(e.getElectricityOutput(), e, this);
		}
		return distribution;
	}

	@Override
	public double getElectricityOutDistribution() {
		double distribution = 0;
		for(ElectricityElement e : getInternalElements()) {
			if(!getSociety().getCities().contains(
					getSociety().getCountry().getCity(e.getDestination()))) {
				distribution += ElectricityUnits.convertFlow(e.getElectricityInput(), e, this);
			}
		}
		return distribution;
	}

	@Override
	public double getElectricityOutDistributionLosses() {
		double distribution = 0;
		for(ElectricityElement e : getInternalElements()) {
			distribution += ElectricityUnits.convertFlow(e.getElectricityInput(), e, this)
					- ElectricityUnits.convertFlow(e.getElectricityOutput(), e, this);
		}
		return distribution;
	}

	@Override
	public double getElectricityProduction() {
		double energyProduction = 0;
		for(ElectricityElement e : getInternalElements()) {
			energyProduction += ElectricityUnits.convertFlow(e.getElectricityProduction(), e, this);
		}
		return energyProduction;
	}

	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	@Override
	public double getElectricityWasted() {
		return Math.max(0, getElectricityProduction() 
				+ getElectricityInDistribution()
				- getElectricityOutDistribution()
				- getSocietyDemand());
	}

	@Override
	public List<ElectricityElement> getElements() {
		List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
		elements.addAll(getInternalElements());
		elements.addAll(getExternalElements());
		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getExportRevenue() {
		return 0;
	}

	@Override
	public List<ElectricityElement> getExternalElements() {
		List<ElectricityElement> elements = new ArrayList<ElectricityElement>();

		if(getSociety().getCountry().getElectricitySystem()
				instanceof ElectricitySystem.Local) {
			ElectricitySystem.Local system = (ElectricitySystem.Local)
					getSociety().getCountry().getElectricitySystem();
			for(ElectricityElement element : system.getElements()) {
				City origin = getSociety().getCountry().getCity(
						element.getOrigin());
				City dest = getSociety().getCountry().getCity(
						element.getDestination());
				// add element if origin is outside this society but 
				// destination is within this society
				if(!getSociety().getCities().contains(origin)
						&& getSociety().getCities().contains(dest)) {
					elements.add(element);
				}
			}
		}

		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getImportExpense() {
		return 0;
	}

	@Override
	public List<ElectricityElement> getInternalElements() {
		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getLocalElectricityFraction() {
		if(getSocietyDemand() > 0) {
			return Math.min(1, (getElectricityProduction() 
					+ getElectricityFromPrivateProduction())
					/ getSocietyDemand());
		}
		return 0;
	}

	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}

	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}

	@Override
	public double getPetroleumConsumption() {
		return getPetroleumConsumptionFromPrivateProduction() 
				+ getPetroleumConsumptionFromPublicProduction();
	}

	@Override
	public double getPetroleumConsumptionFromPrivateProduction() {
		return getElectricityFromPrivateProduction()
				* getPetroleumIntensityOfPrivateProduction();
	}

	@Override
	public double getPetroleumConsumptionFromPublicProduction() {
		double petroleumConsumption = 0;
		for(ElectricityElement e : getInternalElements()) {
			petroleumConsumption += OilUnits.convertFlow(e.getPetroleumConsumption(), e, this);
		}
		return petroleumConsumption;
	}

	/**
	 * Gets the petroleum consumption log.
	 *
	 * @return the petroleum consumption log
	 */
	public Map<Long, Double> getPetroleumConsumptionLog() {
		return new HashMap<Long, Double>(petroleumConsumptionLog);
	}

	@Override
	public double getPetroleumIntensityOfPrivateProduction() {
		return petroleumIntensityOfPrivateProduction;
	}

	@Override
	public double getRenewableElectricityFraction() {
		if(getSocietyDemand() > 0) {
			return getRenewableElectricityProduction() / 
					getSocietyDemand();
		}
		return 0;
	}

	@Override
	public double getRenewableElectricityProduction() {
		double production = 0;
		for(ElectricityElement e : getInternalElements()) {
			if(e.isRenewableElectricity()) {
				production += ElectricityUnits.convertFlow(e.getElectricityProduction(), e, this);
			}
		}
		return production;
	}

	@Override
	public double getSalesRevenue() {
		return getElectricityDomesticPrice() * (getSocietyDemand()
				- getElectricityFromPrivateProduction());
	}

	/**
	 * Gets the society demand for electricity.
	 *
	 * @return the society demand
	 */
	private double getSocietyDemand() {
		return ElectricityUnits.convertFlow(getSociety().getTotalElectricityDemand(), getSociety(), this);
	}

	@Override
	public double getTotalElectricitySupply() {
		return getElectricityProduction() 
				+ getElectricityInDistribution()
				- getElectricityOutDistribution();
	}

	@Override
	public double getUnitProductionCost() {
		if(getElectricityProduction() > 0) {
			return (getLifecycleExpense() + getConsumptionExpense()) 
					/ getElectricityProduction();
		}
		return 0;
	}

	@Override
	public double getUnitSupplyProfit() {
		if(getTotalElectricitySupply() > 0) {
			return getCashFlow() / getTotalElectricitySupply();
		}
		return 0;
	}

	@Override
	public double getWaterConsumption() {
		double waterConsumption = 0;
		for(ElectricityElement e : getInternalElements()) {
			waterConsumption += WaterUnits.convertFlow(e.getWaterConsumption(), e, this);
		}
		return waterConsumption;
	}

	/**
	 * Gets the water consumption log.
	 *
	 * @return the water consumption log
	 */
	public Map<Long, Double> getWaterConsumptionLog() {
		return new HashMap<Long, Double>(waterConsumptionLog);
	}

	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	@Override
	public void initialize(long time) {
		super.initialize(time);
		petroleumConsumptionLog.clear();
		waterConsumptionLog.clear();
		electricityDomesticPriceLog.clear();
	}
	
	@Override
	public synchronized boolean removeElement(ElectricityElement element) {
		return elements.remove(element);
	}

	@Override
	public void tick() {
		super.tick();
		petroleumConsumptionLog.put(time, getPetroleumConsumption());
		waterConsumptionLog.put(time, getWaterConsumption());
		electricityDomesticPriceLog.put(time, getElectricityDomesticPrice());
	}
}