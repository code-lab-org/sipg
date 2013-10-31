package edu.mit.sips.gui.petroleum;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.gui.SpatialStateProvider;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class PetroleumStateProvider.
 */
public class PetroleumStateProvider implements SpatialStateProvider, OilUnitsOutput {
	private final OilUnits oilUnits = OilUnits.Btoe;
	private final TimeUnits oilTimeUnits = TimeUnits.year;
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getConsumption(edu.mit.sips.Society)
	 */
	@Override
	public double getConsumption(Society society) {
		return OilUnits.convertFlow(
				society.getTotalPetroleumDemand(), society, this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getDistributionIn(edu.mit.sips.Society, edu.mit.sips.Society)
	 */
	@Override
	public double getDistributionIn(Society society, Society origin) {
		double distribution = 0;
		if(society.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			PetroleumSystem.Local energySystem = (PetroleumSystem.Local) 
					society.getPetroleumSystem(); 
			for(PetroleumElement e : energySystem.getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(origin.getCities().contains(origCity)) {
					distribution += OilUnits.convertFlow(
							e.getPetroleumOutput(), e, this);
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
		if(society.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			PetroleumSystem.Local energySystem = (PetroleumSystem.Local) 
					society.getPetroleumSystem(); 
			for(PetroleumElement e : energySystem.getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(destination.getCities().contains(destCity)) {
					if(society.getCities().contains(destCity)) {
						// if a self-loop, only add distribution losses
						distribution += OilUnits.convertFlow(
								e.getPetroleumInput() - e.getPetroleumOutput(), e, this);
					} else {
						distribution += OilUnits.convertFlow(
								e.getPetroleumInput(), e, this);
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
	public List<PetroleumElement> getElements(Society society) {
		List<PetroleumElement> elements = new ArrayList<PetroleumElement>();
		if(society.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			PetroleumSystem.Local energySystem = (PetroleumSystem.Local) 
					society.getPetroleumSystem(); 
			for(PetroleumElement element : energySystem.getElements()) {
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
		if(society.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			PetroleumSystem.Local energySystem = (PetroleumSystem.Local) 
					society.getPetroleumSystem(); 
		return OilUnits.convertFlow(
				energySystem.getPetroleumExport(), energySystem, this);
		} 
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getImport(edu.mit.sips.Society)
	 */
	@Override
	public double getImport(Society society) {
		if(society.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			PetroleumSystem.Local energySystem = (PetroleumSystem.Local) 
					society.getPetroleumSystem(); 
		return OilUnits.convertFlow(
				energySystem.getPetroleumImport(), energySystem, this);
		} 
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getInput(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public double getInput(InfrastructureElement element) {
		if(element instanceof PetroleumElement) {
			return OilUnits.convertFlow(
					((PetroleumElement)element).getPetroleumInput(), 
					(PetroleumElement) element, this);
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getNetFlow(edu.mit.sips.Society)
	 */
	@Override
	public double getNetFlow(Society society) {
		if(society.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			PetroleumSystem.Local energySystem = (PetroleumSystem.Local) 
					society.getPetroleumSystem(); 
		return OilUnits.convertFlow(energySystem.getPetroleumExport()
				- energySystem.getPetroleumImport()
				+ energySystem.getPetroleumOutDistribution()
				- energySystem.getPetroleumInDistribution(),
				energySystem, this);
		} 
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherDistributionIn(edu.mit.sips.Society)
	 */
	@Override
	public double getOtherDistributionIn(Society society) {
		double distribution = 0;
		if(society.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			PetroleumSystem.Local energySystem = (PetroleumSystem.Local) 
					society.getPetroleumSystem(); 
			for(PetroleumElement e : energySystem.getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(!society.getCities().contains(origCity)) {
					distribution += OilUnits.convertFlow(
							e.getPetroleumOutput(), e, this);
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
		if(society.getPetroleumSystem() instanceof PetroleumSystem.Local) {
			PetroleumSystem.Local energySystem = (PetroleumSystem.Local) 
					society.getPetroleumSystem(); 
			for(PetroleumElement e : energySystem.getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(!society.getCities().contains(destCity)) {
					distribution += OilUnits.convertFlow(
							e.getPetroleumInput(), e, this);
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
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherProductionLabel()
	 */
	@Override
	public String getOtherProductionLabel() {
		return "";
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getOutput(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public double getOutput(InfrastructureElement element) {
		if(element instanceof PetroleumElement) {
			return OilUnits.convertFlow(
					((PetroleumElement) element).getPetroleumOutput(),
					(PetroleumElement) element, this);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getProduction(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public double getProduction(InfrastructureElement element) {
		if(element instanceof PetroleumElement) {
			return OilUnits.convertFlow(
					((PetroleumElement) element).getPetroleumProduction(),
					(PetroleumElement) element, this);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getUnits()
	 */
	@Override
	public String getUnits() {
		return oilUnits.getAbbreviation();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#isDistribution(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public boolean isDistribution(InfrastructureElement element) {
		if(element instanceof PetroleumElement) {
			return ((PetroleumElement)element).getMaxPetroleumInput() > 0;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#isExportAllowed()
	 */
	@Override
	public boolean isExportAllowed() {
		return true;
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
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#isProduction(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public boolean isProduction(InfrastructureElement element) {
		if(element instanceof PetroleumElement) {
			return ((PetroleumElement)element).getMaxPetroleumProduction() > 0;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilUnits()
	 */
	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.OilUnitsOutput#getOilTimeUnits()
	 */
	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}
}
