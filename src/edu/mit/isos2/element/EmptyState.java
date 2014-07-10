package edu.mit.isos2.element;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;

import edu.mit.isos2.Location;
import edu.mit.isos2.Node;
import edu.mit.isos2.Scenario;
import edu.mit.isos2.Simulator;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceFactory;
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
		Location l = new Location(n1, n1);

		NullState s5 = new NullState();
		TransitionState s4 = new TransitionState("Decommissioning", 
				ResourceFactory.create(ResourceType.CURRENCY, 500e3), 2, s5);
		OperatingState s3 = new OperatingState("Operating", 9, s4);
		TransitionState s2 = new TransitionState("Commissioning", 
				ResourceFactory.create(ResourceType.CURRENCY, 1e6), 3, s3);
		EmptyState s1 = new EmptyState(25, s2);
		
		Element e = new DefaultElement("Dummy", l)
				.states(Arrays.asList(s1, s2, s3, s4, s5))
				.initialState(s1);

		BasicConfigurator.configure();
		
		Simulator sim = new Simulator(new Scenario("Test", 0, Arrays.asList(l), Arrays.asList(e)));
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
