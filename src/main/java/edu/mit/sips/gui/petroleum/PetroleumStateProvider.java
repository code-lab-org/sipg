package edu.mit.sips.gui.petroleum;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.core.petroleum.PetroleumElement;
import edu.mit.sips.core.petroleum.PetroleumSystem;
import edu.mit.sips.gui.base.SpatialStateProvider;
import edu.mit.sips.sim.util.OilUnits;
import edu.mit.sips.sim.util.OilUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * An implementation of the spatial state provider for the petroleum sector.
 * 
 * @author Paul T. Grogan
 */
public class PetroleumStateProvider implements SpatialStateProvider, OilUnitsOutput {
	private final OilUnits oilUnits = OilUnits.Mtoe;
	private final TimeUnits oilTimeUnits = TimeUnits.year;
	
	@Override
	public double getConsumption(Society society) {
		return OilUnits.convertFlow(
				society.getTotalPetroleumDemand(), society, this);
	}

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
		if(element instanceof PetroleumElement) {
			return OilUnits.convertFlow(
					((PetroleumElement) element).getPetroleumOutput(),
					(PetroleumElement) element, this);
		} else {
			return 0;
		}
	}

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

	@Override
	public String getUnits() {
		return oilUnits.getAbbreviation();
	}

	@Override
	public boolean isDistribution(InfrastructureElement element) {
		if(element instanceof PetroleumElement) {
			return ((PetroleumElement)element).getMaxPetroleumInput() > 0;
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
		if(element instanceof PetroleumElement) {
			return ((PetroleumElement)element).getMaxPetroleumProduction() > 0;
		} else {
			return false;
		}
	}

	@Override
	public OilUnits getOilUnits() {
		return oilUnits;
	}

	@Override
	public TimeUnits getOilTimeUnits() {
		return oilTimeUnits;
	}
}
