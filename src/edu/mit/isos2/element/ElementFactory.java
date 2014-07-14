package edu.mit.isos2.element;

import java.util.Arrays;

import edu.mit.isos2.Location;
import edu.mit.isos2.resource.Resource;
import edu.mit.isos2.resource.ResourceMatrix;
import edu.mit.isos2.state.DistributingState;
import edu.mit.isos2.state.EmptyState;
import edu.mit.isos2.state.NullState;
import edu.mit.isos2.state.OperatingState;
import edu.mit.isos2.state.ProducingState;
import edu.mit.isos2.state.RetrievingState;
import edu.mit.isos2.state.TransitioningState;

public abstract class ElementFactory {

	public static Element createDistributionElement(String name, 
			Location initialLocation, long commissionTime, 
			long commissionDuration, Resource commissionExpense,
			long operationDuration, Resource fixedExpense, 
			ResourceMatrix inputMatrix, Resource initialOutputRate,
			long decommissionDuration, Resource decommissionExpense) {
		NullState s5 = new NullState();
		TransitioningState s4 = new TransitioningState("Decommissioning", 
				decommissionExpense, decommissionDuration, s5);
		OperatingState s3 = new DistributingState("Distributing", operationDuration, s4)
				.inputMatrix(inputMatrix)
				.initialOutputRate(initialOutputRate)
				.fixedExpense(fixedExpense);
		TransitioningState s2 = new TransitioningState("Commissioning", 
				commissionExpense, commissionDuration, s3);
		EmptyState s1 = new EmptyState(commissionTime, s2);
		return new LifecycleElement(name, initialLocation, s1, s2, s3, s4, s5);
	}
	
	public static Element createProductionElement(String name, 
			Location initialLocation, long commissionTime, 
			long commissionDuration, Resource commissionExpense,
			long operationDuration, Resource fixedExpense, 
			ResourceMatrix consumptionMatrix, Resource initialProductionRate,
			long decommissionDuration, Resource decommissionExpense) {
		NullState s5 = new NullState();
		TransitioningState s4 = new TransitioningState("Decommissioning", 
				decommissionExpense, decommissionDuration, s5);
		OperatingState s3 = new ProducingState("Producing", operationDuration, s4)
				.consumptionMatrix(consumptionMatrix)
				.initialProductionRate(initialProductionRate)
				.fixedExpense(fixedExpense);
		TransitioningState s2 = new TransitioningState("Commissioning", 
				commissionExpense, commissionDuration, s3);
		EmptyState s1 = new EmptyState(commissionTime, s2);
		return new LifecycleElement(name, initialLocation, s1, s2, s3, s4, s5);
	}
	
	public static Element createRetrievalElement(String name, 
			Location initialLocation, long commissionTime, 
			long commissionDuration, Resource commissionExpense,
			long operationDuration, Resource initialContents, Resource fixedExpense, 
			ResourceMatrix consumptionMatrix,
			long decommissionDuration, Resource decommissionExpense) {
		NullState s5 = new NullState();
		TransitioningState s4 = new TransitioningState("Decommissioning", 
				decommissionExpense, decommissionDuration, s5);
		OperatingState s3 = new RetrievingState("Retrieving", operationDuration, s4)
				.consumptionMatrix(consumptionMatrix)
				.fixedExpense(fixedExpense);
		TransitioningState s2 = new TransitioningState("Commissioning", 
				commissionExpense, commissionDuration, s3);
		EmptyState s1 = new EmptyState(commissionTime, s2);
		return new LifecycleElement(name, initialLocation, s1, s2, s3, s4, s5)
				.initialContents(initialContents);
	}
	
	public static Element createRetrievalElement(String name, 
			Location initialLocation, Resource initialContents, Resource fixedExpense, 
			ResourceMatrix consumptionMatrix) {
		NullState s5 = new NullState();
		OperatingState s3 = new RetrievingState("Retrieving", Long.MAX_VALUE, s5)
				.consumptionMatrix(consumptionMatrix)
				.fixedExpense(fixedExpense);
		return new DefaultElement(name, initialLocation)
				.states(Arrays.asList(s3, s5))
				.initialState(s3)
				.initialContents(initialContents);
	}
}
