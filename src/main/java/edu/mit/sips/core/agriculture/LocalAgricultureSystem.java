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
package edu.mit.sips.core.agriculture;

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
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;

/**
 * The locally-controlled implementation of the agriculture system-of-systems interface.
 * 
 * @author Paul T. Grogan
 */
public class LocalAgricultureSystem extends LocalInfrastructureSystem implements AgricultureSystem.Local {
	private static final WaterUnits waterUnits = WaterUnits.m3;
	private static final TimeUnits waterTimeUnits = TimeUnits.year;
	private static final FoodUnits foodUnits = FoodUnits.GJ;
	private static final TimeUnits foodTimeUnits = TimeUnits.year;

	private final PriceModel domesticPriceModel, importPriceModel, exportPriceModel;
	private final double arableLandArea;	
	private final double laborParticipationRate;
	private final List<AgricultureElement> elements = 
			Collections.synchronizedList(new ArrayList<AgricultureElement>());

	private transient final Map<Long, Double> waterConsumptionLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> totalFoodSupplyLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> foodProductionLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> foodDomesticPriceLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> foodImportPriceLog = 
			new HashMap<Long, Double>();
	private transient final Map<Long, Double> foodExportPriceLog = 
			new HashMap<Long, Double>();
	
	/**
	 * Instantiates a new local agriculture system.
	 */
	public LocalAgricultureSystem() {
		super("Agriculture");
		this.domesticPriceModel = new DefaultPriceModel();
		this.importPriceModel = new DefaultPriceModel();
		this.exportPriceModel = new DefaultPriceModel();
		this.arableLandArea = 0;
		this.laborParticipationRate = 1;
	}
	
