package edu.mit.sips.gui;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystem;

/**
 * The Class WaterStateProvider.
 */
public class WaterStateProvider implements SpatialStateProvider {
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getConsumption(edu.mit.sips.Society)
	 */
	@Override
	public double getConsumption(Society society) {
		return society.getTotalWaterDemand();
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
					distribution += e.getWaterOutput();
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
						distribution += e.getWaterInput() - e.getWaterOutput();
					} else {
						distribution += e.getWaterInput();
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
			return waterSystem.getWaterImport();
		} 
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getInput(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public double getInput(InfrastructureElement element) {
		if(element instanceof WaterElement) {
			return ((WaterElement)element).getWaterInput();
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
			return - waterSystem.getWaterImport()
					+ waterSystem.getWaterOutDistribution()
					- waterSystem.getWaterInDistribution();
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
					distribution += e.getWaterOutput();
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
					distribution += e.getWaterInput();
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
			return waterSystem.getWaterFromArtesianWell();
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
			return ((WaterElement)element).getWaterOutput();
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
			return ((WaterElement)element).getWaterProduction();
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getUnits()
	 */
	@Override
	public String getUnits() {
		return "m^3";
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
