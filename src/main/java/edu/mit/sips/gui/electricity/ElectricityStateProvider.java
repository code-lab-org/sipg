package edu.mit.sips.gui.electricity;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.gui.base.SpatialStateProvider;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * An implementation of the spatial state provider for the electricity sector.
 * 
 * @author Paul T. Grogan
 */
public class ElectricityStateProvider implements SpatialStateProvider, ElectricityUnitsOutput {
	private final ElectricityUnits electricityUnits = ElectricityUnits.TWh;
	private final TimeUnits electricityTimeUnits = TimeUnits.year;
	
	@Override
	public double getConsumption(Society society) {
		return ElectricityUnits.convertFlow(
				society.getTotalElectricityDemand(), society, this);
	}

	@Override
	public double getDistributionIn(Society society, Society origin) {
		double distribution = 0;
		if(society.getElectricitySystem() instanceof ElectricitySystem.Local) {
			ElectricitySystem.Local energySystem = (ElectricitySystem.Local) 
					society.getElectricitySystem(); 
			for(ElectricityElement e : energySystem.getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(origin.getCities().contains(origCity)) {
					distribution += ElectricityUnits.convertFlow(e.getElectricityOutput(), e, this);
				}
			}
		}
		return distribution;
	}

	@Override
	public double getDistributionOut(Society society, Society destination) {
		double distribution = 0;
		if(society.getElectricitySystem() instanceof ElectricitySystem.Local) {
			ElectricitySystem.Local energySystem = (ElectricitySystem.Local) 
					society.getElectricitySystem(); 
			for(ElectricityElement e : energySystem.getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(destination.getCities().contains(destCity)) {
					if(society.getCities().contains(destCity)) {
						// if a self-loop, only add distribution losses
						distribution += ElectricityUnits.convertFlow(
								e.getElectricityInput() - e.getElectricityOutput(), e, this);
					} else {
						distribution += ElectricityUnits.convertFlow(
								e.getElectricityInput(), e, this);
					}
				}
			}
		}
		return distribution;
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
	public List<ElectricityElement> getElements(Society society) {
		List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
		if(society.getElectricitySystem() instanceof ElectricitySystem.Local) {
			ElectricitySystem.Local energySystem = (ElectricitySystem.Local) 
					society.getElectricitySystem(); 
			for(ElectricityElement element : energySystem.getElements()) {
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
		return 0;
	}
	
	@Override
	public double getInput(InfrastructureElement element) {
		if(element instanceof ElectricityElement) {
			return ElectricityUnits.convertFlow(
					((ElectricityElement)element).getElectricityInput(), 
					(ElectricityElement)element, this);
		} else {
			return 0;
		}
	}

	@Override
	public double getNetFlow(Society society) {
		if(society.getElectricitySystem() instanceof ElectricitySystem.Local) {
			ElectricitySystem.Local energySystem = (ElectricitySystem.Local) 
					society.getElectricitySystem(); 
			return ElectricityUnits.convertFlow(
					energySystem.getElectricityOutDistribution()
					- energySystem.getElectricityInDistribution(), 
					energySystem, this);
		} 
		return 0;
	}

	@Override
	public double getOtherDistributionIn(Society society) {
		double distribution = 0;
		if(society.getElectricitySystem() instanceof ElectricitySystem.Local) {
			ElectricitySystem.Local energySystem = (ElectricitySystem.Local) 
					society.getElectricitySystem(); 
			for(ElectricityElement e : energySystem.getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(!society.getCities().contains(origCity)) {
					distribution += ElectricityUnits.convertFlow(
							e.getElectricityOutput(), e, this);
				}
			}
		}
		return distribution;
	}

	@Override
	public double getOtherDistributionOut(Society society) {
		double distribution = 0;
		if(society.getElectricitySystem() instanceof ElectricitySystem.Local) {
			ElectricitySystem.Local energySystem = (ElectricitySystem.Local) 
					society.getElectricitySystem(); 
			for(ElectricityElement e : energySystem.getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(!society.getCities().contains(destCity)) {
					distribution += ElectricityUnits.convertFlow(
							e.getElectricityInput(), e, this);
				}
			}
		}
		return distribution;
	}

	@Override
	public double getOtherProduction(Society society) {
		if(society.getElectricitySystem() instanceof ElectricitySystem.Local) {
			ElectricitySystem.Local energySystem = (ElectricitySystem.Local) 
					society.getElectricitySystem(); 
			return ElectricityUnits.convertFlow(
					energySystem.getElectricityFromPrivateProduction(), 
					energySystem, this);
		} 
		return 0;
	}

	@Override
	public String getOtherProductionLabel() {
		return "Private Production";
	}

	@Override
	public double getOutput(InfrastructureElement element) {
		if(element instanceof ElectricityElement) {
			return ElectricityUnits.convertFlow(
					((ElectricityElement) element).getElectricityOutput(), 
					(ElectricityElement) element, this);
		} else {
			return 0;
		}
	}

	@Override
	public double getProduction(InfrastructureElement element) {
		if(element instanceof ElectricityElement) {
			return ElectricityUnits.convertFlow(
					((ElectricityElement)element).getElectricityProduction(), 
					(ElectricityElement) element, this);
		} else {
			return 0;
		}
	}

	@Override
	public String getUnits() {
		return electricityUnits.getAbbreviation();
	}

	@Override
	public boolean isDistribution(InfrastructureElement element) {
		if(element instanceof ElectricityElement) {
			return ((ElectricityElement)element).getMaxElectricityInput() > 0;
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
		return false;
	}

	@Override
	public boolean isOtherProductionAllowed() {
		return true;
	}

	@Override
	public boolean isProduction(InfrastructureElement element) {
		if(element instanceof ElectricityElement) {
			return ((ElectricityElement)element).getMaxElectricityProduction() > 0;
		} else {
			return false;
		}
	}
}
