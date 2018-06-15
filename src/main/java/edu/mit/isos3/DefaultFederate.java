package edu.mit.isos3;

import hla.rti1516e.exceptions.RTIexception;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.mit.isos3.element.Element;
import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.element.state.ResourceExchanging;
import edu.mit.isos3.element.state.ResourceTransforming;
import edu.mit.isos3.element.state.ResourceTransporting;
import edu.mit.isos3.event.SimulationTimeEvent;
import edu.mit.isos3.event.SimulationTimeListener;
import edu.mit.isos3.hla.ISOSambassador;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;

public abstract class DefaultFederate {
	protected static Logger logger = Logger.getLogger("edu.mit.isos3");
	
	boolean replicationOutputs = true;
	boolean retainReplicationOutputs = true;
	final int numIterations;
	final int numReplications;
	final int stepsPerYear = 1000;
	final long timeStep;
	private final String dir = "isos3";
	private final String federateName;
	
	Node n_a = new Node("A");
	Node n_b = new Node("B");
	Node n_c = new Node("C");
	Location l_aa = new Location(n_a, n_a);
	Location l_bb = new Location(n_b, n_b);
	Location l_cc = new Location(n_c, n_c);
	Location l_ab = new Location(n_a, n_b);
	Location l_ba = new Location(n_b, n_a);
	Location l_bc = new Location(n_b, n_c);
	Location l_cb = new Location(n_c, n_b);
	
	public DefaultFederate(String federateName, int numIterations,
			int numReplications, long timeStep) {
		this.federateName = federateName;
		this.numIterations = numIterations;
		this.numReplications = numReplications;
		this.timeStep = timeStep;
	}
	
