package edu.mit.sips.gui;

import java.util.ArrayList;
import java.util.List;

import edu.mit.sips.core.City;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.energy.PetroleumElement;

/**
 * The Class PetroleumStateProvider.
 */
public class PetroleumStateProvider implements SpatialStateProvider {
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getConsumption(edu.mit.sips.Society)
	 */
	@Override
	public double getConsumption(Society society) {
		return society.getTotalPetroleumDemand();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getDistributionIn(edu.mit.sips.Society, edu.mit.sips.Society)
	 */
	@Override
	public double getDistributionIn(Society society, Society origin) {
		double distribution = 0;
		for(PetroleumElement e : society.getEnergySystem().getPetroleumSystem().getExternalElements()) {
			City origCity = society.getCountry().getCity(e.getOrigin());
			if(origin.getCities().contains(origCity)) {
				distribution += e.getPetroleumOutput();
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
		for(PetroleumElement e : society.getEnergySystem().getPetroleumSystem().getInternalElements()) {
			City destCity = society.getCountry().getCity(e.getDestination());
			if(destination.getCities().contains(destCity)) {
				if(society.getCities().contains(destCity)) {
					// if a self-loop, only add distribution losses
					distribution += e.getPetroleumInput() - e.getPetroleumOutput();
				} else {
					distribution += e.getPetroleumInput();
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
		for(PetroleumElement element : society.getEnergySystem().getPetroleumSystem().getElements()) {
			if(element.isExists()) elements.add(element);
		}
		return elements;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getExport(edu.mit.sips.Society)
	 */
	@Override
	public double getExport(Society society) {
		return society.getEnergySystem().getPetroleumSystem().getPetroleumExport();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getImport(edu.mit.sips.Society)
	 */
	@Override
	public double getImport(Society society) {
		return society.getEnergySystem().getPetroleumSystem().getPetroleumImport();
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getInput(edu.mit.sips.InfrastructureElement)
	 */
	@Override
	public double getInput(InfrastructureElement element) {
		if(element instanceof PetroleumElement) {
			return ((PetroleumElement)element).getPetroleumInput();
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getNetFlow(edu.mit.sips.Society)
	 */
	@Override
	public double getNetFlow(Society society) {
		return society.getEnergySystem().getPetroleumSystem().getPetroleumExport()
				- society.getEnergySystem().getPetroleumSystem().getPetroleumImport()
				+ society.getEnergySystem().getPetroleumSystem().getPetroleumOutDistribution()
				- society.getEnergySystem().getPetroleumSystem().getPetroleumInDistribution();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStateProvider#getOtherDistributionIn(edu.mit.sips.Society)
	 */
	@Override
	public double getOtherDistributionIn(Society society) {
		double distribution = 0;
		for(PetroleumElement e : society.getEnergySystem().getPetroleumSystem().getExternalElements()) {
			City origCity = society.getCountry().getCity(e.getOrigin());
			if(!society.getCities().contains(origCity)) {
				distribution += e.getPetroleumOutput();
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
		for(PetroleumElement e : society.getEnergySystem().getPetroleumSystem().getInternalElements()) {
			City destCity = society.getCountry().getCity(e.getDestination());
			if(!society.getCities().contains(destCity)) {
				distribution += e.getPetroleumInput();
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
			return ((PetroleumElement)element).getPetroleumOutput();
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
			return ((PetroleumElement)element).getPetroleumProduction();
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.gui.SpatialStatePanel#getUnits()
	 */
	@Override
	public String getUnits() {
		return "bbl";
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
}
