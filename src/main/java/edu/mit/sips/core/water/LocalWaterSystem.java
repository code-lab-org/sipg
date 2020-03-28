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
package edu.mit.sips.core.water;

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
import edu.mit.sips.sim.util.CurrencyUnits;
import edu.mit.sips.sim.util.DefaultUnits;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The locally-controlled implementation of the petroleum system interface.
 * 
 * @author Paul T. Grogan
 */
public class LocalWaterSystem extends LocalInfrastructureSystem implements WaterSystem.Local {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;

	private final List<WaterElement> elements = 
			Collections.synchronizedList(new ArrayList<WaterElement>());
	private final PriceModel domesticPriceModel, importPriceModel;
	private final double maxWaterReservoirVolume;
	private final double initialWaterReservoirVolume;
	private final double waterReservoirRechargeRate;
	private final boolean coastalAccess;
	private final double electricalIntensityOfPrivateProduction;
	private final double reservoirIntensityOfPrivateProduction;

	private double waterReservoirVolume;
	private transient double nextWaterReservoirVolume;
	
	private transient final Map<Long, Double> electricityConsumptionLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> waterReservoirVolumeLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> reservoirWithdrawalsLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> waterDomesticPriceLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> waterImportPriceLog = 
			new HashMap<Long, Double>();
	
	/**
	 * Instantiates a new local water system.
	 */
	protected LocalWaterSystem() {
		super("Water");
		this.domesticPriceModel = new DefaultPriceModel();
		this.importPriceModel = new DefaultPriceModel();
		this.maxWaterReservoirVolume = 0;
		this.initialWaterReservoirVolume = 0;
		this.waterReservoirRechargeRate = 0;
		this.coastalAccess = false;
		this.electricalIntensityOfPrivateProduction = 0;
		this.reservoirIntensityOfPrivateProduction = 1;
	}

	/**
	 * Instantiates a new local water system.
	 *
	 * @param coastalAccess the coastal access
	 * @param maxWaterReservoirVolume the max water reservoir volume
	 * @param initialWaterReservoirVolume the initial water reservoir volume
	 * @param waterReservoirRechargeRate the water reservoir recharge rate
	 * @param electricalIntensityOfProduction the electrical intensity of production
	 * @param aquiferIntensityOfProduction the aquifer intensity of production
	 * @param elements the elements
	 * @param domesticPriceModel the domestic price model
	 * @param importPriceModel the import price model
	 */
	public LocalWaterSystem(boolean coastalAccess, double maxWaterReservoirVolume,
			double initialWaterReservoirVolume, 
			double waterReservoirRechargeRate,
			double electricalIntensityOfProduction, 
			double aquiferIntensityOfProduction,
			Collection<? extends WaterElement> elements,
			PriceModel domesticPriceModel, 
			PriceModel importPriceModel) {
		super("Water");
		
		if(maxWaterReservoirVolume < 0) {
			throw new IllegalArgumentException(
					"Max water reservoir volume cannot be negative.");
		}
		this.maxWaterReservoirVolume = maxWaterReservoirVolume;

		if(initialWaterReservoirVolume < 0) {
			throw new IllegalArgumentException(
					"Initial water reservoir volume cannot be negative.");
		} else if(initialWaterReservoirVolume > maxWaterReservoirVolume) {
			throw new IllegalArgumentException(
					"Initial water reservoir volume cannot exceed maximum.");
		}
		this.initialWaterReservoirVolume = initialWaterReservoirVolume;

		this.waterReservoirRechargeRate = waterReservoirRechargeRate;

		if(electricalIntensityOfProduction < 0) {
			throw new IllegalArgumentException(
					"Electrical intensity of production cannot be negative.");
		}
		this.electricalIntensityOfPrivateProduction = electricalIntensityOfProduction;

		if(aquiferIntensityOfProduction < 0) {
			throw new IllegalArgumentException(
					"Aquifer intensity of production cannot be negative.");
		}
		this.reservoirIntensityOfPrivateProduction = aquiferIntensityOfProduction;

		this.coastalAccess = coastalAccess;

		if(elements != null) {
			this.elements.addAll(elements);
		}

		if(domesticPriceModel == null) {
			throw new IllegalArgumentException(
					"Domestic price model cannot be null.");
		}
		this.domesticPriceModel = domesticPriceModel;

		if(importPriceModel == null) {
			throw new IllegalArgumentException(
					"Import price model cannot be null.");
		}
		this.importPriceModel = importPriceModel;
	}
	
	@Override
	public synchronized boolean addElement(WaterElement element) {
		return elements.add(element);
	}
	
