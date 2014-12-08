package edu.mit.isos.app.social;

import edu.mit.isos.app.ElectElement;
import edu.mit.isos.app.PetrolElement;
import edu.mit.isos.app.WaterElement;
import edu.mit.isos.core.context.Location;
import edu.mit.isos.core.context.Resource;
import edu.mit.isos.core.context.ResourceFactory;
import edu.mit.isos.core.context.ResourceMatrix;
import edu.mit.isos.core.context.ResourceType;
import edu.mit.isos.core.element.Element;
import edu.mit.isos.core.element.LocalElement;
import edu.mit.isos.core.state.DefaultState;
import edu.mit.isos.core.state.ResourceExchanging;

public class SocialState extends DefaultState implements ResourceExchanging {
	private ResourceMatrix demandMatrix = new ResourceMatrix();
	private double growthRate;
	
	private ElectElement electSupplier = null;
	private PetrolElement petrolSupplier = null;
	private WaterElement waterSupplier = null;
	private double electReceived, nextElectReceived;
	private double petrolReceived, nextPetrolReceived;
	private double waterReceived, nextWaterReceived;
	
	public void setElectSupplier(ElectElement element) {
		electSupplier = element;
	}
	
	public void setPetrolSupplier(PetrolElement element) {
		petrolSupplier = element;
	}
	
	public void setWaterSupplier(WaterElement element) {
		waterSupplier = element;
	}
	
	public double getElectReceived() {
		return electReceived;
	}
	
	public double getPetrolReceived() {
		return petrolReceived;
	}
	
	public double getWaterReceived() {
		return waterReceived;
	}

	@Override
	public void iterateTick(LocalElement element, long duration) {
		super.iterateTick(element, duration);
		nextElectReceived = getReceived(element, duration)
				.getQuantity(ResourceType.ELECTRICITY);
		nextPetrolReceived = getReceived(element, duration)
				.getQuantity(ResourceType.OIL);
		nextWaterReceived = getReceived(element, duration)
				.getQuantity(ResourceType.WATER);
	}

	@Override
	public void iterateTock() {
		super.iterateTock();
		electReceived = nextElectReceived;
		petrolReceived = nextPetrolReceived;
		waterReceived = nextWaterReceived;
	}
	
	@Override
	public void initialize(LocalElement element, long initialTime) {
		super.initialize(element, initialTime);
		electSupplier = null;
		petrolSupplier = null;
		waterSupplier = null;
		electReceived = nextElectReceived = 0;
		petrolReceived = nextPetrolReceived = 0;
		waterReceived = nextWaterReceived = 0;
	}
	
	public SocialState(double electPC, double oilPC, double waterPC, double growthRate) {
		super("Ops");
		demandMatrix = new ResourceMatrix(
				ResourceType.PEOPLE, ResourceFactory.create(
						new ResourceType[]{ResourceType.ELECTRICITY, 
								ResourceType.WATER, ResourceType.OIL},
								new double[]{electPC, waterPC, oilPC}));
		this.growthRate = growthRate;
	}
	
	@Override
	public Resource getConsumed(LocalElement element, long duration) {
		return demandMatrix.multiply(element.getContents().get(ResourceType.PEOPLE))
				.multiply(duration);
	}

	@Override
	public Resource getProduced(LocalElement element, long duration) {
		return element.getContents().get(ResourceType.PEOPLE)
				.multiply(Math.exp(growthRate*duration)-1);
	}

	@Override
	public Resource getStored(LocalElement element, long duration) {
		return getProduced(element, duration);
	}
	
	@Override
	public Resource getSentTo(LocalElement element1, Element element2, long duration) {
		Resource sent = ResourceFactory.create();
		return sent;
	}
	
	@Override
	public Resource getSent(LocalElement element, long duration) {
		Resource sent = ResourceFactory.create();
		return sent;
	}
	
	@Override
	public Resource getReceivedFrom(LocalElement element1, Element element2, long duration) {
		Resource received = ResourceFactory.create();
		if(element2 != null && element2.equals(electSupplier)) {
			received = received.add(getReceived(element1, duration).get(ResourceType.ELECTRICITY));
		}
		if(element2 != null && element2.equals(petrolSupplier)) {
			received = received.add(getReceived(element1, duration).get(ResourceType.OIL));
		}
		if(element2 != null && element2.equals(waterSupplier)) {
			received = received.add(getReceived(element1, duration).get(ResourceType.WATER));
		}
		return received;
	}
	
	@Override
	public Resource getReceived(LocalElement element, long duration) {
		return getConsumed(element, duration);
	}

	@Override
	public void exchange(LocalElement element1, Element element2, Resource sent, Resource received) {
		if(!sent.isZero() && !element1.getLocation().getDestination().equals(
				element2.getLocation().getOrigin())) {
			throw new IllegalArgumentException("Incompatible resource exchange, " 
					+ element1.getName() + " destination " 
					+ element1.getLocation().getDestination() + " =/= " 
					+ element2.getName() + " origin " 
					+ element2.getLocation().getOrigin());
		}
		if(!received.isZero() && !element1.getLocation().getOrigin().equals(
				element2.getLocation().getDestination())) {
			throw new IllegalArgumentException("Incompatible resource exchange: " 
					+ element1.getName() + " origin " 
					+ element1.getLocation().getOrigin() + " =/= " + 
					element2.getName() + " destination " 
					+ element2.getLocation().getDestination());
		}
	}
	
	@Override
	public Resource getNetFlow(LocalElement element, Location location, long duration) {
		Resource netFlow = super.getNetFlow(element, location, duration);
		if(location.isNodal() && location.getOrigin().equals(element.getLocation().getOrigin())) {
			netFlow = netFlow.subtract(getSent(element, duration))
					.add(getReceived(element, duration));
		}
		return netFlow;
	}

	@Override
	public Resource getNetExchange(LocalElement element1, Element element2,
			long duration) {
		Resource netExchange = super.getNetExchange(element1, element2, duration);
		netExchange = netExchange.add(getSentTo(element1, element2, duration))
				.subtract(getReceivedFrom(element1, element2, duration));
		return netExchange;
	}
}