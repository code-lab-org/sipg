package edu.mit.isos3.element;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.state.DefaultState;
import edu.mit.isos3.element.state.EmptyState;
import edu.mit.isos3.element.state.State;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;
import edu.mit.isos3.resource.ResourceType;

public class WaterPlant extends DefaultElement {
	private PlantState operatingState;
	private EmptyState emptyState;
	private List<? extends State> states;
	
	public WaterPlant(String name, Location location, 
			final long commissionTime, final double capacity, final double desalElect) {
		super(name, location);
		operatingState = new PlantState(capacity, desalElect);
		emptyState = new EmptyState(commissionTime, operatingState);
		states = Arrays.asList(emptyState, operatingState);
	}
	
	@Override
	public void initialize(long initialTime) {
		if(initialTime < emptyState.getStateChangeTime()) {
			initialState(emptyState);
		} else {
			initialState(operatingState);
		}
		super.initialize(initialTime);
	}
	
	@Override
	public Set<State> getStates() {
		return new HashSet<State>(states);
	}
	
	public boolean isOperating() {
		return getState().equals(operatingState);
	}
	
	public PlantState getOperatingState() {
		return operatingState;
	}
	
	public static class PlantState extends DefaultState {
		protected Resource productionCapacity;
		protected ResourceMatrix tfMatrix = new ResourceMatrix();
		protected Resource produced = ResourceFactory.create();
		private final Resource initialProduced = ResourceFactory.create();

		public PlantState(double capacity, double desalElect) {
			super("Ops");
			this.productionCapacity = ResourceFactory.create(ResourceType.WATER, capacity);
			tfMatrix = new ResourceMatrix(
					ResourceType.WATER,
					ResourceFactory.create(ResourceType.ELECTRICITY, desalElect));
		}
		@Override
		public Resource getConsumed(LocalElement element, long duration) {
			return tfMatrix.multiply(getProduced(element, duration));
		}
		@Override
		public Resource getProduced(LocalElement element, long duration) {
			return produced;
		}
		@Override
		public void initialize(LocalElement element, long initialTime) {
			super.initialize(element, initialTime);
			produced = initialProduced;
		}
		protected void setProduced(Resource produced, long duration) {
			if(produced.getQuantity(ResourceType.WATER) > 
			productionCapacity.multiply(duration).getQuantity(ResourceType.WATER)) {
				this.produced = productionCapacity.multiply(duration);
			} else {
				this.produced = produced.truncatePositive();
			}
		}
	}
}