	@Override
	public double getAquiferLifetime() {
		return getAquiferWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getWaterReservoirVolume() / getAquiferWithdrawals());
	}

	@Override
	public double getAquiferRechargeRate() {
		return waterReservoirRechargeRate;
	}

	@Override
	public double getAquiferWithdrawals() {
		return getAquiferWithdrawalsFromPublicProduction() + 
				getAquiferWithdrawalsFromPrivateProduction();
	}

	@Override
	public double getAquiferWithdrawalsFromPrivateProduction() {
		return getWaterFromPrivateProduction() *
				reservoirIntensityOfPrivateProduction;
	}

	@Override
	public double getAquiferWithdrawalsFromPublicProduction() {
		double waterWithdrawals = 0;
		for(WaterElement e : getInternalElements()) {
			waterWithdrawals += WaterUnits.convertFlow(e.getAquiferWithdrawals(), e, this);
		}
		return waterWithdrawals;
	}

	@Override
	public double getConsumptionExpense() {
		return getElectricityConsumptionFromPublicProduction() * DefaultUnits.convert(
				getSociety().getElectricitySystem().getElectricityDomesticPrice(),
				getSociety().getElectricitySystem().getCurrencyUnits(), 
				getSociety().getElectricitySystem().getElectricityUnits(),
				getCurrencyUnits(), getElectricityUnits());
	}

	@Override
	public double getDistributionExpense() {
		return getWaterDomesticPrice() * getWaterInDistribution();
	}

	@Override
	public double getDistributionRevenue() {
		return getWaterDomesticPrice() * (getWaterOutDistribution() 
				- getWaterOutDistributionLosses());
	}

	@Override
	public double getElectricityConsumption() {
		return getElectricityConsumptionFromPrivateProduction() + 
				getElectricityConsumptionFromPublicProduction();
	}

	@Override
	public double getElectricityConsumptionFromPrivateProduction() {
		return getWaterFromPrivateProduction() * electricalIntensityOfPrivateProduction;
	}

	@Override
	public double getElectricityConsumptionFromPublicProduction() {
		double energyConsumption = 0;
		for(WaterElement e : getInternalElements()) {
			if(e.isCoastalAccessRequired() && !isCoastalAccess()) {
				energyConsumption += 0;
			} else {
				energyConsumption += ElectricityUnits.convertFlow(
						e.getElectricityConsumption(), e, this);
			}
		}
		return energyConsumption;
	}

	/**
	 * Gets the electricity consumption log.
	 *
	 * @return the electricity consumption log
	 */
	public Map<Long, Double> getElectricityConsumptionLog() {
		return new HashMap<Long, Double>(electricityConsumptionLog);
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
	public List<WaterElement> getElements() {
		List<WaterElement> elements = new ArrayList<WaterElement>();
		elements.addAll(getInternalElements());
		elements.addAll(getExternalElements());
		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getExportRevenue() {
		return 0;
	}

	@Override
	public List<WaterElement> getExternalElements() {
		List<WaterElement> elements = new ArrayList<WaterElement>();

		if(getSociety().getCountry().getWaterSystem() 
				instanceof WaterSystem.Local) {
			WaterSystem.Local system = (WaterSystem.Local)
					getSociety().getCountry().getWaterSystem();
			for(WaterElement element : system.getElements()) {
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
		return getWaterImportPrice() * getWaterImport();
	}

	@Override
	public List<WaterElement> getInternalElements() {
		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getLocalWaterFraction() {
		if(getSocietyDemand() > 0) {
			return Math.min(1, (getWaterProduction() + getWaterFromPrivateProduction())
					/ getSocietyDemand());
		} 
		return 0;
	}

	@Override
	public double getMaxAquiferVolume() {
		return maxWaterReservoirVolume;
	}

	@Override
	public double getOperationsExpense() {
		double value = 0;
		for(WaterElement e : getInternalElements()) {
			if(e.isCoastalAccessRequired() && !isCoastalAccess()) {
				value += CurrencyUnits.convertFlow(e.getFixedOperationsExpense(), e, this);
			} else {
				value += CurrencyUnits.convertFlow(e.getTotalOperationsExpense(), e, this);
			}
		}
		return value;
	}

	@Override
	public double getRenewableWaterFraction() {
		if(getSocietyDemand() > 0) {
			return getRenewableWaterProduction() 
					/ getSocietyDemand();
		}
		return 0;
	}

	@Override
	public double getRenewableWaterProduction() {
		double renewableProduction = 0;
		for(WaterElement e : getInternalElements()) {
			if(e.getAquiferIntensityOfWaterProduction() < 1) {
				renewableProduction += WaterUnits.convertFlow(e.getWaterProduction(), e, this)
						* (1 - e.getAquiferIntensityOfWaterProduction());
			}
		}
		renewableProduction += Math.min(getAquiferRechargeRate(), 
				getWaterProduction() + getWaterFromPrivateProduction() 
				- renewableProduction);
		return renewableProduction;
	}

	/**
	 * Gets the reservoir withdrawals log.
	 *
	 * @return the reservoir withdrawals log
	 */
	public Map<Long, Double> getReservoirWithdrawalsLog() {
		return new HashMap<Long, Double>(reservoirWithdrawalsLog);
	}

	@Override
	public double getSalesRevenue() {
		return (getSocietyDemand() - getWaterFromPrivateProduction()) * getWaterDomesticPrice();
	}

	/**
	 * Gets the total society demand for water.
	 *
	 * @return the society demand
	 */
	private double getSocietyDemand() {
		return WaterUnits.convertFlow(getSociety().getTotalWaterDemand(), getSociety(), this);
	}

	@Override
	public double getTotalWaterSupply() {
		return getWaterProduction() 
				+ getWaterInDistribution()
				- getWaterOutDistribution()
				+ getWaterImport();
	}

	@Override
	public double getUnitProductionCost() {
		if(getWaterProduction() > 0) {
			return (getLifecycleExpense() + getConsumptionExpense()) 
					/ getWaterProduction();
		}
		return 0;
	}

	@Override
	public double getUnitSupplyProfit() {
		if(getTotalWaterSupply() > 0) {
			return getCashFlow() / getTotalWaterSupply();
		}
		return 0;
	}

	@Override
	public double getWaterDomesticPrice() {
		return domesticPriceModel.getUnitPrice();
	}

	/**
	 * Gets the water domestic price log.
	 *
	 * @return the water domestic price log
	 */
	public Map<Long, Double> getWaterDomesticPriceLog() {
		return new HashMap<Long, Double>(waterDomesticPriceLog);
	}

	@Override
	public double getWaterFromPrivateProduction() {
		return Math.min(getWaterReservoirVolume() - getAquiferWithdrawalsFromPublicProduction(), 
				Math.max(0, getSocietyDemand()
						+ getWaterOutDistribution()
						- getWaterInDistribution()
						- getWaterProduction()));
	}

	@Override
	public double getWaterImport() {
		return Math.max(0, getSocietyDemand()
				+ getWaterOutDistribution()
				- getWaterInDistribution()
				- getWaterProduction()
				- getWaterFromPrivateProduction());
	}

	@Override
	public double getWaterImportPrice() {
		return importPriceModel.getUnitPrice();
	}

	/**
	 * Gets the water import price log.
	 *
	 * @return the water import price log
	 */
	public Map<Long, Double> getWaterImportPriceLog() {
		return new HashMap<Long, Double>(waterImportPriceLog);
	}

	@Override
	public double getWaterInDistribution() {
		double distribution = 0;
		for(WaterElement e : getExternalElements()) {
			distribution += WaterUnits.convertFlow(e.getWaterOutput(), e, this);
		}
		return distribution;
	}

	@Override
	public double getWaterOutDistribution() {
		double distribution = 0;
		for(WaterElement e : getInternalElements()) {
			if(!getSociety().getCities().contains(
					getSociety().getCountry().getCity(e.getDestination()))) {
				distribution += WaterUnits.convertFlow(e.getWaterInput(), e, this);
			}
		}
		return distribution;
	}

	@Override
	public double getWaterOutDistributionLosses() {
		double distribution = 0;
		for(WaterElement e : getInternalElements()) {
			distribution += WaterUnits.convertFlow(e.getWaterInput() - e.getWaterOutput(), e, this);
		}
		return distribution;
	}

	@Override
	public double getWaterProduction() {
		double waterProduction = 0;
		for(WaterElement e : getInternalElements()) {
			if(e.isCoastalAccessRequired() && !isCoastalAccess()) {
				waterProduction += 0;
			} else {
				waterProduction += WaterUnits.convertFlow(e.getWaterProduction(), e, this);
			}
		}
		return waterProduction;
	}

	@Override
	public double getWaterReservoirVolume() {
		return waterReservoirVolume;
	}

	/**
	 * Gets the water reservoir volume map.
	 *
	 * @return the water reservoir volume map
	 */
	public Map<Long, Double> getWaterReservoirVolumeMap() {
		return new HashMap<Long, Double>(waterReservoirVolumeLog);
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
	public double getWaterWasted() {
		return Math.max(0, getWaterProduction() 
				+ getWaterFromPrivateProduction()
				+ getWaterInDistribution()
				- getWaterOutDistribution()
				- getSocietyDemand());
	}

	@Override
	public void initialize(long time) {
		super.initialize(time);
		waterReservoirVolume = initialWaterReservoirVolume;
		electricityConsumptionLog.clear();
		waterReservoirVolumeLog.clear();
		reservoirWithdrawalsLog.clear();
		waterDomesticPriceLog.clear();
		waterImportPriceLog.clear();
	}

	@Override
	public boolean isCoastalAccess() {
		return coastalAccess;
	}

	@Override
	public synchronized boolean removeElement(WaterElement element) {
		return elements.remove(element);
	}

	@Override
	public void tick() {
		super.tick();
		nextWaterReservoirVolume = Math.min(maxWaterReservoirVolume, 
				waterReservoirVolume + waterReservoirRechargeRate 
				- getAquiferWithdrawals());
		if(nextWaterReservoirVolume < 0) {
			throw new IllegalStateException(
					"Water reservoir volume cannot be negative.");
		}
		electricityConsumptionLog.put(time, getElectricityConsumption());
		waterReservoirVolumeLog.put(time, getWaterReservoirVolume());
		reservoirWithdrawalsLog.put(time, getAquiferWithdrawals());
		waterDomesticPriceLog.put(time, getWaterDomesticPrice());
		waterImportPriceLog.put(time, getWaterImportPrice());
	}

	@Override
	public void tock() {
		super.tock();
		waterReservoirVolume = nextWaterReservoirVolume;
	}
}
