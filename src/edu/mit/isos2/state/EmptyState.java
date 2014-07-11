package edu.mit.isos2.state;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;

import edu.mit.isos2.Location;
import edu.mit.isos2.Node;
import edu.mit.isos2.Scenario;
import edu.mit.isos2.Simulator;
import edu.mit.isos2.element.DefaultElement;
import edu.mit.isos2.element.Element;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
import edu.mit.isos2.resource.ResourceMatrix;
import edu.mit.isos2.resource.ResourceType;

public class EmptyState implements State, ElementTransforming {
	private long stateChangeTime;
	private State nextState;
	
	private long time;
	private transient long nextTime;
	
	public EmptyState(long stateChangeTime, State nextState) { 
		this.stateChangeTime = stateChangeTime;
		this.nextState = nextState;
	}
	
	public String getName() {
		return "Empty";
	}
	
	public String toString() {
		return getName();
	}
	
	public long getStateChangeTime() {
		return stateChangeTime;
	}
	
	public State getNextState() {
		return nextState;
	}
	
	public void initialize(Element element, long initialTime) {
		if(!element.getStates().contains(this)) {
			throw new IllegalStateException(
					"Element does not contain state " + this);
		}
		if(!element.getStates().contains(nextState)) {
			throw new IllegalStateException(
					"Element does not contain next state " + nextState);
		}
		time = nextTime = initialTime;
	}
	
	public void tick(Element element, long duration) {
		nextTime = time + duration;

		if(element.getState().equals(this)
				&& nextTime > stateChangeTime) {
			transform(element, nextState);
		}
	}
	
	public void tock() {
		time = nextTime;
	}
	
	public static void main(String[] args) {
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		Location l11 = new Location(n1, n1);
		Location l22 = new Location(n2, n2);
		Location l12 = new Location(n1, n2);

		NullState s5 = new NullState();
		TransitioningState s4 = new TransitioningState("Decomm", 
				ResourceFactory.create(ResourceType.CURRENCY, 500e3), 2, s5);
		OperatingState s3 = new ProducingState("Producing", 9, s4)
				.consumptionMatrix(new ResourceMatrix(ResourceType.WATER, 
						ResourceFactory.create(ResourceType.ELECTRICITY, 2.5)))
				.initialProductionRate(ResourceFactory.create(ResourceType.WATER, 1.2))
				.fixedExpense(ResourceFactory.create(ResourceType.CURRENCY, 1e3));
		TransitioningState s2 = new TransitioningState("Comm", 
				ResourceFactory.create(ResourceType.CURRENCY, 1e6), 3, s3);
		EmptyState s1 = new EmptyState(25, s2);
		
		Element e1 = new DefaultElement("Dummy 1", l11)
				.states(Arrays.asList(s1, s2, s3, s4, s5))
				.initialState(s1);


		NullState e2s5 = new NullState();
		TransitioningState e2s4 = new TransitioningState("Decomm", 
				ResourceFactory.create(ResourceType.CURRENCY, 200e3), 2, e2s5);
		OperatingState e2s3 = new DistributingState("Distrib", 10, e2s4)
				.inputMatrix(new ResourceMatrix(ResourceType.WATER, 
						ResourceFactory.create(ResourceType.ELECTRICITY, 0.1)))
				.initialOutputRate(ResourceFactory.create(ResourceType.WATER, 2))
				.fixedExpense(ResourceFactory.create(ResourceType.CURRENCY, 500));
		TransitioningState e2s2 = new TransitioningState("Comm", 
				ResourceFactory.create(ResourceType.CURRENCY, 300e3), 2, e2s3);
		EmptyState e2s1 = new EmptyState(6, e2s2);
		
		Element e2 = new DefaultElement("Dummy 2", l12)
				.states(Arrays.asList(e2s1, e2s2, e2s3, e2s4, e2s5))
				.initialState(e2s1);

		BasicConfigurator.configure();
		
		Simulator sim = new Simulator(new Scenario("Test", 0, Arrays.asList(l11, l22, l12), Arrays.asList(e1, e2)));
		sim.execute(50, 2);
	}

	@Override
	public void iterateTick(Element element, long duration) { }

	@Override
	public void iterateTock() { }

	@Override
	public void transform(Element element, State nextState) {
		element.setState(nextState);
	}

	@Override
	public Resource getNetFlow(Element element, Location location, long duration) {
		return ResourceFactory.create();
	}

	@Override
	public Resource getNetExchange(Element element1, Element element2,
			long duration) {
		return ResourceFactory.create();
	}
}
