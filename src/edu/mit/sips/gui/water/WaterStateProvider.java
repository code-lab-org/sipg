package edu.mit.sips.gui.water;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystem;
import edu.mit.sips.gui.SpatialStateProvider;
import edu.mit.sips.sim.util.TimeUnits;
import edu.mit.sips.sim.util.WaterUnits;
import edu.mit.sips.sim.util.WaterUnitsOutput;

/**
 * The Class WaterStateProvider.
 */
public class WaterStateProvider implements SpatialStateProvider, WaterUnitsOutput {
	private final WaterUnits waterUnits = WaterUnits.MCM;
	private final TimeUnits waterTimeUnits = TimeUnits.year;
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getConsumption(edu.mit.sips.Society)
	 */
	@Override
	public double getConsumption(Society society) {
		return WaterUnits.convertFlow(society.getTotalWaterDemand(), society, this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getDistributionIn(edu.mit.sips.Society, edu.mit.sips.Society)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getDistributionOut(edu.mit.sips.Society, edu.mit.sips.Society)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getElements(edu.mit.sips.Society)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getExport(edu.mit.sips.Society)
	 */
	@Override
	public double getExport(Society society) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getImport(edu.mit.sips.Society)
	 */
	@Override
	public double getImport(Society society) {
		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			WaterSystem.Local waterSystem = (WaterSystem.Local) 
					society.getWaterSystem(); 
			return WaterUnits.convertFlow(waterSystem.getWaterImport(), waterSystem, this);
		} 
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getInput(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public double getInput(InfrastructureElement element) {
		if(element instanceof WaterElement) {
			return WaterUnits.convertFlow(((WaterElement)element).getWaterInput(), 
					(WaterElement)element, this);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getNetFlow(edu.mit.sips.Society)
	 */
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
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherDistributionIn(edu.mit.sips.Society)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherDistributionOut(edu.mit.sips.Society)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherProduction(edu.mit.sips.Society)
	 */
	@Override
	public double getOtherProduction(Society society) {
		if(society.getWaterSystem() instanceof WaterSystem.Local) {
			WaterSystem.Local waterSystem = (WaterSystem.Local) 
					society.getWaterSystem(); 
			return WaterUnits.convertFlow(waterSystem.getWaterFromArtesianWell(), waterSystem, this);
		} 
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherProductionLabel()
	 */
	@Override
	public String getOtherProductionLabel() {
		return "Artesian Well";
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getOutput(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public double getOutput(InfrastructureElement element) {
		if(element instanceof WaterElement) {
			return WaterUnits.convertFlow(((WaterElement)element).getWaterOutput(), 
					((WaterElement)element), this);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getProduction(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public double getProduction(InfrastructureElement element) {
		if(element instanceof WaterElement) {
			return WaterUnits.convertFlow(((WaterElement)element).getWaterProduction(), 
					((WaterElement)element), this);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getUnits()
	 */
	@Override
	public String getUnits() {
		return waterUnits.getAbbreviation();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterTimeUnits()
	 */
	@Override
	public TimeUnits getWaterTimeUnits() {
		return waterTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.WaterUnitsOutput#getWaterUnits()
	 */
	@Override
	public WaterUnits getWaterUnits() {
		return waterUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#isDistribution(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public boolean isDistribution(InfrastructureElement element) {
		if(element instanceof WaterElement) {
			return ((WaterElement)element).getMaxWaterInput() > 0;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#isExportAllowed()
	 */
	@Override
	public boolean isExportAllowed() {
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#isImportAllowed()
	 */
	@Override
	public boolean isImportAllowed() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#isOtherProductionAllowed()
	 */
	@Override
	public boolean isOtherProductionAllowed() {
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#isProduction(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public boolean isProduction(InfrastructureElement element) {
		if(element instanceof WaterElement) {
			return ((WaterElement)element).getMaxWaterProduction() > 0;
		} else {
			return false;
		}
	}
}