	/**
	 * Instantiates a new local agriculture system.
	 *
	 * @param arableLandArea the arable land area
	 * @param laborParticipationRate the labor participation rate
	 * @param elements the elements
	 * @param domesticPriceModel the domestic price model
	 * @param importPriceModel the import price model
	 * @param exportPriceModel the export price model
	 */
	public LocalAgricultureSystem(double arableLandArea, double laborParticipationRate,
			Collection<? extends AgricultureElement> elements, 
			PriceModel domesticPriceModel, 
			PriceModel importPriceModel, 
			PriceModel exportPriceModel) {
		super("Agriculture");

		if(arableLandArea < 0) {
			throw new IllegalArgumentException(
					"Arable land area cannot be negative.");
		}
		this.arableLandArea = arableLandArea;

		if(laborParticipationRate < 0 || laborParticipationRate > 1) {
			throw new IllegalArgumentException(
					"Labor participation rate must be between 0 and 1.");
		}
		this.laborParticipationRate = laborParticipationRate;

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
	public synchronized boolean addElement(AgricultureElement element) {
		return elements.add(element);
	}

	@Override
	public double getArableLandArea() {
		return arableLandArea;
	}

	@Override
	public double getConsumptionExpense() {
		return getWaterConsumption() * DefaultUnits.convert(
				getSociety().getWaterSystem().getWaterDomesticPrice(),
				getSociety().getWaterSystem().getCurrencyUnits(), 
				getSociety().getWaterSystem().getWaterUnits(),
				getCurrencyUnits(), getWaterUnits());
	}

	@Override
	public double getDistributionExpense() {
		return getFoodDomesticPrice() * getFoodInDistribution();
	}

	@Override
	public double getDistributionRevenue() {
		return getFoodDomesticPrice() * (getFoodOutDistribution() 
				- getFoodOutDistributionLosses());
	}

	@Override
	public List<AgricultureElement> getElements() {
		List<AgricultureElement> elements = new ArrayList<AgricultureElement>();
		elements.addAll(getInternalElements());
		elements.addAll(getExternalElements());
		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getExportRevenue() {
		return getFoodExportPrice() * getFoodExport();
	}

	@Override
	public List<AgricultureElement> getExternalElements() {
		List<AgricultureElement> elements = new ArrayList<AgricultureElement>();

		if(getSociety().getCountry().getAgricultureSystem() 
				instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local system = (AgricultureSystem.Local)
					getSociety().getCountry().getAgricultureSystem();
			for(AgricultureElement element : system.getElements()) {
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
	public double getFoodDomesticPrice() {
		return domesticPriceModel.getUnitPrice();
	}

	/**
	 * Gets the food domestic price log.
	 *
	 * @return the food domestic price log
	 */
	public Map<Long, Double> getFoodDomesticPriceLog() {
		return new HashMap<Long, Double>(foodDomesticPriceLog);
	}

	@Override
	public double getFoodExport() {
		return Math.max(0, getFoodProduction() 
				+ getFoodInDistribution()
				- getFoodOutDistribution()
				- getSocietyDemand());
	}

	@Override
	public double getFoodExportPrice() {
		return exportPriceModel.getUnitPrice();
	}

	/**
	 * Gets the food export price log.
	 *
	 * @return the food export price log
	 */
	public Map<Long, Double> getFoodExportPriceLog() {
		return new HashMap<Long, Double>(foodExportPriceLog);
	}

	@Override
	public double getFoodImport() {
		return Math.max(0, getSocietyDemand()
				+ getFoodOutDistribution() 
				- getFoodInDistribution()
				- getFoodProduction());
	}

	@Override
	public double getFoodImportPrice() {
		return importPriceModel.getUnitPrice();
	}

	/**
	 * Gets the food import price log.
	 *
	 * @return the food import price log
	 */
	public Map<Long, Double> getFoodImportPriceLog() {
		return new HashMap<Long, Double>(foodImportPriceLog);
	}

	@Override
	public double getFoodInDistribution() {
		double distribution = 0;
		for(AgricultureElement e : getExternalElements()) {
			distribution += FoodUnits.convertFlow(e.getFoodOutput(), e, this);
		}
		return distribution;
	}

	@Override
	public double getFoodOutDistribution() {
		double distribution = 0;
		for(AgricultureElement e : getInternalElements()) {
			if(!getSociety().getCities().contains(
					getSociety().getCountry().getCity(e.getDestination()))) {
				distribution += FoodUnits.convertFlow(e.getFoodInput(), e, this);
			}
		}
		return distribution;
	}
	
	@Override
	public double getFoodOutDistributionLosses() {
		double distribution = 0;
		for(AgricultureElement e : getInternalElements()) {
			distribution += FoodUnits.convertFlow(e.getFoodInput() - e.getFoodOutput(), e, this);
		}
		return distribution;
	}
	
	@Override
	public double getFoodProduction() {
		double foodProduction = 0;
		for(AgricultureElement e : getInternalElements()) {
			foodProduction += FoodUnits.convertFlow(e.getFoodProduction(), e, this);
		}
		return foodProduction;
	}
	
	/**
	 * Gets the food production log.
	 *
	 * @return the food production log
	 */
	public Map<Long, Double> getFoodProductionLog() {
		return new HashMap<Long, Double>(foodProductionLog);
	}

	@Override
	public double getFoodSecurity() {
		return getTotalFoodSupply() == 0 ? 1 
				: (getFoodProduction() / getTotalFoodSupply());
	}

	@Override
	public TimeUnits getFoodTimeUnits() {
		return foodTimeUnits;
	}

	@Override
	public FoodUnits getFoodUnits() {
		return foodUnits;
	}

	@Override
	public double getImportExpense() {
		return getFoodImportPrice() * getFoodImport();
	}

	@Override
	public List<AgricultureElement> getInternalElements() {
		return Collections.unmodifiableList(elements);
	}

	@Override
	public double getLaborParticipationRate() {
		return laborParticipationRate;
	}

	@Override
	public long getLaborUsed() {
		long value = 0;
		for(AgricultureElement e : getInternalElements()) {
			value += e.getLaborIntensityOfLandUsed() * e.getLandArea();
		}
		return value;
	}

	@Override
	public double getLandAreaUsed() {
		double landAreaUsed = 0;
		for(AgricultureElement e : getInternalElements()) {
			landAreaUsed += e.getLandArea();
		}
		return landAreaUsed;
	}

	@Override
	public double getLocalFoodFraction() {
		if(getSocietyDemand() > 0) {
			return Math.min(1, getFoodProduction() 
					/ getSocietyDemand());
		} 
		return 0;
	}

	@Override
	public double getLocalFoodSupply() {
		return getFoodProduction() 
				+ getFoodInDistribution() 
				- getFoodOutDistribution();
	}

	@Override
	public double getSalesRevenue() {
		return getFoodDomesticPrice() * getSocietyDemand();
	}

	/**
	 * Gets the society demand.
	 *
	 * @return the society demand
	 */
	private double getSocietyDemand() {
		return FoodUnits.convertFlow(getSociety().getTotalFoodDemand(), getSociety(), this);
	}

	@Override
	public double getTotalFoodSupply() {
		return getLocalFoodSupply() + getFoodImport() - getFoodExport();
	}

	/**
	 * Gets the total food supply log.
	 *
	 * @return the total food supply log
	 */
	public Map<Long, Double> getTotalFoodSupplyLog() {
		return new HashMap<Long, Double>(totalFoodSupplyLog);
	}

	@Override
	public double getUnitProductionCost() {
		if(getFoodProduction() > 0) {
			return (getLifecycleExpense() + getConsumptionExpense()) 
					/ getFoodProduction();
		}
		return 0;
	}

	@Override
	public double getUnitSupplyProfit() {
		if(getTotalFoodSupply() > 0) {
			return getCashFlow() / getTotalFoodSupply();
		}
		return 0;
	}

	@Override
	public double getWaterConsumption() {
		double waterConsumption = 0;
		for(AgricultureElement e : getInternalElements()) {
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
		waterConsumptionLog.clear();
		totalFoodSupplyLog.clear();
		foodProductionLog.clear();
		foodDomesticPriceLog.clear();
		foodImportPriceLog.clear();
		foodExportPriceLog.clear();
	}

	@Override
	public synchronized boolean removeElement(AgricultureElement element) {
		return elements.remove(element);
	}

	@Override
	public void tick() {
		super.tick();
		waterConsumptionLog.put(time, getWaterConsumption());
		totalFoodSupplyLog.put(time, getTotalFoodSupply());
		foodProductionLog.put(time, getFoodProduction());
		foodDomesticPriceLog.put(time, getFoodDomesticPrice());
		foodImportPriceLog.put(time, getFoodImportPrice());
		foodExportPriceLog.put(time, getFoodExportPrice());
	}
}