package edu.mit.sips.gui;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.energy.ElectricityElement;
import edu.mit.sips.core.energy.EnergySystem;

/**
 * The Class ElectricityStateProvider.
 */
public class ElectricityStateProvider implements SpatialStateProvider {
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getConsumption(edu.mit.sips.Society)
	 */
	@Override
	public double getConsumption(Society society) {
		return society.getTotalElectricityDemand();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getDistributionIn(edu.mit.sips.Society, edu.mit.sips.Society)
	 */
	@Override
	public double getDistributionIn(Society society, Society origin) {
		double distribution = 0;
		if(society.getEnergySystem() instanceof EnergySystem.Local) {
			EnergySystem.Local energySystem = (EnergySystem.Local) 
					society.getEnergySystem(); 
			for(ElectricityElement e : energySystem.getElectricitySystem().getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(origin.getCities().contains(origCity)) {
					distribution += e.getElectricityOutput();
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
		if(society.getEnergySystem() instanceof EnergySystem.Local) {
			EnergySystem.Local energySystem = (EnergySystem.Local) 
					society.getEnergySystem(); 
			for(ElectricityElement e : energySystem.getElectricitySystem().getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(destination.getCities().contains(destCity)) {
					if(society.getCities().contains(destCity)) {
						// if a self-loop, only add distribution losses
						distribution += e.getElectricityInput() - e.getElectricityOutput();
					} else {
						distribution += e.getElectricityInput();
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
	public List<ElectricityElement> getElements(Society society) {
		List<ElectricityElement> elements = new ArrayList<ElectricityElement>();
		if(society.getEnergySystem() instanceof EnergySystem.Local) {
			EnergySystem.Local energySystem = (EnergySystem.Local) 
					society.getEnergySystem(); 
			for(ElectricityElement element : energySystem.getElectricitySystem().getElements()) {
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
			return ((ElectricityElement)element).getElectricityInput();
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getNetFlow(edu.mit.sips.Society)
	 */
	@Override
	public double getNetFlow(Society society) {
		if(society.getEnergySystem() instanceof EnergySystem.Local) {
			EnergySystem.Local energySystem = (EnergySystem.Local) 
					society.getEnergySystem(); 
			return energySystem.getElectricitySystem().getElectricityOutDistribution()
					- energySystem.getElectricitySystem().getElectricityInDistribution();
		} 
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherDistributionIn(edu.mit.sips.Society)
	 */
	@Override
	public double getOtherDistributionIn(Society society) {
		double distribution = 0;
		if(society.getEnergySystem() instanceof EnergySystem.Local) {
			EnergySystem.Local energySystem = (EnergySystem.Local) 
					society.getEnergySystem(); 
			for(ElectricityElement e : energySystem.getElectricitySystem().getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(!society.getCities().contains(origCity)) {
					distribution += e.getElectricityOutput();
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
		if(society.getEnergySystem() instanceof EnergySystem.Local) {
			EnergySystem.Local energySystem = (EnergySystem.Local) 
					society.getEnergySystem(); 
			for(ElectricityElement e : energySystem.getElectricitySystem().getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(!society.getCities().contains(destCity)) {
					distribution += e.getElectricityInput();
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
		if(society.getEnergySystem() instanceof EnergySystem.Local) {
			EnergySystem.Local energySystem = (EnergySystem.Local) 
					society.getEnergySystem(); 
			return energySystem.getElectricitySystem().getElectricityFromBurningPetroleum();
		} 
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherProductionLabel()
	 */
	@Override
	public String getOtherProductionLabel() {
		return "Petroleum Burn";
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getOutput(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public double getOutput(InfrastructureElement element) {
		if(element instanceof ElectricityElement) {
			return ((ElectricityElement)element).getElectricityOutput();
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
			return ((ElectricityElement)element).getElectricityProduction();
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getUnits()
	 */
	@Override
	public String getUnits() {
		return "MWh";
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
