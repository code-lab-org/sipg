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
package edu.mit.sips.gui.water;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.gui.base.SpatialStateProvider;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * An implementation of the spatial state provider for the water sector.
 * 
 * @author Paul T. Grogan
 */
public class WaterStateProvider implements SpatialStateProvider, WaterUnitsOutput {
	private final WaterUnits waterUnits = WaterUnits.km3;
	private final TimeUnits waterTimeUnits = TimeUnits.year;
	
	@Override
	public double getConsumption(Society society) {
		return WaterUnits.convertFlow(society.getTotalWaterDemand(), society, this);
	}

	@Override
	public double getDistributionIn(Society society, Society origin) {
		double distribution = 0;
		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			WaterSystem.Local waterSystem = (WaterSystem.Local) 
					society.getWaterSystem(); 
			for(WaterElement e : waterSystem.getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(origin.getCities().contains(origCity)) {
					distribution += WaterUnits.convertFlow(e.getWaterOutput(), e, this);
				}
			}
		}
		return distribution;
	}

	@Override
	public double getDistributionOut(Society society, Society destination) {
		double distribution = 0;
		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			WaterSystem.Local waterSystem = (WaterSystem.Local) 
					society.getWaterSystem(); 
			for(WaterElement e : waterSystem.getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(destination.getCities().contains(destCity)) {
					if(society.getCities().contains(destCity)) {
						// if a self-loop, only add distribution losses
						distribution += WaterUnits.convertFlow(e.getWaterInput() - e.getWaterOutput(), e, this);
					} else {
						distribution += WaterUnits.convertFlow(e.getWaterInput(), e, this);
					}
				}
			}
		}
		return distribution;
	}

	@Override
	public List<WaterElement> getElements(Society society) {
		List<WaterElement> elements = new ArrayList<WaterElement>();
		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			WaterSystem.Local waterSystem = (WaterSystem.Local) 
					society.getWaterSystem(); 
			for(WaterElement element : waterSystem.getElements()) {
				if(element.isExists()) elements.add(element);
			}
		}
		return elements;
	}

	@Override
	public double getExport(Society society) {
		return 0;
	}

	@Override
	public double getImport(Society society) {
		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			WaterSystem.Local waterSystem = (WaterSystem.Local) 
					society.getWaterSystem(); 
			return WaterUnits.convertFlow(waterSystem.getWaterImport(), waterSystem, this);
		} 
		return 0;
	}

	@Override
	public double getInput(InfrastructureElement element) {
		if(element instanceof WaterElement) {
			return WaterUnits.convertFlow(((WaterElement)element).getWaterInput(), 
					(WaterElement)element, this);
		} else {
			return 0;
		}
	}

	@Override
	public double getNetFlow(Society society) {
		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			WaterSystem.Local waterSystem = (WaterSystem.Local) 
					society.getWaterSystem(); 
			return WaterUnits.convertFlow(- waterSystem.getWaterImport()
					+ waterSystem.getWaterOutDistribution()
					- waterSystem.getWaterInDistribution(), waterSystem, this);
		} 
		return 0;
	}
	
	@Override
	public double getOtherDistributionIn(Society society) {
		double distribution = 0;
		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			WaterSystem.Local waterSystem = (WaterSystem.Local) 
					society.getWaterSystem(); 
			for(WaterElement e : waterSystem.getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(!society.getCities().contains(origCity)) {
					distribution += WaterUnits.convertFlow(e.getWaterOutput(), e, this);
				}
			}
		}
		return distribution;
	}

	@Override
	public double getOtherDistributionOut(Society society) {
		double distribution = 0;
		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			WaterSystem.Local waterSystem = (WaterSystem.Local) 
					society.getWaterSystem(); 
			for(WaterElement e : waterSystem.getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(!society.getCities().contains(destCity)) {
					distribution += WaterUnits.convertFlow(e.getWaterInput(), e, this);
				}
			}
		}
		return distribution;
	}

	@Override
	public double getOtherProduction(Society society) {
		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			WaterSystem.Local waterSystem = (WaterSystem.Local) 
					society.getWaterSystem(); 
			return WaterUnits.convertFlow(waterSystem.getWaterFromPrivateProduction(), waterSystem, this);
		} 
		return 0;
	}

	@Override
	public String getOtherProductionLabel() {
		return "Private Production";
	}

	@Override
	public double getOutput(InfrastructureElement element) {
		if(element instanceof WaterElement) {
			return WaterUnits.convertFlow(((WaterElement)element).getWaterOutput(), 
					((WaterElement)element), this);
		} else {
			return 0;
		}
	}

	@Override
	public double getProduction(InfrastructureElement element) {
		if(element instanceof WaterElement) {
			return WaterUnits.convertFlow(((WaterElement)element).getWaterProduction(), 
					((WaterElement)element), this);
		} else {
			return 0;
		}
	}

	@Override
	public String getUnits() {
		return waterUnits.getAbbreviation();
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
	public boolean isDistribution(InfrastructureElement element) {
		if(element instanceof WaterElement) {
			return ((WaterElement)element).getMaxWaterInput() > 0;
		} else {
			return false;
		}
	}

	@Override
	public boolean isExportAllowed() {
		return false;
	}

	@Override
	public boolean isImportAllowed() {
		return true;
	}

	@Override
	public boolean isOtherProductionAllowed() {
		return true;
	}

	@Override
	public boolean isProduction(InfrastructureElement element) {
		if(element instanceof WaterElement) {
			return ((WaterElement)element).getMaxWaterProduction() > 0;
		} else {
			return false;
		}
	}
}
