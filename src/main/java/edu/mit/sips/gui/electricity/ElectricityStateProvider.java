package edu.mit.sips.gui.electricity;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.base.InfrastructureElement;
import edu.mit.sips.core.electricity.ElectricityElement;
import edu.mit.sips.core.electricity.ElectricitySystem;
import edu.mit.sips.gui.SpatialStateProvider;
import edu.mit.sips.sim.util.ElectricityUnits;
import edu.mit.sips.sim.util.ElectricityUnitsOutput;
import edu.mit.sips.sim.util.TimeUnits;

/**
 * The Class ElectricityStateProvider.
 */
public class ElectricityStateProvider implements SpatialStateProvider, ElectricityUnitsOutput {
	private final ElectricityUnits electricityUnits = ElectricityUnits.TWh;
	private final TimeUnits electricityTimeUnits = TimeUnits.year;
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getConsumption(edu.mit.sips.Society)
	 */
	@Override
	public double getConsumption(Society society) {
		return ElectricityUnits.convertFlow(
				society.getTotalElectricityDemand(), society, this);
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getDistributionIn(edu.mit.sips.Society, edu.mit.sips.Society)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getDistributionOut(edu.mit.sips.Society, edu.mit.sips.Society)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityTimeUnits()
	 */
	@Override
	public TimeUnits getElectricityTimeUnits() {
		return electricityTimeUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.ElectricityUnitsOutput#getElectricityUnits()
	 */
	@Override
	public ElectricityUnits getElectricityUnits() {
		return electricityUnits;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getElements(edu.mit.sips.Society)
	 */
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
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getInput(edu.mit.sips.InfrastructureElement)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getNetFlow(edu.mit.sips.Society)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherDistributionIn(edu.mit.sips.Society)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherDistributionOut(edu.mit.sips.Society)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherProduction(edu.mit.sips.Society)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherProductionLabel()
	 */
	@Override
	public String getOtherProductionLabel() {
		return "Private Production";
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getOutput(edu.mit.sips.InfrastructureElement)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getProduction(edu.mit.sips.InfrastructureElement)
	 */
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getUnits()
	 */
	@Override
	public String getUnits() {
		return electricityUnits.getAbbreviation();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#isDistribution(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public boolean isDistribution(InfrastructureElement element) {
		if(element instanceof ElectricityElement) {
			return ((ElectricityElement)element).getMaxElectricityInput() > 0;
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
		return false;
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
		if(element instanceof ElectricityElement) {
			return ((ElectricityElement)element).getMaxElectricityProduction() > 0;
		} else {
			return false;
		}
	}
}
