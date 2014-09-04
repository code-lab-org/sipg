package edu.mit.isos3.element;

import edu.mit.isos3.Location;
import edu.mit.isos3.element.state.DefaultState;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;
import edu.mit.isos3.resource.ResourceMatrix;
import edu.mit.isos3.resource.ResourceType;

public class WaterPipeline extends DefaultElement {
	public WaterPipeline(String name, Location location, 
			double capacity, double efficiency, double pumpElect) {
		super(name, location, new PipelineState(capacity, efficiency, pumpElect));
	}

	public PipelineState getOperatingState() {
		return (PipelineState) getState();
	}
	
	public static class PipelineState extends DefaultState {
		protected Resource outputCapacity;
		protected double eta = 1;
		protected ResourceMatrix tpMatrix = new ResourceMatrix();
		protected Resource output = ResourceFactory.create();
		private final Resource initialOutput = ResourceFactory.create();

		public PipelineState(double capacity, double efficiency, double pumpElect) {
			super("Ops");
			this.outputCapacity = ResourceFactory.create(ResourceType.WATER, capacity);
			eta = efficiency;
			tpMatrix = new ResourceMatrix(
					ResourceType.WATER, 
					ResourceFactory.create(ResourceType.ELECTRICITY, pumpElect));
		}

		@Override
		public Resource getInput(LocalElement element, long duration) {
			return getOutput(element, duration).multiply(1/eta)
					.add(tpMatrix.multiply(getOutput(element, duration)));
		}
		@Override
		public Resource getOutput(LocalElement element, long duration) {
			return output;
		}
		@Override
		public Resource getConsumed(LocalElement element, long duration) {
			return getInput(element, duration).subtract(getOutput(element, duration));
		}
		@Override
		public void initialize(LocalElement element, long initialTime) {
			super.initialize(element, initialTime);
			output = initialOutput;
		}
		protected void setOutput(Resource output, long duration) {
			if(output.getQuantity(ResourceType.WATER) > 
			outputCapacity.multiply(duration).getQuantity(ResourceType.WATER)) {
				this.output = outputCapacity.multiply(duration);
			} else {
				this.output = output.truncatePositive();
			}
		}
	}
}
