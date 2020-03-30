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
package edu.mit.sips.gui.agriculture;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.gui.base.SpatialStateProvider;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.FoodUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * An implementation of the spatial state provider for the agriculture sector.
 * 
 * @author Paul T. Grogan
 */
public class AgricultureStateProvider implements SpatialStateProvider, FoodUnitsOutput {
	private final FoodUnits foodUnits = FoodUnits.EJ;
	private final TimeUnits foodTimeUnits = TimeUnits.year;
	
	@Override
	public double getConsumption(Society society) {
		return FoodUnits.convertFlow(society.getTotalFoodDemand(), society, this);
	}

	@Override
	public double getDistributionIn(Society society, Society origin) {
		double distribution = 0;
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			for(AgricultureElement e : agricultureSystem.getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(origin.getCities().contains(origCity)) {
					distribution += FoodUnits.convertFlow(e.getFoodOutput(), e, this);
				}
			}
		}
		return distribution;
	}

	@Override
	public double getDistributionOut(Society society, Society destination) {
		double distribution = 0;
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			for(AgricultureElement e : agricultureSystem.getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(destination.getCities().contains(destCity)) {
					if(society.getCities().contains(destCity)) {
						// if a self-loop, only add distribution losses
						distribution += FoodUnits.convertFlow(e.getFoodInput() - e.getFoodOutput(), e, this);
					} else {
						distribution += FoodUnits.convertFlow(e.getFoodInput(), e, this);
					}
				}
			}
		}
		return distribution;
	}

	@Override
	public List<AgricultureElement> getElements(Society society) {
		List<AgricultureElement> elements = new ArrayList<AgricultureElement>();
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			for(AgricultureElement element : agricultureSystem.getElements()) {
				if(element.isExists()) elements.add(element);
			}
		}
		return elements;
	}

	@Override
	public double getExport(Society society) {
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			return FoodUnits.convertFlow(
					agricultureSystem.getFoodExport(), agricultureSystem, this);
		}
		return 0;
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
	public double getImport(Society society) {
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			return FoodUnits.convertFlow(agricultureSystem.getFoodImport(), agricultureSystem, this);
		}
		return 0;
	}
	
	@Override
	public double getInput(InfrastructureElement element) {
		if(element instanceof AgricultureElement) {
			AgricultureElement agElement = (AgricultureElement) element;
			return FoodUnits.convertFlow(agElement.getFoodInput(), agElement, this);
		} else {
			return 0;
		}
	}

	@Override
	public double getNetFlow(Society society) {
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			return FoodUnits.convertFlow(agricultureSystem.getFoodExport()
					- agricultureSystem.getFoodImport()
					+ agricultureSystem.getFoodOutDistribution()
					- agricultureSystem.getFoodInDistribution(), agricultureSystem, this);
		} 
		return 0;
	}

	@Override
	public double getOtherDistributionIn(Society society) {
		double distribution = 0;
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			for(AgricultureElement e : agricultureSystem.getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(!society.getCities().contains(origCity)) {
					distribution += FoodUnits.convertFlow(e.getFoodOutput(), e, this);
				}
			}
		}
		return distribution;
	}

	@Override
	public double getOtherDistributionOut(Society society) {
		double distribution = 0;
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			for(AgricultureElement e : agricultureSystem.getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(!society.getCities().contains(destCity)) {
					distribution += FoodUnits.convertFlow(e.getFoodInput(), e, this);
				}
			}
		}
		return distribution;
	}

	@Override
	public double getOtherProduction(Society society) {
		return 0;
	}

	@Override
	public String getOtherProductionLabel() {
		return "";
	}

	@Override
	public double getOutput(InfrastructureElement element) {
		if(element instanceof AgricultureElement) {
			AgricultureElement agElement = (AgricultureElement)element;
			return FoodUnits.convertFlow(agElement.getFoodOutput(), agElement, this);
		} else {
			return 0;
		}
	}

	@Override
	public double getProduction(InfrastructureElement element) {
		if(element instanceof AgricultureElement) {
			AgricultureElement agElement = (AgricultureElement)element;
			return FoodUnits.convertFlow(agElement.getFoodProduction(), agElement, this);
		} else {
			return 0;
		}
	}

	@Override
	public String getUnits() {
		return foodUnits.getAbbreviation() + "/"
				+ foodTimeUnits.getAbbreviation();
	}

	@Override
	public boolean isDistribution(InfrastructureElement element) {
		if(element instanceof AgricultureElement) {
			return ((AgricultureElement)element).getMaxFoodInput() > 0;
		} else {
			return false;
		}
	}

	@Override
	public boolean isExportAllowed() {
		return true;
	}

	@Override
	public boolean isImportAllowed() {
		return true;
	}

	@Override
	public boolean isOtherProductionAllowed() {
		return false;
	}

	@Override
	public boolean isProduction(InfrastructureElement element) {
		if(element instanceof AgricultureElement) {
			return ((AgricultureElement)element).getMaxLandArea() > 0;
		} else {
			return false;
		}
	}
}
