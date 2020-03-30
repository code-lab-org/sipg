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
package edu.mit.sips.core.petroleum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.sips.core.City;
import edu.mit.sips.core.base.LocalInfrastructureSystem;
import edu.mit.sips.core.price.DefaultPriceModel;
import edu.mit.sips.core.price.PriceModel;
import edu.mit.sips.units.DefaultUnits;
import edu.mit.sips.units.ElectricityUnits;
import edu.mit.sips.units.OilUnits;
import edu.mit.sips.units.TimeUnits;

/**
 * The locally-controlled implementation of the petroleum system interface.
 * 
 * @author Paul T. Grogan
 */
public class LocalPetroleumSystem  extends LocalInfrastructureSystem implements PetroleumSystem.Local {
	private static final ElectricityUnits electricityUnits = ElectricityUnits.MWh;
	private static final TimeUnits electricityTimeUnits = TimeUnits.year;
	private static final OilUnits oilUnits = OilUnits.toe;
	private static final TimeUnits oilTimeUnits = TimeUnits.year;

	private final PriceModel domesticPriceModel, importPriceModel, exportPriceModel;
	private final List<PetroleumElement> elements = 
			Collections.synchronizedList(new ArrayList<PetroleumElement>());
	private final double maxPetroleumReservoirVolume;
	private final double initialPetroleumReservoirVolume;
	private double petroleumReservoirVolume;
	private transient double nextPetroleumReservoirVolume;

	private transient final Map<Long, Double> electricityConsumptionLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> petroleumReservoirVolumeLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> petroleumWithdrawalsLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> petroleumDomesticPriceLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> petroleumImportPriceLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> petroleumExportPriceLog = 
			new HashMap<Long, Double>();
	

	/**
	 * Instantiates a new local petroleum system.
	 *
	 */
	public LocalPetroleumSystem() {
		super("Petroleum");
		this.domesticPriceModel = new DefaultPriceModel();
		this.importPriceModel = new DefaultPriceModel();
		this.exportPriceModel = new DefaultPriceModel();
		this.maxPetroleumReservoirVolume = 0;
		this.initialPetroleumReservoirVolume = 0;
	}

	/**
	 * Instantiates a new local petroleum system.
	 *
	 * @param maxPetroleumReservoirVolume the max petroleum reservoir volume
	 * @param initialPetroleumReservoirVolume the initial petroleum reservoir volume
	 * @param elements the elements
	 * @param domesticPriceModel the domestic price model
	 * @param importPriceModel the import price model
	 * @param exportPriceModel the export price model
	 */
	public LocalPetroleumSystem(double maxPetroleumReservoirVolume,
			double initialPetroleumReservoirVolume,
			Collection<? extends PetroleumElement> elements,
			PriceModel domesticPriceModel, 
			PriceModel importPriceModel, 
			PriceModel exportPriceModel) {
		super("Petroleum");

		if(maxPetroleumReservoirVolume < 0) {
			throw new IllegalArgumentException(
					"Max petroleum reservoir volume cannot be negative.");
		}
		this.maxPetroleumReservoirVolume = maxPetroleumReservoirVolume;

		if(initialPetroleumReservoirVolume < 0) {
			throw new IllegalArgumentException(
					"Initial petroleum reservoir volume cannot be negative.");
		} else if(initialPetroleumReservoirVolume > maxPetroleumReservoirVolume) {
			throw new IllegalArgumentException(
					"Initial petroleum reservoir volume cannot exceed maximum.");
		}
		this.initialPetroleumReservoirVolume = initialPetroleumReservoirVolume;

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

		if(exportPriceModel == null) {
			throw new IllegalArgumentException(
					"Export price model cannot be null.");
		}
		this.exportPriceModel = exportPriceModel;
	}
	
	@Override
	public synchronized boolean addElement(PetroleumElement element) {
		return elements.add(element);
	}
	
	@Override
	public double getConsumptionExpense() {
		return getElectricityConsumption() * DefaultUnits.convert(
				getSociety().getElectricitySystem().getElectricityDomesticPrice(),
				getSociety().getElectricitySystem().getCurrencyUnits(),
				getSociety().getElectricitySystem().getElectricityUnits(),
				getCurrencyUnits(), getElectricityUnits());
	}
	
