package edu.mit.isos3;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;

import edu.mit.isos3.element.ElectElement;
import edu.mit.isos3.element.Element;
import edu.mit.isos3.element.LocalElectElement;
import edu.mit.isos3.element.LocalElement;
import edu.mit.isos3.element.LocalPetrolElement;
import edu.mit.isos3.element.LocalSocialElement;
import edu.mit.isos3.element.LocalWaterElement;
import edu.mit.isos3.element.PetrolElement;
import edu.mit.isos3.element.SocialElement;
import edu.mit.isos3.element.WaterController;
import edu.mit.isos3.element.WaterElement;
import edu.mit.isos3.element.WaterPipeline;
import edu.mit.isos3.element.WaterPlant;
import edu.mit.isos3.element.state.ResourceExchanging;
import edu.mit.isos3.element.state.ResourceTransforming;
import edu.mit.isos3.element.state.ResourceTransporting;
import edu.mit.isos3.event.SimulationTimeEvent;
import edu.mit.isos3.event.SimulationTimeListener;
import edu.mit.isos3.resource.Resource;
import edu.mit.isos3.resource.ResourceFactory;

public class DemoScript {
	public static void main(String[] args) throws IOException {
		BasicConfigurator.configure();
		/*
		for(int itr : new int[]{4}) {
			new DemoScript(itr,20,1000).execute();
		}
		*/
		/*
		for(int itr : new int[]{100, 100, 100, 100, 100}) {
			new DemoScript(itr,20,1000).execute();
		}
		for(int itr : new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16, 18, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 90, 100}) {
			new DemoScript(itr,50,1000).execute();
		}
		*/
		/*
		for(int itr : new int[]{100, 100, 100, 100, 100}) {
			new DemoScript(itr,20,1000).execute();
		}
		for(int stp : new int[]{100, 250, 500, 1000, 2500, 10000}) {
			new DemoScript(40,50,stp).execute();
		}
		*/
		new DemoScript(100,1,1000).execute();
	}
	
	boolean replicationOutputs = true;
	boolean retainReplicationOutputs = false;
	final int numIterations;
	final int numReplications;
	final int stepsPerYear = 1000;
	final long timeStep;
	final double simulationDuration = 30.0;
	private final String dir = "isos3";
	
	public DemoScript(int numIterations, int numReplications, int timeStep) {
		this.numIterations = numIterations;
		this.numReplications = numReplications;
		this.timeStep = timeStep;
	}
	
