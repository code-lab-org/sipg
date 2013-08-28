package edu.mit.sips.gui.agriculture;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.gui.SpatialStateProvider;
import edu.mit.sips.sim.util.FoodUnits.DenominatorUnits;
import edu.mit.sips.sim.util.FoodUnits.NumeratorUnits;
import edu.mit.sips.sim.util.FoodUnits;
import edu.mit.sips.sim.util.FoodUnitsOutput;

/**
 * The Class AgricultureStateProvider.
 */
public class AgricultureStateProvider implements SpatialStateProvider, FoodUnitsOutput {
	
	private final FoodUnits.NumeratorUnits foodUnitsNumerator = 
			FoodUnits.NumeratorUnits.GJ;
	private final FoodUnits.DenominatorUnits foodUnitsDenominator = 
			FoodUnits.DenominatorUnits.year;
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getConsumption(edu.mit.sips.Society)
	 */
	@Override
	public double getConsumption(Society society) {
		return society.getTotalFoodDemand();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getDistributionIn(edu.mit.sips.Society, edu.mit.sips.Society)
	 */
	@Override
	public double getDistributionIn(Society society, Society origin) {
		double distribution = 0;
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			for(AgricultureElement e : agricultureSystem.getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(origin.getCities().contains(origCity)) {
					distribution += FoodUnits.convert(e.getFoodOutput(), e, this);
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
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			for(AgricultureElement e : agricultureSystem.getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(destination.getCities().contains(destCity)) {
					if(society.getCities().contains(destCity)) {
						// if a self-loop, only add distribution losses
						distribution += FoodUnits.convert(e.getFoodInput() - e.getFoodOutput(), e, this);
					} else {
						distribution += FoodUnits.convert(e.getFoodInput(), e, this);
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

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getExport(edu.mit.sips.Society)
	 */
	@Override
	public double getExport(Society society) {
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			return agricultureSystem.getFoodExport();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsDenominator()
	 */
	@Override
	public DenominatorUnits getFoodUnitsDenominator() {
		return foodUnitsDenominator;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.sim.util.FoodUnitsOutput#getFoodUnitsNumerator()
	 */
	@Override
	public NumeratorUnits getFoodUnitsNumerator() {
		return foodUnitsNumerator;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getImport(edu.mit.sips.Society)
	 */
	@Override
	public double getImport(Society society) {
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			return FoodUnits.convert(agricultureSystem.getFoodImport(), agricultureSystem, this);
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getInput(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public double getInput(InfrastructureElement element) {
		if(element instanceof AgricultureElement) {
			AgricultureElement agElement = (AgricultureElement) element;
			return FoodUnits.convert(agElement.getFoodInput(), agElement, this);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getNetFlow(edu.mit.sips.Society)
	 */
	@Override
	public double getNetFlow(Society society) {
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			return FoodUnits.convert(agricultureSystem.getFoodExport()
					- agricultureSystem.getFoodImport()
					+ agricultureSystem.getFoodOutDistribution()
					- agricultureSystem.getFoodInDistribution(), agricultureSystem, this);
		} 
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherDistributionIn(edu.mit.sips.Society)
	 */
	@Override
	public double getOtherDistributionIn(Society society) {
		double distribution = 0;
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			for(AgricultureElement e : agricultureSystem.getExternalElements()) {
				City origCity = society.getCountry().getCity(e.getOrigin());
				if(!society.getCities().contains(origCity)) {
					distribution += FoodUnits.convert(e.getFoodOutput(), e, this);
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
		if(society.getAgricultureSystem() instanceof AgricultureSystem.Local) {
			AgricultureSystem.Local agricultureSystem = (AgricultureSystem.Local) 
					society.getAgricultureSystem(); 
			for(AgricultureElement e : agricultureSystem.getInternalElements()) {
				City destCity = society.getCountry().getCity(e.getDestination());
				if(!society.getCities().contains(destCity)) {
					distribution += FoodUnits.convert(e.getFoodInput(), e, this);
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
		if(element instanceof AgricultureElement) {
			AgricultureElement agElement = (AgricultureElement)element;
			return FoodUnits.convert(agElement.getFoodOutput(), agElement, this);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getProduction(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public double getProduction(InfrastructureElement element) {
		if(element instanceof AgricultureElement) {
			AgricultureElement agElement = (AgricultureElement)element;
			return FoodUnits.convert(agElement.getFoodProduction(), agElement, this);
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getUnits()
	 */
	@Override
	public String getUnits() {
		return foodUnitsNumerator.getAbbreviation() + "/"
				+ foodUnitsDenominator.getAbbreviation();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#isDistribution(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public boolean isDistribution(InfrastructureElement element) {
		if(element instanceof AgricultureElement) {
			return ((AgricultureElement)element).getMaxFoodInput() > 0;
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
		if(element instanceof AgricultureElement) {
			return ((AgricultureElement)element).getMaxLandArea() > 0;
		} else {
			return false;
		}
	}
}