	public void execute(double simulationDuration) throws IOException, RTIexception {
		Path dirPath = Paths.get(dir);
		if(!dirPath.toFile().exists()) {
			dirPath.toFile().mkdir();
		}
		
		String testName = numReplications+"rep"+numIterations+"itr"+timeStep+"stp";
		
		Path outputDirPath = Paths.get(dir,testName);
		if(!outputDirPath.toFile().exists()) {
			outputDirPath.toFile().mkdir();
		}
		
		Path summaryPath = Paths.get(dir,testName,federateName+"-summary.txt");
		if(!summaryPath.toFile().exists()) {
			summaryPath.toFile().createNewFile();
		}

		// create an output writer to handle file output
		final BufferedWriter summaryWriter = Files.newBufferedWriter(
				summaryPath, Charset.defaultCharset(), 
				StandardOpenOption.WRITE);
		summaryWriter.write(String.format("%6s%20s%20s%20s\n","Run","Total Time (ms)","Init Time (ms)","Exec Time (ms)"));

		final Simulator sim = new Simulator(buildScenario(stepsPerYear));
		sim.setVerifyFlow(false);
		sim.setVerifyExchange(false);
		
		for(int i = 0; i < numReplications; i++) {
			Path currentOutputDirPath = Paths.get(dir,testName,new Integer(i+1).toString());
			if(!currentOutputDirPath.toFile().exists()) {
				currentOutputDirPath.toFile().mkdir();
			}
			
			final Map<LocalElement, BufferedWriter> elementWriters = new HashMap<LocalElement, BufferedWriter>();
			if(replicationOutputs || i == 1) {
				for(LocalElement e : sim.getScenario().getElements()) {
					Path elementPath = Paths.get(dir,testName,new Integer(i+1).toString(),e.getName() + ".txt");
					if(!elementPath.toFile().exists()) {
						elementPath.toFile().createNewFile();
					}
					BufferedWriter writer = Files.newBufferedWriter(
							elementPath, Charset.defaultCharset(), 
							StandardOpenOption.WRITE);
					writer.write(String.format("%6s%10s%10s%10s%10s%60s%60s%60s%60s%60s%60s%60s\n", 
							"Time", "Element", "State", "Location", "Parent", "Contents", 
							"Consumed", "Produced", "Input", "Output", "Sent", "Received"));
					elementWriters.put(e, writer);
				}
			}
			
			Path warningPath = Paths.get(dir,testName,new Integer(i+1).toString(),federateName+"-warnings.txt");
			if(!warningPath.toFile().exists()) {
				warningPath.toFile().createNewFile();
			}
			final BufferedWriter warningWriter = Files.newBufferedWriter(warningPath, 
					Charset.defaultCharset(), StandardOpenOption.WRITE);
			warningWriter.write(String.format("%6s%10s%20s%60s%60s\n","Time","Type","Unit(s)","Error","% Error"));
			

			final ISOSambassador amb = new ISOSambassador();
			
			SimulationTimeListener listener = new SimulationTimeListener() {
				@Override
				public void timeAdvanced(SimulationTimeEvent event) {
					for(LocalElement e : elementWriters.keySet()) {
						try {
							elementWriters.get(e).write(String.format("%6d%10s%10s%10s%10s%60s%60s%60s%60s%60s%60s%60s\n", 
									event.getTime(), 
									e.getName(),
									e.getState(),
									e.getLocation(), 
									e.getParent().getName(),
									e.getContents(), 
									(e.getState() instanceof ResourceTransforming)?((ResourceTransforming)e.getState()).getConsumed(e, event.getDuration()):"NaN", 
									(e.getState() instanceof ResourceTransforming)?((ResourceTransforming)e.getState()).getProduced(e, event.getDuration()):"NaN", 
									(e.getState() instanceof ResourceTransporting)?((ResourceTransporting)e.getState()).getInput(e, event.getDuration()):"NaN", 
									(e.getState() instanceof ResourceTransporting)?((ResourceTransporting)e.getState()).getOutput(e, event.getDuration()):"NaN", 
									(e.getState() instanceof ResourceExchanging)?((ResourceExchanging)e.getState()).getSent(e, event.getDuration()):"NaN", 
									(e.getState() instanceof ResourceExchanging)?((ResourceExchanging)e.getState()).getReceived(e, event.getDuration()):"NaN"));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					for(Location l : sim.getScenario().getLocations()) {
						Resource netFlow = ResourceFactory.create();
						for(LocalElement element : sim.getScenario().getElements()) {
							netFlow = netFlow.add(element.getNetFlow(l, event.getDuration()));
						}
						if(!netFlow.isZero()) {
							try {
								warningWriter.write(String.format("%6d%10s%20s%60s%60s\n",
										event.getTime(), 
										"Net Flow", l.toString(), 
										netFlow, "NaN"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					for(Element e1 : sim.getScenario().getElements()) {
						for(Element e2 : amb.getElements()) {
							Resource r12 = e1.getNetExchange(e2, event.getDuration());
							Resource r21 = e2.getNetExchange(e1, event.getDuration());
							if(!r12.add(r21).isZero()) {
								try {
									warningWriter.write(String.format("%6d%10s%20s%60s%60s\n",
											event.getTime(), 
											"Exchange", e1.getName() + "<->" + e2.getName(), 
											r12.add(r21), r12.add(r21).absoluteValue().safeDivide(r12.absoluteValue())));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			};
			
			sim.addSimulationTimeListener(listener);

			amb.connect("ISOS Test " + (i+1), "isos.xml", federateName, "Test");
			
			// TODO wait for other federates to join
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error(e);
			}
			
			long initStartTime = sim.initialize(amb, federateName, timeStep, numIterations);
			long initEndTime = new Date().getTime();
			
			long execStartTime = sim.execute(amb, federateName, (int) (simulationDuration*stepsPerYear), 
					timeStep, numIterations);
			long execEndTime = new Date().getTime();
			long initTime = initEndTime - initStartTime;
			long execTime = execEndTime - execStartTime;
			long totalTime = initTime + execTime;
			
			logger.info("Simulation completed in " + totalTime + " ms");
			summaryWriter.write(String.format("%6d%20d%20d%20d\n",(i+1),totalTime, initTime, execTime));
			
			for(Element e : elementWriters.keySet()) {
				elementWriters.get(e).close();
				Path elementPath = Paths.get(dir,testName,new Integer(i+1).toString(),e.getName() + ".txt");
				if(i > 1 && elementPath.toFile().exists() && !retainReplicationOutputs) {
					elementPath.toFile().deleteOnExit();
				}
			}
			
			warningWriter.close();
			
			sim.removeSimulationTimeListener(listener);
			
			amb.disconnect("ISOS Test " + (i+1));
		}
		
		summaryWriter.close();
	}
	
	public abstract Scenario buildScenario(double stepsPerYear);
}