	public void execute() throws IOException {
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
		summaryWriter.write(String.format("%6s%20s\n","Run","Time (ms)"));

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
					for(LocalElement e1 : sim.getScenario().getElements()) {
						for(LocalElement e2 : sim.getScenario().getElements()) {
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
			
			long time = sim.execute((int) (simulationDuration*stepsPerYear), 
					timeStep, numIterations);
			summaryWriter.write(String.format("%6d%20d\n",(i+1),time));
			
			for(Element e : elementWriters.keySet()) {
				elementWriters.get(e).close();
				Path elementPath = Paths.get(dir,testName,new Integer(i+1).toString(),e.getName() + ".txt");
				if(i > 1 && elementPath.toFile().exists() && !retainReplicationOutputs) {
					elementPath.toFile().deleteOnExit();
				}
			}
			
			warningWriter.close();
			
			sim.removeSimulationTimeListener(listener);
		}
		
		summaryWriter.close();
	}

	public static Scenario buildScenario(double stepsPerYear) {
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

		LocalSocialElement e_s1 = new LocalSocialElement("e_S1", l_aa, 0.065/stepsPerYear, 4.0/stepsPerYear, 1.0/stepsPerYear, 3.0, 0.07/stepsPerYear);
		LocalSocialElement e_s2 = new LocalSocialElement("e_S2", l_bb, 0.050/stepsPerYear, 3.0/stepsPerYear, 1.2/stepsPerYear, 1.0, 0.05/stepsPerYear);
		LocalSocialElement e_s3 = new LocalSocialElement("e_S3", l_cc, 0.060/stepsPerYear, 3.5/stepsPerYear, 1.0/stepsPerYear, 6.0, 0.06/stepsPerYear);

		LocalPetrolElement e_o1 = new LocalPetrolElement("e_P1", l_aa, 0.5, 1.0, 5000);
		LocalPetrolElement e_o2 = new LocalPetrolElement("e_P2", l_bb, 0.8, 1.0, 1000);
		LocalPetrolElement e_o3 = new LocalPetrolElement("e_P3", l_cc, 0.6, 1.0, 4000);

		LocalElectElement e_e1 = new LocalElectElement("e_E1", l_aa, 1.0/stepsPerYear, 0.25);
		LocalElectElement e_e2 = new LocalElectElement("e_E2", l_bb, 0.5/stepsPerYear, 0.30);
		LocalElectElement e_e3 = new LocalElectElement("e_E3", l_cc, 0.8/stepsPerYear, 0.25);

		LocalWaterElement e_w1 = new LocalWaterElement("e_W1", l_aa, 1.0, 0.9, 200);
		LocalWaterElement e_w2 = new LocalWaterElement("e_W2", l_bb, 1.0, 0.9, 150);
		LocalWaterElement e_w3 = new LocalWaterElement("e_W3", l_cc, 1.0, 0.9, 250);
		WaterPlant e_w4 = new WaterPlant("e_W4", l_aa, (long)(0*stepsPerYear), 0.5/stepsPerYear, 4.5);
		WaterPlant e_w5 = new WaterPlant("e_W5", l_cc, (long)(0*stepsPerYear), 0.4/stepsPerYear, 4.5);
		WaterPlant e_w6 = new WaterPlant("e_W6", l_cc, (long)(5*stepsPerYear), 0.6/stepsPerYear, 4.5);
		WaterPipeline e_w7 = new WaterPipeline("e_W7", l_ab, 0.02/stepsPerYear, 0.9, 2.5);
		WaterPipeline e_w8 = new WaterPipeline("e_W8", l_cb, 0.02/stepsPerYear, 0.9, 2.0);
		WaterController e_w9 = new WaterController("e_W9", l_aa, 
				Arrays.asList(e_w1, e_w2, e_w3, e_w4, e_w5, e_w6, e_w7, e_w8));

		// federation agreement
		setUpSocial(e_s1, e_e1, e_o1, e_w1);
		setUpSocial(e_s2, e_e2, e_o2, e_w2);
		setUpSocial(e_s3, e_e3, e_o3, e_w3);

		setUpElect(e_e1, e_o1, e_s1, e_w1);
		setUpElect(e_e2, e_o2, e_s2, e_w2);
		setUpElect(e_e3, e_o3, e_s3, e_w3);

		setUpPetrol(e_o1, e_e1, e_s1);
		setUpPetrol(e_o2, e_e2, e_s2);
		setUpPetrol(e_o3, e_e3, e_s3);

		setUpWater(e_w1, e_e1, e_o1, e_s1);
		setUpWater(e_w2, e_e2, e_o2, e_s2);
		setUpWater(e_w3, e_e3, e_o3, e_s3);

		return new Scenario("Demo", 0, 
				Arrays.asList(l_aa, l_bb, l_cc, l_ab, l_ba, l_bc, l_cb), 
				Arrays.asList(e_s1, e_s2, e_s3, e_o1, e_o2, e_o3, 
						e_e1, e_e2, e_e3, e_w1, e_w2, e_w3, e_w4, e_w5, e_w6, e_w7, e_w8, e_w9));
	}
	
	private static void setUpSocial(LocalSocialElement social, ElectElement elect,
			PetrolElement petrol, WaterElement water) {
		social.setElectSupplier(elect);
		social.setPetrolSupplier(petrol);
		social.setWaterSupplier(water);
	}
	
	private static void setUpElect(LocalElectElement elect, PetrolElement petrol, 
			SocialElement social, WaterElement water) {
		elect.setPetrolSupplier(petrol);
		elect.setCustomer(petrol);
		elect.setCustomer(social);
		elect.setCustomer(water);
	}
	
	private static void setUpPetrol(LocalPetrolElement petrol, ElectElement elect, 
			SocialElement social) {
		petrol.setElectSupplier(elect);
		petrol.setCustomer(elect);
		petrol.setCustomer(social);
	}
	
	private static void setUpWater(LocalWaterElement water, ElectElement elect, 
			PetrolElement petrol, SocialElement social) {
		water.setElectSupplier(elect);
		water.setCustomer(social);
	}
}
