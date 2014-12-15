package edu.mit.isos.app;

import hla.rti1516e.exceptions.RTIexception;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.mit.isos.app.elect.LocalElectElement;
import edu.mit.isos.app.hla.ISOSnullAmbassador;
import edu.mit.isos.app.petrol.LocalPetrolElement;
import edu.mit.isos.app.social.LocalSocialElement;
import edu.mit.isos.app.water.LocalWaterElement;
import edu.mit.isos.app.water.WaterController;
import edu.mit.isos.app.water.WaterPipeline;
import edu.mit.isos.app.water.WaterPlant;
import edu.mit.isos.core.context.Location;
import edu.mit.isos.core.context.Node;
import edu.mit.isos.core.context.Resource;
import edu.mit.isos.core.context.ResourceFactory;
import edu.mit.isos.core.context.Scenario;
import edu.mit.isos.core.element.Element;
import edu.mit.isos.core.element.LocalElement;
import edu.mit.isos.core.sim.SimulationTimeEvent;
import edu.mit.isos.core.sim.SimulationTimeListener;
import edu.mit.isos.core.sim.Simulator;
import edu.mit.isos.core.state.ResourceExchanging;
import edu.mit.isos.core.state.ResourceTransforming;
import edu.mit.isos.core.state.ResourceTransporting;

public class SingleFederate {
	protected static Logger logger = Logger.getLogger(SingleFederate.class);
	
	boolean replicationOutputs = true;
	boolean retainReplicationOutputs = true;
	final int numIterations;
	final int numReplications;
	final int stepsPerYear = 1000;
	final long timeStep;
	private final String dir = "isos";
	private final String federateName = "Single";
	
	public final Node n_a = new Node("A");
	public final Node n_b = new Node("B");
	public final Node n_c = new Node("C");
	public final Location l_aa = new Location(n_a, n_a);
	public final Location l_bb = new Location(n_b, n_b);
	public final Location l_cc = new Location(n_c, n_c);
	public final Location l_ab = new Location(n_a, n_b);
	public final Location l_ba = new Location(n_b, n_a);
	public final Location l_bc = new Location(n_b, n_c);
	public final Location l_cb = new Location(n_c, n_b);
	
	private LocalSocialElement e_s1;
	private LocalSocialElement e_s2;
	private LocalSocialElement e_s3;
	private LocalWaterElement e_w1;
	private LocalWaterElement e_w2;
	private LocalWaterElement e_w3;
	private LocalElectElement e_e1;
	private LocalElectElement e_e2;
	private LocalElectElement e_e3;
	private LocalPetrolElement e_o1;
	private LocalPetrolElement e_o2;
	private LocalPetrolElement e_o3;

	public SingleFederate(int numIterations, int numReplications, long timeStep) {
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
		
		Path summaryPath = Paths.get(dir,testName,"summary.txt");
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
			
			Path warningPath = Paths.get(dir,testName,new Integer(i+1).toString(),"warnings.txt");
			if(!warningPath.toFile().exists()) {
				warningPath.toFile().createNewFile();
			}
			final BufferedWriter warningWriter = Files.newBufferedWriter(warningPath, 
					Charset.defaultCharset(), StandardOpenOption.WRITE);
			warningWriter.write(String.format("%6s%10s%20s%60s%60s\n","Time","Type","Unit(s)","Error","% Error"));
			

			final ISOSnullAmbassador amb = new ISOSnullAmbassador();
			
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
						for(Element e2 : sim.getScenario().getElements()) {
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

	public Scenario buildScenario(double stepsPerYear) {
		e_s1 = new LocalSocialElement("e_S1", l_aa, 0.065/stepsPerYear, 4.0/stepsPerYear, 1.0/stepsPerYear, 3.0, 0.07/stepsPerYear);
		e_s2 = new LocalSocialElement("e_S2", l_bb, 0.050/stepsPerYear, 3.0/stepsPerYear, 1.2/stepsPerYear, 1.0, 0.05/stepsPerYear);
		e_s3 = new LocalSocialElement("e_S3", l_cc, 0.060/stepsPerYear, 3.5/stepsPerYear, 1.0/stepsPerYear, 6.0, 0.06/stepsPerYear);
		
		e_w1 = new LocalWaterElement("e_W1", l_aa, 1.0, 0.9, 200);
		e_w2 = new LocalWaterElement("e_W2", l_bb, 1.0, 0.9, 150);
		e_w3 = new LocalWaterElement("e_W3", l_cc, 1.0, 0.9, 250);
		WaterPlant e_w4 = new WaterPlant("e_W4", l_aa, (long)(2014000+0*stepsPerYear), 0.5/stepsPerYear, 4.5);
		WaterPlant e_w5 = new WaterPlant("e_W5", l_cc, (long)(2014000+0*stepsPerYear), 0.4/stepsPerYear, 4.5);
		WaterPlant e_w6 = new WaterPlant("e_W6", l_cc, (long)(2014000+5*stepsPerYear), 0.6/stepsPerYear, 4.5);
		WaterPipeline e_w7 = new WaterPipeline("e_W7", l_ab, 0.02/stepsPerYear, 0.9, 2.5);
		WaterPipeline e_w8 = new WaterPipeline("e_W8", l_cb, 0.02/stepsPerYear, 0.9, 2.0);
		WaterController e_w9 = new WaterController("e_W9", l_aa, 
				Arrays.asList(e_w1, e_w2, e_w3, e_w4, e_w5, e_w6, e_w7, e_w8));
		
		e_o1 = new LocalPetrolElement("e_P1", l_aa, 0.5, 1.0, 5000);
		e_o2 = new LocalPetrolElement("e_P2", l_bb, 0.8, 1.0, 1000);
		e_o3 = new LocalPetrolElement("e_P3", l_cc, 0.6, 1.0, 4000);
		
		e_e1 = new LocalElectElement("e_E1", l_aa, 1.0/stepsPerYear, 0.25);
		e_e2 = new LocalElectElement("e_E2", l_bb, 0.5/stepsPerYear, 0.30);
		e_e3 = new LocalElectElement("e_E3", l_cc, 0.8/stepsPerYear, 0.25);
		return new Scenario("Demo", 2014000, 
				Arrays.asList(l_aa, l_bb, l_cc, l_ab, l_ba, l_bc, l_cb), 
				Arrays.asList(e_s1, e_s2, e_s3, 
						e_w1, e_w2, e_w3, e_w4, e_w5, e_w6, e_w7, e_w8, e_w9, 
						e_o1, e_o2, e_o3, 
						e_e1, e_e2, e_e3));
	}

}