	@Override
	public double getDistributionExpense() {
		return getPetroleumDomesticPrice() * getPetroleumInDistribution();
	}

	@Override
	public double getDistributionRevenue() {
		return getPetroleumDomesticPrice() * (getPetroleumOutDistribution()
				- getPetroleumOutDistributionLosses());
	}

	@Override
	public double getElectricityConsumption() {
		double consumption = 0;
		for(PetroleumElement e : getInternalElements()) {
			consumption += ElectricityUnits.convertFlow(e.getElectricityConsumption(), e, this);
		}
		return consumption;
	}

	/**
	 * Gets the electricity consumption map.
	 *
	 * @return the electricity consumption map
	 */
	public Map<Long, Double> getElectricityConsumptionMap() {
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
	public List<PetroleumElement> getElements() {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
		elements.addAll(getInternalElements());
		elements.addAll(getExternalElements());
		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getExportRevenue() {
		return getPetroleumExportPrice() * getPetroleumExport();
	}

	@Override
	public List<PetroleumElement> getExternalElements() {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();

		if(getSociety().getCountry().getPetroleumSystem()
				instanceof PetroleumSystem.Local) {
			PetroleumSystem.Local system = (PetroleumSystem.Local)
					getSociety().getCountry().getPetroleumSystem();
			for(PetroleumElement element : system.getElements()) {
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
		return getPetroleumImportPrice() * getPetroleumImport();
	}

	@Override
	public List<PetroleumElement> getInternalElements() {
		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getLocalPetroleumFraction() {
		if(getSocietyDemand() > 0) {
			return Math.min(1, getPetroleumProduction() 
					/ getSocietyDemand());
		}
		return 0;
	}

	@Override
	public double getMaxPetroleumReservoirVolume() {
		return maxPetroleumReservoirVolume;
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
	public double getPetroleumDomesticPrice() {
		return domesticPriceModel.getUnitPrice();
	}

	/**
	 * Gets the petroleum domestic price log.
	 *
	 * @return the petroleum domestic price log
	 */
	public Map<Long, Double> getPetroleumDomesticPriceLog() {
		return new HashMap<Long, Double>(petroleumDomesticPriceLog);
	}

	@Override
	public double getPetroleumExport() {
		return Math.max(0, getPetroleumProduction() 
				+ getPetroleumInDistribution()
				- getPetroleumOutDistribution()
				- getSocietyDemand());
	}

	@Override
	public double getPetroleumExportPrice() {
		return exportPriceModel.getUnitPrice();
	}

	/**
	 * Gets the petroleum export price log.
	 *
	 * @return the petroleum export price log
	 */
	public Map<Long, Double> getPetroleumExportPriceLog() {
		return new HashMap<Long, Double>(petroleumExportPriceLog);
	}

	@Override
	public double getPetroleumImport() {
		return Math.max(0, getSocietyDemand()
				+ getPetroleumOutDistribution()
				- getPetroleumInDistribution()
				- getPetroleumProduction());
	}			

	@Override
	public double getPetroleumImportPrice() {
		return importPriceModel.getUnitPrice();
	}

	/**
	 * Gets the petroleum import price log.
	 *
	 * @return the petroleum import price log
	 */
	public Map<Long, Double> getPetroleumImportPriceLog() {
		return new HashMap<Long, Double>(petroleumImportPriceLog);
	}

	@Override
	public double getPetroleumInDistribution() {
		double distribution = 0;
		for(PetroleumElement e : getExternalElements()) {
			distribution += OilUnits.convertFlow(e.getPetroleumOutput(), e, this);
		}
		return distribution;
	}

	@Override
	public double getPetroleumOutDistribution() {
		double distribution = 0;
		for(PetroleumElement e : getInternalElements()) {
			if(!getSociety().getCities().contains(
					getSociety().getCountry().getCity(e.getDestination()))) {
				distribution += OilUnits.convertFlow(e.getPetroleumInput(), e, this);
			}
		}
		return distribution;
	}

	@Override
	public double getPetroleumOutDistributionLosses() {
		double distribution = 0;
		for(PetroleumElement e : getInternalElements()) {
			distribution += OilUnits.convertFlow(e.getPetroleumInput(), e, this)
					- OilUnits.convertFlow(e.getPetroleumOutput(), e, this);
		}
		return distribution;
	}

	@Override
	public double getPetroleumProduction() {
		double petroleumProduction = 0;
		for(PetroleumElement e : getInternalElements()) {
			petroleumProduction += OilUnits.convertFlow(e.getPetroleumProduction(), e, this);
		}
		return petroleumProduction;
	}

	/**
	 * Gets the petroleum reservoir volume log.
	 *
	 * @return the petroleum reservoir volume log
	 */
	public Map<Long, Double> getPetroleumReservoirVolumeLog() {
		return new HashMap<Long, Double>(petroleumReservoirVolumeLog);
	}

	/**
	 * Gets the petroleum withdrawals log.
	 *
	 * @return the petroleum withdrawals log
	 */
	public Map<Long, Double> getPetroleumWithdrawalsLog() {
		return new HashMap<Long, Double>(petroleumWithdrawalsLog);
	}

	public double getReservoirLifetime() {
		return getReservoirWithdrawals() == 0 ? Double.MAX_VALUE 
				: (getReservoirVolume() / getReservoirWithdrawals());
	}

	@Override
	public double getReservoirVolume() {
		return petroleumReservoirVolume;
	}

	@Override
	public double getReservoirWithdrawals() {
		double petroleumWithdrawals = 0;
		for(PetroleumElement e : getInternalElements()) {
			petroleumWithdrawals += OilUnits.convertFlow(e.getReservoirWithdrawals(), e, this);
		}
		return petroleumWithdrawals;
	}

	@Override
	public double getSalesRevenue() {
		return getPetroleumDomesticPrice() * getSocietyDemand();
	}

	/**
	 * Gets the total society demand for petroleum.
	 *
	 * @return the society demand
	 */
	private double getSocietyDemand() {
		return OilUnits.convertFlow(getSociety().getTotalPetroleumDemand(), getSociety(), this);
	}

	@Override
	public double getTotalPetroleumSupply() {
		return getPetroleumProduction() 
				+ getPetroleumInDistribution()
				- getPetroleumOutDistribution()
				+ getPetroleumImport() 
				- getPetroleumExport();
	}

	@Override
	public double getUnitProductionCost() {
		if(getPetroleumProduction() > 0) {
			return (getLifecycleExpense() + getConsumptionExpense()) 
					/ getPetroleumProduction();
		}
		return 0;
	}

	@Override
	public double getUnitSupplyProfit() {
		if(getTotalPetroleumSupply() > 0) {
			return getCashFlow() / getTotalPetroleumSupply();
		}
		return 0;
	}

	@Override
	public void initialize(long time) {
		super.initialize(time);
		petroleumReservoirVolume = initialPetroleumReservoirVolume;
		electricityConsumptionLog.clear();
		petroleumReservoirVolumeLog.clear();
		petroleumWithdrawalsLog.clear();
		petroleumDomesticPriceLog.clear();
		petroleumImportPriceLog.clear();
		petroleumExportPriceLog.clear();
	}

	@Override
	public synchronized boolean removeElement(PetroleumElement element) {
		return elements.remove(element);
	}

	@Override
	public void tick() {
		super.tick();
		nextPetroleumReservoirVolume = Math.min(maxPetroleumReservoirVolume, 
				petroleumReservoirVolume - getReservoirWithdrawals());
		electricityConsumptionLog.put(time, getElectricityConsumption());
		petroleumReservoirVolumeLog.put(time, getReservoirVolume());
		petroleumWithdrawalsLog.put(time, getReservoirWithdrawals());
		petroleumDomesticPriceLog.put(time, getPetroleumDomesticPrice());
		petroleumImportPriceLog.put(time, getPetroleumImportPrice());
		petroleumExportPriceLog.put(time, getPetroleumExportPrice());
	}

	@Override
	public void tock() {
		super.tock();
		petroleumReservoirVolume = nextPetroleumReservoirVolume;
	}
}